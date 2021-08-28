package commons.managers;

public interface IManager<Object> {

    Object create(Object object);
    Object get(Object object);
    Object update(Object object);
    Object delete(Object object);


}
