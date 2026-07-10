package meetinroom;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.locks.ReentrantReadWriteLock;


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
        private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        private final ConcurrentSkipListSet<SlotRange> bookedSlots = new ConcurrentSkipListSet<>(); // or BitMap

        public boolean isAvailable(SlotRange range) {
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

        public boolean book(SlotRange range) {
            try {
                lock.writeLock().lock();
                if (isAvailable(range)) {
                    bookedSlots.add(range);
                    return true;
                } else {
                    return false;
                }
            } finally {
                lock.writeLock().unlock();
            }

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

    public record Meeting(String id, String roomId, MeetingStatus status, SlotRange slotRange) {}

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

        public List<String> findRooms(int floor, SlotRange slotRange, int capacity) {
            return floorRooms.getOrDefault(floor, Collections.emptySet()).stream()
                    .map(roomid -> rooms.get(roomid))
                    .filter(room -> room.capacity >= capacity)
                    .filter(room -> !room.isAvailable(slotRange))
                    .map(room -> room.id)
                    .toList();
        }

        public Meeting bookRoom(int floor, SlotRange slotRange, int capacity) {
            for (String id : floorRooms.getOrDefault(floor, Collections.emptySet())) {
                Room room = rooms.get(id);
                if (room.capacity < capacity || !room.isAvailable(slotRange))
                    continue;
                Meeting meeting = bookRoom(id, slotRange);
                if (meeting != null)
                    return meeting;
            }
            return null;
        }

        public Meeting bookRoom(String roomId, SlotRange slotRange) {
            // validate slot range b/w 1 to 1440
            Room room = Optional.ofNullable(rooms.get(roomId)).orElseThrow();

            if (room.book(slotRange))
                return new Meeting(UUID.randomUUID().toString(), room.id, MeetingStatus.CONFIRM, slotRange);
            return null;
        }
    }

    public static void main(String[] args) {
        Solution sol = new Solution();
        MeetingService serv = sol.new MeetingService();
        serv.addRoom(1, 10);
        serv.addRoom(1, 10);

        var sr = sol.new SlotRange();
        sr.start = 10; sr.end = 100;
        // rom1
        Meeting meeting = serv.bookRoom(1, sr, 7);
        System.out.print(meeting.id + " ");
        System.out.println(meeting.slotRange.start + " " + meeting.slotRange.end + " " + meeting.roomId);

        // rom 2
        meeting = serv.bookRoom(1, sr, 7);
        System.out.print(meeting.id + " ");
        System.out.println(meeting.slotRange.start + " " + meeting.slotRange.end + " " + meeting.roomId);

        // room1
        var sr2 = sol.new SlotRange();
        sr2.start = 1000; sr2.end = 1200;
         meeting = serv.bookRoom(1, sr2, 7);
        System.out.print(meeting.id + " ");
         System.out.println(meeting.slotRange.start + " " + meeting.slotRange.end + " " + meeting.roomId);
    }

}


