package playo;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

/**
 * Will Support multiple games
 * system will support multiple venues
 * each venue will offer multiple games
 * user can book a venue for a specific game offering like pickle ball, table tennis, badminton
 * venue will have a start and end time
 * while booking user should be able to see the available and not available time slots within the venue for the sport.
 * user shall be shown the price for each court beside the timings
 * user should be allowed to select the length of the slot for booking and the court
 * user should make the payment to confirm booking
 * user can create a match and accept requests
 *
 * ------
 * user can search for others booking for a specific game and join game if there is a spot available.
 *
 * Non Func
 * Modular
 * Extensible
 * Scalable
 *
 */
public class Main {
    public enum Sport {
        PICKLE_BALL, BADMINTON, CRICKET, FOOTBALL, TENNIS
    }

    public class Venue {
        private final String id;
        private final String name;
        private String addressId;

        public Venue(String id, String name, String addressId) {
            this.id = id;
            this.name = name;
            this.addressId = addressId;
        }

        public void setAddressId(String addressId) {
            this.addressId = addressId;
        }
    }

    public class Address {
        private String addressLine1;
        private String addressLine2;
        private String geocode;
        private String city;
        private String district;
        private String state;
        private int pinCode;

        @Override
        public String toString() {
            return "Address{" +
                    "addressLine1='" + addressLine1 + '\'' +
                    ", addressLine2='" + addressLine2 + '\'' +
                    ", city='" + city + '\'' +
                    ", district='" + district + '\'' +
                    ", state='" + state + '\'' +
                    ", pinCode=" + pinCode +
                    '}';
        }
    }

    public class VenueOffering {
        private final String id;
        private final String venueId;
        private final Sport sport;
        private LocalTime start;
        private LocalTime end;

        public VenueOffering(String id, String venueId, Sport sport, LocalTime start, LocalTime end) {
            this.id = id;
            this.venueId = venueId;
            this.sport = sport;
            this.start = start;
            this.end = end;
        }
    }

    public class PlayingArea {
        private final String id;
        private final String venueOfferingId;
        private String name; // BadmintonCourt1, FootBallGround1
        private Double pricePerHour;

        public PlayingArea(String id, String venueOfferingId, String name, Double pricePerHour) {
            this.id = id;
            this.venueOfferingId = venueOfferingId;
            this.name = name;
            this.pricePerHour = pricePerHour;
        }
    }

    public class User {
        private String id;
    }

    public enum BookingStatus {
        CREATED, HOLD, CONFIRMED, CANCELLED
    }

    public class PlayingAreaBooking {
        private final String id;
        private final String playingAreaId;
        private final Instant start;
        private final Instant end;
        private final String bookingByUserId;

        private BookingStatus status = BookingStatus.CREATED;
        private Instant holdExpiryAt;

        public PlayingAreaBooking(String id, String playingAreaId, Instant start, Instant end, String bookingByUserId) {
            this.id = id;
            this.playingAreaId = playingAreaId;
            this.start = start;
            this.end = end;
            this.bookingByUserId = bookingByUserId;
        }

        public void setStatus(BookingStatus status) {
            this.status = status;
        }

        public void holdBooking(long holdWindowInSeconds) {
            this.setStatus(BookingStatus.HOLD);
            this.holdExpiryAt = Instant.now().plusSeconds(holdWindowInSeconds);
        }

        public void confirm() {
            if (Instant.now().isAfter(this.holdExpiryAt))
                throw new IllegalStateException();
            this.setStatus(BookingStatus.CONFIRMED);
            this.holdExpiryAt = null;
        }
    }

    public class PlayOService {
        private final Map<String, Address> addresses = new HashMap<>();
        private final Map<String, Venue> venues = new HashMap<>();
        private final Map<String, VenueOffering> venueOfferings = new HashMap<>();
        private final Map<String, PlayingArea> playingAreas = new HashMap<>();
        private final Map<String, PlayingAreaBooking> playingAreaBookings = new HashMap<>();

        private final Map<Sport, Set<String>> gameVenueOfferings = new HashMap<>();
        private final Map<String, Map<LocalDate, Map<String, Set<String>>>> venueOfferingIdToPlayAreaBookingByDate = new HashMap<>();

        private final Map<String, Event> events = new HashMap<>();
        private final Map<String, EventParticipantBooking> eventParticipantBookings = new HashMap<>();
        private final Map<String, Set<String>> playAreaBookingToEventIds = new HashMap<>();

        private static final Set<BookingStatus> UNAVALIABLE_BOOKING_STATUS = Collections.unmodifiableSet(Set.of(BookingStatus.HOLD, BookingStatus.CONFIRMED));

        public Sport[] getGames() {
            return Sport.values();
        }

        public String addAddress(Address address) {
            String id = UUID.randomUUID().toString();
            addresses.put(id, address);
            return id;
        }

        public String addVenue(String name, String addressId) {
            String id = UUID.randomUUID().toString();
            venues.put(id, new Venue(id, name, addressId));
            return id;
        }

        public String addVenueOffering(Sport sport, String venueId, LocalTime start, LocalTime end) {
            String id = UUID.randomUUID().toString();
            venueOfferings.put(id, new VenueOffering(id, venueId, sport, start, end));
            return id;
        }

        public String addPlayingArea(String name, String venueOfferingId, Double pricePerHour) {
            String id = UUID.randomUUID().toString();
            playingAreas.put(id, new PlayingArea(id, venueOfferingId, name, pricePerHour));
            return id;
        }

        public Collection<VenueSearchResponse> getVenuesForGame(Sport sport) {
            return gameVenueOfferings.getOrDefault(sport, Collections.emptySet())
                    .stream()
                    .map(venueOfferingId -> {
                        VenueOffering venueOffering = venueOfferings.get(venueOfferingId);
                        Venue venue = venues.get(venueOffering.venueId);
                        return new VenueSearchResponse(venue.name, addresses.get(venue.addressId).toString(), venueOffering.id);
                    })
                    .toList();
        }

        public VenueOfferingAvailabilityForDateResponse getAvailabilityForVenue(String venueOfferingId, LocalDate date) {
            VenueOffering venueOffering = venueOfferings.get(venueOfferingId);
            List<PlayingAreaAvailabilityResponse> playingAreaAvailabilityResponse = venueOfferingIdToPlayAreaBookingByDate
                    .getOrDefault(venueOfferingId, Collections.emptyMap())
                    .getOrDefault(date, Collections.emptyMap())
                    .values()
                    .stream()
                    .map(playingAreaBookings::get)
                    .filter(booking -> UNAVALIABLE_BOOKING_STATUS.contains(booking.status))
                    .collect(Collectors.groupingBy(
                            booking -> booking.playingAreaId,
                            Collectors.mapping(booking -> new BookedIntervals(booking.start, booking.end),
                            Collectors.toList())))
                    .entrySet()
                    .stream()
                    .map(entry -> {
                        PlayingAreaBooking playingAreaBooking = playingAreaBookings.get(entry.getKey());
                        PlayingArea playingArea = playingAreas.get(playingAreaBooking.playingAreaId);
                        return new PlayingAreaAvailabilityResponse(playingArea.id, playingArea.name, entry.getValue(), playingArea.pricePerHour);
                    }).toList();
            return new VenueOfferingAvailabilityForDateResponse(venueOffering.start, venueOffering.end, playingAreaAvailabilityResponse);
        }

        public String createVenueBooking(String playingAreaId, Instant start, Instant end, String userId) {
            if (end.isBefore(start))
                throw new IllegalArgumentException();

            LocalDate startDate = start.atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endDate   = end.atZone(ZoneId.systemDefault()).toLocalDate();
            LocalTime startTime = start.atZone(ZoneId.systemDefault()).toLocalTime();
            LocalTime endTime   = end.atZone(ZoneId.systemDefault()).toLocalTime();
            PlayingArea playingArea = Optional.ofNullable(playingAreas.get(playingAreaId)).orElseThrow();
            VenueOffering venueOffering = Optional.ofNullable(venueOfferings.get(playingArea.venueOfferingId)).orElseThrow();

            if (!startDate.equals(endDate) || venueOffering.end.compareTo(startTime) < 0 || endTime.compareTo(venueOffering.start) < 0)
                throw new IllegalArgumentException();

            PlayingAreaBooking booking = new PlayingAreaBooking(
                    UUID.randomUUID().toString(),
                    playingAreaId,
                    start,
                    end,
                    userId);

            if (!isPlayAreaAvailable(playingArea.venueOfferingId, startDate, playingAreaId, start, end))
                throw new IllegalStateException();
            synchronized (playingArea) {
                if (!isPlayAreaAvailable(playingArea.venueOfferingId, startDate, playingAreaId, start, end))
                    throw new IllegalStateException();

                playingAreaBookings.put(booking.id, booking);
                booking.holdBooking(300);
                venueOfferingIdToPlayAreaBookingByDate.computeIfAbsent(playingArea.venueOfferingId, e -> new HashMap<>())
                        .computeIfAbsent(startDate, e -> new HashMap<>())
                        .computeIfAbsent(playingAreaId, e -> new HashSet<>())
                        .add(booking.id);
            }

            if (BookingStatus.HOLD.equals(booking.status))
                return booking.id;

            throw new IllegalStateException();
        }

        public boolean confirmVenueBooking(String bookingId, String userId) {
            PlayingAreaBooking booking = Optional.ofNullable(playingAreaBookings.get(bookingId)).orElseThrow();
            if (!booking.bookingByUserId.equals(userId) || !BookingStatus.HOLD.equals(booking.status)) // use state pattern to avoid if conditions
                throw new IllegalStateException();

           synchronized (booking) {
               if (!BookingStatus.HOLD.equals(booking.status)) // use state pattern to avoid if conditions
                   throw new IllegalStateException();

               booking.confirm();
               return true;
           }
        }

        private boolean isPlayAreaAvailable(String venueOfferingId, LocalDate date, String playAreaId, Instant start, Instant end) {
            return venueOfferingIdToPlayAreaBookingByDate
                    .getOrDefault(venueOfferingId, Collections.emptyMap())
                    .getOrDefault(date, Collections.emptyMap())
                    .getOrDefault(playAreaId, Collections.emptySet())
                    .stream()
                    .map(playingAreaBookings::get)
                    .anyMatch(booking -> !(booking.end.compareTo(start) < 0 || end.compareTo(booking.start) < 0));
        }

        public String createEvent(String bookingId, Instant start, Instant end, int maxParticipants, String createdByUserId) {
            Event event = new Event(UUID.randomUUID().toString(), bookingId, start, end, maxParticipants, createdByUserId);
            events.put(event.id, event);
            return event.id;
        }

        public String createParticipantBookingForEvent(String eventId, String participantUserId) {
            Event event = Optional.ofNullable(events.get(eventId)).orElseThrow();
            EventParticipantBooking booking = new EventParticipantBooking(UUID.randomUUID().toString(), event.id, participantUserId);

            event.holdParticipant();
            eventParticipantBookings.put(booking.id, booking);
            return booking.id;
        }

        public boolean confirmParticipantEventBooking(String bookingId) {
            EventParticipantBooking booking = Optional.ofNullable(eventParticipantBookings.get(bookingId)).orElseThrow();
            Event event = Optional.ofNullable(events.get(booking.eventId)).orElseThrow();
            if (!EventParticipantBookingStatus.HOLD.equals(booking.status) || Instant.now().isAfter(booking.holdExpiryAt))
                throw new IllegalStateException();
            synchronized (booking) {
                if (!EventParticipantBookingStatus.HOLD.equals(booking.status) || Instant.now().isAfter(booking.holdExpiryAt))
                    throw new IllegalStateException();
                booking.confirm();
                return event.confirmParticipant(booking.participantUserId);
            }
        }

        public void updateVenueBookingForEvent(String bookingId, String eventId) {
            Event event = Optional.ofNullable(events.get(eventId)).orElseThrow();
            if (EventStatus.BOOKED.equals(event.status)) // use state pattern instead
                throw new IllegalStateException();

            PlayingAreaBooking booking = Optional.ofNullable(playingAreaBookings.get(bookingId)).orElseThrow();
            if (!BookingStatus.CONFIRMED.equals(booking.status))
                throw new IllegalStateException();
            if (!booking.bookingByUserId.equals(event.createdByUserId))
                throw new IllegalArgumentException();
            if (booking.start.compareTo(event.start) != 0 || booking.end.compareTo(event.end) != 0) // booking to event, 1 to 1
                throw new IllegalArgumentException();

            event.setBookingId(bookingId);
        }
    }

    public enum EventStatus {
        CREATED, BOOKED, CANCELLED
    }

    public enum EventParticipantBookingStatus {
        CREATE, HOLD, CONFIRMED, CANCELLED;
    }

    public class EventParticipantBooking {
        private final String id;
        private final String eventId;
        private final String participantUserId;
        private EventParticipantBookingStatus status = EventParticipantBookingStatus.CREATE;
        private Instant holdExpiryAt;

        public EventParticipantBooking(String id, String eventId, String participantUserId) {
            this.id = id;
            this.eventId = eventId;
            this.participantUserId = participantUserId;
        }

        public void setStatus(EventParticipantBookingStatus status) {
            this.status = status;
        }

        public void hold(long holdForSeconds) {
            setStatus(EventParticipantBookingStatus.HOLD);
            holdExpiryAt = Instant.now().plusSeconds(holdForSeconds);
        }

        public void confirm() {
            setStatus(EventParticipantBookingStatus.CONFIRMED);
            holdExpiryAt = null;
        }
    }

    public class Event {
        private final String id;
        private String bookingId;
        private String createdByUserId;
        private final Instant start;
        private final Instant end;
        private final Semaphore availableSpots;
        private final Set<String> participantUserIds = new HashSet<>();
        private final EventStatus status = EventStatus.CREATED;

        public Event(String id, String bookingId, Instant start, Instant end, int totalSpots, String createdByUserId) {
            this.id = id;
            this.bookingId = bookingId;
            this.createdByUserId = createdByUserId;
            this.start = start;
            this.end = end;
            this.availableSpots = new Semaphore(totalSpots);
        }

        public void setBookingId(String bookingId) {
            this.bookingId = bookingId;
        }

        public void holdParticipant() {
            availableSpots.tryAcquire();
        }

        public boolean confirmParticipant(String participantUserId) {
            if (!participantUserIds.add(participantUserId)) {
                availableSpots.release();
                return false;
            }
            return true;
        }
    }

    public class VenueSearchResponse {
        private final String venueName;
        private final String address;
        private final String venueOfferingId;

        public VenueSearchResponse(String venueName, String address, String venueOfferingId) {
            this.venueName = venueName;
            this.address = address;
            this.venueOfferingId = venueOfferingId;
        }
    }

    public class VenueOfferingAvailabilityForDateResponse {
        private final LocalTime startTimeOfDay;
        private final LocalTime endTimeOfDay;
        private final List<PlayingAreaAvailabilityResponse> playingAreaResponses;

        public VenueOfferingAvailabilityForDateResponse(LocalTime startTimeOfDay, LocalTime endTimeOfDay, List<PlayingAreaAvailabilityResponse> playingAreaAvailabilityRespons) {
            this.startTimeOfDay = startTimeOfDay;
            this.endTimeOfDay = endTimeOfDay;
            this.playingAreaResponses = playingAreaAvailabilityRespons;
        }
    }

    private class PlayingAreaAvailabilityResponse {
        private final String playingAreaId;
        private final String playingAreaName;
        private final List<BookedIntervals> unavailableIntervals;
        private final Double pricePerHour;

        private PlayingAreaAvailabilityResponse(String playingAreaId, String playingAreaName, List<BookedIntervals> unavailableIntervals, Double pricePerHour) {
            this.playingAreaId = playingAreaId;
            this.playingAreaName = playingAreaName;
            this.unavailableIntervals = unavailableIntervals;
            this.pricePerHour = pricePerHour;
        }
    }

    public class BookedIntervals {
        private final Instant start;
        private final Instant end;

        public BookedIntervals(Instant start, Instant end) {
            this.start = start;
            this.end = end;
        }
    }

    void main() {

    }

}
