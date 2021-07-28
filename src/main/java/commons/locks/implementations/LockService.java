package commons.locks.implementations;

import commons.locks.ILockService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockService<Name, Lock> implements ILockService<Name, Lock> {

    private final Map<Name, Lock> locks = new ConcurrentHashMap<>();
    private final Class<Lock> lockClass;

    public LockService(final Class<Lock> lockClass) {
        this.lockClass = lockClass;
    }

    @Override
    public Lock getLock(final Name name)  {
        var lock = this.locks.get(name);
        if (lock == null) {
            synchronized (name) {
                lock = this.locks.get(name);
                if (lock == null) {
                    this.locks.put(name, lock = this.getNewLock());
                }
            }
        }
        return (Lock) lock;
    }

    private Lock getNewLock() {
        Lock lock = null;
        if (ReentrantLock.class.isAssignableFrom(this.lockClass)) {
            lock = (Lock) new ReentrantLock();
        } else if (ReentrantReadWriteLock.class.isAssignableFrom(ReentrantReadWriteLock.class)) {
            lock = (Lock) new ReentrantReadWriteLock();
        }
        return lock;
    }

}