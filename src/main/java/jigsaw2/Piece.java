package jigsaw2;

import java.util.Arrays;

public class Piece {

    private final Edge[] edges;

    public Piece(Edge[] edges) {
        this.edges = edges;
    }

    public Edge[] getEdges() {
        return edges;
    }

    public boolean isCorner() {
        return getFlatEdges() == 2;
    }

    public Edge getAnyExposedEdge() {
        for (Edge edge: this.getEdges())
            if (edge.isExposed())
                return edge;
        return null;
    }

    private long getFlatEdges() {
        return Arrays.stream(edges).filter(e -> e.getType() == EdgeType.FLAT).count();
    }

    private long getInnerEdges() {
        return Arrays.stream(edges).filter(e -> e.getType() == EdgeType.INNER).count();
    }

    private long getOuterEdges() {
        return Arrays.stream(edges).filter(e -> e.getType() == EdgeType.OUTER).count();
    }
}
