package objectpool.jdbcconnectionpool;

import java.sql.Connection;

public class JDBCPoolConnection {

    private final Connection connection;

    public JDBCPoolConnection(Connection connection) {
        this.connection = connection;
    }

    protected Connection getConnection() {
        return this.connection;
    }

}
