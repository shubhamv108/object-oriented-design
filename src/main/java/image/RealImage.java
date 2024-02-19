package image;

public class RealImage implements IImage {

    private final String fileName;

    public RealImage(final String fileName) {
        this.fileName = fileName;
        this.loadFromDisk();
    }

    @Override
    public void display() {
        System.out.println("Displaying " + this.fileName);
    }

    public void loadFromDisk() {
        System.out.println("Loading image " + this.fileName + " from disk.");
    }
}
