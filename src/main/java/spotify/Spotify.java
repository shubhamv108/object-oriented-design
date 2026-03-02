package spotify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 1. The music streaming service should allow users to browse and search for songs, albums, and artists.
 * Users should be able to create and manage playlists.
 * Users should be able to play, pause, skip, and seek within songs.
 * The system should recommend songs and playlists based on user preferences and listening history.
 * The system should handle concurrent requests and ensure smooth streaming experience for multiple users.
 * The system should be scalable and handle a large volume of songs and users.
 * The system should be extensible to support additional features such as social sharing and offline playback.
 */
public class Spotify {
    public class MusicService {

        public final Map<String, Song> songs;
        public final Map<String, User> users;

        public MusicService(Map<String, Song> songs, Map<String, User> users) {
            this.songs = songs;
            this.users = users;
        }

        public List<Song> search(String title) {
            return Collections.emptyList();
        }
        public List<Song> geRecommendations() {
            return new GenereRecommendationStartegy().get();
        }
    }
    public interface ArtistObserver {
        void update(Artist artist);
    }
    public class User implements  ArtistObserver {
        private PlaybackStrategy playbackStrategy;
        private final List<Artist> following = new ArrayList<>();

        public PlaybackStrategy getPlaybackStrategy() {
            return playbackStrategy;
        }

        public void follow(Artist artist) {
            following.add(artist);
            artist.addFollower(this);
        }

        @Override
        public void update(Artist artist) {

        }
    }
    public abstract class Subject {
        private final List<User> followers = new ArrayList<>();

        public void addFollower(User user) {}
        public void removeFollower(User user) {}
    }
    public class Artist extends Subject {

    }
    public interface Playable {}
    public class Song implements Playable {
        private final Artist artist;

        public Song(Artist artist) {
            this.artist = artist;
        }
    }
    public class Playlist implements Playable {}
    public class Player  {
        private PlayerState state;
        private List<Song> songs = new ArrayList<>();
        private final User user;
        private int currentSongIndex = -1;

        public Player(User user) {
            this.user = user;
        }

        public void playCurrentSongInQueue() {
            if (currentSongIndex >= 0 && currentSongIndex < songs.size()) {
                Song songToPlay = songs.get(currentSongIndex);
                user.getPlaybackStrategy().play(songToPlay, this);
            }
        }

        public void play() {
            state.play();
        }
        public void pause() {
            state.pause();
        }
        public void stop() {
            state.stop();
        }
        public void next() {}
        public void shuffle() {}
    }
    public enum SubscriptionTier {
        PREMIUM
    }
    public interface PlaybackStrategy {
        void play(Song song, Player player);

        // Simple Factory method to get the correct strategy
        static PlaybackStrategy getStrategy(SubscriptionTier tier, int songsPlayed) {
            return tier == SubscriptionTier.PREMIUM ? new PremiumPlaybackStrategy() : new FreePlaybackStrategy(songsPlayed);
        }
    }
    public static class PremiumPlaybackStrategy implements PlaybackStrategy {
        @Override
        public void play(Song song, Player player) {

        }
    }
    public static class FreePlaybackStrategy implements PlaybackStrategy {
        private int songsPlayed;
        private static final int SONGS_BEFORE_AD = 3;
        public FreePlaybackStrategy(int initialSongsPlayed) {
            this.songsPlayed = initialSongsPlayed;
        }
        @Override
        public void play(Song song, Player player) {

        }
    }
    public interface ShuffleStrategy {}
    public interface PlayerState {
        void play();
        void pause();
        void stop();
    }
    public interface RecommendationStrategy {
        List<Song> get();
    }
    public class GenereRecommendationStartegy implements RecommendationStrategy {
        @Override
        public List<Song> get() {
            return List.of();
        }
    }

    public interface Command {
        void execute();
    }

    public class PlayCommand implements Command {
        private final Player player;

        public PlayCommand(Player player) {
            this.player = player;
        }

        @Override
        public void execute() {
            player.play();
        }
    }

    public class PauseCommand implements Command {
        @Override
        public void execute() {

        }
    }
}
