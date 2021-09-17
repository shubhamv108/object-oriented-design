package texteditor;

public interface IDocument {

    IDocument open();

    void close();

    void append();

    void backspace();

    void enter();

    void unDo();

    void reDo();

    void unDoLastByte();

    void reDoLastByte();

    IDocument save();


}
