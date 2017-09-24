public class Edge {

    private Vertex origin;

    private Vertex destination;

    private double weight;

    public Edge(Vertex origin, Vertex destination, double weight) {
        this.origin = origin;
        this.destination = destination;
        this.weight = weight;
    }

    public Vertex getOrigin() {
        return origin;
    }

    public Vertex getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return origin + " - " + destination;
    }

}

