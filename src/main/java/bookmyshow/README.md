# Problem
Design movie ticket booking system

1. Design entities
2. Design Apis

# Requirements
1. search
    - getMoviesByCity
    - getMovieShowsByCityAndMovieId

2. seatlayout
    - getSeatlayout(moviShowid): SeatLayout

3. BookSeats
   bookSeats(moviShowId, List<Seat>)

4. MakePayment/Confirm
    - confirm(bookingId)


# Design

City
- id: String
- movies: Set<String>
+ getMovieIds(): List<String>

CityManager
- cities: Map<String, City>
- CityManager()
+ getCityById(id: String): City
+ addMovie(cityId: String, movieId: String): void
+ getMovieIds(cityId: Stirng): List<String>
+ getManager(): CityManager

SearchService
- cityManager: CityManager
- movieManager: MovieManager
- movieShowManager: MovieShowManager
- SearchService()
+ getMoviesByCity(cityId: String): List<Movie>
+ getMovieSchedulesByCityIdAndMovieId(cityId: String, movieId: String, start: Instant, end: Instant): List<MovieSchedule>
+ getService(): SearchService

Cron
- seconds: String
- mins: String
- hours: String
- days: String
- weeks: String
- month: String
- years: String

MovieSchedule
- cron: Cron
- movieId: String
- auditoriumId: String
- multiplexId: String
- cityId: String

MovieScheduler
- schedule(movieSchedule: MovieSchedule, start: Instant, end: Instant): void

Movie
- id: String
- name: String
- cityIdTheaterIdSchedules: Map<String, Map<String, List<MovieSchedule>>>
- cityIdTheaterInstanceShow: 
- movieShowIds: TreeMap<Date, String>
- lengthInMinutes: int
+ addSchedule(schedule: MovieSchedule): void
- createOrGetShowId(cityId: String, theaterId: String, instant: Instant): String
- addMovieShowId(movieShowId: String): void
+ getShows(start: Instant, end: Instant): List<MovieShow>

MovieManager
- movies: Map<String, Movie>
- movieScheduler: MovieScheduler
- addScheduleValidator: AddScheduleValidator
- MovieManager()
+ addMovie(movie: Movie): void
+ addSchedule(movieId: String, schedule: MovieSchedule): void
+ addMovieShowId(movieId: String, showId: String): void
+ getMovie(movieId: String): Movie
+ getManager(): MovieManager
+ getSchedules(movieId: String): List<MovieSchedule>

AddScheduleValidator
+ validate(movieSchedule: MovieSchedule)


MovieShow
- id: String
- movieId: String
- auditoriumId: String
- multiplexid: String
- cityId: String
- startTime: Instant

MovieShowManager
- shows: Map<String, MovieShow>
- movieManager: MovieManager
+ addShow(show: MovieShow): void
+ getMovieShowById(showId: String): MovieShow

Multiplex
- id
- auditoriums: List<String>
+ addAudi(audiId: String): void

MultiplexManager
- multiplexes: Map<String, Multiple>
+ addMultiplex(multiplex: Multiplex): void


Auditorium
- id: Stirng
- multiplexId: String
- seats: Map<Integer, Map<Integer, Seat>>
+ getSeats():  Map<Integer, Map<Integer, Seat>>

AuditoriumManager
- audis: Map<String, Multiple>
+ addAuditorium(audi: Auditorium): void
+ getById(id: String): Auditorium
+ getSeats(audId: String): Map<Integer, Map<Integer, Seat>>

SeatStatus
+ INSERVICE
+ MAINTANCE

Seat
- row: int
- col: int
- status: SeatStatus
- auditoriumId: String
+ getAudiSeatId(): Stirng

SeatLayoutService
- movieShowManager: MovieShowManager
- auditoriumManager: AuditoriumManager
- bookingManager: BookingManager
- SeatLayoutService()
+ getSeatLayout(movieShowId: String): SeatLayout
+ getSeatService(): SeatLayoutService

SeatLayout
- seats: Map<Integer, Map<Integer, Seat>>
- unavailableSeatIds: List<String>

BookingStatus
+ CREATED
+ CONFIRMED
+ CANCELLED

Booking
- id
- movieShoewId: String
- bookedAuditoriumSeatSeatId: List<String>
- auditoriumId: String
- status: BookingStatus

BookingManager
- bookings: Map<String, Booking>
- showIdSeatIdToBookingId: Map<String, Map<String, String>>
- movieShowManager: MovieShowManager
- seatLockService: SeatLockService
- bookingStateFactory: BookingStateFactory
- BookingManager()
+ createBooking(movieShowId: String, auditoriumSeatId: List<String>): Booking
+ confirmBooking(bookingId: String): boolean
+ cancelBooking(bookingId: String): boolean
+ getBookedSeatIds(movieShowId: String): List<String>
+ getManager(): BookingManager

SeatLock
- expiryAt
- owner

SeatLockService
- movieShowIdAuditoriumSeatIdToLock: Map<String, Map<String, SeatLock>>
- movieShowIdSeatIdToLocks: Map<String, ReadWriteLock>
+ getLockedSeats(movieShowId: String): List<String>
+ getLock(movieShowSeatId: String): ReadWriteLock
+ isExpired(movieShowId, auditoriumSeatId: Stirng): boolean
+ setExpiry(movieShowId, auditoriumSeatId: Stirng, expiryAt: Instant, owner: String): void
+ takeWriteLocksAndReturnTakenLocksMovieShowSeatIds(movieShowIdSeatIds: List<String>): List<String>
+ releaseWriteLocks(movieShowIdSeatIds: List<String>): void


<<IBookingState>>
confirm(Booking: booking): void
cancel(Booking: booking): void

CreatedBookingState(IBookingState)
ConfirmBookingState(IBookingState)

BookingStateFactory
- states: Map<BookingStatus, IBookingState>
+ getState(status: BookingStatus): IBookingState