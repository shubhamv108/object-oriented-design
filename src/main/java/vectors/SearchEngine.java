package vectors;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

public class SearchEngine {

    public class Document {
        private int id;
        private String text;
        private final Map<String, Integer> tf = new HashMap<>();

        public Document (int id, String text){
            this.id = id;
            this.text = text;
        }
    }

    public class Tokenizer {
        public List<String> tokenize(String text) {
            text = text.toLowerCase();
            text = text.replaceAll("[^a-z0-9 ]"," ");

            String[] words = text.split("\\s+");
            return Arrays.stream(words).filter(word -> !word.isEmpty()).toList();
        }
    }

    public class StopWords {

        private final Set<String> stop = new HashSet<>();

        public StopWords() {
            String[] words = {
                    "a","an","the","is","are",
                    "was","were","of","to",
                    "in","on","at","for",
                    "and","or","by","with",
                    "as","be","this","that",
                    "it","from"
            };
            stop.addAll(Arrays.asList(words));
        }

        boolean isStopWord(String s) {
            return stop.contains(s);
        }
    }

    public class Vocabulary {
        private final Set<String> words = new TreeSet<>();

        public void add(String word) {
            words.add(word);
        }

        public int size() {
            return words.size();
        }

        public void print() {
            System.out.println("\nVocabulary:");
            for (String word : words)
                System.out.print(word + " ");
            System.out.println();
        }
    }

    public class Indexer {
        private final List<Document> documents = new ArrayList<>();
        private final Vocabulary vocabulary = new Vocabulary();
        private final Tokenizer tokenizer = new Tokenizer();
        private final StopWords stopWords = new StopWords();

        public void load(String file) throws Exception {
            AtomicInteger id = new AtomicInteger(1);
            Files.lines(Paths.get(file)).forEach(line -> {
                final Document doc = new Document(id.getAndIncrement(), line);
                final List<String> tokens = tokenizer.tokenize(line);

                for (final String token : tokens) {
                    if (stopWords.isStopWord(token))
                        continue;
                    vocabulary.add(token);
                    doc.tf.put(token, doc.tf.getOrDefault(token,0) + 1);
                }

                documents.add(doc);
            });
        }

        public void printDocuments(){
            System.out.println();
            for (Document document : documents) {
                System.out.println("Doc " + document.id);
                System.out.println(document.text);
                System.out.println(document.tf);
                System.out.println();
            }
        }

        public void stats() {
            System.out.println("\nDocuments : "+ documents.size());
            System.out.println("Vocabulary : "+ vocabulary.size());
        }
    }

    void main(String args[]) throws Exception {
        Indexer indexer= new Indexer();
        indexer.load("src/main/java/vectors/documents.txt");

        indexer.stats();
        indexer.printDocuments();
        indexer.vocabulary.print();
    }
}