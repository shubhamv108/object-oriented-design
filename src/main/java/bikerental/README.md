RentalBikeStatus
+ DOCKED
+ IN_TRIP

RentalBike
- id: String
- registrationNumber: String
- status: RentalBikeStatus
- originalStationId: String
- dockedAtStationId: String
- stationDockNumber: int
- history: List<RentalBikeHistoryEntry>
+ unDock(): void
+ setDock(dockedAtStationId: String, stationDockNumber: int): void
- addHistoryEntry(RentalBikeHistoryEntry): void

RentalBikeHistoryEntry
- status: RentalBikeStatus
- stationId: String
- dockedAtStationId: String
- stationDockerNumber: int
- createdAt: Instant


<<IElectricVehicle>>
+ chargeBattery(minutes: long): void

<<PetrolVehicle>>
+ fillFuel(litres: float): void

ElectricBike(AbstractBike, IElectricVehicle)
- chargeCapacity: float
+ chargeBattery(minutes: long): void

MechanicalBike(AbstractBike, PetrolVehicle)
- fuelCapacityInLitres: float
+ fillFuel(litres: float): void

Customer
- id: String
- location: Location
- bookings: List<Booking>
+ rentBike(rentalBikeId: String): Booking
+ returnBike(bikeId: String, stationId: String, dockNumber: int, bookingId: String): void

BookingStatus
+ Created
+ Completed

Booking
- id: String
- customerId: String
- rentalBikeId: String
- startStationId: String
- endStationId: String
- trip: Trip
- createdAt: Instant
+ updateStatus(BookingStatus): void

Location
- lat: String
- longitude: String

Station
- id: String
- docks: List<Dock>
- address: Address

Address
- location: Location
- cityId: String

DockHistoryEntry
- bikeId: String
- dockedAt: Instant
- unDockedAt: Instant
- dockedByUserId: String 

<<AbstractDock>>
- dockNumber: String
- stationId: String
- bike: AbstractBike
- dockHistory: List<DockHistoryEntry>
+ dockBike(AbstractBike): void
+ undock(dockNumber): AbstractBike

ElectricDock(AbstractDock)
+ dockBike(ElectricBike): void
+ undock(dockNumber): ElectricBike

MechanicalDock(AbstractDock)
+ dockBike(MechanicalBike): void
+ undock(dockNumber): MechanicalBike

TripStatus
- ONGOING
- COMPLETED

Trip
- startLocation: Location
- endLocation: Location
- path: List<Location>
- status: TripStatus
- statusHistory: TripStatusHistory

TripStatusHistory
- tripId: String
- fromStatus: TripStatus
- toStatus: TripStatus
- createdAt: Date