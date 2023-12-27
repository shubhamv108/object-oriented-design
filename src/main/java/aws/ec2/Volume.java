package aws.ec2;

import aws.kms.KMS;


public class Volume {

    StorageType storageType;
    String deviceName;
    long size;

    VolumeType volumeType;

    boolean deleteOnTermination;
    boolean enncrypted;

    KMS kms;

    long throughput;

}
