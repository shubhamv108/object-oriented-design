package router;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Router {

    public enum HttpMethod {
        GET, POST, PUT, PATCH, DELETE, OPTION,
    }

    public class Endpoint {

        private String service;
        private long connectTimeoutInMilliseconds;
        private long readTimeoutInMilliseconds;

        public Endpoint() {}

        public Endpoint(String service) {
            this.service = service;
        }

        public Endpoint(String service, long connectTimeoutInMilliseconds, long readTimeoutInMilliseconds) {
            this.service = service;
            this.connectTimeoutInMilliseconds = connectTimeoutInMilliseconds;
            this.readTimeoutInMilliseconds = readTimeoutInMilliseconds;
        }

        public void invoke(HttpMethod method, URI uri) {
            System.out.println(method + " " + uri);
        }
    }

    public class Route {
        private final HttpMethod method;
        private final String expression;
        private final Endpoint endpoint;

        Route(HttpMethod method, String path, Endpoint endpoint) {
            this.method = method;
            this.expression = path;
            this.endpoint = endpoint;
        }

        public Endpoint getEndpoint() {
            return endpoint;
        }

        public HttpMethod getMethod() {
            return method;
        }

        public String getExpression() {
            return expression;
        }
    }

    public class RouteRegistry {
        public List<Route> getRegexRoutes() {
            List<Route> routes = new ArrayList<>();
            routes.add(new Route(HttpMethod.GET, "/networks", new Endpoint()));
            routes.add(new Route(HttpMethod.POST, "/networks", new Endpoint()));
            routes.add(new Route(HttpMethod.POST, "/networks/[^/]+/devices", new Endpoint()));
            routes.add(new Route(HttpMethod.GET, "/networks/[^/]+/devices", new Endpoint()));
            routes.add(new Route(HttpMethod.DELETE, "/networks/[^/]+/devices", new Endpoint()));
            routes.add(new Route(HttpMethod.GET, "/chunks/[^/]+", new Endpoint()));
            routes.add(new Route(HttpMethod.POST, "/chunks/", new Endpoint()));
            routes.add(new Route(HttpMethod.GET, "/chunks/", new Endpoint()));
            return routes;
        }

        public List<Route> getTreeRoutes() {
            List<Route> routes = new ArrayList<>();
            routes.add(new Route(HttpMethod.GET, "/networks", new Endpoint()));
            routes.add(new Route(HttpMethod.POST, "/networks", new Endpoint()));
            routes.add(new Route(HttpMethod.POST, "/networks/*/devices", new Endpoint()));
            routes.add(new Route(HttpMethod.GET, "/networks/*/devices", new Endpoint()));
            routes.add(new Route(HttpMethod.DELETE, "/networks/*/devices", new Endpoint()));
            routes.add(new Route(HttpMethod.GET, "/chunks/*", new Endpoint()));
            routes.add(new Route(HttpMethod.POST, "/chunks/", new Endpoint()));
            return routes;
        }
    }

    public abstract class AbstractRouter {

        public abstract void init();

        public void route(HttpMethod method, URI uri) {
            Optional.ofNullable(getEndpoint(method, uri))
                    .ifPresent(endpoint -> endpoint.invoke(method, uri));
        }

        public abstract void addRoute(Route route);
        public abstract Endpoint getEndpoint(HttpMethod method, URI uri);
    }

    public class TreeRouter extends AbstractRouter {
        private final Node tree;

        public TreeRouter() {
            super();
            tree = new Node();
        }

        @Override
        public void init() {
            new RouteRegistry().getTreeRoutes().forEach(this::addRoute);
        }

        @Override
        public void addRoute(Route route) {
            String[] path = route.getExpression().replace("/", "^/^").split("\\^");
            int startIndex = path[0] == "" ? 1 : 0;
            tree.add(route.getMethod(), path, startIndex, route.getEndpoint());
        }

        @Override
        public Endpoint getEndpoint(HttpMethod method, URI uri) {
            String[] path = uri.getPath().replace("/", "^/^").split("\\^");
            int startIndex = path[0] == "" ? 1 : 0;
            return tree.get(method, path, startIndex);
        }

        public class Node {
            Map<String, Node> next = new HashMap<>();
            Map<HttpMethod, Endpoint> endpoints = new HashMap<>();

            void add(HttpMethod method, String[] path, int idx, Endpoint endpoint) {
                if (idx == path.length) {
                    if (endpoints.get(method) != null) {
                        throw new RuntimeException("Path already has an endpoint conf");
                    }
                    endpoints.put(method, endpoint);
                    return;
                }

                next.computeIfAbsent(path[idx], e -> new Node())
                        .add(method, path, idx + 1, endpoint);
            }

            Endpoint get(HttpMethod method, String[] path, int idx) {
                if (idx == path.length)
                    return endpoints.get(method);

                Node n = Optional.ofNullable(next.get(path[idx]))
                        .orElse(next.get("*"));

                if (n == null)
                    return null;

                return n.get(method, path, idx + 1);
            }
        }
    }

    public class RegexRouter extends AbstractRouter {

        public final Map<HttpMethod, List<Route>> routes = new HashMap<>();

        @Override
        public void init() {
            new RouteRegistry().getRegexRoutes().forEach(this::addRoute);
        }

        @Override
        public void addRoute(Route route) {
            routes.computeIfAbsent(route.getMethod(), e -> new ArrayList<>())
                    .add(route);
        }

        @Override
        public Endpoint getEndpoint(HttpMethod method, URI uri) {
            List<Route> matchingRoute = new ArrayList<>();
            for (Route route : routes.getOrDefault(method, Collections.emptyList()))
                if (uri.getPath().matches(route.getExpression()))
                    matchingRoute.add(route);

            if (matchingRoute.isEmpty())
                return null;

            if (matchingRoute.size() > 1)
                throw new RuntimeException("Conflicting routes found: " + matchingRoute);

            return matchingRoute.get(0).getEndpoint();
        }
    }

    public static void main(String[] args) throws URISyntaxException {
        AbstractRouter router = new Router()
//                .new TreeRouter();
                .new RegexRouter();
        router.init();
        router.route(HttpMethod.GET, new URI("/networks"));
        router.route(HttpMethod.POST, new URI("/networks"));
        router.route(HttpMethod.POST, new URI("/networks/1/devices"));
        router.route(HttpMethod.PATCH, new URI("/networks/1/devices"));
        router.route(HttpMethod.GET, new URI("/networks/1/devices"));
        router.route(HttpMethod.GET, new URI("/networks/1/"));
        router.route(HttpMethod.GET, new URI("/chunks/1"));
        router.route(HttpMethod.POST, new URI("/chunks/"));
        router.route(HttpMethod.GET, new URI("/chunks/"));
    }

}
