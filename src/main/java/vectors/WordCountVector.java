package vectors;

import java.util.*;

public class WordCountVector {

    public static void main(String[] args) {
        String text = "java is good java is fast";

        Map<String, Integer> vector = new HashMap<>();

        for (String word : text.toLowerCase().split("\\s+")) {
            vector.put(word, vector.getOrDefault(word, 0) + 1);
        }

        System.out.println(vector);
    }
}