package process;

import entity.DFA;
import graphviz.Graphviz;

import java.io.File;
import java.util.*;

public class NFAToDFA {

    public static ArrayList<DFA> dfas = new ArrayList<>();
    public static ArrayList<DFA> DFAs = new ArrayList<>();
    public static ArrayList<Integer> finalState = new ArrayList<>();
    public static HashSet<Integer> allState = new HashSet<>();

    private static ArrayList<ArrayList<Integer>> bg = new ArrayList<>();

    private static HashMap<ArrayList<Integer>, HashMap<Character, ArrayList<Integer>>> maps = new HashMap<>();
    private static String str = "";

    //ε-闭包
    public static void EClosure(int start, ArrayList<Integer> arr) {
//        HashSet<Integer> isRepeat = new HashSet<>();
        if (!arr.contains(start)) {
            arr.add(start);
            for (DFA DFA : dfas) {
                if (DFA.getSt() == start && DFA.getW() == 'ε')
                    EClosure(DFA.getEd(), arr);
            }
        }
    }

    //通过闭包
    public static void NClosure(int start, ArrayList<Integer> arr, char x) {
        for (DFA DFA : dfas) {
            if (DFA.getSt() == start && DFA.getW() == x) {
                arr.add(DFA.getEd());
            }
        }

    }

    //状态子集
    /**
     * 1.求通过闭包
     * 2.求通过闭包的ε-闭包
     */
    public static void SClosure(ArrayList<Integer> arrayList, HashMap<Character, ArrayList<Integer>> map) {

        for (char x : Main.letter) {
            ArrayList<Integer> closure = new ArrayList<>();
            for (int i : arrayList) {
                NClosure(i, closure, x);
            }
            HashSet<Integer> set = new HashSet<>();
            if (closure.size() != 0)
                for (int i : closure) {
                    ArrayList<Integer> e = new ArrayList<>();
                    EClosure(i, e);
                    set.addAll(e);
                }
            closure.addAll(set);
            map.put(x, closure);
        }
        System.out.println(arrayList + " " + map);

    }

    public static void Convert() {

        int startId = Main.stack.peek().getStart().getId();

        int endId = Main.stack.peek().getEnd().getId();

        //输出NFA表达式
        String[] exp = SuffixToNFA.str.split(";");
        for (String s : exp) {
            System.out.println(s);
        }
        //
        HashMap<Character, ArrayList<Integer>> map = new HashMap<>();
        ArrayList<Integer> arrayList = new ArrayList<>();
        //初始化
        EClosure(startId, arrayList);
        SClosure(arrayList, map);
        bg.add(arrayList);
        maps.put(arrayList, map);
        Queue<HashMap<Character, ArrayList<Integer>>> queue = new LinkedList<>();
        queue.add(map);
        //用队列实现遍历
        while (!queue.isEmpty()) {

            HashMap<Character, ArrayList<Integer>> mapN = queue.poll();
            for (char x : Main.letter) {
                HashMap<Character, ArrayList<Integer>> mapX = new HashMap<>();
                if (mapN.get(x).size() != 0) {
                    SClosure(mapN.get(x), mapX);
                    if (!maps.containsKey(mapN.get(x))) {
                        bg.add(mapN.get(x));
                        maps.put(mapN.get(x), mapX);
                        queue.add(mapX);
                    }
                }
            }
        }
        //System.out.println(maps);
        getDotFormat(endId);
        DrawDFA("node[shape=plaintext];0[shape=circle];\"\"->0[label = start];node[shape=circle];rankdir=LR;" + str);

    }

    public static void RemoveNFAToDFA() {
        dfas = new ArrayList<>();
        bg = new ArrayList<>();
        maps = new HashMap<>();
        DFAs = new ArrayList<>();
        str = "";
        finalState = new ArrayList<>();
        allState = new HashSet<>();
    }

    public static void getDotFormat(int endId) {
        Iterator<Map.Entry<ArrayList<Integer>, HashMap<Character, ArrayList<Integer>>>> iter = maps.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = iter.next();
            ArrayList<Integer> key = (ArrayList<Integer>) entry.getKey();
            HashMap<Character, ArrayList<Integer>> val = (HashMap<Character, ArrayList<Integer>>) entry.getValue();

            for (char x : Main.letter) {
                if (val.get(x).size() != 0) {
                    if (key.contains(endId)) {
                        int fs = bg.indexOf(key);
                        str += fs + "[shape=doublecircle];";
                        if (!finalState.contains(fs))
                            finalState.add(fs);
                    }
                    if (val.get(x).contains(endId)) {
                        int fs = bg.indexOf(val.get(x));
                        str += fs + "[shape=doublecircle];";
                        if (!finalState.contains(fs))
                            finalState.add(fs);
                    }


                    allState.add(bg.indexOf(key));
                    allState.add(bg.indexOf(val.get(x)));
                    str += bg.indexOf(key) + "->" + bg.indexOf(val.get(x)) + "[label=" + x + "];";
                    DFAs.add(new DFA(bg.indexOf(key), x, bg.indexOf(val.get(x))));
                }

            }

        }

    }

    public static void DrawDFA(String dotFormat) {
        Graphviz gv = new Graphviz();
        gv.addln(gv.start_graph());
        gv.add(dotFormat);
        gv.addln(gv.end_graph());
        // png为输出格式，还可改为pdf，gif，jpg等
        String type = "png";
        // gv.increaseDpi();
        gv.decreaseDpi();
        gv.decreaseDpi();
        File out = new File("DFA" + "." + type);
        gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
    }
}
