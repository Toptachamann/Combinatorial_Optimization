import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.util.*;

/**
 * Created by Timofey on 8/8/2017.
 */
public class MinCostFlowSolver {
    private class Edge {
        private int v, cap, cost, index;

        public Edge(int v, int cap, int cost, int index) {
            this.v = v;
            this.cap = cap;
            this.cost = cost;
            this.index = index;
        }
    }

    private class Vertex {
        private ArrayList<Edge> inDeg, outDeg;

        public Vertex() {
            inDeg = new ArrayList<>();
            outDeg = new ArrayList<>();
        }
    }

    private ArrayList<Vertex> network;
    private int numOfVertices, numOfEdges, s, t, d;


    public MinCostFlowSolver() {
        network = null;
    }

    public void readGraph(String path) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(new File(path)))) {
            String[] lineTokens = in.readLine().split("\\s");
            if (lineTokens.length != 5) {
                throw new IOException("Wrong input: graph parameters");
            }
            this.numOfVertices = Integer.parseInt(lineTokens[0]);
            this.numOfEdges = Integer.parseInt(lineTokens[1]);
            this.s = Integer.parseInt(lineTokens[2]) - 1;
            this.t = Integer.parseInt(lineTokens[3]) - 1;
            this.d = Integer.parseInt(lineTokens[4]);
            if (this.s == this.t) {
                throw new IOException("Wrong input: s == t");
            }
            network = new ArrayList<>(numOfVertices);
            for (int i = 0; i < numOfVertices; i++) {
                network.add(new Vertex());
            }
            int from, to, cap, cost;
            for (int i = 0; i < numOfEdges; i++) {
                lineTokens = in.readLine().split("\\s");
                if (lineTokens.length != 4) {
                    throw new IOException("Wrong input: incorrect edge parameters");
                }
                from = Integer.parseInt(lineTokens[0]) - 1;
                to = Integer.parseInt(lineTokens[1]) - 1;
                cap = Integer.parseInt(lineTokens[2]);
                cost = Integer.parseInt(lineTokens[3]);
                network.get(from).outDeg.add(new Edge(to, cap, cost, i + 1));
                network.get(to).inDeg.add(new Edge(from, cap, cost, i + 1));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new IOException("Wrong input: number format");
        }
    }

    public void formulateLP(String path) throws IOException {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(new File(path)))) {
            StringBuilder builder = new StringBuilder();
            for (Vertex vertex : network) {
                for (Edge edge : vertex.outDeg) {
                    builder.append(" - " + edge.cost + "x" + edge.index);
                }
            }

            out.write(builder.toString());
            out.write('\n');
            for (int i = 0; i < network.size(); i++) {
                if (i == s || i == t) {
                    continue;
                }
                Vertex vertex = network.get(i);
                builder.setLength(0);
                for (Edge edge : vertex.inDeg) {
                    builder.append("x" + edge.index + " + ");
                }
                if (builder.length() > 0) {
                    builder.delete(builder.length() - 3, builder.length());
                }
                for (Edge edge : vertex.outDeg) {
                    builder.append(" - x" + edge.index);
                }
                builder.append(" = 0\n");
                out.write(builder.toString());
            }
            for (Vertex vertex : network) {
                builder.setLength(0);
                for (Edge edge : vertex.outDeg) {
                    builder.append("x" + edge.index + " <= " + edge.cap + '\n');
                }
                out.write(builder.toString());
            }
            builder.setLength(0);
            Vertex vertex = network.get(s);
            for (Edge edge : vertex.outDeg) {
                builder.append("x" + edge.index + " + ");
            }
            if (builder.length() > 0) {
                builder.delete(builder.length() - 3, builder.length());
            }
            for(Edge edge : vertex.inDeg){
                builder.append(" - x" + edge.index);
            }
            builder.append(" = " + d + "\n");
            out.write(builder.toString());
            builder.setLength(0);
        }
    }
}









