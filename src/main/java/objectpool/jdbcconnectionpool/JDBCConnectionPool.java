package objectpool.jdbcconnectionpool;

import objectpool.IPool;
import objectpool.pools.EagerBoundedBlockingPool;

import java.sql.SQLException;

public class JDBCConnectionPool {

    private final IPool<JDBCPoolConnection> pool;

    public JDBCConnectionPool(final int size, final String driverClassName, final String connectionURL,
                              final String userName, final String password) throws SQLException, ClassNotFoundException {
        this.pool = new EagerBoundedBlockingPool<>(
                size,
                new JDBCConnectionFactory(driverClassName, connectionURL, userName, password),
                new JDBCConnectionValidator());
    }

    public JDBCPoolConnection get() {
        return this.pool.get();
    }

    public void release(final JDBCPoolConnection jdbcPoolConnection) {
        System.out.println("Releasing connection");
        this.pool.release(jdbcPoolConnection);
    }

}
