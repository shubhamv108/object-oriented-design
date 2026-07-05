package vectors;

public class AsciiVector {

    public static int[] convert(String text) {
        int[] vector = new int[text.length()];

        for (int i = 0; i < text.length(); i++) {
            vector[i] = (int) text.charAt(i);
        }

        return vector;
    }

    public static void main(String[] args) {
        String text = "Hello";

        int[] vector = convert(text);

        for (int x : vector)
            System.out.print(x + " ");
    }
}