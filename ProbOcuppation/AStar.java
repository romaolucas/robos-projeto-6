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
        double alpha = 0.4;
        goal.setDistToSource(G);
        pq.add(v);
        int goalX = (int) goal.posX;
        int goalY = (int) goal.posY;
        int adi = 1;
        int pintei = 0;
               for (int y = 0; y < G.height; y ++) {
                for (int x = 0; x < G.width; x++) {
                    // newProbMap[y][x] = applyConvolutionMask(y, x);
                    if (G.probMap[y][x] > 0) System.out.println(G.probMap[y][x]);
                }
            }
            System.out.println("terminei no metodo");
        while (!pq.isEmpty() && !goal.equals(v)) {
            v = pq.poll();

            StdDraw.point(v.posX, v.posY);
            // System.out.println("pintei " + pintei + " e adicionei " + adi);
            pintei++;
            int vX = (int) v.posX;
            int vY = (int) v.posY;
            if (vX == goalX && vY == goalY) break;
            G.explored[vY][vX] = 1;
            double vDist = G.map[vY][vX];
            double vProb = G.probMap[vY][vX];
            if (vProb > 0) System.out.println(vProb);
            double vPriority = G.heuristicMap[vY][vX] + (alpha * vDist) + ((1 - alpha) * vProb);
            List<Vertex> neighbors = v.getNeighbors(4, G.width, G.height);
            for (Vertex neighbor : neighbors) {
                // System.out.println(vDist + vPriority + " < " + vPriority + " ?");
                int x = (int) neighbor.posX;
                int y = (int) neighbor.posY;
                if (G.explored[y][x] == -1) {
                     G.map[y][x] = vPriority;
                     G.probMap[y][x] *= vProb;
                     G.explored[y][x] = 1;
                     neighbor.setDistToSource(G);
                     pq.add(neighbor);
                     adi++;
                }
            }
        }
        System.out.println(adi);
        int x = (int) goal.posX;
        int y = (int) goal.posY;
        System.out.println("y:" + v.posY + " x:" + v.posX);
        System.out.println("y:" + y + " x:" + x);
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
        int goalX = 90 / dim;
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
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.GREEN);
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
