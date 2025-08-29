package KNNClassifier;

public class Distance implements Comparable<Distance> {
    private final TrainingElement el ;
    private final double dist;
    public Distance(TrainingElement el, double dist) {
        this.el = el;
        this.dist = dist;
    }
    @Override
    public int compareTo(Distance other) {
        return Double.compare(this.dist, other.dist);
    }

    public TrainingElement getEl() {
        return el;
    }

    public double getDist() {
        return dist;
    }


}
