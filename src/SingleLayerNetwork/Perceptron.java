package SingleLayerNetwork;

import java.util.Collections;
import java.util.List;

class Perceptron {
    private double learningRate;
    private int epochs;
    private double[] weights;
    private double bias;

    Perceptron(double learningRate, int epochs) {
        this.learningRate = learningRate;
        this.epochs = epochs;
    }

    int predict(double[] input) {
        double activation = getActivation(input);
        return activation >= 0 ? 1 : 0;
    }

    double getActivation(double[] input) {
        return scalarProduct(input, weights) - bias;
    }

    void updateWeightsAndBias(double[] input, int label) {
        int prediction = predict(input);
        int error = label - prediction;

        for (int i = 0; i < weights.length; i++) {
            weights[i] += learningRate * error * input[i];
        }

        bias += learningRate * error;
    }

    void trainWithData(List<Element> elements) {
        Collections.shuffle(elements);

        weights = new double[elements.get(0).getVector().length];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = Math.random() * 0.1 - 0.09;
        }
        bias = Math.random() * 0.1 - 0.09;

        for (int i = 0; i < epochs; i++) {
            for (Element element : elements) {
                updateWeightsAndBias(element.getVector(), element.getD());
            }
        }
    }

    public static double scalarProduct(double[] a, double[] b) {
        double product = 0;
        for (int i = 0; i < a.length; i++) {
            product += a[i] * b[i];
        }
        return product;
    }
}