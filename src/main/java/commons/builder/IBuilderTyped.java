package commons.builder;

@FunctionalInterface
public interface IBuilderTyped<Object, ObjectBuilder> {

    Object build();

}
