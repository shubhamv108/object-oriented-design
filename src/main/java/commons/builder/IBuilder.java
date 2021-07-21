package commons.builder;

@FunctionalInterface
public interface IBuilder<Object> {
    Object build();
}
