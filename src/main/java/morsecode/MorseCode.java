package morsecode;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class MorseCode {

    public class Node {
        private final char character;
        private Node left, right;

        public Node(char character) {
            this.character = character;
        }

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }

        public char getCharacter() {
            return character;
        }
    }

    public class MorseTreeFactory {
        public Node generate(final String order) {
            int idx = 0;
            Node root = new Node(order.charAt(idx++));
            Queue<Node> q = new LinkedList<>();
            q.offer(root);
            while (!q.isEmpty()) {
                Node p = q.poll();
                p.left = new Node(order.charAt(idx++));
                p.right = new Node(order.charAt(idx++));
                q.offer(p);
            }
            return root;
        }
    }

    public class MorseDictionary {

        private final Map<Character, String> charToMorse = new HashMap<>();
        private final Map<String, Character> morseToChar = new HashMap<>();

        private static final String ENGLISH_MORSE_CODE_BINARY_TREE = "$ETIANMSURWDKGOHVF$L$PJBXCYZQ54$3$$2$$$$16$$$$$7$$$8$90$?_$.$@$!$&$:$;$=$+$-$\"$$$";

        public MorseDictionary() {
            init();
        }

        private void init() {

        }

        private void put(char c, String morse) {
            charToMorse.put(c, morse);
            morseToChar.put(morse, c);
        }

        public String getMorse(char c) {
            return charToMorse.get(Character.toUpperCase(c));
        }

        public Character getChar(String morse) {
            return morseToChar.get(morse);
        }
    }

    public final class MorseDictionaryFactory {

        public static Map<Character, String> createCharToMorse() {
            Map<Character, String> map = new HashMap<>();

            // Letters
            map.put('A', ".-");    map.put('B', "-...");
            map.put('C', "-.-.");  map.put('D', "-..");
            map.put('E', ".");     map.put('F', "..-.");
            map.put('G', "--.");   map.put('H', "....");
            map.put('I', "..");    map.put('J', ".---");
            map.put('K', "-.-");   map.put('L', ".-..");
            map.put('M', "--");    map.put('N', "-.");
            map.put('O', "---");   map.put('P', ".--.");
            map.put('Q', "--.-");  map.put('R', ".-.");
            map.put('S', "...");   map.put('T', "-");
            map.put('U', "..-");   map.put('V', "...-");
            map.put('W', ".--");   map.put('X', "-..-");
            map.put('Y', "-.--");  map.put('Z', "--..");

            // Numbers
            map.put('0', "-----"); map.put('1', ".----");
            map.put('2', "..---"); map.put('3', "...--");
            map.put('4', "....-"); map.put('5', ".....");
            map.put('6', "-...."); map.put('7', "--...");
            map.put('8', "---.."); map.put('9', "----.");

            // Punctuation (ITU subset)
            map.put('.', ".-.-.-");
            map.put(',', "--..--");
            map.put('?', "..--..");
            map.put('\'', ".----.");
            map.put('!', "-.-.--");
            map.put('/', "-..-.");
            map.put('(', "-.--.");
            map.put(')', "-.--.-");
            map.put('&', ".-...");
            map.put(':', "---...");
            map.put(';', "-.-.-.");
            map.put('=', "-...-");
            map.put('+', ".-.-.");
            map.put('-', "-....-");
            map.put('_', "..--.-");
            map.put('"', ".-..-.");
            map.put('$', "...-..-");
            map.put('@', ".--.-.");

            return Collections.unmodifiableMap(map);
        }

        public static Map<String, Character> createMorseToChar(Map<Character, String> forward) {
            Map<String, Character> reverse = new HashMap<>();
            for (Map.Entry<Character, String> e : forward.entrySet()) {
                reverse.put(e.getValue(), e.getKey());
            }
            return Collections.unmodifiableMap(reverse);
        }
    }

    public class MorseEncoder {

        private final MorseDictionary dictionary;

        public MorseEncoder(MorseDictionary dictionary) {
            this.dictionary = dictionary;
        }

        public String encode(String text) {
            StringBuilder result = new StringBuilder();

            for (char c : text.toCharArray()) {
                if (c == ' ') {
                    result.append(" / "); // word separator
                } else {
                    String morse = dictionary.getMorse(c);
                    if (morse != null) {
                        result.append(morse).append(" ");
                    }
                }
            }

            return result.toString().trim();
        }
    }

    public class MorseDecoder {

        private final MorseDictionary dictionary;

        public MorseDecoder(MorseDictionary dictionary) {
            this.dictionary = dictionary;
        }

        public String decode(String morseCode) {
            StringBuilder result = new StringBuilder();

            String[] words = morseCode.split(" / ");

            for (String word : words) {
                String[] letters = word.trim().split(" ");

                for (String letter : letters) {
                    Character c = dictionary.getChar(letter);
                    if (c != null) {
                        result.append(c);
                    }
                }
                result.append(" ");
            }

            return result.toString().trim();
        }
    }

    public class MorseService {

        private final MorseEncoder encoder;
        private final MorseDecoder decoder;

        public MorseService() {
            MorseDictionary dict = new MorseDictionary();
            this.encoder = new MorseEncoder(dict);
            this.decoder = new MorseDecoder(dict);
        }

        public String encode(String text) {
            return encoder.encode(text);
        }

        public String decode(String morse) {
            return decoder.decode(morse);
        }
    }

    void main(String[] args) {
        MorseService service = new MorseService();

        String encoded = service.encode("HELLO WORLD");
        System.out.println("Encoded: " + encoded);

        String decoded = service.decode(encoded);
        System.out.println("Decoded: " + decoded);
    }

}
