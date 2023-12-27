package aws.vpc;


import java.util.ArrayList;
import java.util.List;

enum SGType {
    HTTP, SSh, Custom
}

class SGRule {
    SGType sgType;
    String protocol;
    int portRange;
    String source;
    String description;
}
public class SecurityGroup {

    List<SGRule> inBound = new ArrayList<>();
    List<SGRule> outBound = new ArrayList<>();
}
