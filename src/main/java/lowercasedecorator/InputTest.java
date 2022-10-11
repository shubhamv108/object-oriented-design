package lowercasedecorator;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class InputTest {
    public static void main(String[] args) {
        int c;

        try {
            InputStream in =
                new LowerCaseInputStream(
                new BufferedInputStream(
                new FileInputStream("src/test/resources/input.txt")));

            while ((c = in.read()) > 0)
                System.out.println((char) c);

            in.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
