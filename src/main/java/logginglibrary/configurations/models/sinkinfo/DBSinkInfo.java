package logginglibrary.configurations.models.sinkinfo;

import commons.builder.IBuilder;

import java.net.MalformedURLException;
import java.net.URL;

public class DBSinkInfo implements ISinkInfo {

    private final URL url;
    private final String username;
    private final String password;

    private DBSinkInfo(final URL url, final String username, final String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static DBSinkInfoBuilder builder() {
        return new DBSinkInfoBuilder();
    }

    public static class DBSinkInfoBuilder implements IBuilder<DBSinkInfo> {

        private URL url;
        private String username;
        private String password;

        public final DBSinkInfoBuilder withFilePath(final String url) throws MalformedURLException {
            this.url = new URL(url);
            return this;
        }

        public final DBSinkInfoBuilder withUsername(final String username) throws MalformedURLException {
            this.username = username;
            return this;
        }

        public final DBSinkInfoBuilder withPassword(final String password) throws MalformedURLException {
            this.password = password;
            return this;
        }

        @Override
        public DBSinkInfo build() {
            return new DBSinkInfo(url, username, password);
        }
    }

}
