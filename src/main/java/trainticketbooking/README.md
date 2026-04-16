cars
id
type: GENERAL|SLEEPER|3AC|2AC|1AC|Royal

car_seat
id
carId
seat_number
seat_type: LB|MB|UB|SL|SU
isRAC

train_coaches
id
carId
coachNumber
coachPosition
trainId

train
id PK
number UK

train_route
id
trainId
stationId
station_seq_no
travel_day
hour_of_day

stations
id
name
geohash

train_daily_schedules
id
trainId

train_weekly_schedules
id
trainId
start_day_of_week

train_monthly_schedules
id
trainId
start_day_of_year

train_total_availability
id
trainId
carType
from_station_seq
to_station_seq
count

train_instance_seat_occupancy_in_memory (init by cron run before starting booking)
{trainId}::{trainInstanceDate}::{carType} : {{1-2: count}, {2-3: count} ...}
{trainId}::{trainInstanceDate}::{carType}::RAC : {{1-2: count}, {2-3: count} ...}
{trainId}::{trainInstanceDate}::{carType}::TATKAL : {{1-2: count}, {2-3: count} ...}
{trainId}::{trainInstanceDate}::{carType}::RAC::TATKAL : {{1-2: count}, {2-3: count} ...}

train_instance_seat_map_in_memory (init by cron run before starting booking)
{trainId}::{trainInstanceDate}::{carType}::{seat_type}: [{trainCoachNumber: (start_seat_number, end_seat_number)}]
{trainId}::{trainInstanceDate}::{carType}::{seat_type}::RAC: [{trainCoachNumber: (start_seat_number, end_seat_number)}]
{trainId}::{trainInstanceDate}::{carType}::{seat_type}::TATKAL: [{trainCoachNumber: (start_seat_number, end_seat_number)}]
{trainId}::{trainInstanceDate}::{carType}::{seat_type}::RAC::TATKAL: [{trainCoachNumber: (start_seat_number, end_seat_number)}]

train_instance_seat_pointer_in_memory (init by cron run before starting booking)
{trainId}::{trainInstanceDate}::{carType}::{seat_type}: {train_coach_id}::{seat_number}
{trainId}::{trainInstanceDate}::{carType}::{seat_type}::RAC : {train_coach_id}::{seat_number}
{trainId}::{trainInstanceDate}::{carType}::{seat_type}::TATKAL: {train_coach_id}::{seat_number}
{trainId}::{trainInstanceDate}::{carType}::{seat_type}::RAC::TATKAL : {train_coach_id}::{seat_number}

train_instance_seat_lock_in_memory (entry for each adjacent stations i.e. segments)
{trainId}::{trainInstanceDate}::{trainCoachNumber}::{seatNumber}::{fromStationSeq}::{toStationSeq} : true TTL 30 sec

lua_script_to_find_avaliable_seat_and_lock with fallback to code and db transaction

train_instance_seat_booked_in_memory (entry for each adjacent stations i.e. segments)
{trainId}::{trainInstanceDate}::{trainCoachNumber}::{seatNumber}::{fromStationSeq}::{toStationSeq} : true TTL {big_ttl}

train_instance_waitlist_queue
{trainId}::{trainInstanceDate}::{fromStationSeq}::{toStationSeq}::WAITLIST : sortedset
ZADD {train}:{date}:{from}:{to}:WAITLIST score reservationSeatId

reservations
id
trainId
trainInstanceDate
status
reservedByUserId
paymentId

reserved_seats
id
reservationId
trainId
trainInstanceDate
train_coach_number
seatNumber
from_station_seq
to_station_seq
journey_range: int4range(from_station_seq, to_station_seq)
status: PAYMENT_PENDING|RESERVED|RAC|CANCELLED|WAITLISTED
passengerName
passengerAge
passengerId
waitlistNumber
CONSTRAINT no_overlapping_seat_booking
EXCLUDE USING gist (
  trainId WITH =,
  trainInstanceDate WITH =,
  train_coach_id WITH =,
  seatNumber WITH =,
  journey_range WITH &&
)
WHERE (status = 'BOOKED');
