package twitter;

public interface ITwitterUser {


    Tweet tweet(String content);

    Timeline getHomeTimeline();

    Timeline getTimeLine();

    void followUser(ITwitterUser user);

    void comment(String content, Commentable commentable);

    void like(Likable likable);
}
