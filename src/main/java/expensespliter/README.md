SplitService
 - users: Map<String, ratelimiter.User>
 - groups: Map<String, Group>
 + addUser(name: String): String
 + addGroup(name: String, members: List<String>): String
 + createExpense(amount: double, paidBy: ratelimiter.User, participants: List<String>, splitValues: List<Double>, splitStrategy: ISplitStrategy) {synchronized} // useBuilder
 + addTransaction(transaction: Transaction) {synchronized}
 + showBalances(userId: String, otherUserId: String)
 + showBalances(userId: String)
 + simplifyGroupExpenses()

 ratelimiter.User
 - id: String
 - name: String
 - balanceSheet: BalanceSheet
 + getId(): String
 + getBalanceSheet(): BalanceSheet
 + toString(): String
 + equals(Object other): boolean
 + hashCode(): int

 Group
 - id: String
 - members: Set<ratelimiter.User>
 + getId(): String
 + getMembers(): List<ratelimiter.User>

 Expense
 - amount: double
 - paidBy: ratelimiter.User
 - participants: Set<ratelimiter.User>
 - splits: List<Split>
 + getAmount(): Double
 + getPaidBy(): ratelimiter.User
 + getParticipants(): List<ratelimiter.User>
 + getSplits(): List<Split>

 ISplitStrategy
 + createSplits(amount: double, paidBy: ratelimiter.User, participants: Set<ratelimiter.User>, splitValues: List<Double>): List<Split>
 + validateOrThrowException(amount: double, paidBy: ratelimiter.User, participants: Set<ratelimiter.User>, splitValues: List<Double>) throws InvalidExpenseException: boolean

  Split
  - user: ratelimiter.User
  - amount: double
  + getUser(): ratelimiter.User
  + getAmount(): Double

  InvalidExpenseException(Exception)

  EqualSplitStrategy(ISplitStrategy)
  ExactSplitStrategy(ISplitStrategy)
  PercentageSplitStrategy(ISplitStrategy)

 Transaction
 - payerId: String
 - payeeId: String
 - amount: double
 + getPayerId(): String
 + getPayeeId(): String
 + getAmount(): Double
 + toString(): String

 BalanceSheet
 - balances: Map<ratelimiter.User, Double>
 + adjustBalance(other: ratelimiter.User, amount: double)
 + getBalances(): Map<ratelimiter.User, Double>
 + getBalance(ratelimiter.User other): Double
