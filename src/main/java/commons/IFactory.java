package commons;

import java.io.IOException;

@FunctionalInterface
public interface IFactory<Input, Product> {

    Product get(Input input) throws IOException;

}
