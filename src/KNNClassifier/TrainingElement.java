package KNNClassifier;

public class TrainingElement {
    private String actualType;

    private double[] vector;
    public TrainingElement(String actualType, double[] vector) {
        this.actualType = actualType;
        this.vector = vector;
    }

    public String getActualType() {
        return actualType;
    }

    public void setActualType(String actualType) {
        this.actualType = actualType;
    }

    public double[] getVector() {
        return vector;
    }

    public void setVector(double[] vector) {
        this.vector = vector;
    }
}


