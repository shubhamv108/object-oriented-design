package aws.ec2;

public class KeyPair {
    private String name;
    private KeyPairType type;

    public KeyPair(String name) {
        this.name = name;
    }

    public String generateKeyPairAndDownloadPublicKeyFile(final KeyFileFormat privateKeyFileFormat) {
        return "";
    }
}
