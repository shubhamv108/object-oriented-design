package loadbalancer.models;

import java.util.Objects;

public class Server {
    private String ipAddress;
    private String port;

    public Server(String ipAddress, String port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Server)) return false;
        Server server = (Server) o;
        return ipAddress.equals(server.ipAddress) && port.equals(server.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipAddress, port);
    }

    public <R> Object serve(R r) {
        return null;
    }
}
