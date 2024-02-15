# Design a cab booking system 
    - open ended problem
    - any features like payments, notifications etc can be added
## Features
    - user can search for a cab from particilar location to go to a location
    - user can select  cab based on req'd type
    - user can cancel/reschdule cab if he don't like rating of driver
    - user can rate driver after end of trip

CabType
+ SUV
+ SEDAN

User
- bookedCabIds
+ findCabs(source: String, destination: String, cabTypes: List<CabType>): List
+ confirm(cabId: String): void
+ cancel(cabId: String): void
+ endTrip(cabId: String): void

<<IRatable>>
+ addRating(int): void

Cab(IRatable)
- source: String
- destination: String
- type: CabType
- driverRating: DriverRating
+ addRating(int): void
+ getSource(): String
+ getDestination(): String
+ getCabType(): CabType
+ getDriverRating(): int
+ clone(): Cab
+ toString(): String

DriverRating(IRatable)
- totalRides: int
- totalRating: int
+ addRating(int): void
+ getRating(): float
+ toString(): String

CabManager
- cabs: Map<String, Cab>
+ <u>getInstance(): CabManager</u>
+ addCab(cab: Cab)
+- addDriverRating(cabId: String, rating: int): void
+ getBookedCabDriverRating(cabId: String)
+- getCab(cabId: String): Cab

<<CabBookingStrategy>>
+ findCabs(source: String, destination: String, cabTypes: List<CabTypes>): Set<Entry<String, Cab>>
+ book(cabId: String): void
+ markAvailable(cabId: String): void
+ cancel(cabId: String): void
+ end(cabId: String, driverRating: int): void
+ isBooked(cabId: String): boolean


DefaultCabBookingStrategy(CabBookingStrategy)
- availableCabIds: Set<String>
- bookedCabIds: Set<String>

CabBookingStrategyFactory
- strategy: CabBookingStrategy
+ <u>getInstance(): CabBookingStrategyFactory</u>
+ getBookingStrategy(): CabBookingStrategy


        
    