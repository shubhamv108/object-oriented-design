package stackoverflow;

import stackoverflow.actors.Member;

import java.util.Date;
import java.util.Map;

public class AbstractVotable {

    String id;
    Map<VoteType, Integer> votes;
    Member creator;
    Date createdOn;

    public void vote(VoteType type) {}

    public Map<VoteType, Integer> getVotes() {
        return votes;
    }

    public Member getCreator() {
        return creator;
    }
}
