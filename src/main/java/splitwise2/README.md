ExpenseType
+ INDIVIDUAL
+ GROUP

ratelimiter.User
- name
+ ratelimiter.User(name: String)
+ getName(): String

<<Expense>>
+ getPayer(): ratelimiter.User
+ getAmount(): double
+ getType(): ExpenseType

IndividualExpense(Expense)
- payer: ratelimiter.User
- amount: double

GroupExpense(Expense)
- payer: ratelimiter.User
- amount: double
- participants: List<ratelimiter.User>

ExpenseFactory
+ create(payer: ratelimiter.User, amount: double, participants: List<ratelimiter.User>)

<<Settlement>>
+ getDebitor():  ratelimiter.User
+ getCreditor(): ratelimiter.User
+ getAmount(): double

IndividualSettlement(Settlement)
- debitor: ratelimiter.User
- creditor: ratelimiter.User

GroupSettlement(Settlement)
- debitor: ratelimiter.User
- creditor: ratelimiter.User

SettlementManager
- settlements: List<Settlement>
- balances: Map<ratelimiter.User, Double>
+ getInstance(): SettlementManager
+ showBalances(): void

SplitWiseManager
- expenses: List<Expense>
+ getInstance(): SplitWiseManager
+ addExpense(Expense): void
+ showSettlements(): void 