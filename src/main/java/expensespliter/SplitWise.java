package expensespliter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Design and implement a Splitwise System that allows users to split expenses among groups and individuals. The system should handle expense tracking, balance calculations, and settlement of debts between users.
 */
public class SplitWise {

    public class SplitWiseService {
        private final Map<String, User> users = new HashMap<>();
        private final Map<String, Group> groups = new HashMap<>();

        public String addUser(String name) {
            User user = new User(name);
            users.put(user.getId(), user);
            return user.getId();
        }
        public String addGroup(List<String> members) {
            Group group = new Group(members.stream().map(users::get).collect(Collectors.toSet()));
            groups.put(group.getId(), group);
            return group.getId();
        }

        public void createExpense(double amount, String paidByUserId, Set<String> participantIds, List<Double> splitValues, ISplitStrategy splitStrategy) throws InvalidExpenseException {
            User paidBy = users.get(paidByUserId);
            List<User> participants = participantIds.stream().map(users::get).toList();
            createExpense(new Expense(amount, paidBy, participants, splitValues, splitStrategy));
        }

        private synchronized void createExpense(Expense expense) {
            User paidBy = expense.getPaidBy();
            for (Split split : expense.getSplits()) {
                if (paidBy.equals(split.getUser()))
                    continue;

                split.getUser().getBalanceSheet().adjustBalance(paidBy, -split.getAmount());
                paidBy.getBalanceSheet().adjustBalance(split.getUser(), split.getAmount());
            }
        }

        public synchronized void settle(Transaction transaction) {
            User payer = users.get(transaction.getFrom());
            User payee = users.get(transaction.getTo());
            double amount = transaction.getAmount();

            payer.getBalanceSheet().adjustBalance(payee, amount);
            payee.getBalanceSheet().adjustBalance(payer, -amount);
        }

        public void showBalanceSheet(String userId) {
            User user = users.get(userId);
            System.out.println(user.getBalanceSheet().getBalances());
        }

        public void showBalanceSheet(String userId, String otherUserId) {
            User user = users.get(userId);
            User otherUser = users.get(otherUserId);
            System.out.println(user.getBalanceSheet().getBalance(otherUser));
        }

        public List<Transaction> simplifyGroupDebts(String groupId) {
            Group group = groups.get(groupId);

            Map<User, Double> netBalances = new HashMap<>();
            for (User member : group.getMembers()) {
                double balance = 0;
                for (Map.Entry<User, Double> entry : member.getBalanceSheet().getBalances().entrySet())
                    if (group.getMembers().contains(entry.getKey()))
                        balance += entry.getValue();

                netBalances.put(member, balance);
            }

            List<Map.Entry<User, Double>> creditors = netBalances.entrySet().stream()
                    .filter(e -> e.getValue() > 0)
                    .collect(Collectors.toList());
            List<Map.Entry<User, Double>> debtors = netBalances.entrySet().stream()
                    .filter(e -> e.getValue() < 0)
                    .collect(Collectors.toList());

            creditors.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
            debtors.sort(Map.Entry.comparingByValue());

            List<Transaction> transactions = new ArrayList<>();
            int i = 0, j = 0;
            while (i < creditors.size() && j < debtors.size()) {
                Map.Entry<User, Double> creditor = creditors.get(i);
                Map.Entry<User, Double> debtor = debtors.get(j);

                double amountToSettle = Math.min(creditor.getValue(), -debtor.getValue());
                transactions.add(new Transaction(debtor.getKey().toString(), creditor.getKey().toString(), amountToSettle));

                creditor.setValue(creditor.getValue() - amountToSettle);
                debtor.setValue(debtor.getValue() + amountToSettle);

                if (Math.abs(creditor.getValue()) < 0.01)
                    ++i;
                if (Math.abs(debtor.getValue()) < 0.01)
                    ++j;
            }
            return transactions;
        }
    }

    public class User {
        private final String id;
        private final String name;
        private final BalanceSheet balanceSheet = new BalanceSheet();

        public User(String name) {
            this.name = name;
            this.id = UUID.randomUUID().toString();
        }

        public BalanceSheet getBalanceSheet() {
            return balanceSheet;
        }

        public String getId() {
            return id;
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            User user = (User) object;
            return Objects.equals(id, user.id);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id);
        }
    }
    public class Group {
        private final String id;
        private final Set<User> members;

        public Group(Set<User> members) {
            this.members = new HashSet<>(members);
            this.id = UUID.randomUUID().toString();
        }

        public String getId() {
            return id;
        }

        public Set<User> getMembers() {
            return Collections.unmodifiableSet(members);
        }
    }

    public static class Split {
        private final User user;
        private final double amount;

        public Split(final User user, final double amount) {
            this.user = user;
            this.amount = amount;
        }

        public User getUser() {
            return user;
        }

        public double getAmount() {
            return amount;
        }
    }

    public static class Expense {
        private final String id;
        private final double amount;
        private final User paidBy;
        private final List<Split> splits;

        public Expense(double amount, User paidBy, List<User> participants, List<Double> splitValues, ISplitStrategy splitStrategy) throws InvalidExpenseException { // use builder pattern
            this.id = UUID.randomUUID().toString();
            this.amount = amount;
            this.paidBy = paidBy;
            this.splits = splitStrategy.createSplit(amount, paidBy, participants, splitValues);
        }

        public String getId() {
            return id;
        }

        public double getAmount() {
            return amount;
        }

        public User getPaidBy() {
            return paidBy;
        }

        public List<Split> getSplits() {
            return Collections.unmodifiableList(splits);
        }
    }
    public class BalanceSheet {
        private final Map<User, Double> balances = new ConcurrentHashMap<>();

        public void adjustBalance(User other, double amount) {
            balances.put(other, balances.getOrDefault(other, 0D) + amount);
        }
        public Double getBalance(User other) {
            return balances.getOrDefault(other, 0D);
        }
        public Map<User, Double> getBalances() {
            return Collections.unmodifiableMap(balances);
        }
    }
    public class Transaction {
        private final String from;
        private final String to;
        private final double amount;

        public Transaction(String from, String user, double amount) {
            this.from = from;
            to = user;
            this.amount = amount;
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }

        public double getAmount() {
            return amount;
        }

        @Override
        public String toString() {
            return "Transaction{" +
                    "from=" + from +
                    ", to=" + to +
                    ", amount=" + amount +
                    '}';
        }
    }

    public interface ISplitStrategy {
        List<Split> createSplit(double amount, User paidBy, List<User> participants, List<Double> splitValues);
        boolean validateOrThrowException(double amount, User paidBy, List<User> participants, List<Double> splitValues) throws InvalidExpenseException;
    }

    public static class EqualSplitStrategy implements ISplitStrategy {
        @Override
        public List<Split> createSplit(double amount, User paidBy, List<User> participants, List<Double> splitValues) {
            double equalAmount = amount / participants.size();
            return participants.stream().map(user -> new Split(user, equalAmount)).toList();
        }

        @Override
        public boolean validateOrThrowException(double amount, User paidBy, List<User> participants, List<Double> splitValues) {
            return true;
        }
    }

    public static class ExactSplitStrategy implements ISplitStrategy {
        @Override
        public List<Split> createSplit(double amount, User paidBy, List<User> participants, List<Double> splitValues) {
            validateOrThrowException(amount, paidBy, participants, splitValues);

            return IntStream.range(0, participants.size())
                    .mapToObj(i -> new Split(participants.get(i), splitValues.get(i)))
                    .toList();
        }

        @Override
        public boolean validateOrThrowException(double amount, User paidBy, List<User> participants, List<Double> splitValues) throws InvalidExpenseException {
            if (participants.size() != splitValues.size())
                throw new InvalidExpenseException("");

            if (Math.abs(splitValues.stream().mapToDouble(Double::doubleValue).sum() - amount) > 0.01)
                throw new InvalidExpenseException("Sum of exact amounts must equal the total expense amount.");

            return true;
        }
    }
    public static class PercentageSplitStrategy implements ISplitStrategy {
        @Override
        public List<Split> createSplit(double amount, User paidBy, List<User> participants, List<Double> splitValues) {
            validateOrThrowException(amount, paidBy, participants, splitValues);

            List<Split> splits = new ArrayList<>();
            for (int i = 0; i < participants.size(); i++) {
                double percentAmount = (amount * splitValues.get(i)) / 100.0;
                splits.add(new Split(participants.get(i), percentAmount));
            }
            return splits;
        }

        @Override
        public boolean validateOrThrowException(double amount, User paidBy, List<User> participants, List<Double> splitValues) {
            if (participants.size() != splitValues.size())
                throw new InvalidExpenseException();

            if (Math.abs(splitValues.stream().mapToDouble(Double::doubleValue).sum() - 100.0) > 0.01) {
                throw new InvalidExpenseException("Sum of percentages must be 100.");
            }

            return true;
        }
    }

    public static class InvalidExpenseException extends RuntimeException {
        private String message;

        public InvalidExpenseException() {}

        public InvalidExpenseException(String message) {
            super(message);
        }
    }

    public static void main(String[] args) throws InvalidExpenseException {
        SplitWise splitWise = new SplitWise();
        SplitWiseService service = splitWise.new SplitWiseService();
        String userAId = service.addUser("A");
        String userBId = service.addUser("B");
        String userCId = service.addUser("C");
        String userDId = service.addUser("D");
        String groupId = service.addGroup(Arrays.asList(userAId, userBId, userCId, userDId));
        service.createExpense(1000, userAId, Set.of(userAId, userBId, userCId ,userDId), null, new EqualSplitStrategy());

        service.showBalanceSheet(userAId);
        service.showBalanceSheet(userBId);
        service.showBalanceSheet(userCId);
        service.showBalanceSheet(userDId);
        System.out.println(service.simplifyGroupDebts(groupId));

        service.settle(splitWise.new Transaction(userDId, userAId, 20));

        service.showBalanceSheet(userAId);
        service.showBalanceSheet(userBId);
        service.showBalanceSheet(userCId);
        service.showBalanceSheet(userDId);
        System.out.println(service.simplifyGroupDebts(groupId));
    }
}
