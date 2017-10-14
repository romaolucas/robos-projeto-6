import java.util.Comparator;
import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import java.awt.geom.Line2D;


public class WaveFront {

    public static Comparator<Vertex> vertexComparator = new Comparator<Vertex>() {

        @Override
        public int compare(Vertex v, Vertex w) {
            return v.compareTo(w);
        }

    };

    public static void init(Graph G, double cellSize, int w, int h) {
        int width = w;
        int height = h;
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
          new Line2D.Double(704,150,503,76)
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

        // faz a ida com vizinhanca 4
        Queue<Vertex> q = new LinkedList<Vertex>();
        q.add(new Vertex(startX, startY));
        while (!q.isEmpty()) {
            Vertex v = q.poll();
            int vX = (int) v.posX;
            int vY = (int) v.posY;
            List<Vertex> neighbors = v.getNeighbors(4, G.width, G.height);
            for (Vertex neighbor : neighbors) {
                int x = (int) neighbor.posX;
                int y = (int) neighbor.posY;
                if (G.map[y][x] != 0) continue;
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

        // faz a volta com vizinhanca 8
        Vertex v = new Vertex(goalX, goalY);
        Stack<Vertex> path = new Stack();
        Stack<Vertex> nonLinearizedPath = new Stack();
        path.add(v);
        nonLinearizedPath.add(v);
        int minNeighborDist = G.width * G.height;
        while (minNeighborDist > 1) {
            Vertex minNeighbor = new Vertex(0, 0);
            List<Vertex> neighbors = v.getNeighbors(8, G.width, G.height);
            for (Vertex neighbor : neighbors) {
                int x = (int) neighbor.posX;
                int y = (int) neighbor.posY;
                if (G.map[y][x] == -1) continue;
                if (G.map[y][x] == 0) {
                    continue;
                }
                if (G.map[y][x] < minNeighborDist) {
                    minNeighborDist = (int) G.map[y][x];
                    minNeighbor = neighbor;
                }
            }
            v = minNeighbor;
            path.push(minNeighbor);
            nonLinearizedPath.push(minNeighbor);
        }
        path.push(new Vertex(startX, startY));
        nonLinearizedPath.push(new Vertex(startX, startY));

        // desenha o caminho nao linearizado
        while (!nonLinearizedPath.isEmpty()) {
            v = nonLinearizedPath.pop();
            int vX = (int) v.posX;
            int vY = (int) v.posY;
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.point(vX, vY);
        }
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.point(startX, startY);

        // lineariza e desenha o caminho linearizado
        Queue<Vertex> finalPath = G.linearizePath(path);
        System.out.println("Caminho linearizado de (" + startX + ", " + startY + ") ateh (" + goalX + ", " + goalY + ")");
        for (Vertex vertex : finalPath) {
            int vX = (int) vertex.posX;
            int vY = (int) vertex.posY;
            System.out.println("X: " + vX + " Y: " + vY);
            StdDraw.setPenRadius(0.02);
            StdDraw.setPenColor(StdDraw.GREEN);
            StdDraw.point(vX, vY);
        }


    }

}
