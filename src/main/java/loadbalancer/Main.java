package loadbalancer;


import loadbalancer.models.Server;
import loadbalancer.strategies.LoadBalanceStrategy;
import loadbalancer.strategies.LoadBalanceStrategyFactory;

public class Main {

    public static void main(String[] args) {
        final int defaultMaxServers = 10;
        final LoadBalanceStrategy defaultLoadBalanceStrategy = LoadBalanceStrategy.RANDOM;
        final LoadBalanceStrategyFactory loadBalanceStrategyFactory = new LoadBalanceStrategyFactory();
        final LoadBalancerService loadBalancerService = new LoadBalancerService(
                defaultMaxServers, defaultLoadBalanceStrategy, loadBalanceStrategyFactory);

        final String backendName = "A";
        loadBalancerService.register(backendName, new Server("ip1", "80"));
        loadBalancerService.register(backendName, new Server("ip2", "80"));
        loadBalancerService.register(backendName, new Server("ip3", "80"));
        loadBalancerService.register(backendName, new Server("ip4", "80"));
        loadBalancerService.register(backendName, new Server("ip5", "80"));
        loadBalancerService.register(backendName, new Server("ip6", "80"));
        loadBalancerService.register(backendName, new Server("ip7", "80"));
        loadBalancerService.register(backendName, new Server("ip8", "80"));
        loadBalancerService.register(backendName, new Server("ip9", "80"));
        loadBalancerService.register(backendName, new Server("ip10", "80"));
        loadBalancerService.register(backendName, new Server("ip11", "80"));

        System.out.println(loadBalancerService.serve(backendName, null).getIpAddress());
        System.out.println(loadBalancerService.serve(backendName, null).getIpAddress());
        System.out.println(loadBalancerService.serve(backendName, null).getIpAddress());
        System.out.println(loadBalancerService.serve(backendName, null).getIpAddress());
        System.out.println(loadBalancerService.serve(backendName, null).getIpAddress());
        System.out.println(loadBalancerService.serve(backendName, null).getIpAddress());
        System.out.println(loadBalancerService.serve(backendName, null).getIpAddress());
        System.out.println(loadBalancerService.serve(backendName, null).getIpAddress());
        System.out.println(loadBalancerService.serve(backendName, null).getIpAddress());
        System.out.println(loadBalancerService.serve(backendName, null).getIpAddress());
    }
}
