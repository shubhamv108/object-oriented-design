package commons.locks;

public interface ILockService<Name, Lock> {

    Lock getLock(Name name);

}
