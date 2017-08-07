import java.io.*;
import java.util.*;

/**
 * Created by Timofey on 7/31/2017.
 */
public class MatchingSolver {
    private class Edge{
        int to, index;
        public Edge(int to, int index){
            this.to = to;
            this.index = index;
        }
    }
    private ArrayList<ArrayList<Edge>> leftPart, rightPart;
    private int a, b, m;
    private int solution;

    public MatchingSolver(){
        leftPart = new ArrayList<>();
        rightPart = new ArrayList<>();
        this.m = this.a = this.b = this.solution = -1;
    }

    private void reload(){
        leftPart.clear();
        rightPart.clear();
        this.m = this.a = this.b = this.solution = -1;
    }

    public void readGraph(String path) throws IOException{
        File file = new File(path);
        if(file.exists()){
            try(BufferedReader in = new BufferedReader(new FileReader(file))){
                reload();
                String[] buf = in.readLine().split("\\s");
                if(buf.length != 3){
                    throw new IOException("Wrong input");
                }
                int a = Integer.parseInt(buf[0]),  b = Integer.parseInt(buf[1]);
                this.m = Integer.parseInt(buf[2]);
                int u, v;
                if(a < b){
                    this.a = a;
                    this.b = b;
                    for(int i = 0; i < this.a; i++){
                        leftPart.add(new ArrayList<>());
                    }
                    for(int i = 0; i < this.b; i++){
                        rightPart.add(new ArrayList<>());
                    }
                    for(int i = 0; i < m; i++){
                        buf = in.readLine().split("\\s");
                        if(buf.length != 2){
                            throw new IOException("Wrong input");
                        }
                        u = Integer.parseInt(buf[0]);
                        v = Integer.parseInt(buf[1]);
                        leftPart.get(u-1).add(new Edge(v-1, i));
                        rightPart.get(v - 1).add(new Edge(u-1, i));
                    }
                }else{
                    this.a = b;
                    this.b = a;
                    for(int i = 0; i < this.a; i++){
                        leftPart.add(new ArrayList<>());
                    }
                    for(int i = 0; i < this.b; i++){
                        rightPart.add(new ArrayList<>());
                    }
                    for(int i = 0; i < m; i++){
                        buf = in.readLine().split("\\s");
                        if(buf.length != 2){
                            throw new IOException("Wrong input");
                        }
                        u = Integer.parseInt(buf[0]);
                        v = Integer.parseInt(buf[1]);
                        leftPart.get(v-1).add(new Edge(u-1, i));
                        rightPart.get(u-1).add(new Edge(v-1, i));
                    }
                }
            }
        }else{
            throw new FileNotFoundException();
        }
    }

    private void greedy(ArrayList<Boolean> matchedA, ArrayList<Integer> matchedB){
        for(int i = 0; i < a; i++){
            ArrayList<Edge> adjVer = leftPart.get(i);
            for(Edge j : adjVer){
                if(matchedB.get(j.to) == -1){
                    matchedB.set(j.to, i);
                    matchedA.set(i, true);
                    ++solution;
                    break;
                }
            }
        }
    }

    private boolean kuhn_try(int vertex, ArrayList<Boolean> matchedA, ArrayList<Boolean> used, ArrayList<Integer> matchedB){
        if(used.get(vertex)){
            return false;
        }
        used.set(vertex, true);
        ArrayList<Edge> adjVer = leftPart.get(vertex);
        for(Edge i : adjVer){
            if(matchedB.get(i.to) == -1 || kuhn_try(matchedB.get(i.to), matchedA, used, matchedB)){
                matchedA.set(vertex, true);
                matchedB.set(i.to, vertex);
                return true;
            }
        }
        return false;
    }


    public int solveMaxMatching(){
        if(leftPart.size() > 0){
            solution = 0;
            ArrayList<Boolean> matchedA = new ArrayList<>(a);
            ArrayList<Boolean> used = new ArrayList<>(a);
            ArrayList<Integer> matchedB = new ArrayList<>(b);
            for(int i = 0; i < a; i++){
                matchedA.add(false);
                used.add(false);
            }
            for(int i = 0; i < b; i++){
                matchedB.add(-1);
            }
            greedy(matchedA, matchedB);
            for(int i = 0; i < a; i++){
                if(!matchedA.get(i)){
                    for(int j = 0; j < a; j++){
                        used.set(j, false);
                    }
                    if(kuhn_try(i, matchedA, used, matchedB)){
                        ++solution;
                    }
                }
            }
            return this.solution;
        }else{
            return -1;
        }
    }

    public void formulateLinearProgramme(String pathToLPFile) throws IOException {
        try(BufferedWriter out = new BufferedWriter(new FileWriter(new File(pathToLPFile)))){
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < m; i++){
                builder.append("x" + String.valueOf(i + 1) + " + ");
            }
            if(builder.length() > 0){
                builder.delete(builder.length() - 2, builder.length() - 1);
                out.write(builder.toString());
                out.write('\n');
            }
            for(int i = 0; i < this.a; i++){
                builder.setLength(0);
                for(Edge edge : leftPart.get(i)){
                    builder.append("x" + String.valueOf(edge.index + 1) + " + ");
                }
                if(builder.length() > 0){
                    builder.delete(builder.length() - 3, builder.length());
                    builder.append(" <= 1");
                    out.write(builder.toString());
                    out.write('\n');
                }
            }
            for(int i = 0; i < this.b; i++){
                builder.setLength(0);
                for(Edge edge : rightPart.get(i)){
                    builder.append("x" + String.valueOf(edge.index + 1) + " + ");
                }
                if(builder.length() > 0){
                    builder.delete(builder.length() - 3, builder.length());
                    builder.append(" <= 1");
                    out.write(builder.toString());
                    out.write('\n');
                }
            }
        }
    }
}
