package vectors;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class VectorIndexer {

    List<String> documents = new ArrayList<>();
    Set<String> vocabulary = new TreeSet<>();
    List<Map<String,Integer>> docVectors = new ArrayList<>();

    public void load(String file) throws Exception {

        for(String line : Files.readAllLines(Paths.get(file))){

            documents.add(line);

            Map<String,Integer> vector = new HashMap<>();

            for(String word : tokenize(line)){
                vocabulary.add(word);
                vector.put(word,
                        vector.getOrDefault(word,0)+1);
            }

            docVectors.add(vector);
        }
    }

    List<String> tokenize(String text){

        text=text.toLowerCase();

        text=text.replaceAll("[^a-z ]","");

        return Arrays.asList(text.split("\\s+"));
    }

    public void printVectors(){

        for(int i=0;i<documents.size();i++){

            System.out.println(documents.get(i));
            System.out.println(docVectors.get(i));
            System.out.println();
        }
    }

    double cosine(Map<String,Integer> a,
                  Map<String,Integer> b){

        double dot=0;

        for(String key:a.keySet()){

            dot+=a.get(key)*b.getOrDefault(key,0);
        }

        double magA=0;
        double magB=0;

        for(int x:a.values())
            magA+=x*x;

        for(int x:b.values())
            magB+=x*x;

        return dot/(Math.sqrt(magA)*Math.sqrt(magB));
    }

    public void search(String query){

        Map<String,Integer> q=new HashMap<>();

        for(String word:tokenize(query))
            q.put(word,q.getOrDefault(word,0)+1);

        TreeMap<Double,String> result=
                new TreeMap<>(Collections.reverseOrder());

        for(int i=0;i<documents.size();i++){

            double score=cosine(q,docVectors.get(i));

            result.put(score,documents.get(i));
        }

        for(Double score:result.keySet()){

            if(score>0)
                System.out.println(score+" -> "+
                                           result.get(score));
        }
    }

    public static void main(String args[]) throws Exception{

        VectorIndexer v=new VectorIndexer();

        v.load("documents.txt");

        v.printVectors();
    }
}