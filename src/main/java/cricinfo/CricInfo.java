package cricinfo;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CricInfo {

    public class CricinfoService {
        private final Map<String, Match> matches;
        private final Map<String, Player> players;

        public CricinfoService(Map<String, Match> matches, Map<String, Player> players) {
            this.matches = matches;
            this.players = players;
        }
        public Player addPlayer(String name) {
            return null;
        }
        public String createMatch(Team teamA, Team two, MatchTypeStrategy matchTypeStrategy) {
            return null;
        }
        public void startMatch(String matchId) {}
        public void processBall(String matchId, Ball ball) {}
        public void startNextInnings(String matchId) {}
        public void subscribe(String matchId, MatchObserver observer) {}
        public void endMatch(String matchId, MatchObserver observer) {}
    }

    public class Player {
        private final String id;
        private final String name;

        public Player(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        @Override
        public String toString() {
            return "Player{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
    public class Team {
        private final List<Player> players;

        public Team(List<Player> players) {
            this.players = players;
        }

        public List<Player> getPlayers() {
            return players;
        }

        @Override
        public String toString() {
            return "Team{" +
                    "players=" + players +
                    '}';
        }
    }
    public abstract class Subject {
        private final List<MatchObserver> observers = new ArrayList<>();
        public void subscribe(MatchObserver observer) {}
        public void unsubscribe(MatchObserver observer) {}
        public void notify(Match match, Ball ball) {}
    }
    public class Match extends Subject {
        private final Deque<Team> teams;
        private final MatchTypeStrategy matchTypeStrategy;
        private final List<Innings> innings = new ArrayList<>();
        private MatchState state = new ScheduledMatchState();
        private Team winner;

        public Match(Deque<Team> teams, MatchTypeStrategy matchTypeStrategy) {
            this.teams = new LinkedList<>(teams);
            this.matchTypeStrategy = matchTypeStrategy;
        }

        public void processBall(Ball ball) {
            this.state.processBall(this, ball);
        }

        public void setState(MatchState state) {
            this.state = state;
        }

        public void createNewInnings() {
            if (innings.size() >= matchTypeStrategy.getMaxInnings()) {
                System.out.println("Max innigs done");
                return;
            }
            this.innings.add(new Innings(teams.peek(), teams.getLast()));
        }

        public List<Innings> getAllInnings() {
            return innings;
        }

        public Innings getInnings() {
            return innings.getLast();
        }

        public Team getWinner() {
            return winner;
        }
    }
    public class Innings {
        private final Team battingteam;
        private final Team bowlingteam;
        private int score;
        private int wickets;
        private final List<Ball> balls = new ArrayList<>();

        public Innings(Team battingteam, Team bowlingteam) {
            this.battingteam = battingteam;
            this.bowlingteam = bowlingteam;
        }

        public void addBall(Ball ball) {}

        public int getOvers() {
            return balls.size()/6;
        }

        public int getScore() {
            return score;
        }

        public int getWickets() {
            return wickets;
        }

        public Team getBattingteam() {
            return battingteam;
        }

        public Team getBowlingteam() {
            return bowlingteam;
        }
    }
    public class Ball {
        private final Player batsman;
        private final Player bowler;
        private final int runs;

        public Ball(Player batsman, Player bowler, int runs) {
            this.batsman = batsman;
            this.bowler = bowler;
            this.runs = runs;
        }
    }
    public class NoBall extends Ball {
        public NoBall(Player batsman, Player bowler, int runs) {
            super(batsman, bowler, runs);
        }
    }
    public class WideBall extends Ball {
        public WideBall(Player batsman, Player bowler, int runs) {
            super(batsman, bowler, runs);
        }
    }
    public class Wicket {
        private final Ball ball;
        private final Player caughtBy;

        public Wicket(Ball ball, Player caughtBy) {
            this.ball = ball;
            this.caughtBy = caughtBy;
        }
    }
    public interface MatchTypeStrategy {
        int getMaxInnings();
        int getMaxOverInAnInnings();
    }
    public class T20MatchTypeStrategy implements MatchTypeStrategy {
        @Override
        public int getMaxInnings() {
            return 0;
        }

        @Override
        public int getMaxOverInAnInnings() {
            return 0;
        }
    }
    public class ODIMatchTypeStrategy implements MatchTypeStrategy {
        @Override
        public int getMaxInnings() {
            return 0;
        }

        @Override
        public int getMaxOverInAnInnings() {
            return 0;
        }
    }
    public interface MatchState {
        void processBall(Match match, Ball ball);
    }
    public class ScheduledMatchState implements MatchState {
        @Override
        public void processBall(Match match, Ball ball) {

        }
    }
    public class LiveMatchState implements MatchState {
        @Override
        public void processBall(Match match, Ball ball) {

        }
    }
    public class BreakMatchState implements MatchState {
        @Override
        public void processBall(Match match, Ball ball) {

        }
    }
    public class FinishedMatchState implements MatchState {
        @Override
        public void processBall(Match match, Ball ball) {

        }
    }
    public interface MatchObserver {
        void update(Match match, Ball lastBall);
    }
    public class Broadcaster implements MatchObserver {
        @Override
        public void update(Match match, Ball lastBall) {

        }
    }
    public class ScoreBoard implements MatchObserver {
        @Override
        public void update(Match match, Ball lastBall) {

        }
    }
}
