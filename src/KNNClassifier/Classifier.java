package KNNClassifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Classifier {
    public static void classifyTestData(String trainingFile, String testFile, int k) {
        List<TrainingElement> trainingElements= readTrainingSet(trainingFile);
        List<TestElement> testElements= new ArrayList<>();
        try {
            File testFile2 = new File(testFile);
            if (!testFile2.exists()) {
                throw new FileNotFoundException("Test file does not exist");
            }
            String line;
            BufferedReader br = new BufferedReader(new FileReader(testFile2));
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                String[] values = Arrays.copyOf(tokens, tokens.length -1);
                TestElement el = new TestElement(tokens[tokens.length-1],Arrays.stream(values).mapToDouble(Double::parseDouble).toArray());
                testElements.add(el);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        int counter = 0;
        for( TestElement testElement : testElements) {
            deduceType(testElement, trainingElements, k, false);
            if( testElement.getDeducedType().equals(testElement.getActualType())) {
                counter++;
            }
        }
        double accuracy = (double)counter/testElements.size();
        System.out.println("Accuracy: " + accuracy);
    }

    public static  List<TrainingElement> readTrainingSet(String trainingFile){
        List<TrainingElement> trainingElements = new ArrayList<>();
        try {
            File trainingFile2 = new File(trainingFile);
            if (!trainingFile2.exists()) {
                throw new FileNotFoundException("Training file does not exist");
            }
            BufferedReader br = new BufferedReader(new FileReader(trainingFile2));
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                String[] values = Arrays.copyOf(tokens, tokens.length - 1);
                TrainingElement el = new TrainingElement(tokens[tokens.length - 1], Arrays.stream(values).mapToDouble(Double::parseDouble).toArray());
                trainingElements.add(el);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return trainingElements;
    }
    public static void deduceType(TestElement testElement, List<TrainingElement> trainingSet, int k, boolean print) {
        List<Distance> distances = new ArrayList<>();
        for(TrainingElement trainingElement : trainingSet) {
            Distance dis = new Distance(trainingElement, calculateDistance(testElement.getVector(), trainingElement.getVector()) );
            distances.add(dis);
        }
        distances.sort(Distance::compareTo);
        Distance[] nearestValues = Arrays.copyOf(distances.toArray(new Distance[distances.size()]), k);
        Map<String,Integer> frequency = new HashMap<>();
        for(Distance distance : nearestValues) {
            frequency.merge(distance.getEl().getActualType(), 1, Integer::sum);
        }
        String finalType = frequency.keySet().stream().findFirst().get();
        for(Map.Entry<String,Integer> entry : frequency.entrySet()) {
            if (entry.getValue() > frequency.get(finalType)) {
                finalType = entry.getKey();
            }
        }
        testElement.setDeducedType(finalType);
        if(print) {
            System.out.println("The vector is of type " + finalType);
        }
    }

    public static double calculateDistance(double[] testVector, double[] trainVector) {
        double distance = 0;
        if (testVector.length != trainVector.length) {
            throw new IllegalArgumentException("Vectors must have the same length");
        }
        for(int i = 0; i < testVector.length; i++) {
            distance += Math.pow(testVector[i] - trainVector[i], 2);
        }
        return Math.sqrt(distance);
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("K-NN Classifier\n1 - Test Iris data\n2 - Test your vector data for identifying Iris type \n3 - Test wdbc data");
        int choice = scan.nextInt();
        switch (choice) {
            case 1 -> {
                System.out.println("Enter k: ");
                int k = scan.nextInt();
                classifyTestData("exemplary_data/iris.data", "exemplary_data/iris.test.data", k);
            }
            case 2 -> {
                System.out.println("Enter your vector data (4 doubles separated by spaces): ");
                scan.nextLine();
                String line = scan.nextLine();
                double[] vector = Arrays.stream(line.split(" ")).mapToDouble(Double::parseDouble).toArray();
                TestElement testElement = new TestElement(null, vector);
                System.out.println("Enter k: ");
                int k = scan.nextInt();
                scan.nextLine();
                deduceType(testElement,readTrainingSet("exemplary_data/iris.data"),k,true);
            }

            case 3 -> {
                System.out.println("Enter k: ");
                int k2 = scan.nextInt();
                classifyTestData("exemplary_data/wdbc.data", "exemplary_data/wdbc.test.data", k2);
            }
        }


    }
}