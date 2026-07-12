package meetingroom;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;


/*****
 Room booking system :
 Support multiple meeting rooms, possibly across multiple floors.
 Allow users to book rooms for specific time slots.
 Support room booking for different capacity.
 Prevent double booking (no overlapping reservations).
 Support querying available rooms for a given time range.
 Allow cancellation of bookings.
 user1:[13,14]

 Optional
 Support recurring reservations.
 Support modification of bookings
 Support different room capacities and equipment.
 user1 : [1300,1415]
 ******/

/**
 Fn
 2. Multiple rooms at multiple locations and floor
 3. These rooms can be searched for availability for given time range
 4. Room can be booked by user specific for any tme range at minute level.
 5. Cancellation of room is allowed.

 non fn
 1. Modular / Extensible
 2. Scalable

 Actors
 User

 CoreEntities
 Room
 SlotRange(startSlotNumber, endSlotNumber)
 Meeting/Booking
 MeetingService
 */

public class Solution {

    public class Room {
        private String id;
        private int floor;
        private int capacity;
        private final Map<LocalDate, RoomBookingStrategy> schedules = new ConcurrentHashMap<>();

        public boolean isAvailable(LocalDate date, SlotRange range) {
            return schedule(date).isAvailable(range);
        }

        public boolean book(LocalDate date, SlotRange range) {
            return schedule(date).book(range);
        }

        public boolean cancel(LocalDate date, SlotRange range) {
            return schedule(date).cancel(range);
        }

        private RoomBookingStrategy schedule(LocalDate date) {
            return schedules.computeIfAbsent(date, d -> new RangeSetRoomBookingStrategy());
        }
    }

    public interface RoomBookingStrategy {
        boolean isAvailable(SlotRange range);
        boolean book(SlotRange range);
        boolean cancel(SlotRange range);
    }

    // Time: O(n), Space: O(bookings), Concurrency: DayLevel, Best for interview - small code, production
    public class RangeSetRoomBookingStrategy implements RoomBookingStrategy {
        private final StampedLock lock = new StampedLock();
        private final ConcurrentSkipListSet<SlotRange> bookedSlots = new ConcurrentSkipListSet<>();

        @Override
        public boolean isAvailable(SlotRange range) {
            long stamp = lock.tryOptimisticRead();
            boolean available = isAvailableInternal(range);

            if (lock.validate(stamp)) {
                return available;
            }

            stamp = lock.readLock();
            try {
                return isAvailableInternal(range);
            } finally {
                lock.unlockRead(stamp);
            }
        }

        @Override
        public boolean book(SlotRange range) {
            long stamp = lock.writeLock();
            try {
                if (!isAvailableInternal(range))
                    return false;
                bookedSlots.add(range);
                return true;
            } finally {
                lock.unlockWrite(stamp);
            }
        }

        @Override
        public boolean cancel(SlotRange range) {
            long stamp = lock.writeLock();

            try {
                return bookedSlots.remove(range);
            } finally {
                lock.unlockWrite(stamp);
            }
        }

        private boolean isAvailableInternal(SlotRange range) {
            SlotRange floor = bookedSlots.floor(range);
            if (floor != null && floor.end >= range.start) {
                return false;
            }

            SlotRange ceiling = bookedSlots.ceiling(range);
            if (ceiling != null && ceiling.start <= range.end) {
                return false;
            }

            return true;
        }
    }

    // Time: O(minutes), Space: O(1440 bits/day), Concurrency: word level,
    public class BitSetRoomBookingStrategy implements RoomBookingStrategy {
        private static final int BITS_PER_SET = 64;

        private final ReentrantReadWriteLock[] locks;
        private final BitSet[] bitSets;

        public BitSetRoomBookingStrategy(int maxSlotNumber) {
            int count = Math.ceilDiv(maxSlotNumber, BITS_PER_SET);

            locks = new ReentrantReadWriteLock[count];
            bitSets = new BitSet[count];

            for (int i = 0; i < count; i++) {
                locks[i] = new ReentrantReadWriteLock();
                bitSets[i] = new BitSet(BITS_PER_SET);
            }
        }

        @Override
        public boolean isAvailable(SlotRange range) {
            int[] segmentRange = getBitSetRangeForSlot(range);
            readLock(segmentRange[0], segmentRange[1]);

            try {
                for (int slot = range.start; slot <= range.end; ++slot) {
                    int segment = slot / BITS_PER_SET;
                    int offset = slot % BITS_PER_SET;

                    if (bitSets[segment].get(offset))
                        return false;
                }
                return true;
            } finally {
                readUnlock(segmentRange[0], segmentRange[1]);
            }
        }

        @Override
        public boolean book(SlotRange range) {
            int[] segmentRange = getBitSetRangeForSlot(range);
            writeLock(segmentRange[0], segmentRange[1]);

            try {
                for (int slot = range.start; slot <= range.end; ++slot) {
                    int segment = slot / BITS_PER_SET;
                    int offset = slot % BITS_PER_SET;

                    if (bitSets[segment].get(offset))
                        return false;
                }

                for (int slot = range.start; slot <= range.end; ++slot) {
                    int segment = slot / BITS_PER_SET;
                    int offset = slot % BITS_PER_SET;
                    bitSets[segment].set(offset);
                }
                return true;
            } finally {
                writeUnlock(segmentRange[0], segmentRange[1]);
            }
        }

        @Override
        public boolean cancel(SlotRange range) {
            int[] segmentRange = getBitSetRangeForSlot(range);
            writeLock(segmentRange[0], segmentRange[1]);
            try {
                for (int slot = range.start; slot <= range.end; slot++) {
                    int segment = slot / BITS_PER_SET;
                    int offset = slot % BITS_PER_SET;
                    bitSets[segment].clear(offset);
                }
                return true;
            } finally {
                writeUnlock(segmentRange[0], segmentRange[1]);
            }
        }

        private int[] getBitSetRangeForSlot(SlotRange range) {
            return new int[] {
                range.start / BITS_PER_SET,
                range.end / BITS_PER_SET
            };
        }

        private void readLock(int start, int end) {
            for (int i = start; i <= end; i++)
                locks[i].readLock().lock();
        }

        private void readUnlock(int start, int end) {
            for (int i = end; i >= start; i--)
                locks[i].readLock().unlock();
        }

        private void writeLock(int start, int end) {
            for (int i = start; i <= end; i++)
                locks[i].writeLock().lock();
        }

        private void writeUnlock(int start, int end) {
            for (int i = end; i >= start; i--)
                locks[i].writeLock().unlock();
        }
    }

    // Time: O(k) (k = affected 64bit words), Space: O(1440 bits/day), Concurrency: word level,
    public class BitMapRoomBookingStrategy implements RoomBookingStrategy {
        private static final int WORD_SIZE = 64;
        private final long[] bitmap;
        private final StampedLock[] locks;

        public BitMapRoomBookingStrategy(int maxSlotNumber) {
            int words = Math.ceilDiv(maxSlotNumber, WORD_SIZE);
            bitmap = new long[words];
            locks = new StampedLock[words];
            for (int i = 0; i < words; i++) {
                locks[i] = new StampedLock();
            }
        }

        @Override
        public boolean isAvailable(SlotRange range) {
            int startWord = word(range.start);
            int endWord = word(range.end);
            long[] optimisticStamps = new long[endWord - startWord + 1];

            // optimistic read
            for (int i = startWord; i <= endWord; i++) {
                optimisticStamps[i - startWord] = locks[i].tryOptimisticRead();
            }

            boolean available = isAvailableInternal(range);
            // validate
            boolean valid = true;
            for (int i = startWord; i <= endWord; i++) {
                if (!locks[i].validate(optimisticStamps[i - startWord])) {
                    valid = false;
                    break;
                }
            }

            if (valid) {
                return available;
            }

            // fallback to read lock
            long[] readStamps = new long[endWord - startWord + 1];
            for (int i = startWord; i <= endWord; i++) {
                readStamps[i - startWord] = locks[i].readLock();
            }

            try {
                return isAvailableInternal(range);
            } finally {
                for (int i = endWord; i >= startWord; i--) {
                    locks[i].unlockRead(readStamps[i - startWord]);
                }
            }
        }

        @Override
        public boolean book(SlotRange range) {
            int startWord = word(range.start);
            int endWord = word(range.end);
            long[] writeStamps = new long[endWord - startWord + 1];

            for (int i = startWord; i <= endWord; i++) {
                writeStamps[i - startWord] = locks[i].writeLock();
            }

            try {
                if (!isAvailableInternal(range))
                    return false;

                reserve(range);
                return true;
            } finally {
                for (int i = endWord; i >= startWord; i--) {
                    locks[i].unlockWrite(writeStamps[i - startWord]);
                }
            }
        }

        @Override
        public boolean cancel(SlotRange range) {
            int startWord = word(range.start);
            int endWord = word(range.end);
            long[] writeStamps = new long[endWord - startWord + 1];

            for (int i = startWord; i <= endWord; ++i)
                writeStamps[i - startWord] = locks[i].writeLock();

            try {
                release(range);
                return true;
            } finally {
                for (int i = endWord; i >= startWord; i--) {
                    locks[i].unlockWrite(writeStamps[i - startWord]);
                }
            }
        }

        private void release(SlotRange range) {
            int current = range.start;
            while (current <= range.end) {
                int word = word(current);
                int offset = offset(current);
                int lastBit = Math.min(range.end, ((word + 1) * WORD_SIZE) - 1);

                bitmap[word] &= ~mask(offset, lastBit - current + 1);

                current = lastBit + 1;
            }
        }

        private boolean isAvailableInternal(SlotRange range) {
            int current = range.start;
            while (current <= range.end) {
                int word = word(current);
                int offset = offset(current);
                int lastBit = Math.min(range.end, ((word + 1) * WORD_SIZE) - 1);
                long mask = mask(offset, lastBit - current + 1);

                if ((bitmap[word] & mask) != 0)
                    return false;

                current = lastBit + 1;
            }

            return true;
        }

        private void reserve(SlotRange range) {
            int current = range.start;
            while (current <= range.end) {
                int word = word(current);
                int offset = offset(current);
                int lastBit = Math.min(range.end, ((word + 1) * WORD_SIZE) - 1);
                bitmap[word] |= mask(offset, lastBit - current + 1);
                current = lastBit + 1;
            }
        }

        private static int word(int slot) {
            return slot / WORD_SIZE;
        }

        private static int offset(int slot) {
            return slot % WORD_SIZE;
        }

        private static long mask(int offset, int bits) {
            if (bits == WORD_SIZE)
                return -1L;
            return ((1L << bits) - 1) << offset;
        }
    }

    // Time: O(k) (k = affected 64bit words), Space: O(1440 bits/day), Concurrency: day level,
    public class BitMapOptimisticReadRoomBookingStrategy implements RoomBookingStrategy {

        private static final int WORD_SIZE = 64;

        private final long[] bitmap;
        private final StampedLock lock = new StampedLock();

        public BitMapOptimisticReadRoomBookingStrategy(int maxSlots) {
            bitmap = new long[Math.ceilDiv(maxSlots, WORD_SIZE)];
        }

        @Override
        public boolean isAvailable(SlotRange range) {

            long stamp = lock.tryOptimisticRead();

            boolean available = isAvailableInternal(range);

            if (lock.validate(stamp))
                return available;

            stamp = lock.readLock();
            try {
                return isAvailableInternal(range);
            } finally {
                lock.unlockRead(stamp);
            }
        }

        @Override
        public boolean book(SlotRange range) {

            while (true) {

                long stamp = lock.tryOptimisticRead();

                if (!isAvailableInternal(range))
                    return false;

                // somebody modified while we were reading
                if (!lock.validate(stamp))
                    continue;

                long writeStamp = lock.tryWriteLock();

                if (writeStamp == 0L)
                    continue;

                try {

                    // verify again under write lock
                    if (!isAvailableInternal(range))
                        return false;

                    reserve(range);

                    return true;

                } finally {
                    lock.unlockWrite(writeStamp);
                }
            }
        }

        @Override
        public boolean cancel(SlotRange range) {

            long stamp = lock.writeLock();

            try {
                release(range);
                return true;
            } finally {
                lock.unlockWrite(stamp);
            }
        }

        private boolean isAvailableInternal(SlotRange range) {

            int current = range.start;

            while (current <= range.end) {

                int word = current / WORD_SIZE;
                int offset = current % WORD_SIZE;

                int lastBit = Math.min(range.end, word * WORD_SIZE + WORD_SIZE - 1);

                int bits = lastBit - current + 1;

                long mask = mask(offset, bits);

                if ((bitmap[word] & mask) != 0)
                    return false;

                current = lastBit + 1;
            }

            return true;
        }

        private void reserve(SlotRange range) {

            int current = range.start;

            while (current <= range.end) {

                int word = current / WORD_SIZE;
                int offset = current % WORD_SIZE;

                int lastBit = Math.min(range.end, word * WORD_SIZE + WORD_SIZE - 1);

                bitmap[word] |= mask(offset, lastBit - current + 1);

                current = lastBit + 1;
            }
        }

        private void release(SlotRange range) {

            int current = range.start;

            while (current <= range.end) {

                int word = current / WORD_SIZE;
                int offset = current % WORD_SIZE;

                int lastBit = Math.min(range.end, word * WORD_SIZE + WORD_SIZE - 1);

                bitmap[word] &= ~mask(offset, lastBit - current + 1);

                current = lastBit + 1;
            }
        }

        private static long mask(int offset, int bits) {

            if (bits == WORD_SIZE)
                return -1L;

            return ((1L << bits) - 1) << offset;
        }
    }

    // cannot atomically reserve multiple words using only CAS
    public class AtomicBitmapRoomBookingStrategy implements RoomBookingStrategy {
        private static final int WORD_SIZE = 64;
        private final AtomicLongArray bitmap;
        public AtomicBitmapRoomBookingStrategy(int maxSlots) {
            bitmap = new AtomicLongArray(Math.ceilDiv(maxSlots, WORD_SIZE));
        }

        @Override
        public boolean isAvailable(SlotRange range) {
            int current = range.start;
            while (current <= range.end) {
                int word = current / WORD_SIZE;
                int offset = current % WORD_SIZE;
                int lastBit = Math.min(range.end, word * WORD_SIZE + WORD_SIZE - 1);
                long mask = mask(offset, lastBit - current + 1);
                if ((bitmap.get(word) & mask) != 0)
                    return false;
                current = lastBit + 1;
            }
            return true;
        }

        @Override
        public boolean book(SlotRange range) {
            int startWord = range.start / WORD_SIZE;
            int endWord = range.end / WORD_SIZE;

            // Pure CAS only supports one word atomically.
            if (startWord != endWord)
                throw new UnsupportedOperationException(
                        "Multi-word booking requires locking or transactional OCC.");

            int offset = range.start % WORD_SIZE;
            int bits = range.end - range.start + 1;
            long mask = mask(offset, bits);
            while (true) {
                long current = bitmap.get(startWord);
                if ((current & mask) != 0)
                    return false;

                long updated = current | mask;
                if (bitmap.compareAndSet(startWord, current, updated))
                    return true;

                // CAS failed because another thread updated the word.
                // Retry.
            }
        }

        @Override
        public boolean cancel(SlotRange range) {
            int startWord = range.start / WORD_SIZE;
            int endWord = range.end / WORD_SIZE;
            if (startWord != endWord)
                throw new UnsupportedOperationException(
                        "Multi-word cancel requires locking or transactional OCC.");

            int offset = range.start % WORD_SIZE;
            int bits = range.end - range.start + 1;
            long mask = mask(offset, bits);
            while (true) {
                long current = bitmap.get(startWord);
                long updated = current & ~mask;
                if (bitmap.compareAndSet(startWord, current, updated))
                    return true;
            }
        }

        private static long mask(int offset, int bits) {
            if (bits == WORD_SIZE)
                return -1L;
            return ((1L << bits) - 1) << offset;
        }
    }

    public enum MeetingStatus {
        CREATE, CONFIRM, CANCEL
    }

    public class SlotRange implements Comparable<SlotRange> {

        int start; int end;

        @Override
        public int compareTo(Solution.SlotRange o) {
            return o.start == start ? end - o.end : start - o.start;
        }
    }

    public enum Frequency {
        DAILY, WEEKLY, MONTHLY
    }

    public record Occurrence(LocalDate date, SlotRange slotRange) {}

    public class RecurrenceEngine {
        public List<Occurrence> generate(LocalDate start, LocalDate end, SlotRange slot, RecurrenceRule rule) {
            List<Occurrence> result = new ArrayList<>();
            LocalDate current = start;
            while (!current.isAfter(end)) {
                switch (rule.frequency) {
                    case DAILY -> {
                        result.add(new Occurrence(current, slot));
                        current = current.plusDays(rule.interval);
                    }
                    case WEEKLY -> {
                        if (rule.daysOfWeek.contains(current.getDayOfWeek()))
                            result.add(new Occurrence(current, slot));
                        current = current.plusDays(1);
                    }
                    case MONTHLY -> {
                        if (current.getDayOfMonth() == rule.dayOfMonth)
                            result.add(new Occurrence(current, slot));
                        current = current.plusDays(1);
                    }
                }

                if (rule.occurrenceCount != null && result.size() == rule.occurrenceCount)
                    break;
            }

            return result;
        }
    }

    public class RecurrenceRule {
        private Frequency frequency;
        private int interval = 1;
        private Set<DayOfWeek> daysOfWeek;
        private Integer weekOfMonth;
        private Integer dayOfMonth;
        private Integer occurrenceCount;
        private LocalDate untilDate;
    }

    public class RecurringMeeting {
        private final String id;
        private String roomId;
        private RecurrenceRule rule;
        private final LocalDate start, end;
        private List<String> meetingIds;

        public RecurringMeeting(String id, LocalDate start, LocalDate end) {
            this.id = id;
            this.start = start;
            this.end = end;
        }
    }

    public class Meeting {
        private final String id;
        private final String roomId;
        private MeetingStatus status;
        private LocalDate date;
        private final SlotRange slotRange;

        public Meeting(String id, String roomId, MeetingStatus status, SlotRange slotRange) {
            this.id = id;
            this.roomId = roomId;
            this.status = status;
            this.slotRange = slotRange;
        }
    }

    public class MeetingService {
        private final Map<String, Room> rooms = new ConcurrentHashMap<>();
        private final Map<String, Meeting> meetings = new ConcurrentHashMap<>();
        private final Map<Integer, Set<String>> floorRooms = new ConcurrentHashMap<>();

        public String addRoom(int floor, int capacity) {
            Room room = new Room();
            room.id = UUID.randomUUID().toString();
            room.floor = floor;
            room.capacity = capacity;
            rooms.put(room.id, room);
            floorRooms.computeIfAbsent(room.floor, e -> new HashSet<>()).add(room.id);
            return room.id;
        }

        public List<String> findRooms(LocalDate date, int floor, SlotRange slotRange, int capacity) {
            return floorRooms.getOrDefault(floor, Collections.emptySet()).stream()
                    .map(rooms::get)
                    .filter(room -> room.capacity >= capacity)
                    .filter(room -> !room.isAvailable(date, slotRange))
                    .map(room -> room.id)
                    .toList();
        }

//        public String createRecurrenceMeeting() {}

        public Meeting createMeeting(LocalDate date, int floor, SlotRange slotRange, int capacity) {
            // validate slot range b/w 1 to 1440
            for (String id : floorRooms.getOrDefault(floor, Collections.emptySet())) {
                Room room = rooms.get(id);
                if (room.capacity < capacity || !room.isAvailable(date, slotRange))
                    continue;
                Meeting meeting = bookRoom(id, date, slotRange);
                if (meeting != null)
                    return meeting;
            }
            return null;
        }

        private Meeting bookRoom(String roomId, LocalDate date, SlotRange slotRange) {
            Room room = Optional.ofNullable(rooms.get(roomId)).orElseThrow();
            if (room.book(date, slotRange)) {
                var meeting = new Meeting(UUID.randomUUID().toString(), room.id, MeetingStatus.CONFIRM, slotRange);
                meetings.put(meeting.id, meeting);
                return meeting;
            }
            return null;
        }

        private Meeting cancelMeeting(String meetingId) {
            Meeting meeting = Optional.ofNullable(meetings.get(meetingId))
                    .filter(meet -> MeetingStatus.CONFIRM.equals(meet.status))
                    .orElseThrow();
            Room room = Optional.ofNullable(rooms.get(meeting.roomId)).orElseThrow();
            if (room.cancel(meeting.date, meeting.slotRange)) {
                meeting.status = MeetingStatus.CANCEL;
            }
            return null;
        }
    }

    void main(String[] args) {
        Solution sol = new Solution();
        MeetingService serv = sol.new MeetingService();
        serv.addRoom(1, 10);
        serv.addRoom(1, 10);

        var sr = sol.new SlotRange();
        sr.start = 10; sr.end = 100;
        // rom1
        Meeting meeting = serv.createMeeting(LocalDate.now(), 1, sr, 7);
        System.out.print(meeting.id + " ");
        System.out.println(meeting.slotRange.start + " " + meeting.slotRange.end + " " + meeting.roomId);

        // rom 2
        meeting = serv.createMeeting(LocalDate.now(), 1, sr, 7);
        System.out.print(meeting.id + " ");
        System.out.println(meeting.slotRange.start + " " + meeting.slotRange.end + " " + meeting.roomId);

        // room1
        var sr2 = sol.new SlotRange();
        sr2.start = 1000; sr2.end = 1200;
         meeting = serv.createMeeting(LocalDate.now(), 1, sr2, 7);
        System.out.print(meeting.id + " ");
         System.out.println(meeting.slotRange.start + " " + meeting.slotRange.end + " " + meeting.roomId);
    }

}


