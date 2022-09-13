package unixfilesearch.filters;

import unixfilesearch.filesystem.INode;
import unixfilesearch.filesystem.Node;
import unixfilesearch.filters.base.AbstractFilter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameFilter extends AbstractFilter<INode> {

    private final String patternString;

    public NameFilter(String patternString) {
        this.patternString = patternString;
    }

    @Override
    public boolean test(INode node) {
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(node.getName());
        return matcher.matches();
    }
}
