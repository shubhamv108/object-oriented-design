package notificationservice.actors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private final Account account;
    private final String email;
    private final String mobileNumber;

    private final Map<DeviceType, List<Device>> devices;

    public User(Account account, String email, String mobileNumber, List<Device> devices) {
        this.account = account;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.devices = new HashMap<>();
        Arrays.stream(DeviceType.values()).forEach(deviceType -> this.devices.put(deviceType, new ArrayList<>()));
        devices.forEach(device -> this.devices.get(device.getType()).add(device));
    }
}
