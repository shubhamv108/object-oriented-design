package objectpool.jdbcconnectionpool;

import objectpool.IValidator;

import java.sql.SQLException;

public class JDBCConnectionValidator implements IValidator<JDBCPoolConnection> {
    @Override
    public boolean isValid(final JDBCPoolConnection poolConnection) {
        boolean isValid = false;
        try {
            if (poolConnection != null && poolConnection.getConnection() != null && !poolConnection.getConnection().isClosed())
                isValid = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isValid;
    }

    @Override
    public void invalidate(final JDBCPoolConnection poolConnection) {
        try {
            if (poolConnection != null && poolConnection.getConnection() != null && poolConnection.getConnection().isClosed()) {
                poolConnection.getConnection().close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
