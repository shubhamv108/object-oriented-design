package aws.ec2;

import aws.iam.Role;
import aws.vpc.SecurityGroup;

public class EC2instanceConfiguration {
    private AMI ami;

    private EC2InstanceSecurity ec2InstanceSecurity = new EC2InstanceSecurity();
    private NetworkSettings networkSettings = new NetworkSettings();
    private final EC2UserData ec2UserData = new EC2UserData();

    public EC2instanceConfiguration(final AMI ami) {
        this.ami = ami;
    }
}

class EC2InstanceSecurity {
    private Role role;

    private SecurityGroup securityGroup;
}
