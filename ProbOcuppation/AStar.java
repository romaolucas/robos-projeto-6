import java.util.*;

public class AStar {

    public static Comparator<Vertex> vertexComparator = new Comparator<Vertex>() {
        
        @Override
        public int compare(Vertex v, Vertex w) {
            return v.compareTo(w);
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
            //Vertex v = q.poll();
            v = q.poll();
            int vX = (int) v.posX;
            int vY = (int) v.posY;
            double vDist = G.map[vY][vX];
            double vPriority = G.heuristicMap[vY][vX] + vDist;
            List<Vertex> neighbors = v.getNeighbors(4, G.width, G.height);
            for (Vertex neighbor : neighbors) {
                int x = (int) neighbor.posX;
                int y = (int) neighbor.posY;
                double neighDist = G.map[y][x];
                if (vDist + vPriority < neighDist) {
                    parent[neighbor] = v;
                    G.map[y][x] = vDist + vPriority;
                    if (pq.contains(neighbor)) {
                        pq.remove(neighbor);
                        pq.add(neighbor);
                    } else {
                        pq.add(neighbor);
                    }
                }
                G.map[y][x] = 1 + G.map[vY][vX];
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.point(x, y);
                q.add(neighbor);
                if (x == goalX && y == goalY) {
                    q.clear();
                    break;
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
        int dim = 10; // precisao da discretizacao, 10 = 1cmx1cm, 20 = 2cmx2cm, 1 = discretizacao minima, cada vertice 1 milimetro.
        int radius = 0; // raio de dilatacao das linhas
        // dimensao do mapa em milimetros
        int widthMap = 1189;
        int heightMap = 841;
        // obstaculos do mapa
        Line2D.Double[] lines = {
          /* L-shape polygon */
          new Line2D.Double(164,356,58,600),
          new Line2D.Double(58,600,396,721),
          new Line2D.Double(396,721,455,600),
          new Line2D.Double(455,600,227,515),
          new Line2D.Double(227,515,280,399),
          new Line2D.Double(280,399,164,356),
          /* Triangle */
          new Line2D.Double(778,526,1079,748),
          new Line2D.Double(1079,748,1063,436),
          new Line2D.Double(1063,436,778,526),
          /* Pentagon */
          new Line2D.Double(503,76,333,267),
          new Line2D.Double(333,267,481,452),
          new Line2D.Double(481,452,730,409),
          new Line2D.Double(730,409,704,150),
          new Line2D.Double(704,150,503,76) //base do pentagono
        };
        StdDraw.setXscale(0, widthMap / dim);
        StdDraw.setYscale(0, heightMap / dim);
        StdDraw.setPenRadius(0.04);
        StdDraw.setPenColor(StdDraw.BLACK);
        Graph G = new Graph(widthMap, heightMap, dim, radius, lines);

        // pontos iniciais e finais
        double startX = 90 / dim;
        double startY = 730 / dim;
        int goalX = 490 / dim;
        int goalY = 18 / dim;

        for (int y = 0; y < G.height; y++) {
            for (int x = 0; x < G.width; x++) {
                if (G.map[y][x] == -1) {
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.point(x, y);
                } 
            }
        }

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
