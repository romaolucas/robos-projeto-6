import java.util.List;
import java.util.ArrayList;
import java.awt.geom.Line2D; 
import java.awt.geom.Point2D;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Stack;

public class Graph {

    public double[][] map; //distance to source Map
    public double[][] heuristicMap; //heuristic relative to the source Map
    public double[][] probMap; //prob of obstacle Map
    public int width;
    public int height;
    public int dim;
    public Line2D.Double[] lines;

    public Graph(int width, int height, int dim, int radius, Line2D.Double[] lines) {
        this.width = width;
        this.height = height;
        this.dim = dim;
        this.map = new double[height][width];
        this.probMap = new double[height][width];
        for (int i = 0; i < height; i ++) {
            for (int j = 0; j < width; j++) {
                this.map[i][j] = Double.MAX_VALUE;
                this.probMap[i][j] = 0.0;
            }
        }
        this.lines = lines;
        fillMapWithObstacles(lines);
        discretizeMap(dim); 
    }

    public void fillMapWithObstacles(Line2D.Double[] lines) {
        for (Line2D.Double line : lines) {
            double startX = Math.min(line.getX1(), line.getX2());
            double finalX = Math.max(line.getX1(), line.getX2());
            double m = (line.getY2() - line.getY1()) / (line.getX2() - line.getX1()); 
            for (int x = (int) startX; x <= (int) finalX; x++) {
                int y = (int) (m * (x - line.getX1()) + line.getY1());
                probMap[y][x] = 1.0;
            }
        }
    }

    public void discretizeMap(int dim) {
        int newHeight = height / dim;
        int newWidth = width / dim;
        double newMap[][] = new double[newHeight][newWidth];
        double newProbMap[][] = new double[newHeight][newWidth];
        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                newMap[y][x] = Double.MAX_VALUE;
            }
        }


        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                for (int y1 = y * dim; y1 < (y * dim) + dim; y1++) {
                    for (int x1 = x * dim; x1 < (x * dim) + dim; x1++) {
                        if (probMap[y1][x1] == 1.0) newProbMap[y][x] = 1.0;
                    }
                }
            }
        }

        height = newHeight;
        width = newWidth;
        map = newMap;
        probMap = newProbMap;
    }

    public void fillMapForSource(int sourceX, int sourceY) { //consider sourceX and sourceY in mm
        sourceX = sourceX / dim;
        sourceY = sourceY / dim;
        this.heuristicMap = new double[height][width];
        for (int y = 0; y < height; y ++) {
            for (int x = 0; x < width; x++) {
            double deltaX = sourceX - x);
            double deltaY = sourceY - y;
            this.heuristicMap[y][x] = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            this.map[y][x] = Math.sqrt(deltaX * deltaX + deltaY * deltaY); 
            } 
        }
    }

    public void convolution(int n) {
        for (int i = 0; i < n; i++) {
            double newProbMap[][] = new double[height][width];
            for (int y = 0; y < height; y ++) {
                for (int x = 0; x < width; x++) {
                    newProbMap[y][x] = applyConvolutionMask(x, y);
                }
            }
            probMap = newProbMap;
        }

    }

    public double applyConvolutionMask(int x, int y) {
        double mask[][] = new double[3][3];

        return 0.0;
    }

    public Queue<Vertex> linearizePath(Stack<Vertex> path) {
        System.out.println("Caminho antes de linearizar com tamanho: " + path.size());
        Queue<Vertex> finalPath = new LinkedList<Vertex>();
        boolean intersectsAnyLine = false;
        Vertex v = path.pop();
        Vertex lastNonIntersectingVertex = v;
        Vertex u;
        int i = 0;
        finalPath.add(v);
        while (!path.isEmpty()) {
            u = path.pop();
            Point2D pointV = new Point2D.Double(v.posX * dim, v.posY * dim);
            Point2D pointU = new Point2D.Double(u.posX * dim, u.posY * dim);
            // System.out.println(" pontos: " + pointV + " e " + pointU);
            Line2D pathLine = new Line2D.Double(pointV, pointU);

            for (Line2D line : lines) {
                if (line.intersectsLine(pathLine)) {
                    intersectsAnyLine = true;
                    break;
                }
            }
            if (!intersectsAnyLine) {
                i++;
                lastNonIntersectingVertex = u;
            }
            else {
                i = 0;
                // System.out.println(" nunca aqui?");
                finalPath.add(lastNonIntersectingVertex);
                v = lastNonIntersectingVertex;
            }
            intersectsAnyLine = false;
        }
        finalPath.add(lastNonIntersectingVertex);
        System.out.println("Caminho depois de linearizar com tamanho: " + finalPath.size());;

        return finalPath;
    }

}
