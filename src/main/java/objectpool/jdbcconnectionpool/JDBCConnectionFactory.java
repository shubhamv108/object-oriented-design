package objectpool.jdbcconnectionpool;

import objectpool.IObjectFactory;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCConnectionFactory implements IObjectFactory<JDBCPoolConnection> {

    private final String driverClassName;
    private final String connectionURL;
    private final String userName;
    private final String password;
    private final Properties properties;

    public JDBCConnectionFactory(final String driverClassName, final String connectionURL,
                                 final String userName, final String password) throws ClassNotFoundException {
        super();
        this.driverClassName = driverClassName;
        this.connectionURL = connectionURL;
        this.userName = userName;
        this.password = password;
        this.properties = new Properties();
        this.properties.setProperty("user", this.userName);
        this.properties.setProperty("password", this.password);
        Class.forName(driverClassName);
    }

    @Override
    public JDBCPoolConnection create() throws SQLException {
        try {
            return new JDBCPoolConnection(DriverManager.getConnection(this.connectionURL, this.properties));
        } catch (SQLException e) {
            throw e;
        }
    }

}
