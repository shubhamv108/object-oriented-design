package twitter;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();
        ITwitterUser userA = userService.register("A");
        ITwitterUser userB = userService.register("B");

        userB.followUser(userA);
        Tweet tweet = userA.tweet("TweetByA");
        Timeline timeline = userB.getTimeLine();
        System.out.println(timeline);
        userB.like(tweet);
        userB.comment("CommentByB", tweet);
        timeline = userB.getTimeLine();
        System.out.println(timeline);

        System.out.println(userA.getHomeTimeline());
    }
}
