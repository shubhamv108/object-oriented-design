State Machine
```                 ┌─────────────┐
                    │   STARTING  │
                    └──────┬──────┘
                           ↓
                    ┌─────────────┐
             ┌─────→│   STANDBY   │←─────┐
             │      └──────┬──────┘      │
             │             │             │
             │       ┌─────┴─────┐       │
             │       ↓           ↓       │
             │  CHARGING    DISCHARGING  │
             │       │           │       │
             └───────┴───────────┘       │
                                         │
                        FAULT ────────────┘

```

Class Relationship
```
                    EMS
                     |
                  Site
                     |
        +------------+------------+
        |            |            |
   SolarPlant   BatterySystem     PCS
        |            |
  SolarInverter     BMS
        |
    PV Arrays


TelemetryService ───────► SiteStatus
                              │
                              ▼
                       DispatchEngine
                              │
                    ┌─────────┴─────────┐
                    ▼                   ▼
             DispatchStrategy     ConstraintEngine
                    │                   │
        ┌───────────┼─────────┐         │
        ▼           ▼         ▼         ▼
      Peak       Arbitrage   Solar    SOC/Power/
     Shaving                        Temp/Grid...
                    │
                    ▼
              DispatchDecision
                    │
                    ▼
              Safe Setpoint
                    │
                    ▼
                   PCS
                    │
                    ▼
                Battery
```