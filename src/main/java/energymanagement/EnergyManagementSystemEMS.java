package energymanagement;

import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class EnergyManagementSystemEMS {
    public enum AlarmSeverity {
        INFO,
        WARNING,
        ERROR,
        CRITICAL
    }
    public enum AlarmStatus {
        ACTIVE,
        ACKNOWLEDGED,
        CLEARED
    }
    public enum AlarmType {
        // Battery
        LOW_SOC,
        HIGH_SOC,
        HIGH_TEMPERATURE,
        LOW_TEMPERATURE,
        CELL_OVERVOLTAGE,
        CELL_UNDERVOLTAGE,
        // PCS
        PCS_FAULT,
        PCS_OVERLOAD,
        // Grid
        GRID_OVERVOLTAGE,
        GRID_UNDERVOLTAGE,
        GRID_FREQUENCY_FAULT,
        // Communication
        BMS_COMMUNICATION_FAILURE,
        PCS_COMMUNICATION_FAILURE,
        // System
        EMERGENCY_STOP,
        FIRE_DETECTED
    }
    @Getter
    public class Alarm {
        private final String id;
        private final AlarmType type;
        private final AlarmSeverity severity;
        private final String source;
        private final String message;
        private final Instant createdAt;
        private AlarmStatus status;
        private Instant acknowledgedAt;
        private Instant clearedAt;
        public Alarm(String id, AlarmType type, AlarmSeverity severity, String source, String message) {
            this.id = id;
            this.type = type;
            this.severity = severity;
            this.source = source;
            this.message = message;
            this.createdAt = Instant.now();
            this.status = AlarmStatus.ACTIVE;
        }
        public void acknowledge() {
            if (AlarmStatus.ACTIVE.equals(status)) {
                status = AlarmStatus.ACKNOWLEDGED;
                acknowledgedAt = Instant.now();
            }
        }

        public void clear() {
            if (!AlarmStatus.CLEARED.equals(status)) {
                status = AlarmStatus.CLEARED;
                clearedAt = Instant.now();
            }
        }

        public boolean isActive() {
            return status == AlarmStatus.ACTIVE || status == AlarmStatus.ACKNOWLEDGED;
        }

        public boolean isCritical() {
            return severity == AlarmSeverity.CRITICAL;
        }
    }
    public class AlarmService {
        private final Map<String, Alarm> alarms = new ConcurrentHashMap<>();

        public void raise(Alarm alarm) {
            alarms.put(alarm.getId(), alarm);
        }

        public void acknowledge(String alarmId) {
            Alarm alarm = alarms.get(alarmId);
            if (alarm != null)
                alarm.acknowledge();
        }

        public void clear(String alarmId) {
            Alarm alarm = alarms.get(alarmId);
            if (alarm != null)
                alarm.clear();
        }

        public boolean hasCriticalAlarm() {
            return alarms.values()
                    .stream()
                    .anyMatch(a -> a.isActive() && a.isCritical());
        }

        public List<Alarm> getActiveAlarms() {
            return alarms.values()
                    .stream()
                    .filter(Alarm::isActive)
                    .toList();
        }
    }

    /**
     * Current battery telemetry.
     *
     * This is dynamic data and changes continuously.
     */
    public record BatteryStatus(
            // State of Charge, e.g. 72%
            double soc,
            // State of Health, e.g. 95%
            double soh,
            double temperature,
            double voltage,
            double current,
            // Maximum power battery can safely accept right now.
            double availableChargeKW,
            // Maximum power battery can safely provide right now.
            double availableDischargeKW) {}

    /**
     * Abstraction over the physical Battery Management System.
     *
     * EMS should depend on this interface instead of depending
     * directly on Modbus/CAN/vendor-specific APIs.
     */
    public interface BatteryManagementSystem {
        // Current aggregate battery state.
        BatteryStatus getBatteryStatus();
        // Whether battery is currently operational.
        boolean isAvailable();
        // Alarms reported by the BMS.
        List<Alarm> getActiveAlarms();
    }

    public interface ModbusClient {
        int readHoldingRegister(int address);
        int[] readHoldingRegisters(int startAddress, int count);
        boolean readCoil(int address);
        void writeRegister(int address, int value);
        void writeCoil(int address, boolean value);
        boolean isConnected();
        void connect();
        void disconnect();
    }

    public final class BMSRegisters {
        public static final int SOC = 100;
        public static final int SOH = 101;
        public static final int TEMPERATURE = 102;
        public static final int VOLTAGE = 103;
        public static final int CURRENT = 104;

        public static final int CHARGE_LIMIT = 105;
        public static final int DISCHARGE_LIMIT = 106;

        private BMSRegisters() {}
    }

    public class ModbusBatteryManagementSystem implements BatteryManagementSystem {
        private final ModbusClient client;
        public ModbusBatteryManagementSystem(ModbusClient client) {
            this.client = client;
        }

        @Override
        public BatteryStatus getBatteryStatus() {
            int rawSoc = client.readHoldingRegister(100);
            int rawSoh = client.readHoldingRegister(101);
            int rawTemperature = client.readHoldingRegister(102);
            int rawVoltage = client.readHoldingRegister(103);
            int rawCurrent = client.readHoldingRegister(104);
            return new BatteryStatus(
                    rawSoc / 10.0,
                    rawSoh / 10.0,
                    rawTemperature / 10.0,
                    rawVoltage / 10.0,
                    rawCurrent / 10.0,
                    getAvailableChargePower(),
                    getAvailableDischargePower()
            );
        }

        private double getAvailableChargePower() {
            int rawValue = client.readHoldingRegister(BMSRegisters.CHARGE_LIMIT);
            return rawValue / 10.0;
        }

        private double getAvailableDischargePower() {
            int rawValue = client.readHoldingRegister(BMSRegisters.DISCHARGE_LIMIT);
            return rawValue / 10.0;
        }

        @Override
        public boolean isAvailable() {
            return false;
        }

        @Override
        public List<Alarm> getActiveAlarms() {
            return List.of();
        }

        // ...
    }

    public enum PCSOperatingState {
        OFF,
        STARTING,
        STANDBY,
        CHARGING,
        DISCHARGING,
        FAULT,
        EMERGENCY_STOP
    }
    public record PCSStatus(
            PCSOperatingState state,
            double activePowerKW,
            double reactivePowerKVAR,
            double acVoltageV,
            double acCurrentA,
            double acFrequencyHz,
            double dcVoltageV,
            double dcCurrentA,
            double temperatureC,
            double availableChargePowerKW,
            double availableDischargePowerKW,
            boolean available) {}
    public interface PowerConversionSystem {
        PCSStatus getStatus();
        void setPower(double powerKW);
        void stop();
    }

    public enum SolarOperatingState {
        OFF,
        STARTING,
        GENERATING,
        CURTAILED,
        STANDBY,
        FAULT
    }
    public interface SolarInverter {
        SolarPlantStatus getStatus();
        void setActivePowerLimit(double powerKW);
        void setReactivePower(double reactivePowerKVAR);
        void stop();
    }
    public record SolarPlantStatus(
            SolarOperatingState state,
            double activePowerKW,
            double reactivePowerKVAR,
            double availablePowerKW,
            double acVoltageV,
            double acCurrentA,
            double frequencyHz,
            double energyGeneratedTodayKWh,
            boolean available) {}
    public class SolarPlant {
        private final String id;
        private final String name;
        private final double ratedCapacityKW;
        private final SolarInverter inverter;
        public SolarPlant(String id, String name, double ratedCapacityKW, SolarInverter inverter) {
            this.id = id;
            this.name = name;
            this.ratedCapacityKW = ratedCapacityKW;
            this.inverter = inverter;
        }
        public SolarPlantStatus getStatus() {
            return inverter.getStatus();
        }
    }

    public record SiteLoadStatus(
            double activePowerKW,
            double reactivePowerKVAR,
            double voltageV,
            double currentA,
            double frequencyHz,
            boolean available) {}
    public interface LoadMeter {
        SiteLoadStatus getStatus();
    }
    public class SiteLoad {
        private final String id;
        private final String name;
        private final LoadMeter meter;
        public SiteLoad(String id, String name, LoadMeter meter) {
            this.id = id;
            this.name = name;
            this.meter = meter;
        }
        public SiteLoadStatus getStatus() {
            return meter.getStatus();
        }
    }

    public enum GridState {
        CONNECTED,
        DISCONNECTED,
        FAULT
    }
    public record GridStatus(
            GridState state,
            double activePowerKW,
            double reactivePowerKVAR,
            double voltageV,
            double currentA,
            double frequencyHz,
            boolean available) {}
    public interface GridMeter {
        GridStatus getStatus();
    }
    @Getter
    public class GridConnection {
        private final String id;
        private final String name;
        private final GridMeter meter;
        private final double maxImportKW;
        private final double maxExportKW;
        public GridConnection(String id, String name, GridMeter meter, double maxImportKW, double maxExportKW) {
            this.id = id;
            this.name = name;
            this.meter = meter;
            this.maxImportKW = maxImportKW;
            this.maxExportKW = maxExportKW;
        }
        public GridStatus getStatus() {
            return meter.getStatus();
        }
        public boolean canImport(double powerKW) {
            return powerKW <= maxImportKW;
        }
        public boolean canExport(double powerKW) {
            return powerKW <= maxExportKW;
        }
    }
    public record SiteStatus(
            double batterySOC,
            double batteryPowerKW,
            double solarPowerKW,
            double loadPowerKW,
            double gridImportKW,
            double gridExportKW
    ) {}
    public class Site {
        private final BatterySystem battery;
        private final PowerConversionSystem pcs;
        private final SolarPlant solar;
        private final SiteLoad load;
        private final GridConnection grid;
        public Site(BatterySystem battery, PowerConversionSystem pcs, SolarPlant solar, SiteLoad load, GridConnection grid) {
            this.battery = battery;
            this.pcs = pcs;
            this.solar = solar;
            this.load = load;
            this.grid = grid;
        }
        public SiteStatus getStatus() {
            // aggregate latest measurements
            return null;
        }
    }

    public enum RackState {
        OFFLINE,
        STARTING,
        STANDBY,
        CHARGING,
        DISCHARGING,
        FAULT
    }
    public record BatteryRackStatus(
            RackState state,
            double soc,
            double soh,
            double voltageV,
            double currentA,
            double temperatureC,
            double availableChargePowerKW,
            double availableDischargePowerKW,
            boolean available) {}
    public interface RackBMS {
        BatteryRackStatus getStatus();
        List<Alarm> getActiveAlarms();
        void connect();
        void disconnect();
    }
    public enum ModuleState {
        OFFLINE,
        STANDBY,
        CHARGING,
        DISCHARGING,
        FAULT
    }
    public record BatteryModuleStatus(
            ModuleState state,
            double soc,
            double soh,
            double voltageV,
            double currentA,
            double temperatureC,
            double minCellVoltageV,
            double maxCellVoltageV,
            boolean available) {}
    public interface ModuleBMS {
        BatteryModuleStatus getStatus();
        List<Alarm> getActiveAlarms();
    }
    public class BatteryModule {
        private final String id;
        private final double ratedCapacityKWh;
        private final double nominalVoltageV;
        private final ModuleBMS bms;
        public BatteryModule(
                String id,
                double ratedCapacityKWh,
                double nominalVoltageV,
                ModuleBMS bms) {

            this.id = id;
            this.ratedCapacityKWh = ratedCapacityKWh;
            this.nominalVoltageV = nominalVoltageV;
            this.bms = bms;
        }
        public BatteryModuleStatus getStatus() {
            return bms.getStatus();
        }
    }
    @Getter
    public class BatteryRack {
        private final String id;
        private final List<BatteryModule> modules;
        private final RackBMS rackBMS;
        private final double ratedCapacityKWh;
        private final double maxChargePowerKW;
        private final double maxDischargePowerKW;
        public BatteryRack(String id, List<BatteryModule> modules, RackBMS rackBMS, double ratedCapacityKWh, double maxChargePowerKW, double maxDischargePowerKW) {
            this.id = id;
            this.modules = modules;
            this.rackBMS = rackBMS;
            this.ratedCapacityKWh = ratedCapacityKWh;
            this.maxChargePowerKW = maxChargePowerKW;
            this.maxDischargePowerKW = maxDischargePowerKW;
        }
        public BatteryRackStatus getStatus() {
            return rackBMS.getStatus();
        }
    }

    public class BatterySystem {
        private final String id;
        private final List<BatteryRack> racks;
        private final BatteryManagementSystem bms;
        private double capacityKWh;
        private double maxChargeKW;
        private double maxDischargeKW;
        public BatterySystem(String id, List<BatteryRack> racks, BatteryManagementSystem bms, double capacityKWh, double maxChargeKW, double maxDischargeKW) {
            this.id = id;
            this.racks = racks;
            this.bms = bms;
            this.capacityKWh = capacityKWh;
            this.maxChargeKW = maxChargeKW;
            this.maxDischargeKW = maxDischargeKW;
        }
        public BatteryStatus getStatus() {
            return bms.getBatteryStatus();
        }
    }

    public record ForecastPoint(
        Instant startTime,
        Instant endTime,
        double expectedLoadKW,
        double expectedSolarKW) {}
    public record Forecast(Instant generatedAt, List<ForecastPoint> points) {}
    public record MarketData(double currentBuyPricePerKWh, double currentSellPricePerKWh, List<MarketPricePoint> futurePrices) {
        public double currentPrice() {
            return currentBuyPricePerKWh;
        }
    }
    public interface MarketDataService {
        MarketData getMarketData();
    }
    public record MarketPricePoint(Instant startTime, Instant endTime, double buyPricePerKWh, double sellPricePerKWh) {}
    public interface DispatchStrategy {
        DispatchDecision calculate(SiteStatus site, Forecast forecast, MarketData marketData);
    }
    public record DispatchDecision(double requestedPowerKW, String reason) {}
    public class PeakShavingStrategy implements DispatchStrategy {
        private final double maxGridImportKW;
        public PeakShavingStrategy(double maxGridImportKW) {
            this.maxGridImportKW = maxGridImportKW;
        }
        @Override
        public DispatchDecision calculate(SiteStatus site, Forecast forecast, MarketData marketData) {
            double excess = site.gridImportKW() - maxGridImportKW;
            if (excess > 0)
                return new DispatchDecision(excess, "Peak shaving");
            return new DispatchDecision(0, "No action");
        }
    }

    public class ArbitrageStrategy implements DispatchStrategy {
        private final double buyThreshold;
        private final double sellThreshold;
        public ArbitrageStrategy(double buyThreshold, double sellThreshold) {
            this.buyThreshold = buyThreshold;
            this.sellThreshold = sellThreshold;
        }

        @Override
        public DispatchDecision calculate(SiteStatus site, Forecast forecast, MarketData market) {
            double price = market.currentPrice();
            if (price < buyThreshold)
                return new DispatchDecision(-5000, "Low electricity price");
            if (price > sellThreshold)
                return new DispatchDecision(5000, "High electricity price");
            return new DispatchDecision(0, "Hold");
        }
    }

    public interface Constraint {
        double apply(double requestedPowerKW, SiteStatus status);
    }

    public class SOCConstraint implements Constraint {
        private final double minSOC;
        private final double maxSOC;
        public SOCConstraint(double minSOC, double maxSOC) {
            this.minSOC = minSOC;
            this.maxSOC = maxSOC;
        }
        @Override
        public double apply(double requestedPowerKW, SiteStatus status) {
            if (requestedPowerKW > 0 && status.batterySOC() <= minSOC)
                return 0;
            if (requestedPowerKW < 0 && status.batterySOC() >= maxSOC)
                return 0;
            return requestedPowerKW;
        }
    }

    public class ConstraintEngine {
        private final List<Constraint> constraints;

        public ConstraintEngine(List<Constraint> constraints) {
            this.constraints = constraints;
        }
        public double apply(double requestedPower, SiteStatus status) {
            double allowedPower = requestedPower;
            for (Constraint constraint : constraints)
                allowedPower = constraint.apply(allowedPower, status);
            return allowedPower;
        }
    }

    public class DispatchEngine {
        private DispatchStrategy strategy;
        private final ConstraintEngine constraintEngine;
        private final PowerConversionSystem pcs;
        public DispatchEngine(ConstraintEngine constraintEngine, PowerConversionSystem pcs) {
            this.constraintEngine = constraintEngine;
            this.pcs = pcs;
        }
        public void dispatch(SiteStatus status, Forecast forecast, MarketData marketData) {
            DispatchDecision decision = strategy.calculate(status, forecast, marketData);
            double allowedPower = constraintEngine.apply(decision.requestedPowerKW(), status);
            pcs.setPower(allowedPower);
        }
    }

    public class TelemetryService {
        private final BatterySystem battery;
        private final PowerConversionSystem pcs;
        private final SolarPlant solar;
        private final SiteLoad load;
        private final GridConnection grid;
        public TelemetryService(BatterySystem battery, PowerConversionSystem pcs, SolarPlant solar, SiteLoad load, GridConnection grid) {
            this.battery = battery;
            this.pcs = pcs;
            this.solar = solar;
            this.load = load;
            this.grid = grid;
        }
        public SiteStatus getSiteStatus() {
            BatteryStatus batteryStatus = battery.getStatus();
            PCSStatus pcsStatus = pcs.getStatus();
            SolarPlantStatus solarStatus = solar.getStatus();
            SiteLoadStatus loadStatus = load.getStatus();
            GridStatus gridStatus = grid.getStatus();
            return new SiteStatus(
                    batteryStatus.soc(),
                    pcsStatus.activePowerKW(),
                    solarStatus.activePowerKW(),
                    loadStatus.activePowerKW(),
                    Math.max(gridStatus.activePowerKW(), 0),
                    Math.max(-gridStatus.activePowerKW(), 0)
            );
        }
    }

    public interface ForecastService {
        Forecast getForecast();
    }
    public record LoadForecastPoint(Instant startTime, Instant endTime, double expectedLoadKW) {}
    public interface LoadForecastProvider {
        List<LoadForecastPoint> getForecast();
    }
    public record SolarForecastPoint(Instant startTime, Instant endTime, double expectedSolarKW) {}
    public interface SolarForecastProvider {
        List<SolarForecastPoint> getForecast();
    }
    public class DefaultForecastService implements ForecastService {
        private final LoadForecastProvider loadProvider;
        private final SolarForecastProvider solarProvider;
        public DefaultForecastService(LoadForecastProvider loadProvider, SolarForecastProvider solarProvider) {
            this.loadProvider = loadProvider;
            this.solarProvider = solarProvider;
        }

        @Override
        public Forecast getForecast() {
            List<LoadForecastPoint> loads = loadProvider.getForecast();
            List<SolarForecastPoint> solar = solarProvider.getForecast();
            return merge(loads, solar);
        }

        private Forecast merge(List<LoadForecastPoint> loads, List<SolarForecastPoint> solar) {
            // Match load and solar forecasts by time interval
            // and create ForecastPoints.
            return null;
        }
    }


    public class EnergyManagementSystem {
        private final Site site;
        private final TelemetryService telemetry;
        private final ForecastService forecastService;
        private final MarketDataService marketDataService;
        private final DispatchEngine dispatchEngine;
        private final AlarmService alarmService;

        public EnergyManagementSystem(Site site, TelemetryService telemetry, ForecastService forecastService, MarketDataService marketDataService, DispatchEngine dispatchEngine, AlarmService alarmService) {
            this.site = site;
            this.telemetry = telemetry;
            this.forecastService = forecastService;
            this.marketDataService = marketDataService;
            this.dispatchEngine = dispatchEngine;
            this.alarmService = alarmService;
        }

        public void runControlLoop() {
            SiteStatus status = telemetry.getSiteStatus();
            Forecast forecast = forecastService.getForecast();
            MarketData market = marketDataService.getMarketData();
            if (alarmService.hasCriticalAlarm())
                return;
            dispatchEngine.dispatch(status, forecast, market);
        }
    }
    public enum EMSState {
        STARTING,
        STANDBY,
        CHARGING,
        DISCHARGING,
        FAULT,
        MAINTENANCE,
        SHUTDOWN
    }


//    void main(String[] args) {
//
//        // =========================================================
//        // 1. BMS
//        // =========================================================
//        BatteryManagementSystem bms = new FakeBMS();
//        BatterySystem battery = new BatterySystem(
//                "BATTERY-1",
//                List.of(),
//                bms,
//                20_000,      // 20 MWh
//                5_000,       // 5 MW charge
//                5_000        // 5 MW discharge
//        );
//
//        // =========================================================
//        // 2. PCS
//        // =========================================================
//        PowerConversionSystem pcs = new FakePCS();
//
//        // =========================================================
//        // 3. SOLAR
//        // =========================================================
//        SolarInverter inverter = new FakeSolarInverter();
//        SolarPlant solar = new SolarPlant(
//                "SOLAR-1",
//                "Main Solar Plant",
//                10_000,      // 10 MW
//                inverter
//        );
//
//
//        // =========================================================
//        // 4. SITE LOAD
//        // =========================================================
//        LoadMeter loadMeter = new FakeLoadMeter();
//        SiteLoad siteLoad = new SiteLoad(
//                "LOAD-1",
//                "Factory Load",
//                loadMeter
//        );
//
//
//        // =========================================================
//        // 5. GRID
//        // =========================================================
//        GridMeter gridMeter = new FakeGridMeter();
//        GridConnection grid = new GridConnection(
//                "GRID-1",
//                "Utility Grid",
//                gridMeter,
//                10_000,      // maximum import = 10 MW
//                5_000        // maximum export = 5 MW
//        );
//
//
//        // =========================================================
//        // 6. TELEMETRY
//        // =========================================================
//        TelemetryService telemetry = new DefaultTelemetryService(battery, pcs, solar, siteLoad, grid);
//
//        // =========================================================
//        // 7. FORECAST
//        // =========================================================
//        ForecastService forecastService = new FakeForecastService();
//
//        // =========================================================
//        // 8. MARKET DATA
//        // =========================================================
//        MarketDataService marketDataService = new FakeMarketDataService();
//
//
//        // =========================================================
//        // 9. CONSTRAINTS
//        // =========================================================
//        ConstraintEngine constraintEngine = new ConstraintEngine(List.of(new SOCConstraint(10, 95), new BatteryPowerConstraint()));
//
//
//        // =========================================================
//        // 10. DISPATCH STRATEGY
//        // =========================================================
//        DispatchStrategy strategy = new ArbitrageStrategy(
//                4.0,     // charge below ₹4/kWh
//                8.0      // discharge above ₹8/kWh
//        );
//
//
//        // =========================================================
//        // 11. DISPATCH ENGINE
//        // =========================================================
//        DispatchEngine dispatchEngine = new DispatchEngine(strategy, constraintEngine, pcs);
//
//
//        // =========================================================
//        // 12. ALARMS
//        // =========================================================
//        AlarmService alarmService = new AlarmService();
//
//        // =========================================================
//        // 13. EMS
//        // =========================================================
//        EnergyManagementSystem ems = new EnergyManagementSystem(null, telemetry, forecastService, marketDataService, dispatchEngine, alarmService);
//
//        // =========================================================
//        // RUN ONE EMS CONTROL CYCLE
//        // =========================================================
//        ems.runControlLoop();
//    }

}
