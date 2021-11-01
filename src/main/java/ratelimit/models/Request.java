package ratelimit.models;

public class Request {
    String serviceId;
    String apiName;
    String userName;
    String request;

    public String getServiceId() {
        return serviceId;
    }

    public String getApiName() {
        return apiName;
    }

    public String getUserName() {
        return userName;
    }
}
