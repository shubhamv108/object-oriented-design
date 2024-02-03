ExpenseType
+ INDIVIDUAL
+ GROUP

User
- name
+ User(name: String)
+ getName(): String

<<Expense>>
+ getPayer(): User
+ getAmount(): double
+ getType(): ExpenseType

IndividualExpense(Expense)
- payer
- amount

GroupExpense(Expense)
- payer
- amount
- participants: List<User>

ExpenseFactory
+ create(payer: User, amount: double, participants: List<User>)

<<Settlement>>
+ getDebitor():  User
+ getCreditor(): User
+ getAmount(): double

IndividualSettlement(Settlement)
- debitor: User
- creditor: User

GroupSettlement(Settlement)
- debitor: User
- creditor: User

SettlementManager
- settlements: List<Settlement>
- balances: Map<User, Double>
+ getInstance(): SettlementManager
+ showBalances(): void

SplitWiseManager
- expenses: List<Expense>
+ getInstance(): SplitWiseManager
+ addExpense(Expense): void
+ showSettlements(): void 