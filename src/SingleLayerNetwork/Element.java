package SingleLayerNetwork;

class Element {
    private double[] vector;
    private int d;
    private String label;
    private String text;

    public Element(double[] vector, int d, String label, String text) {
        this.vector = vector;
        this.d = d;
        this.label = label;
        this.text = text;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public double[] getVector() {
        return vector;
    }

    public void setVector(double[] vector) {
        this.vector = vector;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
