package Kmeans;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class KMeans {

    Random rand = new Random();

    public List<Point> loadPoints(String filename) throws IOException {
        List<Point> data = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            double[] attributes = new double[4];
            for (int i = 0; i < 4; i++) {
                attributes[i] = Double.parseDouble(parts[i]);
            }
            data.add(new Point(attributes, parts[4]));
        }

        reader.close();
        return data;
    }

    public static void standardize(List<Point> data) {
        int numOfAttributes = data.get(0).attributes.length;
        double[] means = new double[numOfAttributes];
        double[] stds = new double[numOfAttributes];

        for (int i = 0; i < numOfAttributes; i++) {
            for (Point p : data) means[i] += p.attributes[i];
            means[i] /= data.size();
        }

        for (int i = 0; i < numOfAttributes; i++) {
            for (Point p : data) stds[i] += Math.pow(p.attributes[i] - means[i], 2);
            stds[i] = Math.sqrt(stds[i] / data.size());
        }

        for (Point p : data) {
            for (int i = 0; i < numOfAttributes; i++) {
                if (stds[i] != 0) {
                    p.attributes[i] = (p.attributes[i] - means[i]) / stds[i];
                }
            }
        }
    }

    public double calculateDistance(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++)
            sum += Math.pow(a[i] - b[i], 2);
        return Math.sqrt(sum);
    }

    public void printResults( List<Point> resultPoints, int k , int[] pointsAssignmentsToCluster){
        for (int i = 0; i < k; i++) {
            System.out.println("Cluster " + i + ":");
            for (int j = 0; j < resultPoints.size(); j++) {
                if (pointsAssignmentsToCluster[j] == i) {
                    System.out.println(Arrays.toString(resultPoints.get(j).attributes)+ resultPoints.get(j).label);
                }
            }
        }

        System.out.println("\nPurity:");
        for (int i = 0; i < k; i++) {

            Map<String, Integer> freq = new HashMap<>();
            int total = 0;
            for (int j = 0; j < resultPoints.size(); j++) {
                if (pointsAssignmentsToCluster[j] == i) {
                    freq.put(resultPoints.get(j).label, freq.getOrDefault(resultPoints.get(j).label, 0) + 1);
                    total++;
                }
            }


            System.out.println("Cluster " + i + ":");
            for (Map.Entry<String, Integer> entry : freq.entrySet()) {
                double purity = (entry.getValue() * 100.0) / total;
                System.out.println( entry.getKey() + " " + purity);
            }
        }
    }


    public  int[] calculateKMeans(List<Point> points, int k) {
        int numberOfAttributes = points.get(0).attributes.length;
        int numberOfPoints = points.size();

        double[][] centroids = new double[k][numberOfAttributes];
        for (int i = 0; i < k; i++) {
            int ind = rand.nextInt(numberOfPoints);
            centroids[i] = Arrays.copyOf(points.get(ind).attributes, numberOfAttributes);
        }

        int[] pointAssignmentsToClusters = new int[numberOfPoints];
        boolean stop;
        int iteration = 0;

        do {
            stop = true;
            double totalDistance = 0;

            for (int i = 0; i < numberOfPoints; i++) {
                double minDist = Double.MAX_VALUE;
                int closestCentroidIndex = 0;
                for (int j = 0; j < k; j++) {
                    double dist = calculateDistance(points.get(i).attributes, centroids[j]);
                    if (dist < minDist) {
                        minDist = dist;
                        closestCentroidIndex = j;
                    }
                }
                totalDistance += minDist;

                if (pointAssignmentsToClusters[i] != closestCentroidIndex) {
                    pointAssignmentsToClusters[i] = closestCentroidIndex;
                    stop = false;
                }
            }

            System.out.println("Iteration " + ++iteration + "  : " + totalDistance);

            double[][] newCentroids = new double[k][numberOfAttributes];
            int[] counts = new int[k];

            for (int i = 0; i < numberOfPoints; i++) {
                int cluster = pointAssignmentsToClusters[i];
                for (int j = 0; j < numberOfAttributes; j++) {
                    newCentroids[cluster][j] += points.get(i).attributes[j];
                }
                counts[cluster]++;
            }

            for (int i = 0; i < k; i++) {
                if (counts[i] > 0) {
                    for (int j = 0; j < numberOfAttributes; j++) {
                        newCentroids[i][j] /= counts[i];
                    }
                }
            }

            centroids = newCentroids;
        } while (!stop);

        return pointAssignmentsToClusters;
    }


    public void startKMeans(int k, String filename) throws IOException {
        List<Point> points = loadPoints(filename);
        standardize(points);
        int[] assignments = calculateKMeans(points, k);
        printResults(points, k, assignments);
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter k: ");
        int k = sc.nextInt();
        KMeans km = new KMeans();
        km.startKMeans(k, "examplary_data/iris.data");
    }


}
