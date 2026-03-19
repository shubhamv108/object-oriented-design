package dating;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Dating {

    public class System {
        private final Map<String, Profile> profiles = new HashMap<>();
        private final Map<String, Map<String, Swipe>> leftSwipe = new HashMap<>();
        private final Map<String, Map<String, Swipe>> rightSwipe = new HashMap<>();
        private final Map<String, List<Profile>> stacks = new HashMap<>();

        public String createProfile(Profile profile) {
           return null;
        }
        public List<Profile> getStacks(String profileId, String geoHash) {
            Profile profile = Optional.ofNullable(profiles.get(profileId)).orElseThrow();
            return new ArrayList<>(SuggestionStrategyFactory.getInstance().getDefault().getSuggestions(profiles.values(), geoHash, profile.preference));
        }
        public Boolean swipe(String a, String b, boolean liked) {
            Profile aProfile = Optional.ofNullable(profiles.get(a)).orElseThrow();
            Profile bProfile = Optional.ofNullable(profiles.get(b)).orElseThrow();

            /**
             * Sorting to avoid deadlock.
             */
            Profile aLockProfileRef = aProfile, bLockProfileRef = bProfile;
            if (a.compareTo(b) > 0) {
                Profile t = aLockProfileRef;
                aLockProfileRef = bLockProfileRef;
                bLockProfileRef = t;
            }
            Swipe swipe = new Swipe(aProfile, bProfile, liked);

            synchronized (aLockProfileRef) {
                synchronized (bLockProfileRef) {
                    if (liked) {
                        rightSwipe.computeIfAbsent(a, e -> new HashMap<>()).put(b, swipe);
                        return Optional.ofNullable(rightSwipe.get(a))
                                .map(m -> m.get(b))
                                .map(Swipe::isLiked)
                                .orElse(false);
                    } else {
                        leftSwipe.computeIfAbsent(swipe.a.id, e -> new HashMap<>()).put(swipe.b.id, swipe);
                    }
                }
            }
            return null;
        }
    }
    public class Profile {
        private final String id;
        private String geoHash;
        private final Preference preference = new Preference();
        public Profile(String id, String geoHash) {
            this.id = id;
            this.geoHash = geoHash;
        }

        public void setGeoHash(String geoHash) {
            this.geoHash = geoHash;
        }
    }
    public class Preference {}
    public class Swipe {
        private final Profile a;
        private final Profile b;
        private final boolean liked;
        private final long createdAt;

        public Swipe(Profile a, Profile b, boolean liked) {
            this.a = a;
            this.b = b;
            this.liked = liked;
            this.createdAt = java.lang.System.currentTimeMillis();
        }

        public boolean isLiked() {
            return liked;
        }
    }
    public interface MatchState {}
    public class Matched implements MatchState {}
    public class Match {
        private final Profile a;
        private final Profile b;
        private MatchState state;

        public Match(Profile a, Profile b) {
            this.a = a;
            this.b = b;
            state = new Matched();
        }
    }
    public enum SuggestionStrategy {
        NEAREST, PREFERENCE, NEAREST_PREFERENCE
    }
    public interface ISuggestionStrategy {
        Collection<Profile> getSuggestions(Collection<Profile> profiles, String geoHash, Preference preference);
    }
    public static class NearestISuggestionStrategy implements ISuggestionStrategy {
        @Override
        public Collection<Profile> getSuggestions(Collection<Profile> profiles, String geoHash, Preference preference) {
            return List.of();
        }
    }
    public static class PrefrenceBasedISuggestionStrategy implements ISuggestionStrategy {
        @Override
        public Collection<Profile> getSuggestions(Collection<Profile> profiles, String geoHash, Preference preference) {
            return List.of();
        }
    }
    public static class CompositeISuggestionStrategy implements ISuggestionStrategy {
        private final List<ISuggestionStrategy> strategies;
        public CompositeISuggestionStrategy(List<ISuggestionStrategy> strategies) {
            this.strategies = new ArrayList<>(strategies);
        }

        @Override
        public Collection<Profile> getSuggestions(Collection<Profile> profiles, String geoHash, Preference preference) {
            for (ISuggestionStrategy strategy : strategies)
                profiles = strategy.getSuggestions(profiles, geoHash, preference);
            return profiles;
        }
    }

    public static class SuggestionStrategyFactory {

        private static final class SingletonHolder {
            private static final SuggestionStrategyFactory INSTANCE = new SuggestionStrategyFactory();
        }

        private final Map<SuggestionStrategy, ISuggestionStrategy> strategies = new HashMap<>();

        private SuggestionStrategyFactory() {
            strategies.put(SuggestionStrategy.NEAREST, new NearestISuggestionStrategy());
            strategies.put(SuggestionStrategy.PREFERENCE, new PrefrenceBasedISuggestionStrategy());
            strategies.put(SuggestionStrategy.NEAREST_PREFERENCE, new CompositeISuggestionStrategy(
                    Arrays.asList(new NearestISuggestionStrategy(), new PrefrenceBasedISuggestionStrategy())));
        }

        public static SuggestionStrategyFactory getInstance() {
            return SingletonHolder.INSTANCE;
        }
        public ISuggestionStrategy get(SuggestionStrategy strategy) {
            return strategies.get(strategy);
        }

        public ISuggestionStrategy getDefault() {
            return strategies.get(SuggestionStrategy.NEAREST_PREFERENCE);
        }

        @Override
        public Object clone() {
            return this;
        }
    }
}
