package loadbalancer;

import loadbalancer.models.Request;
import loadbalancer.models.Server;

public interface ILoadBalanced {

    boolean add(Server server);
    Server get(Request input);

}
