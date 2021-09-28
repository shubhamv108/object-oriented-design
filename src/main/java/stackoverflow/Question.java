package stackoverflow;

import java.util.List;

public class Question extends AbstractCommentable {

    String id;
    String heading;
    String question;
    QuestionStatus status;
    List<Tag> tags;

    int flags;

    List<Answer> answers;
    List<QuestionEditHistory> editHistory;

    public List<Tag> getTags() {
        return tags;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public String getQuestion() {
        return question;
    }

    public String getHeading() {
        return heading;
    }

    public QuestionStatus getStatus() {
        return status;
    }

    public int getFlags() {
        return flags;
    }
}
