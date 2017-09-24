import java.util.List;
import java.util.ArrayList;

public class Graph {

    private int V;
    
    private List<Vertex> vertexes;

    public Graph(int V) {
        this.V = V;
        vertexes = new ArrayList<>(V);
    }

    public Graph(List<Vertex> vertexes) {
        this.V = vertexes.size();
        this.vertexes = vertexes;
    }

    public void addVertex(Vertex v) {
        this.vertexes.add(v);
    }

    public void addEdge(int origin, int destination) {
        Vertex v = getVertex(origin);
        Vertex w = getVertex(destination);
        double deltaX, deltaY;
        deltaX = v.getPosX() - w.getPosX();
        deltaY = v.getPosY() - w.getPosY();
        double dist = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
        v.addEdge(new Edge(v, w, dist));
        w.addEdge(new Edge(w, v, dist));
    }

    public List<Vertex> getVertexes() {
        return vertexes;
    }

    public int getV() {
        return V;
    }

    public Vertex getVertex(int i) {
        return vertexes.get(i);
    }

}
