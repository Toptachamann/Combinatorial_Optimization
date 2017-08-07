import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.util.*;

/**
 * Created by Timofey on 8/3/2017.
 */
public class MaximumFlowSolver {
    private int s, t, n;
    private final int INF = 1000 * 1000 * 1000;
    ArrayList<ArrayList<Integer>> capacities;

    public MaximumFlowSolver() {
        capacities = null;
        s = t = -1;
    }

    public void readGraph(String path) throws IOException {
        File file = new File(path);
        try (BufferedReader in = new BufferedReader(new FileReader(file));) {
            String[] buf = in.readLine().split("\\s");
            if (buf.length != 3) {
                throw new IOException("Wrong input");
            }
            this.n = Integer.parseInt(buf[0]);
            this.s = Integer.parseInt(buf[1]) - 1;
            this.t = Integer.parseInt(buf[2]) - 1;
            if (s >= n || s < 0 || t >= n || t < 0) {
                throw new IOException("Wrong source or sink parameters");
            }
            capacities = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                ArrayList<Integer> temp = new ArrayList<>(n);
                buf = in.readLine().split("\\s");
                if (buf.length != n) {
                    throw new IOException("Wrong input");
                }
                for (String s : buf) {
                    temp.add(Integer.parseInt(s));
                }
                capacities.add(temp);
            }
            in.close();
        }
    }

    public int getMaximumFlow() {
        if(capacities == null){
            return -1;
        }
        ArrayList<Integer> h = new ArrayList<>(n), e = new ArrayList<>(n), maxH = new ArrayList<>(n);
        ArrayList<ArrayList<Integer>> flows = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            h.add(0);
            e.add(0);
            maxH.add(0);
            ArrayList<Integer> temp = new ArrayList<>(n);
            for (int j = 0; j < n; j++) {
                temp.add(0);
            }
            flows.add(temp);
        }
        for (int i = 0; i < n; i++) {
            int flowOnEdge = capacities.get(s).get(i);
            flows.get(s).set(i, flowOnEdge);
            flows.get(i).set(s, -flowOnEdge);
            e.set(i, flowOnEdge);
            e.set(s, e.get(s) - flowOnEdge);
        }
        h.set(s, n);
        int size = 0;
        for (; ; ) {
            if (size == 0) {
                for (int i = 0; i < n; i++) {
                    if (i != s && i != t && e.get(i) > 0) {
                        if (size > 0 && h.get(i) > h.get(maxH.get(0))) {
                            size = 0;
                        }
                        if (size == 0 || h.get(i) == h.get(maxH.get(0))) {
                            maxH.set(size, i);
                            ++size;
                        }
                    }
                }
                if (size == 0) {
                    break;
                }
                while (size > 0) {
                    int v = maxH.get(size - 1);
                    boolean pushed = false;
                    //trying to push
                    for (int i = 0; i < n && e.get(v) > 0; i++) {
                        if (capacities.get(v).get(i) - flows.get(v).get(i) > 0 && h.get(v) == h.get(i) + 1) {
                            int flowIncrement = Math.min(capacities.get(v).get(i) - flows.get(v).get(i), e.get(v));
                            flows.get(v).set(i, flows.get(v).get(i) + flowIncrement);
                            flows.get(i).set(v, flows.get(i).get(v) - flowIncrement);
                            e.set(v, e.get(v) - flowIncrement);
                            e.set(i, e.get(i) + flowIncrement);
                            if (e.get(v) == 0) {
                                --size;
                            }
                        }
                    }
                    if (!pushed) {
                        // trying to relabel
                        h.set(v, INF);
                        for (int i = 0; i < n; i++) {
                            if (capacities.get(v).get(i) - flows.get(v).get(i) > 0 && h.get(v) > h.get(i) + 1) {
                                h.set(v, h.get(i) + 1);
                            }
                        }
                        if (h.get(v) > h.get(maxH.get(0))) {
                            size = 0;
                            break;
                        }
                    }
                }
            }
        }
        return e.get(t);
    }

    private class Edge {
        private ArrayList<Integer> inEdges, outEdge;

        public Edge() {
            inEdges = new ArrayList<>();
            outEdge = new ArrayList<>();
        }

    }

    public void formulateLP(String path) throws IOException {
        File file = new File(path);
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
            StringBuilder builder = new StringBuilder();
            StringBuilder auxBuilder = new StringBuilder();
            ArrayList<Edge> adjList = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                adjList.add(new Edge());
            }
            int index = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (capacities.get(i).get(j) > 0) {
                        ++index;
                        adjList.get(i).outEdge.add(index);
                        adjList.get(j).inEdges.add(index);
                        auxBuilder.append("x" + index + " <= " + capacities.get(i).get(j) + "\n");
                    }
                }
            }
            Edge edge = adjList.get(s);
            for (Integer ind : edge.outEdge) {
                builder.append("x" + ind + " + ");
            }
            if (builder.length() > 0) {
                builder.delete(builder.length() - 3, builder.length());
                builder.append(" - ");
            }
            for (Integer ind : edge.inEdges) {
                builder.append("x" + ind + " - ");
            }
            if (builder.length() > 0) {
                builder.delete(builder.length() - 3, builder.length());
            }
            builder.append('\n');
            out.write(builder.toString());
            for (int i = 0; i < n; i++) {
                if (i == s || i == t) {
                    continue;
                }
                builder.setLength(0);
                edge = adjList.get(i);
                for (Integer ind : edge.outEdge) {
                    builder.append("x" + ind + " + ");
                }
                if (builder.length() > 0) {
                    builder.delete(builder.length() - 3, builder.length());
                    builder.append(" - ");
                }
                for (Integer ind : edge.inEdges) {
                    builder.append("x" + ind + " - ");
                }
                if (builder.length() > 0) {
                    builder.delete(builder.length() - 3, builder.length());
                }
                builder.append(" = 0\n");
                out.write(builder.toString());
            }
            out.write(auxBuilder.toString());
            out.close();
        }
    }


}
