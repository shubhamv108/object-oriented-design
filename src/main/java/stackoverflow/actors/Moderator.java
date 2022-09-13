package stackoverflow.actors;

import stackoverflow.Question;

public class Moderator extends Member {

    public Boolean closeQuestion(Question question) { return false; }
    public Boolean restoreQuestion(Question question) {  return false; }

}
