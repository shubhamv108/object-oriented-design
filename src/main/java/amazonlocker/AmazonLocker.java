package amazonlocker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Desing  a locker system like Amazon locker where delivery drivers can deposit packages and customers can pick them up using a code.
 *
 * Required Capacilities
 * 1. Are there diff sized compartments.
 * 2. how does customer get their code ? Are they sent an email.
 *
 * Error handling
 * 1. Can 1 customer can have multiple packages at once in the system at once ? Are access token unique per package ?
 * 2. How long do the codes last ? What happens to the package if it is never picker up ?
 * 3. What of all sized compartments are full if the driver tries to deposit ?
 *
 * Scope
 * 1. Are we modeling the whole delivery flow or just the piece when the driver arrives at the locker until the customer picks up ?
 *
 * Requirements
 * 1. Carrier deposits a package by specifying size (small, medium, large)
 *   - Systems assigns an available compartment of matching size.
 *   - Opens compartments and returns access token, or error if no space.
 *  2. Upon successful deposit, access token is generated and returned.
 *   - One access token per package
 *  3. User retrieves by entering access token.
 *   - System validates codes and access compartment.
 *   - Throws specific error code if code is invalid or expired
 *  4. Access token expired 7 days
 *   - Expired codes are rejected if used for pickup
 *   - Package remains in compartment until staff removes it.
 *  5. Staff can open all expired compartments to manually handle packages
 *   - System opens all compartment with expired token.
 *   - Staff physically removes packages and returns them to sender.
 *
 */
public class AmazonLocker {

    public class Carrier {

        public String deposit(Locker locker, Package pkg) throws NoSpaceException {
            Compartment compartment = locker
                    .assign(pkg.getSize(), CompartmentAssignStrategyFactory.getInstance().get(CompartmentAssignStrategies.FIRST_VAIALBLE));
            return compartment.place(pkg);
        }

    }

    public enum PackageSize {
        SMALL, MEDIUM, LARGE
    }

    public class Sender {

        private final String name;

        public Sender(String name) {
            this.name = name;
        }

        public void receiveReturned(Package pkg) {
            System.out.println("Returned package received");
        }

        @Override
        public String toString() {
            return "Sender{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    public class Package {
        private final PackageSize size;
        private final Sender sender ;

        public Package(PackageSize size, Sender sender) {
            this.size = size;
            this.sender = sender;
        }


        public PackageSize getSize() {
            return size;
        }

        @Override
        public String toString() {
            return "Package{" +
                    "size=" + size +
                    ", sender=" + sender +
                    '}';
        }
    }

    public enum CompartmentSize {
        SMALL, MEDIUM, LARGE
    }

    public enum CompartmentState {
        EMPTY, LOCKED, UNLOCKED
    }

    public class Compartment {
        private final CompartmentSize size;
        private Package pkg;
        private AccessToken accessToken;
        private CompartmentState state;

        public Compartment (CompartmentSize size) {
            this.size = size;
            this.state = CompartmentState.EMPTY;
        }

        public String place(Package pkg) {
            System.out.println(String.format("User opening compartment of size: %s to place package", size.name()));
            this.pkg = pkg;
            return accessToken.getToken();
        }

        public Package remove() {
            Package pkg = this.pkg;
            this.pkg = null;
            return pkg;
        }

        public void lock() {
            setState(CompartmentState.LOCKED);
        }

        public void setState(CompartmentState state) {
            this.state = state;
        }

        public boolean isAccessTokenValid() {
            return !accessToken.isExpired();
        }

        public String clearToken() {
            String token = this.accessToken.getToken();
            this.setAccessToken(null);
            return token;
        }

        public void setAccessToken(AccessToken accessToken) {
            this.accessToken = accessToken;
        }

        public CompartmentSize getSize() {
            return size;
        }

        public Package getPkg() {
            return pkg;
        }

        public boolean isNotLocked() {
            return CompartmentState.UNLOCKED.equals(this.state);
        }

        public boolean isNotEmpty() {
            return CompartmentState.EMPTY.equals(this.state);
        }
    }

    public class InvalidAccessTokenException extends Exception {
        private final String accessToken;

        public InvalidAccessTokenException(String accessToken) {
            this.accessToken = accessToken;
        }
    }

    public class AccessToken {
        private String token;
        private long expiryAtInMilliSeconds;

        public AccessToken (String token, long ttl) {
            this.token = token;
            this.expiryAtInMilliSeconds = System.currentTimeMillis() + ttl;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() >= this.expiryAtInMilliSeconds;
        }

        public String getToken() {
            return token;
        }
    }

    public class User {

        public Package retrieve(Locker locker, String accessToken) throws InvalidAccessTokenException {
            Compartment compartment = locker.retrieve(accessToken);
            return compartment.remove();
        }
    }

    public class Staff {

        public void removeAndReturn(Locker locker, Compartment compartment) {
            if (compartment.isNotLocked() && compartment.isNotEmpty()) {
                Package removed = compartment.remove();
                compartment.setState(CompartmentState.EMPTY);
                returnPkg(removed, removed.sender);
                String token = compartment.clearToken();
                locker.makeAvailable(token);
            }
        }

        private void returnPkg(Package pkg, Sender sender) {
            sender.receiveReturned(pkg);
        }

    }

    public class Locker {
        private final Map<CompartmentSize, List<Compartment>> availableCompartments = new HashMap<>();
        private final Map<String, Compartment> usedCompartments = new ConcurrentHashMap<>();

        public Locker (Map<CompartmentSize, Integer> compartments) {
            Arrays.stream(CompartmentSize.values())
                    .forEach(size -> availableCompartments.put(size, new CopyOnWriteArrayList<>()));
            compartments.forEach((key, value) -> IntStream.range(0, value).forEach(i -> availableCompartments.get(key).add(new Compartment(key))));
            unlockExpiredCompartments();
        }

        public Compartment assign(PackageSize size, CompartmentAssignStrategy compartmentAssignStrategy) throws NoSpaceException {
            if (availableCompartments.get(CompartmentSize.valueOf(size.name())).isEmpty())
                throw new NoSpaceException(size);

            Compartment compartment = null;
            synchronized (this) {
                compartment = compartmentAssignStrategy.assign(this.availableCompartments, size);
            }
            compartment.setState(CompartmentState.UNLOCKED);
            String token = generateToken();
            compartment.setAccessToken(new AccessToken(token, 1000000000));
            this.usedCompartments.put(token, compartment);
            return compartment;
        }

        private String generateToken() {
            return UUID.randomUUID().toString();
        }

        public Compartment retrieve(String accessToken) throws InvalidAccessTokenException {
            Compartment compartment = usedCompartments.get(accessToken);
            if (compartment == null || !compartment.isAccessTokenValid())
                throw new InvalidAccessTokenException(accessToken);
            compartment.setState(CompartmentState.UNLOCKED);
            makeAvailable(accessToken);
            return compartment;
        }

        public void unlockExpiredCompartments() {
            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);

            scheduledExecutorService.scheduleWithFixedDelay(() -> {
                usedCompartments
                        .values()
                        .stream()
                        .filter(c -> !c.isAccessTokenValid())
                        .forEach(c -> availableCompartments.get(c.getSize()).add(c));
            }, 10, 10, TimeUnit.SECONDS);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                scheduledExecutorService.shutdown();
                try {
                    if (!scheduledExecutorService.awaitTermination(60, TimeUnit.SECONDS)) {
                        scheduledExecutorService.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    scheduledExecutorService.shutdownNow();
                    Thread.currentThread().interrupt();
                }
            }));
        }

        public void makeAvailable(String accessToken) {
            final Compartment compartment = usedCompartments.remove(accessToken);
            availableCompartments.get(compartment.getSize()).add(compartment);
        }
    }

    public interface CompartmentAssignStrategy {
        Compartment assign(Map<CompartmentSize, List<Compartment>> availableComppartments, PackageSize size) throws NoSpaceException;
    }

    public static class FirstSizeAvailableDefaultCompartmentAssignStrategy implements CompartmentAssignStrategy {
        public Compartment assign(Map<CompartmentSize, List<Compartment>> availableCompartments, PackageSize size) throws NoSpaceException {
            List<Compartment> compartments = availableCompartments.get(CompartmentSize.valueOf(size.name()));
            if (compartments.isEmpty())
                throw new NoSpaceException(size);
            return compartments.get(0);
        }
    }

    public enum CompartmentAssignStrategies {
        FIRST_VAIALBLE
    }

    public static class CompartmentAssignStrategyFactory {
        private final Map<CompartmentAssignStrategies, CompartmentAssignStrategy> strategies = new HashMap<>();

        private CompartmentAssignStrategyFactory() {
            strategies.put(CompartmentAssignStrategies.FIRST_VAIALBLE, new FirstSizeAvailableDefaultCompartmentAssignStrategy());
        }

        public static CompartmentAssignStrategyFactory getInstance() {
            return SingletonHolder.INSTANCE;
        }

        private static final class SingletonHolder {
            private static final CompartmentAssignStrategyFactory INSTANCE = new CompartmentAssignStrategyFactory();
        }

        public CompartmentAssignStrategy get(CompartmentAssignStrategies strategy) {
            return strategies.get(strategy);
        }
    }

    public static class NoSpaceException extends Exception {
        private PackageSize packageSize;

        public NoSpaceException(PackageSize size) {
            this.packageSize = size;
        }
    }

    public static void main(String[] args) throws NoSpaceException, InvalidAccessTokenException {
        final AmazonLocker amazonLocker = new AmazonLocker();
        final Locker locker = amazonLocker.new Locker(Map.of(CompartmentSize.SMALL, 1, CompartmentSize.MEDIUM, 3, CompartmentSize.LARGE, 4));
        final Sender sender = amazonLocker.new Sender("shubhamv108");
        final Carrier carrier = amazonLocker.new Carrier();
        final String accessToken = carrier.deposit(locker, amazonLocker.new Package(PackageSize.SMALL, sender));
        final User user = amazonLocker.new User();
//        final Package pkg = user.retrieve(locker, accessToken);
//        System.out.println(pkg);
    }

}
