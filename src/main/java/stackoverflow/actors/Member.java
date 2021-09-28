package stackoverflow.actors;

import stackoverflow.AbstractCommentable;
import stackoverflow.AbstractVotable;
import stackoverflow.Answer;
import stackoverflow.Comment;
import stackoverflow.Question;
import stackoverflow.Tag;
import stackoverflow.VoteType;
import stackoverflow.actors.badges.Badge;

import java.util.List;

public class Member extends User {

    String name;
    Account account;
    List<Badge> badges;
    List<Question> questions;
    List<Answer> answers;
    List<Comment> comments;
    int reputation;

    public Question addQuestion(Question question) {}
    public Answer addAnswer(Question question, Answer answer) {}
    public Comment addComment(AbstractCommentable commentable, Comment comment) {}
    public void vote(AbstractVotable votable, VoteType voteType) {}
    public void addTag(Question question, Tag tag) {}
    public void flag(AbstractVotable votable) {}
    public List<Badge> getBadges() {
        return badges;
    }

    public int getReputation() {
        return reputation;
    }

    public void addReputation(int reputation) {
        this.reputation += reputation;
    }
}
