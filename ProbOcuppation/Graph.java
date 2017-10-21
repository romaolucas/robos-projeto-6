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
    public Vertex[][] parent;
    public int[][] explored;   
    public int width;
    public int height;
    public int dim;
    public Line2D.Double[] lines;

    public Graph(int width, int height, int dim, int numberOfConvolutions, Line2D.Double[] lines) {
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
        convolution(numberOfConvolutions); 
        for (int y = 0; y < this.height; y ++) {
            for (int x = 0; x < this.width; x++) {
                // newProbMap[y][x] = applyConvolutionMask(y, x);
                // if (probMap[y][x] > 0) System.out.println(probMap[y][x]);
                }
            }
        //fillMapForSouce is eventually called from outside;
    }

    public void fillMapWithObstacles(Line2D.Double[] lines) {
        for (Line2D.Double line : lines) {
            double startX = Math.min(line.getX1(), line.getX2());
            double finalX = Math.max(line.getX1(), line.getX2());
            double m = (line.getY2() - line.getY1()) / (line.getX2() - line.getX1()); 
            for (int x = (int) startX; x <= (int) finalX; x++) {
                int y = (int) (m * (x - line.getX1()) + line.getY1());
                this.probMap[y][x] = 1.0;
            }
        }
    }

    public void discretizeMap(int dim) {
        int newHeight = this.height / dim;
        int newWidth = this.width / dim;
        double newMap[][] = new double[newHeight][newWidth];
        double newProbMap[][] = new double[newHeight][newWidth];
        parent = new Vertex[newHeight][newWidth];
        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                newMap[y][x] = Double.MAX_VALUE;
                this.parent[y][x] = null;
            }
        }
        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                for (int y1 = y * dim; y1 < (y * dim) + dim; y1++) {
                    for (int x1 = x * dim; x1 < (x * dim) + dim; x1++) {
                        if (this.probMap[y1][x1] == 1.0) newProbMap[y][x] = 1.0;
                    }
                }
            }
        }

        this.height = newHeight;
        this.width = newWidth;
        this.map = newMap;
        this.probMap = newProbMap;
    }

    public void fillMapForSource(int sourceX, int sourceY) { //consider sourceX and sourceY in mm
        int height = this.height;
        int width = this.width;
        sourceX = sourceX / dim;
        sourceY = sourceY / dim;
        this.heuristicMap = new double[height][width];
        this.explored = new int[height][width];
        for (int y = 0; y < height; y ++) {
            for (int x = 0; x < width; x++) {
                double deltaX = sourceX - x;
                double deltaY = sourceY - y;
                this.explored[y][x] = -1;
                this.heuristicMap[y][x] = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                // this.map[y][x] = Math.sqrt(deltaX * deltaX + deltaY * deltaY); 
            } 
        }
        this.heuristicMap[sourceY][sourceX] = 0;
        this.map[sourceY][sourceX] = 0;
    }


    public void convolution(int n) {
        int height = this.height;
        int width = this.width;
        for (int i = 0; i < n; i++) {
            double newProbMap[][] = new double[height][width];
            for (int y = 0; y < height; y ++) {
                for (int x = 0; x < width; x++) {
                    newProbMap[y][x] = applyConvolutionMask(y, x);
                    // if (newProbMap[y][x] > 0) System.out.println(newProbMap[y][x]);
                }
            }
            this.probMap = newProbMap;
        }

       // for (int y = 0; y < height; y ++) {
       //      for (int x = 0; x < width; x++) {
       //              // newProbMap[y][x] = applyConvolutionMask(y, x);
       //              // if (probMap[y][x] > 0) System.out.println(probMap[y][x]);
       //          }
       //      }
    }

    public double applyConvolutionMask(int y, int x) {
        double mask[][] = new double[3][3];
        mask[0][0] = 0.05;
        mask[0][1] = 0.1;
        mask[0][2] = 0.05;
        mask[1][0] = 0.1;
        mask[1][1] = 0.4;
        mask[1][2] = 0.1;
        mask[2][0] = 0.05;
        mask[2][1] = 0.05;
        mask[2][2] = 0.05;

        // double test[][] = new double[3][3];
        // test[0][0] = 0;
        // test[0][1] = 0;
        // test[0][2] = 0;
        // test[1][0] = 0;
        // test[1][1] = 1;
        // test[1][2] = 0;
        // test[2][0] = 0;
        // test[2][1] = 0;
        // test[2][2] = 0;

        double maskedValue = 0;
        int n = 0;
        int m = 0;
        for (int i = 1; i > -2; i--) {
            m = 0;
            for (int j = 1; j > -2; j--) {
                if (y - i < 0 || y - i >= height || x - j < 0 || x - j >= width) {
                    maskedValue += 0;
                } else {
                    // System.out.println("to fazendo " + (y - i) + ", " + (x - j) + " com " + n + ", " + m);
                    // System.out.println("to fazendo " + test[y - i][x -j] + " com " + mask[n][m]);
                    maskedValue += mask[n][m] * probMap[y - i][x - j];
                }
                m++;
            }
            n++;
        }
        return maskedValue;
    }

    public void setParent(int y, int x, Vertex v) {
        parent[y][x] = v;
        System.out.println("settei " + v.distToSource); 
    }

    public Vertex parentOf(int y, int x) {
        return parent[y][x]; 
    }

    public static void main(String[] args) {

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j ++) {
                System.out.println(i+j);
            }
        }

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
