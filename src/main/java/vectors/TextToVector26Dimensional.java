package vectors;

public class TextToVector26Dimensional {

    public static int[] textToVector(String text) {
        int[] vector = new int[26];

        text = text.toLowerCase();

        for (char c : text.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                vector[c - 'a']++;
            }
        }

        return vector;
    }

    public static void main(String[] args) {
        String text = "Hello World";

        int[] vector = textToVector(text);

        System.out.print("[");
        for (int i = 0; i < vector.length; i++) {
            System.out.print(vector[i]);
            if (i < vector.length - 1)
                System.out.print(", ");
        }
        System.out.println("]");
    }
}