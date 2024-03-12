One LLD of Newspapers (Physical) delivery system.

Subscription -> newspaper, timings, delivery person , address


Delivery
Newspapers


Newspaper station
Inventory

Location
Lat: String
Long: String

Address
cityId: Stirng
location: Location


	NewpaperStationService
cityid
station: Map<String, NewpaperStation>

NewpaperStation
Id: String
address: Address
inventory: Inventory
deliveryPersonIds: List<String>
availableNewspaper: Set<Newspaper>


	Inventory
newspaperDailyCount: Map<Newspaper, Map<Date, Integer>>
updateCount(DailyNewspaper,  decrementBy: int): void

	DaliveryStatus
OUTFORDELIVERY
DLELIVERED
FAILED


Delivery
id
SubscriptionId
Status: DeliveryStatus
date: Date
reason: String


	DeliveryPerson
Id
List<Route>

	DeliveryRouteStatus
CREATED
ASSIGNED_TO_DELIVERY

	DeliveryRouteInstance
		- deliveryPersonId: String
startedAt
Date: Date
deliveryId: String
DeliveryRouteStatus

	DeliveryRoute
id
SubscriptionId
startTime: LocalTime
endTime: LocalTime


	Newspaper
Id
Language
List<String>
addSubscription(subscriptionId: String): void


DailyNewspaper
Newspaper: Newspaper
date: Date

	SubscriptionFrequency
DAILY
WEEKLY

	SubscriptionStatus
ACTIVE
INACTIVE


Subscription
id
Newspaper
SubscriptionFrequency
deliveryTime: LocalTime
dayOfWeeks: List<DayOfWeek>
Address address
NewpaperStation
deliveryIds: List<String>
SubscriptionStatus



	DeliveryManager
deliveries: Map<String, Delivery>

SubscriptionManager
subscriptions: Map<String, Subscription>
