import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;    

public class Vertex implements Comparable<Vertex> {

    public double posX;

    public double posY;

    public double distToSource;

    public Vertex(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
        this.distToSource = 0.0;
    }

    public List<Vertex> getNeighbors(int neighborhood, int width, int height) {
        Vertex down = new Vertex(this.posX, Math.max(this.posY - 1, 0));
        Vertex up = new Vertex(this.posX, Math.min(this.posY + 1, height - 1));
        Vertex right = new Vertex(Math.max(this.posX - 1, 0), this.posY);
        Vertex left = new Vertex(Math.min(this.posX + 1, width - 1), this.posY);
        if (neighborhood == 4) return Arrays.asList(up, down, left, right);

        Vertex bottomLeft = new Vertex(Math.max(this.posX - 1, 0), Math.max(this.posY - 1, 0));
        Vertex bottomRight = new Vertex(Math.min(this.posX + 1, width - 1), Math.max(this.posY - 1, 0));
        Vertex topLeft = new Vertex(Math.max(this.posX - 1, 0), Math.min(this.posY + 1, height - 1));
        Vertex topRight = new Vertex(Math.min(this.posX + 1, width - 1), Math.min(this.posY + 1, height - 1));

        List<Vertex> neighbors = new ArrayList<Vertex>();
        neighbors.add(up);
        neighbors.add(down);
        neighbors.add(left);
        neighbors.add(right);
        neighbors.add(topLeft);
        neighbors.add(topRight);
        neighbors.add(bottomLeft);
        neighbors.add(bottomRight);
        
        return neighbors;
    }

    public int compareTo(Vertex w) {
        if (this.distToSource < w.distToSource)
            return -1;
        if (this.distToSource > w.distToSource)
            return 1;
        return 0;
    }

    @Override
    public String toString() {
        return "(" + posX + ", " + posY + ")";
    }

}

