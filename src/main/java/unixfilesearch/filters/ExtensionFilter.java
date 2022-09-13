package unixfilesearch.filters;

import unixfilesearch.filesystem.File;
import unixfilesearch.filesystem.INode;
import unixfilesearch.filters.base.AbstractFilter;

public class ExtensionFilter extends AbstractFilter<INode> {
    private final String extension;

    public ExtensionFilter(String extension) {
        this.extension = extension;
    }

    public boolean test(INode node) {
        if (!(node instanceof File))
            return false;
        File file = (File) node;
        return extension.equals(file.getExtension());
    }
}
