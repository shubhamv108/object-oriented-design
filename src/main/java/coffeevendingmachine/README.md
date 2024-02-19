CoffeeVendingMachineState
+ Idle
+ InsertedMoney
+ SelectedCoffee
+ DispensingCoffee
+ Aborted

Coin
+ ONE
+ TWO
+ FIVE
+ TEN

<<StateOperations>>
+ insertCoin(coins: List<Coin>): void
+ selectCoffeeBeverage(beverageNumber int): void
+ addIngredient(ingredientNumber: int): void
+ dispenseCoffee(): AbstractCoffeeBeverage
+ dispenseCash(): int
+ abort(): void

AbstractVendingMachineState(StateOperations)
- coffeeVendingMachine: CoffeeVendingMachine

IdleState(AbstractVendingMachineState)
InsertedMoneyState(AbstractVendingMachineState)
SelectedCoffeeState(AbstractVendingMachineState)
DispensingCoffeeState(AbstractVendingMachineState)
AbortState(AbstractVendingMachineState)

<<CoffeeBeverage>>
+ getName(): String

CoffeeBeverageDecorator(CoffeeBeverage)
- beverage: CoffeeBeverage
+ CoffeeBeverageDecorator(beverage: CoffeeBeverage)
+ getName(): String

CoffeeVendingMachine(State)
- currentState: State
- states: Map<CoffeeVendingMachineState, State>
- inventory: Map<Integer, CoffeeBeverage>
- selectedBeverage: CoffeeBeverage
+ CoffeeVendingMachine()
+ changeStateTo(state: CoffeeVendingMachineState): void