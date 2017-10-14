import java.util.*;
import java.awt.geom.Line2D;

public class AStar {

    public static int NONE = -1;
    public static int NO_MOVE = 0;
    public static int LEFT = 1;
    public static int RIGHT = 2;
    public static int UP = 3;
    public static int DOWN = 4;
    public static int UP_LEFT = 5;
    public static int UP_RIGHT = 6;
    public static int DOWN_LEFT = 7;
    public static int DOWN_RIGHT = 8;

    public static Comparator<Vertex> vertexComparator = new Comparator<Vertex>() {
        
        @Override
        public int compare(Vertex v, Vertex w) {
            return v.compareTo(w);
        } 
    
    };

    public static List<Vertex> findShortestPath(Graph G, Vertex origin, Vertex goal) {
        Queue<Vertex> pq = new PriorityQueue<>(11, vertexComparator);
        List<Vertex> path = new ArrayList<>();
        Vertex v = origin;
        v.setDistToSource(G);
        pq.add(origin);
        while (!pq.isEmpty() && !goal.equals(v)) {
            v = pq.poll();
            int vX = (int) v.posX;
            int vY = (int) v.posY;
            double vDist = G.map[vY][vX];
            double vPriority = G.heuristicMap[vY][vX] + vDist;
            List<Vertex> neighbors = v.getNeighbors(4, G.width, G.height);
            for (Vertex neighbor : neighbors) {
                neighbor.setDistToSource(G);
                int x = (int) neighbor.posX;
                int y = (int) neighbor.posY;
                double neighDist = G.map[y][x];
                if (vDist + vPriority < neighDist) {
                    G.setParent(y, x, v);
                    G.map[y][x] = vDist + vPriority;
                    //tem que atualizar heuristicMap pro neighbor?
                    if (pq.contains(neighbor)) {
                        pq.remove(neighbor);
                        pq.add(neighbor);
                    } else {
                        pq.add(neighbor);
                    }
                }
            }
        }
        int x = (int) goal.posX;
        int y = (int) goal.posY;
        while (G.parentOf(y, x).posX != x || G.parentOf(y, x).posY != y) {
            path.add(new Vertex(y, x));
            Vertex next = G.parentOf(y, x);
            x = (int) next.posX;
            y = (int) next.posY;
        }
        path.add(new Vertex(y, x));
        Collections.reverse(path);
        return path; 
    }

    public static void main(String[] args) {
        int dim = 10; // precisao da discretizacao, 10 = 1cmx1cm, 20 = 2cmx2cm, 1 = discretizacao minima, cada vertice 1 milimetro.
        int numberOfConvolutions = 1; // numero de convolucoes a se aplicar no mapa de probabilidade
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
        StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(StdDraw.BLACK);
        Graph G = new Graph(widthMap, heightMap, dim, numberOfConvolutions, lines);

        // pontos iniciais e finais
        double startX = 90 / dim;
        double startY = 730 / dim;
        int goalX = 490 / dim;
        int goalY = 18 / dim;

        for (int y = 0; y < G.height; y++) {
            for (int x = 0; x < G.width; x++) {
                if (G.probMap[y][x] > 0.3) {
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.point(x, y);
                } 
            }
        }

        G.fillMapForSource((int) startX * dim, (int) startY * dim);

        List<Vertex> path = new ArrayList<>();
        Vertex startVertex = new Vertex(startX, startY);
        Vertex goalVertex = new Vertex(goalX, goalY);
        path = findShortestPath(G, startVertex, goalVertex);
        System.out.println("Caminho de (90, 730) ate (490, 18)");
        for (Vertex v : path) {
            System.out.println(v);
        }
    }

}
