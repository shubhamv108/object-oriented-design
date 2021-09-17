package ridesharing.strategies;

import ridesharing.entites.Location;
import ridesharing.entites.Trip;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class DurationSuggestTripStrategy implements ISuggestTripStrategy {
    private final TreeMap<Long, Map<Location, Set<Trip>>> sourceRoutes = new TreeMap<>();
    private final TreeMap<Long, Map<Location, Set<Trip>>> destinationRoutes = new TreeMap<>();

    public Collection<Trip> getTrips(final Long startTime, final Long endTime, final Location source, final Location destination) {
        Set<Trip> trips = this.sourceRoutes.subMap(startTime, endTime)
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(source))
                .map(entry -> entry.getValue().get(source))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        trips.addAll(this.destinationRoutes.subMap(startTime, endTime)
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(destination))
                .map(entry -> entry.getValue().get(destination))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet()));
        return trips;
    }

    public void addTrip(final Trip trip) {
        this.addTrip(trip, this.sourceRoutes, trip.getStartTimeFromSource().getTime(), trip.getSource());
        this.addTrip(trip, this.destinationRoutes, trip.getEndTimeAtDestination().getTime(), trip.getDestination());
    }

    private void addTrip(final Trip trip, TreeMap<Long, Map<Location, Set<Trip>>> trips, Long time, Location location) {
        Map<Location, Set<Trip>> locations = trips.get(time);
        if (locations == null) trips.put(trip.getStartTimeFromSource().getTime(), locations = new HashMap<>());
        Set<Trip> tripList = locations.get(trip.getSource());
        if (tripList == null) locations.put(location, tripList = new HashSet<>());
        tripList.add(trip);
    }

    public void removeTrip(final Trip trip) {
        this.removeTrip(trip, this.sourceRoutes, trip.getStartTimeFromSource().getTime(), trip.getSource());
        this.removeTrip(trip, this.destinationRoutes, trip.getEndTimeAtDestination().getTime(), trip.getDestination());
    }

    private void removeTrip(final Trip trip, final TreeMap<Long, Map<Location, Set<Trip>>> trips,
                            final Long time, final Location location) {
        final Map<Location, Set<Trip>> locations = trips.get(time);
        if (locations != null) {
            final Set<Trip> tripSet = locations.get(location);
            if (tripSet != null) {
                tripSet.remove(trip);
                if (tripSet.isEmpty()) {
                    locations.remove(location);
                    if (locations.isEmpty()) {
                        trips.remove(time);
                    }
                }
            }
        }
    }

}
