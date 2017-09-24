import java.util.*;

public class BestChoice {

    public static Comparator<Vertex> vertexComparator = new Comparator<Vertex>() {
        
        @Override
        public int compare(Vertex v, Vertex w) {
            if (v.getHeuristic() < w.getHeuristic()) {
                return -1;
            } else if (v.getHeuristic() > w.getHeuristic()) {
                return 1;
            }
            return 0;
        }
    };
    
    public static void init(Graph G) {
        G.addVertex(new Vertex(9.4, 73.7));
        G.addVertex(new Vertex(42.8, 80.0));
        G.addVertex(new Vertex(114.7, 81.1));
        G.addVertex(new Vertex(112.7, 35.5));
        G.addVertex(new Vertex(83.1, 43.0));
        G.addVertex(new Vertex(69.3, 49.4));
        G.addVertex(new Vertex(44.9, 51.5));
        G.addVertex(new Vertex(26.0, 27.1));
        G.addVertex(new Vertex(53.4, 30.3));
        G.addVertex(new Vertex(99.4, 8.8));
        G.addVertex(new Vertex(48.9, 1.8));
        G.addEdge(0, 1);
        G.addEdge(0, 2);
        G.addEdge(1, 2);
        G.addEdge(1, 5);
        G.addEdge(2, 6);
        G.addEdge(2, 3);
        G.addEdge(3, 4);
        G.addEdge(3, 9);
        G.addEdge(3, 5);
        G.addEdge(4, 5);
        G.addEdge(4, 6);
        G.addEdge(4, 9);
        G.addEdge(5, 6);
        G.addEdge(6, 7);
        G.addEdge(7, 10);
        G.addEdge(9, 10);
    }

    public static void initDistancesAndHeuristic(Graph G) {
        Vertex s = G.getVertex(0);
        for (int i = 1; i < G.getV(); i++) {
            Vertex v = G.getVertex(i);
            v.setDistToSource(Double.MAX_VALUE);            
            double deltaX = s.getPosX() - v.getPosX();
            double deltaY = s.getPosY() - v.getPosY();
            double heuristic = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            v.setHeuristic(heuristic);
        }
    }


    public static List<Vertex> findShortestPath(Graph G, Vertex origin, Vertex goal) {
        Queue<Vertex> pq = new PriorityQueue<>(11, vertexComparator);
        List<Vertex> path = new ArrayList<>();
        int[] parent = new int[11]; 
        Vertex v = origin;
        pq.add(origin);
        int idx = G.getVertexes().indexOf(origin);
        parent[idx] = idx;
        while (!pq.isEmpty() && !goal.equals(v)) {
            v = pq.poll();
            for (int i = 0; i < v.inDegree(); i++) {
                Edge vu = v.getAdjList().get(i);
                Vertex u = vu.getDestination();
                if (v.getDistToSource() + v.getHeuristic() < u.getDistToSource()) {
                    idx = G.getVertexes().indexOf(u);
                    parent[idx] = G.getVertexes().indexOf(v);
                    u.setDistToSource(v.getDistToSource() + v.getHeuristic());
                    if (pq.contains(u)) {
                        pq.remove(u);
                        pq.add(u);
                    } else {
                        pq.add(u);
                    }
                }
            }
        }
        idx = G.getVertexes().indexOf(goal);
        while (parent[idx] != idx) {
            path.add(G.getVertex(idx));
            idx = parent[idx];
        }
        path.add(G.getVertex(idx));
        Collections.reverse(path);
        return path; 
    }

    public static void main(String[] args) {
        Graph G = new Graph(11);
        List<Vertex> path = new ArrayList<>();
        init(G);
        initDistancesAndHeuristic(G);
        path = findShortestPath(G, G.getVertex(0), G.getVertex(7));
        System.out.println("Caminho de P1 a P8");
        for (Vertex v : path) {
            System.out.println(v);
        }
        initDistancesAndHeuristic(G);
        path = findShortestPath(G, G.getVertex(0), G.getVertex(9));       
        System.out.println("Caminho de P1 a P10");
        for (Vertex v : path) {
            System.out.println(v);
        }
    }

}
