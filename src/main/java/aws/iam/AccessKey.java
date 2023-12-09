package aws.iam;

import java.util.UUID;

public class AccessKey {

    private final String accessKeyID;
    private char[] secretAccessKey;

    private String description;

    public AccessKey() {
        this.accessKeyID = UUID.randomUUID().toString();
    }

    public void createAccessKey(final AccessKeyUseCase useCase) {

    }

    private enum AccessKeyUseCase {
        CLI, LOCAl, AppOnAWSComputeService, ThirdParty, AppOutSideAWS, OTHER
    }
}
