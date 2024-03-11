RoomType(Enum)
+ King
+ Queen

RoomStatus(Enum)
+ IN_SERVICE
+ MAINTANANCE

RoomAvailabilityStrategyType(Enum)
+ SAME_ROOM_FOR_ALL_DAYS
+ ALL_ROOM_SAME_DAY

Hotel
- hotelId: String
- rooms: Map<RoomType, List<Room>>
+ addRoom(roomType: RoomType): void
+ getInServiceRoomIds(roomType: RoomType): List<String>

HotelManager
- hotels: Map<String, Hotel>
- HotelManager()
+ getManager(): HotelManager
+ addHotel(cityId: String, hotelId: String)
+ addRoom(hotelId: String, roomType: RoomType)
+ getInServiceRoomIds(roomType: RoomType): Lis<String>

Filter
- cityId: String
- roomType: RoomType
- start: Instant
- end: Instant
- roomAvailabilityStrategyType: RoomAvailabilityStrategyType
- roomCount: int

SearchService
- cityIdToHotelId: Map<String, List<String>>
- SearchService()
+ getService(): SearchService
+ addHotelInCity(cityId: String, hotelId: String): void
+ search(filter: Filter): List<Hotel>

RoomTypeInventory
- hotelId: String
- roomTypeBooked: Map<RoomType, Map<LocalDate, Integer>>
+ RoomTypeInventory(hotelId: String)
+ incrementBookedCount(roomType: RoomType, date: LocalDate): void
+ isRoomAvailable(roomType: RoomType, dates: List<LocalDate>): Boolean

AbstractRoomAvailabilityStrategy
- roomIdDateBookings: Map<String, Map<LocalDate, Set<String>>>
- hotelIdToRoomInventory: Map<String, RoomTypeInventory>
+ isRoomAvailable(roomId: String, start: Instant, end: Instant, roomCount: int): Boolean
+ abstract isRoomTypeAvailable(roomType: RoomType, dates: List<LocalDate>, roomCount: int)
+ addBooking(booking: Booking): void
+ getAvailableRoomIds(hotelId: String, roomType: RoomType, start: Instant, end: Instant, roomCount: int): List<String>
+ abstract getAvailableRoomIdsWithDates(hotelId: String, roomType: RoomType, dates: List<LocalDate>, roomCount: int): Map<String: Set<LocalDate>>

SameRoomAllDaysRoomAvailabilityStrategy(AbstractRoomAvailabilityStrategy):
+ isRoomTypeAvailable(roomType: RoomType, start: date: List<LocalDate>, roomCount: int)
+ getAvailableRoomIdsWithDates(hotelId: String, roomType: RoomType, dates: List<LocalDate>, roomCount: int): Map<String: Set<LocalDate>>

AnyRoomAnyDayRoomAvailabilityStrategy(AbstractRoomAvailabilityStrategy):
+ isRoomTypeAvailable(roomType: RoomType, start: date: List<LocalDate>, roomCount: int)
+ getAvailableRoomIdsWithDates(hotelId: String, roomType: RoomType, dates: List<LocalDate>, roomCount: int): Map<String: Set<LocalDate>>

RoomAvailabilityStrategyFactory:
- strategies: Map<RoomAvailabilityStrategyType, AbstractRoomAvailabilityStrategy>
- RoomAvailabilityStrategyFactory()
+ getDefault(): AbstractRoomAvailabilityStrategy
+ getStrategy(type: RoomAvailabilityStrategyType): AbstractRoomAvailabilityStrategy
+ getFactory(): RoomAvailabilityStrategyFactory

Booking
- hotelId: String
- roomId: String
- roomType: RoomType
- start: Instant
- end: Instant

BookingManager
- bookings: Map<String, Booking>
- BookingManager()
+ createBooking(hotelId:String, roomType: RoomType, start: Instant, end: Instant, roomAvailabilityStrategyType: RoomAvailabilityStrategyType): List<Booking>
+ getManager(): BookingManager

DateListGenerator:
- generate(start: Instant, end: Instant): List<LocalDate>

<<IBookingState>>
+ confirm(booking: Booking)
+ cancel(booking: Booking)

CreatedBookingState(IBookingState)
ConfirmedBookingState(IBookingState)

BookingStateFactory
- states: Map<BookingStatus, IBookingState>
- BookingStateFactory()
+ getByStatus(status: BookingStatus)
+ getFactory(): BookingStateFactory