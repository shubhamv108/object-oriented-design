package image;

import org.checkerframework.checker.units.qual.A;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ImageProxy {

    private final String fileName;

    private RealImage realImage;

    private final List<String> cachedImages = new ArrayList<>();

    public ImageProxy(final String fileName) {
        this.fileName = fileName;
    }

    public void display() throws IOException {
        if (!isAuthenticated()) {
            System.out.println("Authentication failed");
            return;
        }

        if (realImage == null) {

            if (cachedImages.contains(fileName))
                System.out.println("Retrieving " + fileName + " from cache.");
            else {
                realImage = new RealImage(fileName); // lazy initializaton
                cachedImages.add(fileName);
            }
        }

        realImage.display();
    }

    private boolean isAuthenticated() throws IOException {
        final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter username: ");
        String userName = br.readLine();
        System.out.println("Enter password: ");
        String password = br.readLine();
        return AuthenticationService.getService().isUserAuthenticated(userName, password);
    }
}
