package stackoverflow;

import stackoverflow.actors.Member;

import java.util.Date;

public class QuestionEditHistory {

    String id;
    Question previousQuestion;
    Question updatedQueston;

    Date createdOn;
    Member creator;
}
