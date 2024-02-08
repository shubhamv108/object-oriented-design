CoffeeVendingMachineState
+ Idle
+ InsertedMoney
+ SelectedCoffee
+ DispensingCoffee
+ Aborted

Coin
number: int

<<State>>
+ insertCoin(coins: List<Coin>): void
+ selectCoffeeBeverage(beverageNumber int): void
+ addIngredient(ingredientNumber: int): void
+ dispenseCoffee(): AbstractCoffeeBeverage
+ dispenseCash(): int
+ abort(): void

AbstractState(State)
- coffeeVendingMachine: CoffeeVendingMachine

IdleState(AbstractState)
InsertedMoneyState(AbstractState)
SelectedCoffeeState(AbstractState)
DispensingCoffeeState(AbstractState)
AbortState(AbstractState)

AbstractCoffeeBeverage
- name: String
+ AbstractCoffeeBeverage(beverage: AbstractCoffeeBeverage)
+ getName(): String

CoffeeVendingMachine(State)
- currentState: State
- states: Map<CoffeeVendingMachineState, State>
- inventory: Map<Integer, AbstractCoffeeBeverage>
- selectedBeverage: AbstractCoffeeBeverage
+ CoffeeVendingMachine()
+ changeStateTo(state: CoffeeVendingMachineState): void