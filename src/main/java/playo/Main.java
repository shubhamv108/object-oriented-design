package playo;

import playo.Main.PlayOService.SlotRange;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
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
    public enum Sport { // move to class
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
        private final int startSlotNumber;
        private final int endSlotNumber;
        private final int slotSizeInMins;
        private Set<String> playingAreaIds = ConcurrentHashMap.newKeySet();

        public VenueOffering(String id, String venueId, Sport sport, int slotSizeInMins, int maxSlots, int startSlotNumber, int endslotNumber) {
            this.id = id;
            this.venueId = venueId;
            this.sport = sport;
            this.startSlotNumber = startSlotNumber;
            this.endSlotNumber = endslotNumber;
            this.slotSizeInMins = slotSizeInMins;
        }
    }

    public enum SlotBookingStatus {
        CREATED, HOLD, CONFIRMED, CANCELLED;
    }

    public record Slot(int slotNumber, SlotBookingStatus status) {}

    public class PlayingArea {
        private final String id;
        private final String venueOfferingId;
        private String name; // BadmintonCourt1, FootBallGround1
        private Double pricePerHour;

        private final Map<LocalDate, PlayingAreaSchedule> schedules = new ConcurrentHashMap<>();

        public PlayingArea(String id, String venueOfferingId, String name, Double pricePerHour) {
            this.id = id;
            this.venueOfferingId = venueOfferingId;
            this.name = name;
            this.pricePerHour = pricePerHour;
        }

        public PlayingAreaSchedule getSchedule(LocalDate date) {
            return schedules.computeIfAbsent(date, d -> new PlayingAreaSchedule());
        }

        public List<Slot> getSlots(LocalDate date, SlotRange range) {
            PlayingAreaSchedule schedule = getSchedule(date);
            BitSet held = schedule.heldSnapshot();
            BitSet confirmed = schedule.confirmedSnapshot();
            List<Slot> slots = new ArrayList<>();

            for (int i = range.startIndex; i <= range.endIndex; ++i) {
                SlotBookingStatus status;
                if (confirmed.get(i)) {
                    status = SlotBookingStatus.CONFIRMED;
                } else if (held.get(i)) {
                    status = SlotBookingStatus.HOLD;
                } else {
                    status = SlotBookingStatus.CREATED;
                }

                slots.add(new Slot(i, status));
            }
            return slots;
        }
    }

    public class User {
        private String id;
    }

    public enum BookingStatus {
        CREATE, HOLD, CONFIRM, CANCEL, FAIL
    }

    public class PlayingAreaBooking {
        private final String id;
        private final String playingAreaId;
        private final LocalDate date;
        private final SlotRange range;
        private final String bookingByUserId;
        private Instant holdExpiryAt;
        private String eventId;
        private final Sport sport;

        private BookingStatus status = BookingStatus.CREATE;

        public PlayingAreaBooking(String id, String playingAreaId, LocalDate date, SlotRange range, String bookingByUserId, Sport sport) {
            this.id = id;
            this.playingAreaId = playingAreaId;
            this.date = date;
            this.range = range;
            this.bookingByUserId = bookingByUserId;

            this.sport = sport;
        }

        public void setStatus(BookingStatus status) {
            this.status = status;
        }

        public void setHoldExpiryAt(Instant instant) {
            this.holdExpiryAt = instant;
        }

        public Instant getHoldExpiryAt() {
            return holdExpiryAt;
        }
    }

    public class PlayingAreaSchedule {
        private final ReentrantLock lock = new ReentrantLock();

        // Slots held while payment is in progress
        private final BitSet heldSlots = new BitSet(48);
        // Slots successfully booked
        private final BitSet confirmedSlots = new BitSet(48);

        public void lock() {
            lock.lock();
        }

        public void unlock() {
            lock.unlock();
        }

        public boolean isAvailable(SlotRange range) {
            return heldSlots.get(range.startIndex, range.endIndex + 1).isEmpty()
                    && confirmedSlots.get(range.startIndex, range.endIndex + 1).isEmpty();
        }

        public void hold(SlotRange range) {
            heldSlots.set(range.startIndex, range.endIndex + 1);
        }

        public void confirm(SlotRange range) {
            heldSlots.clear(range.startIndex, range.endIndex + 1);
            confirmedSlots.set(range.startIndex, range.endIndex + 1);
        }

        public void release(SlotRange range) {
            heldSlots.clear(range.startIndex, range.endIndex + 1);
            confirmedSlots.clear(range.startIndex, range.endIndex + 1);
        }

        public BitSet heldSnapshot() {
            return (BitSet) heldSlots.clone();
        }

        public BitSet confirmedSnapshot() {
            return (BitSet) confirmedSlots.clone();
        }
    }

    public class PlayOService {
        private final Map<String, Address> addresses = new HashMap<>();
        private final Map<String, Venue> venues = new HashMap<>();
        private final Map<String, VenueOffering> venueOfferings = new HashMap<>();
        private final Map<String, PlayingArea> playingAreas = new HashMap<>();
        private final Map<String, PlayingAreaBooking> playingAreaBookings = new HashMap<>();

        private final Map<Sport, Set<String>> sportVenueOfferings = new HashMap<>();

        private final Map<String, Event> events = new HashMap<>();
        private final Map<String, EventParticipantBooking> eventParticipantBookings = new HashMap<>();
        private final Map<String, Set<String>> playAreaBookingToEventIds = new HashMap<>();

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
//            venueOfferings.put(id, new VenueOffering(id, venueId, sport, start, end, , ));
            return id;
        }

        public String addPlayingArea(String name, String venueOfferingId, Double pricePerHour) {
            String id = UUID.randomUUID().toString();
            VenueOffering venueOffering = Optional.ofNullable(venueOfferings.get(venueOfferingId)).orElseThrow();
            playingAreas.put(id, new PlayingArea(id, venueOfferingId, name, pricePerHour));
            venueOffering.playingAreaIds.add(id);
            return id;
        }

        public Collection<VenueSearchResponse> getVenuesForSport(Sport sport) {
            return sportVenueOfferings.getOrDefault(sport, Collections.emptySet())
                    .stream()
                    .map(venueOfferingId -> {
                        VenueOffering venueOffering = venueOfferings.get(venueOfferingId);
                        Venue venue = venues.get(venueOffering.venueId);
                        return new VenueSearchResponse(venue.name, addresses.get(venue.addressId).toString(), venueOffering.id);
                    })
                    .toList();
        }

        public Map<String, List<Slot>> getAvailabilityForVenue(String venueOfferingId, LocalDate date) {
            VenueOffering venueOffering = Optional.ofNullable(venueOfferings.get(venueOfferingId)).orElseThrow();
            SlotRange range = new SlotRange(venueOffering.startSlotNumber, venueOffering.endSlotNumber);
            return venueOffering.playingAreaIds.stream()
                    .map(playingAreas::get)
                    .collect(Collectors.toMap(
                            playingArea -> playingArea.id,
                            playingArea -> playingArea.getSlots(date, range)));
        }

        public record SlotRange(int startIndex, int endIndex) {}

        public String createVenueBooking(String playingAreaId, LocalDate date, SlotRange slotRange, String userId) {
            PlayingArea playingArea = Optional.ofNullable(playingAreas.get(playingAreaId)).orElseThrow();
            VenueOffering venueOffering = Optional.ofNullable(venueOfferings.get(playingArea.venueOfferingId)).orElseThrow();

            if (slotRange.startIndex < venueOffering.startSlotNumber
                    || slotRange.endIndex > venueOffering.endSlotNumber
                    || slotRange.startIndex > slotRange.endIndex)
                throw new IllegalArgumentException();

            PlayingAreaSchedule schedule = playingArea.getSchedule(date);

            try {
                schedule.lock();
                if (!schedule.isAvailable(slotRange))
                    throw new IllegalStateException("Slots already booked");

                schedule.hold(slotRange);
                PlayingAreaBooking booking = new PlayingAreaBooking(
                    UUID.randomUUID().toString(),
                    playingAreaId,
                    date,
                    slotRange,
                    userId,
                    venueOffering.sport);

                booking.setStatus(BookingStatus.CREATE);
                booking.setHoldExpiryAt(
                        Instant.now().plusSeconds(300));

                playingAreaBookings.put(booking.id, booking);
                return booking.id;
            } finally {
                schedule.unlock();
            }
        }

        public boolean confirmVenueBooking(String bookingId) {
            PlayingAreaBooking booking = Optional.ofNullable(playingAreaBookings.get(bookingId)).orElseThrow();
            if (BookingStatus.CONFIRM.equals(booking.status))
                return true;
            if (!BookingStatus.CREATE.equals(booking.status))
                throw new IllegalStateException();

            PlayingArea playingArea = Optional.ofNullable(playingAreas.get(booking.playingAreaId)).orElseThrow();
            PlayingAreaSchedule schedule = playingArea.getSchedule(booking.date);

            try {
                schedule.lock();
                // Another thread may have already confirmed/cancelled
                if (BookingStatus.CONFIRM.equals(booking.status))
                    return true;

                if (Instant.now().isAfter(booking.getHoldExpiryAt())) {
                    schedule.release(booking.range);
                    booking.setStatus(BookingStatus.FAIL);
                    throw new IllegalStateException("Booking hold expired");
                }

                if (!BookingStatus.CREATE.equals(booking.status))
                    throw new IllegalStateException();

                schedule.confirm(booking.range);
                booking.setStatus(BookingStatus.CONFIRM);
            } finally {
                schedule.unlock();
            }
            return true;
        }

        public void cancelBooking(String bookingId) {
            PlayingAreaBooking booking = Optional.ofNullable(playingAreaBookings.get(bookingId)).orElseThrow();
            PlayingAreaSchedule schedule = playingAreas.get(booking.playingAreaId).getSchedule(booking.date);

            try {
                schedule.lock();
                if (BookingStatus.CANCEL.equals(booking.status))
                    return;

                schedule.release(booking.range);
                booking.setStatus(BookingStatus.CANCEL);
            } finally {
                schedule.unlock();
            }
        }

        public String createEvent(String bookingId, Instant start, Instant end, int maxParticipants, String createdByUserId, LocalDate date, SlotRange preferredSlots) {
            Event event = new Event(UUID.randomUUID().toString(), bookingId, maxParticipants, createdByUserId, date, preferredSlots);
            events.put(event.id, event);
            return event.id;
        }

        public String joinEvent(String eventId, String participantUserId) {
            Event event = Optional.ofNullable(events.get(eventId)).orElseThrow();

            if (!event.availableSpots.tryAcquire())
                throw new IllegalStateException("No spots available");

            EventParticipantBooking booking = new EventParticipantBooking(
                UUID.randomUUID().toString(),
                eventId,
                participantUserId);

            booking.hold(300);
            eventParticipantBookings.put(booking.id, booking);

            return booking.id;
        }

        public boolean confirmParticipantBooking(String bookingId) {
            EventParticipantBooking booking = Optional.ofNullable(eventParticipantBookings.get(bookingId)).orElseThrow();
            if (BookingStatus.CONFIRM.equals(booking.status))
                return true;

            if (!BookingStatus.HOLD.equals(booking.status))
                throw new IllegalStateException();

            if (Instant.now().isAfter(booking.holdExpiryAt)) {
                cancelParticipantBooking(booking.id);
                throw new IllegalStateException("Hold expired");
            }

            Event event = Optional.ofNullable(events.get(booking.eventId)).orElseThrow();

            synchronized (event) {
                if (!event.participantUserIds.add(booking.participantUserId))
                    throw new IllegalStateException("Already joined");

                booking.setStatus(BookingStatus.CONFIRM);
                return true;
            }
        }

        public void cancelParticipantBooking(String bookingId) {
            EventParticipantBooking booking = Optional.ofNullable(eventParticipantBookings.get(bookingId)).orElseThrow();
            Event event = Optional.ofNullable(events.get(booking.eventId)).orElseThrow();

            if (booking.status == BookingStatus.CONFIRM) {
                synchronized (event) {
                    event.participantUserIds.remove(booking.participantUserId);
                }
            }

            event.availableSpots.release();
            booking.setStatus(BookingStatus.CANCEL);
        }

        public void attachBookingToEvent(String eventId, String bookingId) {
            Event event = Optional.ofNullable(events.get(eventId)).orElseThrow();
            PlayingAreaBooking booking = Optional.ofNullable(playingAreaBookings.get(bookingId)).orElseThrow();
            if (event.bookingId != null)
                throw new IllegalStateException("Event already linked to a booking.");

            if (booking.eventId != null)
                throw new IllegalStateException("Booking already linked to an event.");

            // Optional business validations
            if (!event.sport.equals(booking.sport))
                throw new IllegalArgumentException("Sport mismatch.");

            if (!event.date.equals(booking.date))
                throw new IllegalArgumentException("Date mismatch.");

            event.bookingId = booking.id;
            booking.eventId = event.id;
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
        private BookingStatus status = BookingStatus.CREATE;
        private Instant holdExpiryAt;

        public EventParticipantBooking(String id, String eventId, String participantUserId) {
            this.id = id;
            this.eventId = eventId;
            this.participantUserId = participantUserId;
        }

        public void setStatus(BookingStatus status) {
            this.status = status;
        }

        public void hold(long holdForSeconds) {
            setStatus(BookingStatus.HOLD);
            holdExpiryAt = Instant.now().plusSeconds(holdForSeconds);
        }

        public void confirm() {
            setStatus(BookingStatus.CONFIRM);
            holdExpiryAt = null;
        }
    }

    public class Event {
        private final String id;
        private String bookingId;
        private final String organizerId;
        private Sport sport;
        private final LocalDate date;
        private final SlotRange preferredSlot;
        private String venueOfferingId;
        private final Semaphore availableSpots; // alternate use AtomicInteger + ReentrantLock + HashSet
        private final Set<String> participantUserIds = ConcurrentHashMap.newKeySet();
        private final EventStatus status = EventStatus.CREATED;

        public Event(String id, String bookingId, int totalSpots, String organizerId, LocalDate date, SlotRange preferredSlot) {
            this.id = id;
            this.bookingId = bookingId;
            this.organizerId = organizerId;
            this.availableSpots = new Semaphore(totalSpots);
            this.date = date;
            this.preferredSlot = preferredSlot;
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
