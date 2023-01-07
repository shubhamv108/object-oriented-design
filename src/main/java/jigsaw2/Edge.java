package jigsaw2;

public class Edge {
    private final EdgeType type;
    private final Piece piece;
    private final int index;

    private Edge attachedTo;

    public Edge(EdgeType type, Piece piece, int index) {
        this.type = type;
        this.piece = piece;
        this.index = index;
    }

    public Piece getPiece() {
        return piece;
    }

    public boolean fitsWith(Edge edge) {
        return (this.type == EdgeType.OUTER && edge.getType() == EdgeType.INNER) ||
                (this.type == EdgeType.INNER && edge.getType() == EdgeType.OUTER) ||
                (this.type == EdgeType.FLAT && edge.getType() == EdgeType.FLAT);
    }

    public boolean attach(Edge edge) {
        if (this.attachedTo != null || !fitsWith(edge))
            return false;
        edge.attach(this);
        this.attachedTo = edge;
        return true;
    }

    public EdgeType getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    public boolean isExposed() {
        return this.type != EdgeType.FLAT && this.attachedTo == null;
    }
}
