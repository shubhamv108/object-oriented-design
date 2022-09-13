package unixfilesearch;

import unixfilesearch.filesystem.Directory;
import unixfilesearch.filesystem.File;
import unixfilesearch.filesystem.INode;
import unixfilesearch.filters.ExtensionFilter;
import unixfilesearch.filters.MinSizeFilter;
import unixfilesearch.filters.NameFilter;
import unixfilesearch.searchers.RecursiveNodeSearcher;
import unixfilesearch.searchers.Searcher;

import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        var directory = new Directory("TestDirectory");
        var file = new File("TestDirectory-TestPdfFile.pdf");
        file.write("Content".getBytes());
        file.append("Appended".getBytes());

        directory.addEntry(file);
        directory.addEntry(new File("TestDirectory-TestXMLFile.xml"));
        directory.addEntry(new File("TestDirectory-TestJSONFile.json"));
        file = new File("TestPdfFile.pdf");
        file.write("Content".getBytes());
        file.append("Appended".getBytes());
        var file2 = new File("TestXMLFile.xml");
        var file3 = new File("TestJSONFile.json");

        var filter = new NameFilter("[Test\\w.-]*").
                and(new ExtensionFilter(".pdf")).
                and(new MinSizeFilter(10L));

        var nodes = List.of(directory, file, file2, file3);
        var result = nodes.stream().filter(filter).collect(Collectors.toList());
        System.out.println(result.stream().map(INode::getName).collect(Collectors.toList()));

        filter = new ExtensionFilter(".pdf").and(new MinSizeFilter(10L));
        new RecursiveNodeSearcher(new Searcher(filter)).search(nodes.toArray(INode[]::new));
    }
}
