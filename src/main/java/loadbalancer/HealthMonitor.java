package loadbalancer;

import loadbalancer.models.Backend;
import loadbalancer.models.Server;

public class HealthMonitor {
    
    // This method checks the health of the server
    public boolean checkHealth(Server server) {
        // Simulate health check (for example, a ping to the server or a health endpoint)
        return server.isHealthy(); // Assume the server has a method to report health
    }

    // If the server fails the health check, the LoadBalancer should be notified to remove it
    public void handleHealthCheckFailure(Server server, Backend backend) {
        System.out.println("Server " + backend.getName() + " is down.");
        backend.removeServer(server);  // This tells the load balancer to remove the server
    }
}