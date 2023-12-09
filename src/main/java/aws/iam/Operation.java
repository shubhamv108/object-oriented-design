package aws.iam;

public interface Operation {
    enum EC2 implements Operation {
        DESCRIBE
    }

    enum CLOUDWATCH implements Operation {
        LISTMETRICS,
        GETMETRICSTATISTICS,
        DESCRIBE,
    }
}
