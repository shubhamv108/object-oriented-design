package playo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

// Slot-Match combined
public class Main {

    enum OfferingName {
        BADMINTON
    }

    class Offering {
        private final OfferingName name;

        public String toString() {
            return name.toString();
        }

        Offering(OfferingName offeringName) {
            this.name = offeringName;
        }

        private Map<String, Facility> facilities = new HashMap<>();

        public void addFacility(Facility facility, LocalTime startHour, LocalTime endHour) {
            this.facilities.put(facility.name, facility);
            facility.addOffering(this.name, startHour, endHour);
        }

        public List<Facility> filterFacilitiesByStartEndHours(OfferingName offeringName, LocalTime start, LocalTime end) {
            return facilities.values()
                    .stream()
                    .filter(facility -> facility.isOpenForOfferingByStartEndHour(offeringName, start, end))
                    .toList();

        }
    }

    public class Facility {
        private final String name;
        private final Map<OfferingName, FacilityOffering> facilityOfferings = new HashMap<>();

        public Facility(final String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        public Match createMatch(OfferingName offeringName, LocalDate matchDate, LocalTime matchStartTime, LocalTime matchEndTime, int totalSlots) {
            FacilityOffering facilityOffering = facilityOfferings.get(offeringName);
            if (facilityOffering == null)
                throw new IllegalArgumentException("new such offering at facility");
            return facilityOffering.createMatch(matchDate, matchStartTime, matchEndTime, totalSlots);
        }

        public void addOffering(OfferingName offeringName, LocalTime offerringDailyStartTime, LocalTime offeringDailyEndTime) {
            facilityOfferings.put(offeringName, new FacilityOffering(this, offeringName, offerringDailyStartTime, offeringDailyEndTime));
        }

        public boolean isOpenForOfferingByStartEndHour(OfferingName offeringName, LocalTime start, LocalTime end) {
            return Optional.ofNullable(facilityOfferings.get(offeringName))
                    .map(facilityOffering -> facilityOffering.isOpen(start, end))
                    .orElse(false);
        }

        public List<Match> getMatchesWithAvailableSlotsByDate(OfferingName offering, LocalDate date) {
            return Optional.ofNullable(facilityOfferings.get(offering))
                    .map(facilityOffering -> facilityOffering.getMatchesWithAvailableSlotsByDate(date))
                    .orElse(Collections.emptyList());
        }
    }

    public class FacilityOffering {
        private final Facility facility;
        private final OfferingName offeringName;
        private final Map<LocalDate, List<String>> matches = new HashMap<>();
        private LocalTime dailyStartTime;
        private LocalTime dailyEndTime;


        public FacilityOffering(
                final Facility facility,
                final OfferingName offeringName,
                final LocalTime dailyStartTime,
                final LocalTime dailyEndTime) {
            this.facility = facility;
            this.offeringName = offeringName;
            this.dailyStartTime = dailyStartTime;
            this.dailyEndTime = dailyEndTime;
        }

        @Override
        public String toString() {
            return "FacilityOffering{" +
                    "facility=" + facility +
                    ", offeringName=" + offeringName +
                    ", matches=" + matches +
                    ", dailyStartTime=" + dailyStartTime +
                    ", dailyEndTime=" + dailyEndTime +
                    '}';
        }

        public boolean isOpen(final LocalTime start, final LocalTime end) {
            return dailyStartTime.compareTo(start) <= 0 && end.compareTo(dailyEndTime) <= 0;
        }

        public Match createMatch(LocalDate date, LocalTime startTime, LocalTime endTime, int totalSlots) {
            if (date.compareTo(LocalDate.now()) < 0)
                throw  new IllegalArgumentException("no booking for past dates");
            if (endTime.compareTo(startTime) <= 0)
                throw new IllegalArgumentException("invalid start and end time");
            if (startTime.compareTo(dailyStartTime) < 0
                    || startTime.compareTo(dailyEndTime) > 0
                    || endTime.compareTo(dailyStartTime) < 0
                    || endTime.compareTo(dailyEndTime) > 0)
                throw new IllegalArgumentException("Facility not supported at given time for given offering");

            final Match overlappingMatch = MatchService.getInstance()
                    .findFirstOverLappingMatchFromListByStartAndEndTime(matches.getOrDefault(date, Collections.emptyList()), startTime, endTime);
            if (overlappingMatch != null)
                throw new IllegalArgumentException("Slot unavailable");
            final Match match = new Match(this.facility, this.offeringName, date, startTime, endTime, totalSlots);
            MatchService.getInstance().create(match);
            matches.computeIfAbsent(date, e -> new ArrayList<>()).add(match.id);
            return match;
        }

        public List<Match> getMatchesWithAvailableSlotsByDate(LocalDate date) {
            return Optional.ofNullable(matches.get(date))
                    .map(dateMatches -> dateMatches
                            .stream()
                            .filter(Objects::nonNull)
                            .map(matchId -> MatchService.getInstance().getById(matchId))
                            .filter(Objects::nonNull)
                            .filter(Match::hasAvailableSlots)
                            .toList())
                    .orElse(Collections.emptyList());
        }

        public List<Match> getMatchesByDate(final LocalDate date) {
            return Optional.ofNullable(matches.get(date))
                    .map(dateMatches -> dateMatches
                            .stream()
                            .map(matchId -> MatchService.getInstance().getById(matchId))
                            .filter(Objects::nonNull)
                            .toList())
                    .orElse(Collections.emptyList());
        }


    }

    public class Match {
        String id;
        Facility facility;
        OfferingName offering;
        LocalDate date;
        LocalTime startTime;
        LocalTime endTime;
        int totalSlots;
        int bookedSlots;

        public Match (
                final Facility facility,
                final OfferingName offeringName,
                final LocalDate date,
                final LocalTime startTime,
                final LocalTime endTime,
                final int totalSlots) {
            this.facility = facility;
            this.offering = offeringName;
            this.date = date;
            this.startTime = startTime;
            this.endTime = endTime;
            this.totalSlots = totalSlots;
        }

        @Override
        public String toString() {
            return "Match{" +
                    "facility=" + facility +
                    ", offering=" + offering +
                    ", start=" + startTime +
                    ", end=" + endTime +
                    ", totalSlots=" + totalSlots +
                    ", bookedSlots=" + bookedSlots +
                    '}';
        }

        public boolean hasAvailableSlots() {
            return totalSlots > bookedSlots;
        }

        public boolean allocateSlot() {
            if (bookedSlots < totalSlots) {
                bookedSlots++;
                return true;
            }
            return false;
        }
    }

    public static class MatchService {
        private final Map<String, Match> matches = new HashMap<>();

        public static MatchService getInstance() {
            return SingletonHolder.INSTANCE;
        }

        private static final class SingletonHolder {
            private static final MatchService INSTANCE = new MatchService();
        }

        public Match create(final Match match) {
            match.id = UUID.randomUUID().toString();
            matches.put(match.id, match);
            return match;
        }

        public Match findFirstOverLappingMatchFromListByStartAndEndTime(
                final List<String> matchIds,
                final LocalTime startTime,
                final LocalTime endTime) {
            return matchIds.stream()
                    .map(matches::get)
                    .filter(match -> DateUtils.areOverlapping(match.startTime, match.endTime, startTime, endTime))
                    .findFirst()
                    .orElse(null);
        }

        public Match getById(final String id) {
            return matches.get(id);
        }

        public boolean bookSlot(final String matchId) {
            return this.getById(matchId).allocateSlot();
        }

    }

    public class DateUtils {
        public static boolean areOverlapping(
                final LocalTime aStart,
                final LocalTime aEnd,
                final LocalTime bStart,
                final LocalTime bEnd) {
            return aStart.isBefore(bEnd) && bStart.isBefore(aEnd);
        }
    }

    class PlayO {
        private final Map<OfferingName, Offering> offerings = new HashMap<>(); // move to service
        private final Map<String, Facility> facilities = new HashMap<>(); // move to service

        List<Facility> getAllFacilitiesByOfferingAndStartHourAndEndHour(OfferingName offeringName, LocalTime startHour, LocalTime endHour) {
            if (endHour.compareTo(startHour) <= 0)
                throw new IllegalArgumentException("invalid start and end time");
            return Optional.ofNullable(offerings.get(offeringName))
                    .map(offer -> offer.filterFacilitiesByStartEndHours(offeringName, startHour, endHour))
                    .orElse(Collections.emptyList());
        }

        Facility addFacility(String facilityName) {
            if (facilities.containsKey(facilityName))
                throw new IllegalArgumentException("Facility already exists");
            Facility facility = new Facility(facilityName);
            facilities.put(facilityName, facility);
            return facility;
        }

        Match createMatch(
                final String facilityName,
                final OfferingName offeringName,
                final LocalDate matchDate,
                final LocalTime matchStartTime,
                final LocalTime matchEndTime,
                final int totalSlots) {
            Facility facility = facilities.get(facilityName);
            if (Objects.isNull(facility))
                throw new IllegalArgumentException("No such facility found");

            return facility.createMatch(offeringName, matchDate, matchStartTime, matchEndTime, totalSlots);
        }

        void addOffering(String facilityName, OfferingName offeringName, LocalTime startHour, LocalTime endHour) {
            if (endHour.compareTo(startHour) <= 0)
                throw new IllegalArgumentException("invalid start and end time");
            Facility facility = facilities.get(facilityName);
            if (Objects.isNull(facility))
                throw new IllegalArgumentException("No such facility found");
            Offering offering = new Offering(offeringName);
            Offering existingOffering = offerings.get(offeringName);
            if (existingOffering == null)
                offerings.put(offeringName, offering);
            else
                offering = existingOffering;
            offering.addFacility(facility, startHour, endHour);
        }

        List<Match> getAllAvailableSlotsForFacilityAndDate(OfferingName offering, String facilityId, LocalDate date) {
            return Optional.ofNullable(facilities.get(facilityId))
                    .map(facility -> facility.getMatchesWithAvailableSlotsByDate(offering, date))
                    .orElse(Collections.emptyList());
        }

        public boolean boookSlot(final String matchId) {
            return MatchService.getInstance().getById(matchId)
                    .allocateSlot();
        }
    }

    public static void main(String[] args) {
        String facilityName = "facilityName";
        Main main = new Main();
        LocalTime facilityStartTime = LocalTime.of(11, 0, 0, 0);
        LocalTime facilityEndTime = LocalTime.of(23, 0, 0, 0);
        LocalDate matchDate = LocalDate.of(2025, 11, 8);
        LocalTime matchStartTime = LocalTime.of(11, 0, 0, 0);
        LocalTime matchEndTime = LocalTime.of(12, 0, 0, 0);
        PlayO playO = main.new PlayO();
        System.out.println(playO.addFacility(facilityName));
        playO.addOffering(facilityName, OfferingName.BADMINTON, facilityStartTime, facilityEndTime);
        System.out.println(playO.createMatch(facilityName, OfferingName.BADMINTON, matchDate, matchStartTime, matchEndTime, 4));
//        System.out.println(playO.createMatch(facilityName, OfferingName.BADMINTON, matchDate, matchStartTime, matchEndTime, 4));
        System.out.println(playO.getAllFacilitiesByOfferingAndStartHourAndEndHour(
                OfferingName.BADMINTON,
                LocalTime.of(11, 0, 0, 0),
                LocalTime.of(11, 20, 0, 0)));
        List<Match> matches = playO.getAllAvailableSlotsForFacilityAndDate(OfferingName.BADMINTON, facilityName, matchDate);
        System.out.println(matches);
        System.out.println(playO.boookSlot(matches.get(0).id));
        System.out.println(playO.boookSlot(matches.get(0).id));

    }
}