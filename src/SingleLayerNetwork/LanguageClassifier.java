package SingleLayerNetwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class LanguageClassifier {
    private List<String> languages;
    private List<Perceptron> perceptrons;
    private double learningRate;
    private int epochs;

    LanguageClassifier(double learningRate, int epochs) {
        this.learningRate = learningRate;
        this.epochs = epochs;
        perceptrons = new ArrayList<Perceptron>();
        languages = new ArrayList<>();
    }

    public void train(String trainingDataFile) {
        List<Element> elements = readElements(trainingDataFile);

        languages = elements.stream()
                .map(Element::getLabel)
                .distinct()
                .collect(Collectors.toList());

        for (String lan : languages) {
            Perceptron perceptron = new Perceptron(learningRate, epochs);
            List<Element> perceptronTrainingData = elements.stream()
                    .map(element -> {
                        int expectedOutput = element.getLabel().equals(lan) ? 1 : 0;
                        Element newElement = new Element(element.getVector(), expectedOutput, element.getLabel(), element.getText());
                        return newElement;
                    })
                    .collect(Collectors.toList());

            perceptron.trainWithData(perceptronTrainingData);
            perceptrons.add(perceptron);
        }


    }

    public void test(String testDataFile) {
        List<Element> elements = readElements(testDataFile);

        List<Element> incorrect = new ArrayList<>();
        int correct = 0;
        for (Element element : elements) {
            String predictedLanguage = classify(element.getVector());
            if (element.getLabel().equals(predictedLanguage)) {
                correct++;
            } else {
                incorrect.add(element);
            }
        }
        double accuracy = (double) correct / elements.size();
        System.out.println("Test Accuracy: " + String.format("%.2f", accuracy * 100) + "%");
        if(!incorrect.isEmpty()) {
            System.out.println("Incorrectly classified texts");
            for(Element element : incorrect) {
              String predictedLanguage = classify(element.getVector());
              String actualLanguage = element.getLabel();
              String content = element.getText();

                System.out.println("Text: " + content);
                System.out.println("Actual: " + actualLanguage);
                System.out.println("Predicted: " + predictedLanguage);

            }
        }



    }

    public String classify(double[] vector){
        List<Double> activations = new ArrayList<>();

        for (Perceptron perceptron : perceptrons) {
            activations.add(perceptron.getActivation(vector));
        }


        int max = 0;
        for (int i = 1; i < activations.size(); i++) {
            if (activations.get(i) > activations.get(max)) {
                max = i;
            }
        }

        return languages.get(max);
    }


    void classifyText(String text) {
        double[] vector = vectorize(text);
        String language = classify(vector);
        System.out.println("Text is " + language);
    }

    public List<Element> readElements(String trainingDataFile) {
        List<Element> elements = new ArrayList<>();
        try {
            File elementsFile = new File(trainingDataFile);
            BufferedReader br = new BufferedReader(new FileReader(elementsFile));
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);

                String text = null;
                String language = null;
                if (parts.length == 2) {
                     language = parts[0];
                     text = parts[1];
                }

                double[] vector = vectorize(text);

                elements.add(new Element(vector, -1, language, text));
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return elements;
    }

    private double[] vectorize(String text) {
        int[] letterCounts = new int[26];

        for (char c : text.toLowerCase().toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                int index = c - 'a';
                letterCounts[index]++;
            }
        }

        double[] vector = new double[26];
        for (int i = 0; i < 26; i++) {
            vector[i] = letterCounts[i];
        }


        return normalizeVector(vector);
    }

    public double[] normalizeVector(double[] vector) {

        double len = 0;
        for (double v : vector) {
            len += v * v;
        }
        len = Math.sqrt(len);

        if (len > 0) {
            for (int i = 0; i < vector.length; i++) {
                vector[i] /= len;
            }
        }

        return vector;
    }

    public static void main(String[] args) {
        LanguageClassifier languageClassifier = new LanguageClassifier(0.0002, 1000);
        languageClassifier.train("exemplary_data/lang.train.csv");
        languageClassifier.test("exemplary_data/lang.test.csv");
        System.out.println("Enter a text to classify: ");
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();
        languageClassifier.classifyText(text);

    }


}
