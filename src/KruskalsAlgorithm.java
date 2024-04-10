import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

class Edge implements Comparable<Edge> {
    int u;
    int v;
    int weight;

    public Edge(int u, int v, int weight) {
        this.u = u;
        this.v = v;
        this.weight = weight;
    }

    public int compareTo(Edge edge) {
        if (this.weight > edge.weight) {
            return 1;
        } else if (this.weight == edge.weight) {
            return 0;
        } else {
            return -1;
        }
    }
}

class DisjointSet {
    int[] parent;
    int[] rank;
    public DisjointSet(int size) {
        parent = new int[size];
        rank = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }
    // метод find находит представителя множества, в котором находится вершина vertex
    public int find(int vertex) {
        if (parent[vertex] != vertex) {
            parent[vertex] = find(parent[vertex]);
        }
        return parent[vertex];
    }
    //эта функция позволяет проверить вершины v и u на связность
    public boolean find(int v, int u) {
        return find(v) == find(u);
    }

    //функция union объединяет множества (компоненты связности), содержащие элементы х и у
    public void union(int v, int u) {
        int root1 = find(v);
        int root2 = find(u);
        if (root1 == root2) {
            return;
        }
        if (rank[root1] < rank[root2]) {
            parent[root1] = root2;
        } else if (rank[root1] > rank[root2]) {
            parent[root2] = root1;
        } else {
            parent[root1] = root2;
            rank[root2]++;
        }
    }
}

public class KruskalsAlgorithm {
    public static void main(String[] args) {
        int numSets = 50 + (int)(Math.random() * 51); // количество наборов
        try {
            FileWriter fileWriter = new FileWriter("data.txt"); // создание файла
            PrintWriter printWriter = new PrintWriter(fileWriter);
            for (int i = 0; i < numSets; i++) {
                int setSize = 100 + (int)(Math.random() * 9901); // количество элементов в наборе
                printWriter.println(setSize);
                for (int j = 0; j < setSize; j++) {
                    int v = (int)(Math.random() * setSize);
                    int u = (int)(Math.random() * setSize);
                    int weight = 1 + (int)(Math.random() * 100); // случайный вес ребра
                    printWriter.println(v + " " + u + " " + weight);
                }
            }
            printWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            File file = new File("data.txt");
            Scanner scanner = new Scanner(file);
            long totalTime = 0; // переменная для подсчета общего времени работы алгоритма
            int totalIterations = 0; // переменная для подсчета общего количества итераций
            while (scanner.hasNext()) {
                int setSize = Integer.parseInt(scanner.nextLine());
                ArrayList<Edge> edges = new ArrayList<Edge>();
                for (int i = 0; i < setSize; i++) {
                    String[] edgeString = scanner.nextLine().split(" ");
                    int v = Integer.parseInt(edgeString[0]);
                    int u = Integer.parseInt(edgeString[1]);
                    int weight = Integer.parseInt(edgeString[2]);
                    Edge edge = new Edge(v, u, weight);
                    edges.add(edge);
                }
                long startTime = System.nanoTime();
                ArrayList<Edge> minimumSpanningTree = kruskalAlgorithm(edges, setSize);
                long endTime = System.nanoTime();
                long duration = (endTime - startTime); // время работы алгоритма
                int iterations = minimumSpanningTree.size(); // количество итераций
                System.out.println("Set size: " + setSize);
                System.out.println("Time: " + duration + " ns");
                System.out.println("Iterations: " + iterations);
                System.out.println();
                totalTime += duration;
                totalIterations += iterations;
            }
            scanner.close();
            System.out.println("Total Time: " + totalTime + " ns");
            System.out.println("Total Iterations: " + totalIterations);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Edge> kruskalAlgorithm(ArrayList<Edge> edges, int numVertices) {
        ArrayList<Edge> minimumSpanningTree = new ArrayList<Edge>();
        Collections.sort(edges);
        DisjointSet disjointSet = new DisjointSet(numVertices);
        for (Edge edge : edges) {
            int u = edge.u;
            int v = edge.v;
            if (!disjointSet.find(u, v)) {
                disjointSet.union(u, v);
                minimumSpanningTree.add(edge);
            }
        }
        return minimumSpanningTree;

    }
}
