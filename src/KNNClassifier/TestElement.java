package KNNClassifier;

public class TestElement extends TrainingElement {
    private String deducedType;
    public TestElement(String actualType, double[] vector){
        super(actualType, vector);
        deducedType = null;
    }
    public String getDeducedType() {
        return deducedType;
    }

    public void setDeducedType(String deducedType) {
        this.deducedType = deducedType;
    }


}
