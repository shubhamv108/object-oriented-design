package texteditor.memento;

import texteditor.IDocument;

public class DocumentMemento implements IDocumentCareTaker, IDocumentOriginator {

    private final IDocument document;

    public DocumentMemento(final IDocument document) {
        this.document = document;
    }

    @Override
    public IDocument getDocument() {
        return this.document;
    }
}
