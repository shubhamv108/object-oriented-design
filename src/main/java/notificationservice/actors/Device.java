package notificationservice.actors;

public class Device {
    private final String token;
    private final DeviceType type;

    public Device(String token, DeviceType type) {
        this.token = token;
        this.type = type;
    }

    public DeviceType getType() {
        return type;
    }

    public String getToken() {
        return token;
    }
}
