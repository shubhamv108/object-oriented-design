package loadbalancer.factories;

import loadbalancer.enums.LoadBalanceStrategy;
import loadbalancer.models.Backend;
import loadbalancer.strategies.DynamicWeightedRoundRobinLeastConnectionsLoadBalancer;
import loadbalancer.strategies.DynamicWeightedRoundRobinLoadBalancer;
import loadbalancer.strategies.HashLoadBalancerStrategy;
import loadbalancer.strategies.ILoadBalanceStrategy;
import loadbalancer.strategies.LeastConnectionLoadBalanceStrategy;
import loadbalancer.strategies.LeastResponseTimeLoadBalanceStrategy;
import loadbalancer.strategies.NullLoadBalanceStrategy;
import loadbalancer.strategies.PeakExponentiallyWeightedMovingAverageLoadBalancer;
import loadbalancer.strategies.RandomServerLoadBalanceStrategy;
import loadbalancer.strategies.RoundRobinLoadBalanceStrategy;
import loadbalancer.strategies.WeightedRandomLoadBalancer;
import loadbalancer.strategies.WeightedRoundRobinLoadBalancer;

public class LoadBalanceStrategyFactory {

    public static LoadBalanceStrategyFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        private static final LoadBalanceStrategyFactory INSTANCE = new LoadBalanceStrategyFactory();
    }

    public ILoadBalanceStrategy get(LoadBalanceStrategy loadBalanceStrategy, Backend backend) {
        if (loadBalanceStrategy == null)
            return new NullLoadBalanceStrategy();

        switch (loadBalanceStrategy) {
            case RANDOM: return new RandomServerLoadBalanceStrategy(backend);
            case HASH: return new HashLoadBalancerStrategy(backend);
            case ROUND_ROBIN: return new RoundRobinLoadBalanceStrategy(backend);
            case LEAST_RESPONSE_TIME: return new LeastResponseTimeLoadBalanceStrategy(backend);
            case LEAST_CONNECTIONS: return new LeastConnectionLoadBalanceStrategy(backend);
            case WEIGHTED: return new WeightedRandomLoadBalancer(backend);
            case WEIGHTED_ROUND_ROBIN: return new WeightedRoundRobinLoadBalancer(backend);
            case DYNAMIC_WEIGHTED_ROUND_ROBIN: return new DynamicWeightedRoundRobinLoadBalancer(backend);
            case DYNAMIC_WEIGHTED_ROUND_ROBIN_LEAST_CONNECTIONS: return new DynamicWeightedRoundRobinLeastConnectionsLoadBalancer(backend);
            case PEAK_EXPONENTIALLY_WEIGHTED_MOVING_AVERAGE: return new PeakExponentiallyWeightedMovingAverageLoadBalancer(backend);
            default: return new NullLoadBalanceStrategy();
        }
    }
}
