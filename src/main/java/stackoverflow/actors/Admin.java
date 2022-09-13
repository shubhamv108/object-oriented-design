package stackoverflow.actors;

public class Admin extends Member {

    public Boolean blockMember(Member member) { return false; }
    public Boolean unBlockMember(Member member) { return false; }

}
