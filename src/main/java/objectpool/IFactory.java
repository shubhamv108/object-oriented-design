package objectpool;

import java.sql.SQLException;

public interface IFactory<Product> {

    Product create() throws SQLException;

}
