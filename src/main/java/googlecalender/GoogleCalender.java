package googlecalender;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class GoogleCalender {

    static class Settings {
        String timezone;
        String language;
        String dateFormat;
    }

    class User {
        private final Long id;
        private Calender defaultCalender;
        private final Collection<Calender> calenders = new CopyOnWriteArrayList<>();

        public User(Long id) {
            this.id = id;
        }

        public void setDefaultCalender(Calender defaultCalender) {
            this.defaultCalender = defaultCalender;
        }
    }

    class UserManager {
        private final Map<Long, User> users = new ConcurrentHashMap<>();
        private final AtomicLong atomicInteger = new AtomicLong();
        private final CalenderManager calenderManager;

        public UserManager(final CalenderManager calenderManager) {
            this.calenderManager = calenderManager;
        }

        public User createUser() {
            Long id = atomicInteger.incrementAndGet();
            User user = users.computeIfAbsent(id, identity -> new User(identity));
            user.setDefaultCalender(this.calenderManager.create(user.id));
            return user;
        }
    }

    class CalenderManager {
        private final Map<Long, Calender> calenders = new ConcurrentHashMap<>();
        private final AtomicLong atomicInteger = new AtomicLong();
        private final Settings DEFAULT_SETTINGS;

        public CalenderManager(final Settings defaultSettings) {
            DEFAULT_SETTINGS = defaultSettings;
        }

        public Calender create(final Long userId) {
            Long id = atomicInteger.incrementAndGet();
            return calenders.computeIfAbsent(id, identity -> new Calender(identity, userId, DEFAULT_SETTINGS));
        }
    }

    class Calender {
        private final Long id;
        private final Long userId;
        private final Settings settings;
        private final Map<Long, Event> events = new ConcurrentHashMap<>();

        public Calender(Long id, Long userId, Settings settings) {
            this.id = id;
            this.userId = userId;
            this.settings = settings;
        }

        public void addEvent(final Event event) {
            this.validate(event);
            if (events.containsKey(event.id))
                throw new IllegalArgumentException("Event already added in calender");
            this.events.putIfAbsent(event.id, event);
        }

        private void validate(final Event event) {

        }

        public void updateAttendeeStatus(final Long eventId, final AttendeeStatus status) {
            Event event = events.get(eventId);
            if (event == null)
                throw new IllegalArgumentException();
            event.updateAttendeeStatus(this.userId, status);
        }
    }

    enum EventStatus {
        SCHEDULED, CANCELED, RESCHEDULED
    }

    interface IEvent {

        void addAttendee(Attendee attendee);

        void updateAttendeeStatus(Long userId, AttendeeStatus attendeeStatus);

        void setEventStatus(EventStatus eventStatus);
    }

    class Event implements IEvent {
        private final Long id;
        private final Map<Long, Attendee> calenderIdToAttendee = new ConcurrentHashMap<>();
        private final Attendee creater;
        private final Date start;
        private final Date end;
        private final RecurringEvent recurringEvent;
        private EventStatus eventStatus;

        public Event(Long id, Attendee creater, Date start, Date end, RecurringEvent recurringEvent) {
            this.id = id;
            this.recurringEvent = recurringEvent;
            this.calenderIdToAttendee.put(creater.calenderId, creater);
            this.creater = creater;
            this.start = start;
            this.end = end;
            this.eventStatus = EventStatus.SCHEDULED;
        }

        @Override
        public void addAttendee(Attendee attendee) {
            if (calenderIdToAttendee.containsKey(attendee.calenderId))
                throw new IllegalArgumentException();
            calenderIdToAttendee.putIfAbsent(attendee.calenderId, attendee);
        }

        @Override
        public void updateAttendeeStatus(Long calenderId, AttendeeStatus attendeeStatus) {
            if (System.currentTimeMillis() > end.getTime())
                throw new IllegalArgumentException();

            Attendee attendee = calenderIdToAttendee.get(calenderId);
            attendee.setStatus(attendeeStatus);
        }

        @Override
        public void setEventStatus(EventStatus eventStatus) {
            this.eventStatus = eventStatus;
        }

        public Date getEnd() {
            return end;
        }
    }

    class EventManager {
        private final Map<Long, IEvent> events = new ConcurrentHashMap<>();
        private final AtomicLong atomicInteger = new AtomicLong();

        public IEvent create(final Attendee creator, Date start, Date end, RecurringEvent recurringEvent) {
            Long id = atomicInteger.incrementAndGet();
            return events.computeIfAbsent(id, identity -> new Event(identity, creator, start, end, recurringEvent));
        }
    }

    public class RecurringEvent implements IEvent  {
        private Long id;
        private Long durationInMilliseconds;
        private final Collection<Event> events = new CopyOnWriteArrayList<>();
        private final EventManager eventManager;
        private EventStatus eventStatus = EventStatus.SCHEDULED;

        public RecurringEvent(Long id, Attendee creator, Date start, Date end, Long durationInMilliseconds, Long recurrenceGapInMilliseconds, EventManager eventManager) {
            this.id = id;
            this.eventManager = eventManager;
            Long cur = start.getTime();
            while (cur < end.getTime()) {
                events.add((Event) this.eventManager.create(creator, new Date(cur), new Date(cur + durationInMilliseconds), this));
                cur += recurrenceGapInMilliseconds;
            }
        }

        @Override
        public void addAttendee(Attendee attendee) {
            getCurrentOrFutureEventsStream().forEach(event -> event.addAttendee(attendee));
        }

        @Override
        public void updateAttendeeStatus(Long userId, AttendeeStatus attendeeStatus) {
            getCurrentOrFutureEventsStream().forEach(event -> event.updateAttendeeStatus(userId, attendeeStatus));
        }

        @Override
        public void setEventStatus(EventStatus eventStatus) {
            getCurrentOrFutureEventsStream().forEach(event -> event.setEventStatus(eventStatus));
        }

        private Stream<Event> getCurrentOrFutureEventsStream() {
            return events.stream().filter(event -> event.getEnd().after(new Date()));
        }
    }

    enum AttendeeStatus {
        REQUESTED, ACCEPTED, DECLINED, TENTATIVE
    }

    class Attendee {
        private final Long calenderId;
        private AttendeeStatus status;
        private boolean isRequired;

        public Attendee(final Long calenderId) {
            this.calenderId = calenderId;
        }

        public void setStatus(AttendeeStatus status) {
            this.status = status;
        }
    }

}
