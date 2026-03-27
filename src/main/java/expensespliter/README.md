SplitService
 - users: Map<String, User>
 - groups: Map<String, Group>
 + addUser(name: String): String
 + addGroup(name: String, members: List<String>): String
 + createExpense(amount: double, paidBy: User, participants: List<String>, splitValues: List<Double>, splitStrategy: ISplitStrategy) {synchronized} // useBuilder
 + addTransaction(transaction: Transaction) {synchronized}
 + showBalances(userId: String, otherUserId: String)
 + showBalances(userId: String)
 + simplifyGroupExpenses()

 User
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
 - members: Set<User>
 + getId(): String
 + getMembers(): List<User>

 Expense
 - amount: double
 - paidBy: User
 - participants: Set<User>
 - splits: List<Split>
 + getAmount(): Double
 + getPaidBy(): User
 + getParticipants(): List<User>
 + getSplits(): List<Split>

 ISplitStrategy
 + createSplits(amount: double, paidBy: User, participants: Set<User>, splitValues: List<Double>): List<Split>
 + validateOrThrowException(amount: double, paidBy: User, participants: Set<User>, splitValues: List<Double>) throws InvalidExpenseException: boolean

  Split
  - user: User
  - amount: double
  + getUser(): User
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
 - balances: Map<User, Double>
 + adjustBalance(other: User, amount: double)
 + getBalances(): Map<User, Double>
 + getBalance(User other): Double
