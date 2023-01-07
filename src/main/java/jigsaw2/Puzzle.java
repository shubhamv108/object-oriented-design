package jigsaw2;

import java.util.HashSet;
import java.util.Iterator;

public class Puzzle {
    private final Piece[] pieces;
    private Piece[][] solution;

    private HashSet<Edge> inners = new HashSet<>(), outers = new HashSet<>(), flats;
    private Piece[] corners = new Piece[4];

    public Puzzle(Piece[] pieces) {
        this.pieces = pieces;
    }

    void sort() {
        int cornerIndex = 0;
        for (Piece piece: this.pieces) {
            if (piece.isCorner())
                this.corners[cornerIndex++] = piece;

            for (Edge edge: piece.getEdges()) {
                if (EdgeType.INNER == edge.getType())
                    inners.add(edge);
                if (EdgeType.OUTER == edge.getType())
                    outers.add(edge);
            }
        }
    }

    public void solve() {
        Edge currentEdge = corners[0].getAnyExposedEdge();
        while (currentEdge != null) {
            HashSet<Edge> opposites = currentEdge.getType() == EdgeType.OUTER ? inners : outers;
            Iterator<Edge> iterator = opposites.iterator();
            while (iterator.hasNext()) {
                Edge edge = iterator.next();
                if (currentEdge.fitsWith(edge)) {
                    currentEdge.attach(edge);
                    iterator.remove();
                    removeFromList(currentEdge);
                    currentEdge = this.nextExposedEdge(edge);
                    break;
                }
            }
        }
    }

    private void removeFromList(Edge edge) {
        if (edge.getType() == EdgeType.FLAT)
            return;
        HashSet<Edge> edges = edge.getType() == EdgeType.INNER ? inners : outers;
        edges.remove(edge);
    }

    private Edge nextExposedEdge(Edge edge) {
        int nextIndex = (edge.getIndex() + 2) % 4;
        Edge nextEdge = edge.getPiece().getEdges()[nextIndex];
        if (nextEdge.isExposed())
            return nextEdge;
        return edge.getPiece().getAnyExposedEdge();
    }
}
