import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Timofey on 8/9/2017.
 */
public class MinCostMatchingSolver {
    private class Edge {
        int to, cost, index;

        public Edge(int to, int cost, int index) {
            this.to = to;
            this.cost = cost;
            this.index = index;
        }
    }

    public class Vertex {
        ArrayList<Edge> indeg, outdeg;

        public Vertex() {
            indeg = new ArrayList<>();
            outdeg = new ArrayList<>();
        }
    }

    private ArrayList<Vertex> graph;
    int n, m;

    public MinCostMatchingSolver() {
        graph = new ArrayList<>();
    }

    public void readGraph(String path) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(new File(path)))) {
            String[] buf = in.readLine().split("\\s");
            this.n = Integer.parseInt(buf[0]);
            this.m = Integer.parseInt(buf[1]);
            if (buf.length != 2) {
                throw new IOException("Wrong input: graph parameters");
            }
            graph.clear();
            graph.ensureCapacity(n);
            for (int i = 0; i < n; i++) {
                graph.add(new Vertex());
            }
            int u, v, c;
            for (int i = 0; i < m; i++) {
                buf = in.readLine().split("\\s");
                if (buf.length != 3) {
                    throw new IOException("Wrong input: edge parameters");
                }
                u = Integer.parseInt(buf[0]) - 1;
                v = Integer.parseInt(buf[1]) - 1;
                c = Integer.parseInt(buf[2]);
                graph.get(u).indeg.add(new Edge(v, c, i + 1));
                graph.get(v).outdeg.add(new Edge(u, c, i + 1));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new IOException("Wrong input: incorrect number representation");
        }
    }

    public void formulateFinalLP(String path, int maxCardinalityMatching) throws IOException {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(new File(path)))) {
            StringBuilder auxiliaryBuilder = new StringBuilder(),
                    builder = new StringBuilder();
            for (Vertex vertex : graph) {
                for (Edge edge : vertex.indeg) {
                    builder.append(" - " + edge.cost + "x" + edge.index);
                    auxiliaryBuilder.append("x" + edge.index + " + ");
                }
            }
            if (auxiliaryBuilder.length() > 0) {
                auxiliaryBuilder.delete(auxiliaryBuilder.length() - 3, auxiliaryBuilder.length());
                auxiliaryBuilder.append(" = " + maxCardinalityMatching + "\n");
            }
            out.write(builder.toString());
            out.write('\n');
            out.write(auxiliaryBuilder.toString());
            writeConstraints(out);
        }
    }

    public void formulateInitialLP(String path) throws IOException {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(new File(path)))) {
            StringBuilder builder = new StringBuilder();
            for (Vertex vertex : graph) {
                for (Edge edge : vertex.indeg) {
                    builder.append("x" + edge.index + " + ");
                }
            }
            if (builder.length() > 0) {
                builder.delete(builder.length() - 3, builder.length());
            }
            out.write(builder.toString());
            out.write('\n');
            writeConstraints(out);
        }
    }

    private void writeConstraints(BufferedWriter out) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (Vertex vertex : graph) {
            for (Edge edge : vertex.indeg) {
                builder.append("x" + edge.index + " + ");
            }
            for (Edge edge : vertex.outdeg) {
                builder.append("x" + edge.index + " + ");
            }
            if (builder.length() > 0) {
                builder.delete(builder.length() - 3, builder.length());
                builder.append(" <= 1\n");
                out.write(builder.toString());
            }
            builder.setLength(0);
        }
    }
}