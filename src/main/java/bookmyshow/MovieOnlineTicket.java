package bookmyshow;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.*;
import java.util.stream.*;

class CacheEntry {
    private Object value;
    private Long expiryAt;

    public CacheEntry(Object value) {
        this.value = value;
    }

    public CacheEntry(Object value, long ttlInNS) {
        this.value = value;
        this.expiryAt = System.nanoTime() + ttlInNS;
    }

    public boolean isExpired() {
        return expiryAt != null && System.nanoTime() > expiryAt;
    }

    public <T> T value() {
        if (isExpired())
            return null;
        return (T) value;
    }

    public void setAdd(Object value) {
        if (value == null)
            value = new LinkedHashSet<>();
        ((Set) value).add(value);
    }

    public void listAdd(Object value) {
        if (value == null)
            value = new LinkedList<>();
        ((Set) value).add(value);
    }
}

enum Cache {

    INSTANCE;

    public static Cache getInstance() {
        return INSTANCE;
    }

    private final Map<String, CacheEntry> cache = new HashMap<>();

    public <T> T get(String key) {
        return (T) Optional.ofNullable(this.cache.get(key))
                .map(CacheEntry::value)
                .orElse(null);
    }

    public void put(String key, Object value) {
        this.cache.put(key, new CacheEntry(value));
    }

    public void put(String key, Object value, long ttlInNS) {
        this.cache.put(key, new CacheEntry(value, ttlInNS));
    }

    public void remove(String key) {
        this.cache.remove(key);
    }

    public void setAdd(String key, Object value) {
        ((Set) this.cache.computeIfAbsent(key, e -> new CacheEntry(new LinkedHashSet<>())).<LinkedHashSet>value()).add(value);
    }

    public void listAdd(String key, Object value) {
        ((Set) this.cache.computeIfAbsent(key, e -> new CacheEntry(new LinkedList<>())).value()).add(value);
    }
}

record City(String id, String name, String country) {
}

class CityManager {
    private final Map<String, City> cities = new HashMap<>();

    public City add(City city) {
        final City newCity = new City(UUID.randomUUID().toString(), city.name(), city.country());
        this.cities.put(newCity.id(), newCity);
        Cache.getInstance().setAdd("cities", city.id());
        Cache.getInstance().put("City::" + city.id(), city);
        return newCity;
    }

    public City get(String cityId) {
        City city = Cache.getInstance().<City>get("City::" + cityId);
        if (city == null) {
            city = this.cities.get(cityId);
            Cache.getInstance().put("City::" + city.id(), city);
        }
        return city;
    }

    public List<City> getAll() {
        return Cache.getInstance().<Set<String>>get("cities")
                .stream()
                .map(this::get)
                .toList();
    }
}

enum Genre {
    ANY
}

enum Language {
    HINDI
}

class Movie {
    private String id;
    private String name;

    private final List<MovieWeeklySchedule> weeklySchedules = new ArrayList<>();
    private final List<MovieCustomSchedule> customSchedules = new ArrayList<>();

    public Movie(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void add(MovieWeeklySchedule schedule) {
        this.weeklySchedules.add(schedule);
    }

    public void add(MovieCustomSchedule schedule) {
        this.customSchedules.add(schedule);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", weeklySchedules=" + weeklySchedules +
                ", customSchedules=" + customSchedules +
                '}';
    }
}

class MovieManager {
    private final Map<String, Movie> data = new HashMap<>();

    public Movie add(Movie movie) {
        final Movie newMovie = new Movie(UUID.randomUUID().toString(), movie.getName());

        this.data.put(newMovie.getId(), newMovie);
        Cache.getInstance().put("Movie::" + newMovie.getId(), newMovie);
        return newMovie;
    }

    public Movie get(String movieId) {
        Movie movie = Cache.getInstance().<Movie>get("Movie::" + movieId);
        if (movie == null) {
            movie = this.data.get(movieId);
            Cache.getInstance().put("Movie::" + movie.getId(), movie);
        }
        return movie;
    }
}

class MovieScheduleManager {

    private final MovieManager movieManager;
    private final MovieShowManager movieShowManager;

    private final Map<String, LinkedHashSet<MovieWeeklySchedule>> cityMovieWeeklyScheduleIndex = new HashMap<>();
    private final Map<String, LinkedHashSet<MovieCustomSchedule>> cityMovieCustomScheduleIndex = new HashMap<>();

    public MovieScheduleManager(final MovieManager movieManager, final MovieShowManager movieShowManager) {
        this.movieManager = movieManager;
        this.movieShowManager = movieShowManager;
    }

    public MovieSchedule add(MovieWeeklySchedule schedule) {
        final MovieWeeklySchedule movieSchedule = new MovieWeeklySchedule(
                UUID.randomUUID().toString(),
                schedule.getMovieId(), schedule.getAuditoriumId(), schedule.getMultiplexId(),
                schedule.getCityId(), schedule.getSchedule(), schedule.getSeatPrices());
        final Movie movie = Optional.ofNullable(this.movieManager.get(schedule.getMovieId()))
                .orElseThrow(() -> new RuntimeException(String.format("No movie by id: %s", schedule.getMovieId())));
        movie.add(movieSchedule);
        this.cityMovieWeeklyScheduleIndex.computeIfAbsent(schedule.getCityId(), e -> new LinkedHashSet<>()).add(schedule);
        // ADD List<MovieShow>
        return movieSchedule;
    }

    public MovieSchedule add(MovieCustomSchedule schedule) {
        final MovieCustomSchedule movieSchedule = new MovieCustomSchedule(
                UUID.randomUUID().toString(), schedule.getMovieId(),
                schedule.getAuditoriumId(), schedule.getMultiplexId(),
                schedule.getCityId(), schedule.getSchedule(),
                schedule.getSeatPrices());
        final Movie movie = Optional.ofNullable(this.movieManager.get(movieSchedule.getMovieId()))
                .orElseThrow(() -> new RuntimeException(String.format("No movie by id: %s", movieSchedule.getMovieId())));
        movie.add(movieSchedule);
        this.cityMovieCustomScheduleIndex.computeIfAbsent(movieSchedule.getCityId(), e -> new LinkedHashSet<>()).add(movieSchedule);
        this.movieShowManager.add(
                new MovieShow(null, movieSchedule.getMovieId(), movieSchedule.getAuditoriumId(),
                        movieSchedule.getMultiplexId(), movieSchedule.getCityId(), movieSchedule.getSchedule().getInstant()));
        return movieSchedule;
    }


    public List<String> getMovieIdsByCityId(String cityId, Instant startDate, Instant endDate) {
        return Stream.concat(
                        this.cityMovieWeeklyScheduleIndex.getOrDefault(cityId, new LinkedHashSet<>())
                                .stream()
                                .filter(schedule -> schedule.getSchedule().isOverLapping(startDate, endDate))
                                .map(MovieSchedule::getMovieId),
                        this.cityMovieCustomScheduleIndex.getOrDefault(cityId, new LinkedHashSet<>())
                                .stream()
                                .filter(schedule -> schedule.getSchedule().isOverlapping(startDate, endDate))
                                .map(MovieSchedule::getMovieId))
                .toList();
    }
}

sealed abstract class Schedule permits WeeklySchedule {
    private Instant startDate;
    private Instant endDate;

    public Schedule(Instant startDate, Instant endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public boolean isOverLapping (Instant start, Instant end) {
        return (start.isAfter(this.startDate) && start.isBefore(this.endDate)) ||
                (end.isAfter(this.startDate) && end.isBefore(this.endDate));
    }
}

final class WeeklySchedule extends Schedule {
    private LocalTime sheduledTime;
    private TimeZone timeZone;
    private DayOfWeek dayOfWeek;

    public WeeklySchedule(
            Instant startDate,
            Instant endDate,
            LocalTime sheduledTime,
            TimeZone timeZone,
            DayOfWeek dayOfWeek) {
        super(startDate, endDate);
        this.sheduledTime = sheduledTime;
        this.timeZone = timeZone;
        this.dayOfWeek = dayOfWeek;
    }


    public int getDayOfWeekInt() {
        return dayOfWeek.getValue();
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getSheduledTime() {
        return sheduledTime;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    @Override
    public String toString() {
        return "WeeklySchedule{" +
                "sheduledTime=" + sheduledTime +
                ", timeZone=" + timeZone +
                ", dayOfWeek=" + dayOfWeek +
                '}';
    }
}

final class CustomSchedule {
    private Instant instant;

    public CustomSchedule(Instant instant) {
        this.instant = instant;
    }

    public Instant getInstant() {
        return this.instant;
    }

    public boolean isOverlapping(Instant start, Instant end) {
        return this.instant.isAfter(start) && this.instant.isBefore(end);
    }

    @Override
    public String toString() {
        return "CustomSchedule{" +
                "instant=" + instant +
                '}';
    }
}

abstract class MovieSchedule {

    private String id;
    private String movieId;
    private String auditoriumId;
    private String multiplexId;
    private String cityId;
    private final Map<SeatType, BigDecimal> seatPrices = new HashMap<>();

    public MovieSchedule(String id, String movieId, String auditoriumId, String multiplexId, String cityId, Map<SeatType, BigDecimal> seatPrices) {
        this.id = id;
        this.movieId = movieId;
        this.auditoriumId = auditoriumId;
        this.multiplexId = multiplexId;
        this.cityId = cityId;
        this.seatPrices.putAll(seatPrices);
    }

    public String getId() {
        return id;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getAuditoriumId() {
        return auditoriumId;
    }

    public String getMultiplexId() {
        return multiplexId;
    }

    public String getCityId() {
        return cityId;
    }

    public Map<SeatType, BigDecimal> getSeatPrices() {
        return seatPrices;
    }

    @Override
    public String toString() {
        return "MovieSchedule{" +
                "id='" + id + '\'' +
                ", movieId='" + movieId + '\'' +
                ", multiplexId='" + multiplexId + '\'' +
                ", cityId='" + cityId + '\'' +
                ", seatPrices=" + seatPrices +
                '}';
    }
}

class MovieWeeklySchedule extends MovieSchedule {
    private WeeklySchedule schedule;

    public MovieWeeklySchedule(
            String id, String movieId, String auditoriumId, String multiplexId, String cityId, WeeklySchedule schedule,
            Map<SeatType, BigDecimal> seatPrice) {
        super(id, movieId, auditoriumId, multiplexId, cityId, seatPrice);
        this.schedule = schedule;
    }

    public WeeklySchedule getSchedule() {
        return schedule;
    }

    @Override
    public String toString() {
        return "MovieWeeklySchedule{" +
                "id='" + this.getId() + '\'' +
                ", movieId='" + getMovieId() + '\'' +
                ", auditoriumId='" + getAuditoriumId() + '\'' +
                ", multiplexId='" + getMultiplexId() + '\'' +
                ", cityId='" + getCityId() + '\'' +
                ", seatPrices=" + getSeatPrices() +
                "schedule=" + schedule +
                '}';
    }
}

class MovieCustomSchedule extends MovieSchedule {
    private CustomSchedule schedule;

    public MovieCustomSchedule(String id, String movieId, String auditoriumId, String multiplexId, String cityId, CustomSchedule schedule, Map<SeatType, BigDecimal> seatPrice) {
        super(id, movieId, auditoriumId, multiplexId, cityId, seatPrice);
        this.schedule = schedule;
    }

    public CustomSchedule getSchedule() {
        return schedule;
    }

    @Override
    public String toString() {
        return "MovieCustomSchedule{" +
                "id='" + this.getId() + '\'' +
                ", movieId='" + getMovieId() + '\'' +
                ", auditoriumId='" + getAuditoriumId() + '\'' +
                ", multiplexId='" + getMultiplexId() + '\'' +
                ", cityId='" + getCityId() + '\'' +
                ", seatPrices=" + getSeatPrices() +
                "schedule=" + schedule +
                '}';
    }
}

class Multiplex {
    private String id;

    private String name;
    private String cityId;

    public Multiplex(String id, String name, String cityId) {
        this.id = id;
        this.name = name;
        this.cityId = cityId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCityId() {
        return cityId;
    }

    @Override
    public String toString() {
        return "Multiplex{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", cityId='" + cityId + '\'' +
                '}';
    }
}

class MultiplexManager {
    private final Map<String, Multiplex> data = new HashMap<>();

    public Multiplex add(Multiplex multiplex) {
        final Multiplex newMultiplex = new Multiplex(
                UUID.randomUUID().toString(), multiplex.getName(), multiplex.getCityId());
        this.data.put(multiplex.getId(), multiplex);
        Cache.getInstance().put("Multiplex::" + newMultiplex.getId(), newMultiplex);
        return newMultiplex;
    }

    public Multiplex get(String id) {
        return this.data.get(id);
    }
}

class Auditorium {
    private String id;
    private String number;
    private String multiplexId;
    private List<Seat> seats = new ArrayList<>();

    public Auditorium(String id, String number, String multiplexId, List<Seat> seats) {
        this.id = id;
        this.number = number;
        this.multiplexId = multiplexId;
        this.seats.addAll(
                seats.stream()
                        .map(s -> {
                            final Seat seat = new Seat(
                                s.getNumber(),
                                s.getSeatType());
                            seat.setAuditoriumId(this.id);
                            return seat;
                        })
                        .toList());
    }

    public String getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getMultiplexId() {
        return multiplexId;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    @Override
    public String toString() {
        return "Auditorium{" +
                "id='" + id + '\'' +
                ", number='" + number + '\'' +
                ", multiplexId='" + multiplexId + '\'' +
                ", seats=" + seats +
                '}';
    }
}

class AuditoriumManager {
    private final Map<String, Auditorium> data = new HashMap<>();

    public Auditorium add(Auditorium auditorium) {
        final Auditorium newAuditorium = new Auditorium(
                UUID.randomUUID().toString(), auditorium.getNumber(), auditorium.getMultiplexId(), auditorium.getSeats());
        this.data.put(newAuditorium.getId(), newAuditorium);
        return newAuditorium;
    }

    public List<Seat> getSeats(String auditoriumId) {
        return Optional.ofNullable(this.data.get(auditoriumId))
                .map(Auditorium::getSeats)
                .orElse(List.of());
    }
}

enum SeatType {
    FRONT, MIDDLE, BACK
}

class Seat {
    private int number;
    private SeatType seatType;

    private String auditoriumId;

    public Seat(int number, SeatType seatType) {
        this.number = number;
        this.seatType = seatType;
    }

    public void setAuditoriumId(String auditoriumId) {
        this.auditoriumId = auditoriumId;
    }

    public String getId() {
        return this.auditoriumId + "::" + number;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public String getAuditoriumId() {
        return auditoriumId;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "number=" + number +
                ", seatType=" + seatType +
                '}';
    }
}

enum MovieShowStatus {
    SCHEDULED, ONGOING, COMPLETED
}

class MovieShow {
    private String id;
    private Instant startInstant;
    private Duration duration;
    private String movieId;
    private String auditoriumId;
    private String multiplexId;
    private String cityId;
    private MovieShowStatus status;

    public MovieShow(String id, String movieId, String auditoriumId, String multiplexId, String cityId, Instant startInstant) {
        this.id = id;
        this.movieId = movieId;
        this.auditoriumId = auditoriumId;
        this.multiplexId = multiplexId;
        this.cityId = cityId;
        this.startInstant = startInstant;
        this.status = MovieShowStatus.SCHEDULED;
    }

    public String getId() {
        return id;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getAuditoriumId() {
        return auditoriumId;
    }

    public String getMultiplexId() {
        return multiplexId;
    }

    public String getCityId() {
        return cityId;
    }

    public Instant getStartInstant() {
        return startInstant;
    }

    public boolean isOverlapping(Instant start, Instant end) {
        return start.isBefore(this.startInstant) && end.isAfter(this.startInstant);
    }

    @Override
    public String toString() {
        return "MovieShow{" +
                "id='" + id + '\'' +
                ", startInstant=" + startInstant +
                ", duration=" + duration +
                ", movieId='" + movieId + '\'' +
                ", auditoriumId='" + auditoriumId + '\'' +
                ", multiplexId='" + multiplexId + '\'' +
                ", cityId='" + cityId + '\'' +
                ", status=" + status +
                '}';
    }
}

class MovieShowManager {
    private final Map<String, MovieShow> data = new HashMap<>();
    private final Map<String, Set<MovieShow>> cityIdIndex = new HashMap<>();
    private final Map<String, Map<String, Set<MovieShow>>> cityIdMovieIdIndex = new HashMap<>();

    public MovieShow add(MovieShow show) {
        final MovieShow movieShow = new MovieShow(
                UUID.randomUUID().toString(),
                show.getMovieId(),
                show.getAuditoriumId(),
                show.getMultiplexId(),
                show.getCityId(),
                show.getStartInstant());
        this.data.put(movieShow.getId(), movieShow);
        this.cityIdIndex.computeIfAbsent(movieShow.getCityId(), e -> new LinkedHashSet<>()).add(movieShow);
        this.cityIdMovieIdIndex
                .computeIfAbsent(show.getCityId(), e -> new HashMap<>())
                .computeIfAbsent(show.getMovieId(), e -> new LinkedHashSet<>())
                .add(movieShow);
        return movieShow;
    }

    public List<MovieShow> getAllMovieShowsByCity(String movieId, String cityId, Instant startDate, Instant endDate) {
        return Optional.ofNullable(this.cityIdMovieIdIndex.getOrDefault(cityId, new HashMap<>()).get(movieId))
                .map(shows -> shows
                        .stream()
                        .filter(show -> show.isOverlapping(startDate, endDate))
                        .toList())
                .orElse(List.of());
    }

    public MovieShow get(String id) {
        return this.data.get(id);
    }
}

class SearchService {
    private final MovieShowManager movieShowManager;
    private final MovieScheduleManager movieScheduleManager;
    private final MovieManager movieManager;
    private final MultiplexManager multiplexManager;

    public SearchService(MovieShowManager movieShowManager, MovieScheduleManager movieScheduleManager, MovieManager movieManager, MultiplexManager multiplexManager) {
        this.movieShowManager = movieShowManager;
        this.movieScheduleManager = movieScheduleManager;
        this.movieManager = movieManager;
        this.multiplexManager = multiplexManager;
    }

    public List<Movie> getMoviesByCity(String cityId) {
        return this.getMoviesByCity(cityId, Instant.now(), Instant.now().plus(30, ChronoUnit.DAYS));
    }

    public List<Movie> getMoviesByCity(String cityId, Instant startInstant, Instant endInstant) {
        return Optional.ofNullable(this.movieScheduleManager.getMovieIdsByCityId(cityId, startInstant, endInstant))
                .map(movieIds -> movieIds.stream()
                        .map(movieId ->
                                Optional.ofNullable(Cache.getInstance().<Movie>get("Movie::" + movieId))
                                        .orElse(this.movieManager.get(movieId)))
                        .filter(Objects::nonNull)
                        .toList())
                .orElse(List.of());
    }

    public List<MovieShow> getMovieShows(String movieId, String cityId) {
        return this.movieShowManager.getAllMovieShowsByCity(movieId, cityId, Instant.now(), Instant.now().plus(30, ChronoUnit.DAYS));
    }

    public Map<Multiplex, List<MovieShow>> getMovieShows(String movieId, String cityId, Instant startDate, Instant endDate) {
        final Map<Multiplex, List<MovieShow>> multiplexShows = new HashMap<>();
        final List<MovieShow> shows = this.movieShowManager.getAllMovieShowsByCity(movieId, cityId, startDate, endDate);
        shows.forEach(show -> {
            final Multiplex multiplex =
                    Optional.ofNullable(Cache.getInstance().<Multiplex>get("Multiplex::" + show.getMultiplexId()))
                            .orElse(this.multiplexManager.get(show.getMultiplexId()));
            if (multiplex != null)
                multiplexShows.computeIfAbsent(multiplex, e -> new LinkedList<>()).add(show);
        });
        return multiplexShows;
    }
}

class SeatLock {
    private String name;
    private String owner;

    public SeatLock(String name, String owner) {
        this.name = name;
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }
}

class SeatLockService {
    private final Map<String, Lock> locks = new ConcurrentHashMap<>();

    private static final long DEFAULT_SEAT_LOCK_TTL_IN_NS = (long) 1e11 * 3l;

    public boolean lock(String movieShowId, List<String> seatIds, String personId) {
        if (seatIds.stream().anyMatch(seatId -> !this.lock(movieShowId, seatId, personId)))
            throw new SeatLockedException();
        return true;
    }

    public void unlock(String movieShowId, List<String> seatIds, String personId) {
        seatIds.stream().forEach(seatId -> this.unlock(movieShowId, seatId, personId));
    }

    public boolean lock(String movieShowId, String seatId, String personId) {
        final String lockKey = this.getSeatLockKey(movieShowId, seatId);
        if (this.isLocked(lockKey))
            return false;
        try {
            if (!this.getLock(seatId).tryLock())
                return false;
            if (this.isLocked(lockKey))
                return false;
            Cache.getInstance().put(lockKey, new SeatLock(seatId, personId), DEFAULT_SEAT_LOCK_TTL_IN_NS);
            return true;
        } finally {
            this.getLock(seatId).unlock();
        }
    }

    public boolean unlock(String movieShowId, String seatId, String personId) {
        final String lockKey = this.getSeatLockKey(movieShowId, seatId);
        if (this.isLockedByPerson(lockKey, personId))
            return false;
        try {
            if (!this.getLock(seatId).tryLock())
                return false;
            if (this.isLockedByPerson(lockKey, personId))
                return false;
            Cache.getInstance().remove(lockKey);
            return true;
        } finally {
            this.getLock(seatId).unlock();
        }
    }

    public boolean isLocked(String movieShowId, String seatId) {
        return this.isLocked(this.getSeatLockKey(movieShowId, seatId));
    }

    private boolean isLocked(String key) {
        return Optional.ofNullable(Cache.getInstance().<SeatLock>get(key))
                .isPresent();
    }

    private boolean isLockedByPerson(String key, String personId) {
        return Optional.ofNullable(Cache.getInstance().<SeatLock>get(key))
                .map(lockInfo -> lockInfo.getOwner().equals(personId))
                .orElse(false);
    }

    private String getSeatLockKey(String movieShowId, String seatId) {
        return "SeatLock::" + movieShowId + "::" + seatId;
    }

    private Lock getLock(String key) {
        synchronized (key) {
            return this.locks.computeIfAbsent(key, e -> new ReentrantLock());
        }
    }

}

class SeatLockedException extends RuntimeException { }

enum BookingStatus {
    PAYMENT_PENDING, CONFIRMED, CANCELLED
}

class Booking {
    private String id;
    private final List<String> seats = new ArrayList<>();
    private String bookingPersonId;
    private BookingStatus status;
    private String movieShowId;

    public Booking(String id, Set<String> seats, String movieShowId, String bookingPersonId) {
        this.id = id;
        this.seats.addAll(seats);
        this.bookingPersonId = bookingPersonId;
        this.movieShowId = movieShowId;
        this.status = BookingStatus.PAYMENT_PENDING;
    }

    public String getId() {
        return id;
    }

    public List<String> getSeats() {
        return seats;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public String getMovieShowId() {
        return movieShowId;
    }

    public String getBookingPersonId() {
        return bookingPersonId;
    }

    public boolean isConfirmed() {
        return BookingStatus.CONFIRMED.equals(this.status);
    }

    public boolean isNotConfirmed() {
        return BookingStatus.CONFIRMED.equals(this.status);
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id='" + id + '\'' +
                ", seats=" + seats +
                ", bookingPersonId='" + bookingPersonId + '\'' +
                ", status=" + status +
                ", movieShowId='" + movieShowId + '\'' +
                '}';
    }
}

class BookingService {
    private final Map<String, Booking> bookings = new HashMap<>();
    private final Map<String, Set<Booking>> movieShowIdIndex = new HashMap<>();
    private final Map<String, Map<String, Set<Booking>>> movieShowIdSeatIdIndex = new HashMap<>();
    private final SeatLockService seatLockService;

    public BookingService(SeatLockService seatLockService) {
        this.seatLockService = seatLockService;
    }

    public Booking book(Booking booking) {
        if (!this.areSeatsAvailable(booking.getMovieShowId(), booking.getSeats()))
            return null;
        Booking finalBooking = null;
        try {
            if (!this.seatLockService.lock(booking.getMovieShowId(), booking.getSeats(), booking.getBookingPersonId()))
                return null;
            if (!this.areSeatsAvailable(booking.getMovieShowId(), booking.getSeats()))
                return null;

            final Booking newBooking = new Booking(
                    UUID.randomUUID().toString(),
                    new HashSet<>(booking.getSeats()),
                    booking.getMovieShowId(),
                    booking.getBookingPersonId()
            );
            this.bookings.put(newBooking.getId(), newBooking);
            this.movieShowIdIndex
                    .computeIfAbsent(newBooking.getMovieShowId(), e -> new LinkedHashSet<>())
                    .add(newBooking);
            booking.getSeats()
                    .forEach(seatId -> this.movieShowIdSeatIdIndex
                            .computeIfAbsent(newBooking.getMovieShowId(), e -> new LinkedHashMap<>())
                            .computeIfAbsent(seatId, e -> new LinkedHashSet<>())
                            .add(newBooking));
            finalBooking = newBooking;
        } catch (SeatLockedException seatLockedException) {
            this.seatLockService.unlock(booking.getMovieShowId(), booking.getSeats(), booking.getBookingPersonId());
        }
        return finalBooking;
    }

    private boolean areSeatsAvailable(String movieShowId, List<String> seatIds) {
        if (Optional.ofNullable(movieShowIdSeatIdIndex.get(movieShowId))
                .map(seatIdIndex -> seatIds.stream().map(seatIdIndex::get)
                        .filter(Objects::nonNull)
                        .flatMap(Collection::stream)
                        .anyMatch(Booking::isConfirmed))
                .orElse(false))
            return false;
        return true;
    }

    public Set<String> getConfirmedSeats(String movieShowId) {
        return this.movieShowIdIndex.getOrDefault(movieShowId, Set.of())
                .stream()
                .filter(Booking::isConfirmed)
                .map(Booking::getSeats)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public boolean updateStatus(String bookingId, BookingStatus status) {
        final Booking booking = Optional.ofNullable(this.bookings.get(bookingId))
                .orElseThrow(() -> new RuntimeException("No booking for given id"));
        if (BookingStatus.CONFIRMED.equals(status))
            if (!BookingStatus.PAYMENT_PENDING.equals(booking.getStatus()))
                return false;
        if (BookingStatus.CANCELLED.equals(status)) {
            if (!BookingStatus.CONFIRMED.equals(booking.getStatus()))
                return false;
            this.seatLockService.unlock(booking.getMovieShowId(), booking.getSeats(), booking.getBookingPersonId());
        }
        booking.setStatus(status);
        return true;
    }
}

class MovieShowSeatDTO {
    private int seatNumber;
    private String status;
    private SeatType seatType;
    private String seatId;

    public MovieShowSeatDTO(int seatNumber, String status, SeatType seatType, String seatId) {
        this.seatNumber = seatNumber;
        this.status = status;
        this.seatType = seatType;
        this.seatId = seatId;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public String getSeatId() {
        return seatId;
    }

    @Override
    public String toString() {
        return "MovieShowSeat{" +
                "seatNumber='" + seatNumber + '\'' +
                ", status='" + status + '\'' +
                ", seatType=" + seatType +
                '}';
    }
}

class SeatLayoutService {
    private final MovieShowManager movieShowManager;
    private final AuditoriumManager auditoriumManager;
    private final BookingService bookingService;
    private final SeatLockService seatLockService;

    public SeatLayoutService(MovieShowManager movieShowManager, AuditoriumManager auditoriumManager, BookingService bookingService, SeatLockService seatLockService) {
        this.movieShowManager = movieShowManager;
        this.auditoriumManager = auditoriumManager;
        this.bookingService = bookingService;
        this.seatLockService = seatLockService;
    }

    public List<MovieShowSeatDTO> getSeatLayout(final String movieShowId) {
        final MovieShow show = Optional.ofNullable(this.movieShowManager.get(movieShowId))
                .orElseThrow(() -> new RuntimeException(String.format("No movie show found for id: %s", movieShowId)));

        final Set<String> bookedSeats = this.bookingService.getConfirmedSeats(movieShowId);
        return this.auditoriumManager.getSeats(show.getAuditoriumId())
                .stream()
                .map(seat -> {
                    boolean isNotAvailable = bookedSeats.contains(seat.getId()) ||
                            this.seatLockService.isLocked(movieShowId, seat.getId());
                    return new MovieShowSeatDTO(
                            seat.getNumber(),
                            isNotAvailable ? "UNAVAILABLE" : "AVAILABLE",
                            seat.getSeatType(),
                            seat.getAuditoriumId() + "::" + seat.getNumber()
                    );
                })
                .toList();
    }
}

class Driver {
    public static void main(String[] args) throws InterruptedException {
        final CityManager cityManager = new CityManager();
        final City cityA = cityManager.add(new City(null, "Bengaluru", "India"));
        System.out.println(cityA);

        final MultiplexManager multiplexManager = new MultiplexManager();
        final Multiplex multiplexA = multiplexManager.add(new Multiplex(null, "MultiplexA", cityA.id()));
        System.out.println(multiplexA);
        final AuditoriumManager auditoriumManager = new AuditoriumManager();
        final Auditorium auditorium1 = auditoriumManager.add(
                new Auditorium(null,
                        "1",
                        multiplexA.getId(),
                        List.of(new Seat(21, SeatType.BACK), new Seat(22, SeatType.BACK))));
        System.out.println(auditorium1);

        final MovieManager movieManager = new MovieManager();
        final Movie movieA = movieManager.add(new Movie(null, "MovieA"));
        System.out.println(movieA);

        final MovieShowManager movieShowManager = new MovieShowManager();


        final LocalDate localDate = LocalDate.parse("2024-01-17");
        final LocalDateTime localDateTime = localDate.atStartOfDay();
        final Instant instant = localDateTime.toInstant(ZoneOffset.UTC);

        final MovieScheduleManager movieScheduleManager = new MovieScheduleManager(movieManager, movieShowManager);
        final MovieSchedule movieASchedule = movieScheduleManager.add(
                new MovieCustomSchedule(null, movieA.getId(), auditorium1.getId(), multiplexA.getId(), cityA.id(),
                        new CustomSchedule(instant),
                        new HashMap<>() {{
                            put(SeatType.FRONT, BigDecimal.valueOf(30));
                            put(SeatType.MIDDLE, BigDecimal.valueOf(70));
                            put(SeatType.BACK, BigDecimal.valueOf(100));
                        }}));
        System.out.println(movieASchedule);

        final SearchService searchService = new SearchService(
                movieShowManager, movieScheduleManager, movieManager, multiplexManager);
        System.out.println(searchService.getMoviesByCity(cityA.id()));

        final List<MovieShow> movieShows = searchService.getMovieShows(movieA.getId(), cityA.id());
        System.out.println(movieShows);
        final SeatLockService seatLockService = new SeatLockService();
        final BookingService bookingService = new BookingService(seatLockService);
        final SeatLayoutService seatLayoutService = new SeatLayoutService(
                movieShowManager, auditoriumManager, bookingService, seatLockService);
        final List<MovieShowSeatDTO> movieShowSeatDTOs = seatLayoutService.getSeatLayout(movieShows.get(0).getId());
        System.out.println(seatLayoutService.getSeatLayout(movieShows.get(0).getId()));
        final Booking bookingB = bookingService.book(
                new Booking(
                        null,
                        Set.of(String.valueOf(movieShowSeatDTOs.get(0).getSeatId()), String.valueOf(movieShowSeatDTOs.get(1).getSeatId())),
                        movieShows.get(0).getId(),
                        "owner"
                )
        );
        System.out.println(bookingB);
        System.out.println(seatLayoutService.getSeatLayout(movieShows.get(0).getId()));
        bookingService.updateStatus(bookingB.getId(), BookingStatus.CONFIRMED);
        System.out.println(bookingB);
        System.out.println(seatLayoutService.getSeatLayout(movieShows.get(0).getId()));
        Thread.sleep(1000 * 301);
        System.out.println(seatLayoutService.getSeatLayout(movieShows.get(0).getId()));
        final Booking bookingC = bookingService.book(
                new Booking(
                        null,
                        Set.of(String.valueOf(movieShowSeatDTOs.get(0).getSeatId()), String.valueOf(movieShowSeatDTOs.get(1).getSeatId())),
                        movieShows.get(0).getId(),
                        "owner"
                )
        );
        System.out.println(bookingC);
        System.out.println(seatLayoutService.getSeatLayout(movieShows.get(0).getId()));
        bookingService.updateStatus(bookingB.getId(), BookingStatus.CANCELLED);
        System.out.println(bookingB);
        System.out.println(seatLayoutService.getSeatLayout(movieShows.get(0).getId()));
        final Booking bookingD = bookingService.book(
                new Booking(
                        null,
                        Set.of(String.valueOf(movieShowSeatDTOs.get(0).getSeatId()), String.valueOf(movieShowSeatDTOs.get(1).getSeatId())),
                        movieShows.get(0).getId(),
                        "owner"
                )
        );
        System.out.println(bookingD);
        System.out.println(seatLayoutService.getSeatLayout(movieShows.get(0).getId()));
    }
}