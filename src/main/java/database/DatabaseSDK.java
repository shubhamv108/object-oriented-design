package database;

import javax.management.Query;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DatabaseSDK {
    public enum DBType {
        POSTGRES, MYSQL
    }

    public interface AuthMech {
        Credentials authenticate();
    }

    public class Credentials {}

    public class Basic implements AuthMech {
        @Override
        public Credentials authenticate() {
            return null;
        }
    }
    public class Certifaicate implements AuthMech {
        @Override
        public Credentials authenticate() {
            return null;
        }
    }

    public interface Database {
        void connection();
        Result executeQuery(Query query);
        void disconnect();
    }

    public class Result {}

    public abstract class ConnectionConfig {}

    public class MySQlConeectionConfig extends ConnectionConfig {}
    public class PostgresConeectionConfig extends ConnectionConfig {}

    public class MYSQl implements Database {
        private final ConnectionConfig connectionConfig;
        private final AuthMech authMech;
        private final ConnectionPool connectionPool;

        public MYSQl(ConnectionConfig connectionConfig, AuthMech authMech, ConnectionPoolConfig connectionPoolConfig, ConnectionPool connectionPool) {
            this.connectionConfig = connectionConfig;
            this.authMech = authMech;

            this.connectionPool = ConnectionPoolFactory.getInstance().get(DBType.MYSQL, connectionPoolConfig);
        }

        @Override
        public void connection() {

        }

        @Override
        public Result executeQuery(Query query) {
            return null;
        }

        @Override
        public void disconnect() {

        }
    }
    public class Postgres implements Database {
        @Override
        public void connection() {

        }

        @Override
        public Result executeQuery(Query query) {
            return null;
        }

        @Override
        public void disconnect() {

        }
    }

    public abstract class ConnectionPoolConfig {
        private int poolsize;

        protected ConnectionPoolConfig(int poolsize) {
            this.poolsize = poolsize;
        }
    }

    public static class ConnectionPool {
        private final BlockingQueue<Connection> connections = new LinkedBlockingQueue<>();

        public ConnectionPool(List<Connection> connections) {
            for (Connection connection : connections)
                this.connections.offer(connection);
        }

        public Connection acquire() throws InterruptedException {
            return connections.take();
        }
        public void release(Connection connection) {
            connections.offer(connection);
        }
    }

    public interface Connection {}
    public class MySQlConnection implements Connection {}

    public class mySQlConnectionPoolConfig extends ConnectionConfig {}

    public class DatabaseFactory {
        public Database get(DBType type, AuthMech authMech, ConnectionConfig connectionConfig, ConnectionPoolConfig connectionPoolConfig) {
            switch (type) {
                case MYSQL -> {

                }
            }
            return null;
        }
    }

    public static class ConnectionPoolFactory {

        public static ConnectionPoolFactory getInstance() {
            return SingletonHolder.INSTANCE;
        }

        private static final class SingletonHolder {
            private static final ConnectionPoolFactory INSTANCE = new ConnectionPoolFactory();
        }

        public ConnectionPool get(DBType type, ConnectionPoolConfig connectionPoolConfig) {
            return new ConnectionPool(Arrays.asList());
        }
    }

}
