package loadbalancer.strategies;

public class LoadBalanceStrategyFactory {
    public ILoadBalanceStrategy get(LoadBalanceStrategy loadBalanceStrategy) {
        switch (loadBalanceStrategy) {
            case RANDOM: return new RandomServerLoadBalanceStrategy();
            default: return new NullLoadBalanceStrategy();
        }
    }
}
