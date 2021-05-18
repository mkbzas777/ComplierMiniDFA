package process;

import entity.DFA;
import graphviz.Graphviz;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DFAToMiniDFA {

    public static HashMap<Integer, HashMap<Character, Integer>> stateChange = new HashMap<>();
    private static String str = "";

    //删除多余
    public static void DeleteSurplus() {
        ArrayList<DFA> surplus;
        do {
            surplus = new ArrayList<>();
            for (DFA dfaI : NFAToDFA.DFAs) {
                boolean flag = false;
                for (DFA dfaJ : NFAToDFA.DFAs)
                    if (dfaI.getSt() == 0 || dfaI.getSt() == dfaJ.getEd()) {
                        flag = true;
                        break;
                    }
                if (!flag)
                    surplus.add(dfaI);
            }
        } while (surplus.size() != 0);
        NFAToDFA.DFAs.removeAll(surplus);
    }

    //获取状态转换图
    public static void GetStateChange() {
        System.out.println(NFAToDFA.DFAs);
        System.out.println(NFAToDFA.allState);
        for (DFA dfaI : NFAToDFA.DFAs) {
            HashMap<Character, Integer> hm = new HashMap<>();
            for (char x : Main.letter)
                hm.put(x, -1);
            for (DFA dfaJ : NFAToDFA.DFAs) {
                if (dfaI.getSt() == dfaJ.getSt())
                    hm.put(dfaJ.getW(), dfaJ.getEd());
            }
            NFAToDFA.allState.remove(dfaI.getSt());
            stateChange.put(dfaI.getSt(), hm);
        }
        for (int i : NFAToDFA.allState) {
            HashMap<Character, Integer> hm = new HashMap<>();
            for (char x : Main.letter)
                hm.put(x, -1);
            stateChange.put(i, hm);
        }

    }

    public static void ShowState() {

        System.out.println();
        System.out.print("  ");
        for (char x : Main.letter) {
            System.out.print("  " + x);
        }
        System.out.println();
        int n = stateChange.size();
        for (int i = 0; i < n; i++) {
            System.out.print(i + ":");
            for (char x : Main.letter) {
                if (stateChange.get(i) == null)
                    System.out.printf("  -");
                if (stateChange.get(i).get(x) != -1)
                    System.out.printf("%3d", stateChange.get(i).get(x));
                else
                    System.out.print("  -");
            }
            System.out.println();
        }
    }

    //
    public static int Move(int i, char x) {
        for (DFA DFA : NFAToDFA.DFAs) {
            if (DFA.getSt() == i && DFA.getW() == x)
                return DFA.getEd();
        }
        return -1;
    }

    //  [a][b][group][ga][gb][state]
    public static void InitArray(int[][] array) {
        System.out.println(stateChange);
        int group = Main.letter.size();
        for (int i = 0; i < stateChange.size(); i++) {
            for (int j = 0; j < Main.letter.size(); j++) {
                if (stateChange.get(i) != null)
                    array[i][j] = stateChange.get(i).get(Main.letter.get(j));
                else
                    array[i][j] = -1;
            }
            if (NFAToDFA.finalState.contains(i))
                array[i][Main.letter.size()] = 0;
            else
                array[i][Main.letter.size()] = 1;

        }
        //设置标志位
        //System.out.println(Arrays.deepToString(array));
        ArrayList<ArrayList<Integer>> arrayLists = new ArrayList<>();
        for (int i = 0; i < stateChange.size(); i++) {
            ArrayList<Integer> arrayList = new ArrayList<>();
            for (int j = Main.letter.size() + 1, n = 0; j < 2 * Main.letter.size() + 1 || n < Main.letter.size(); j++, n++) {

                if (array[i][n] != -1)
                    array[i][j] = array[array[i][n]][Main.letter.size()];
                else
                    array[i][j] = -1;
                if (array[i][group] == 1)
                    arrayList.add(array[i][j]);

            }
            //arrayLists.add(arrayList);
        }
        //System.out.println(arrayLists);
        ArrayList<Integer> simple = new ArrayList<>();
        for (int i = 0; i < arrayLists.size(); i++) {
            if (!arrayLists.get(i).isEmpty()) {
                simple = arrayLists.get(i);
                break;
            }

        }

        //System.out.println(simple);
        int id = 2;
        for (int i = 0; i < arrayLists.size(); i++) {
            //System.out.println(arrayLists.get(i));
            //System.out.println(arrayLists.get(i).equals(simple));
            if (!arrayLists.get(i).isEmpty() && (!arrayLists.get(i).equals(simple))) {
                array[i][2 * group + 2 - 1] = 1;
            }
        }
        //System.out.println(Arrays.deepToString(array));
    }

    public static int RenewArray(int[][] array, int groupID) {
        int group = Main.letter.size();
        boolean flag = true;
//        System.out.println();
//        System.out.println("array b:"+Arrays.deepToString(array));
//        System.out.println();
        for (int i = 0; i < stateChange.size(); i++) {
            if (array[i][2 * group + 1] == 1) {
                array[i][group] = groupID;
                array[i][2 * group + 1] = 0;
                flag = false;
            }
        }
        if (!flag)
            groupID += 1;
        for (int g = 0; g < groupID; g++) {
            ArrayList<ArrayList<Integer>> arrayLists = new ArrayList<>();
            for (int i = 0; i < stateChange.size(); i++) {
                ArrayList<Integer> arrayList = new ArrayList<>();
                for (int j = Main.letter.size() + 1, n = 0; j < 2 * Main.letter.size() + 1 || n < Main.letter.size(); j++, n++) {
                    if (array[i][n] != -1)
                        array[i][j] = array[array[i][n]][Main.letter.size()];
                    else
                        array[i][j] = -1;
                    if (array[i][group] == g)
                        arrayList.add(array[i][j]);

                }
                arrayLists.add(arrayList);
            }
            ArrayList<Integer> simple = new ArrayList<>();
            for (int x = 0; x < arrayLists.size(); x++) {
                if (!arrayLists.get(x).isEmpty()) {
                    simple = arrayLists.get(x);
                    break;
                }

            }
            //System.out.println(simple);
            for (int x = 0; x < arrayLists.size(); x++) {
                if (!arrayLists.get(x).isEmpty() && (!arrayLists.get(x).equals(simple))) {
                    array[x][2 * group + 2 - 1] = 1;
                    flag = false;
                }
            }
        }
//        System.out.println();
//        System.out.println("array e:"+Arrays.deepToString(array));
//        System.out.println();
        if (!flag) {
            return RenewArray(array, groupID);
        }
        return groupID;

    }

    public static void RemoveDFAToMiniDFA() {
        stateChange = new HashMap<>();
        str = "";
    }

    public static void Divide() {
        GetStateChange();
        ShowState();
        System.out.println("FinalState:" + NFAToDFA.finalState);

        int[][] array = new int[stateChange.size()][2 * Main.letter.size() + 2];
        InitArray(array);
        System.out.println("Begin:" + Arrays.deepToString(array));
        int num = RenewArray(array, 2) - 1;
        System.out.println("Last:" + Arrays.deepToString(array));
        int group = Main.letter.size();
        ArrayList<ArrayList<Integer>> arrayLists = new ArrayList<>();
        for (int i = 0; i < stateChange.size(); i++) {
            for (int j = group + 1; j < 2 * group + 1; j++) {
                ArrayList<Integer> arrayList = new ArrayList<>();
                arrayList.add(array[i][group]);
                arrayList.add(array[i][j]);
                arrayList.add(j);
                if (NFAToDFA.finalState.contains(i) && !arrayLists.contains(arrayList))
                    str += array[i][group] + "[shape=doublecircle];";
                if (array[i][j] != -1 && !arrayLists.contains(arrayList)) {
                    str += array[i][group] + "->" + array[i][j] + "[label=" + Main.letter.get(j - group - 1) + "];";
                    arrayLists.add(arrayList);
                }
            }
        }
        str += "node[shape=plaintext];\"\"->" + array[0][group] + "[label=start];";

    }


    public static void Convert() {
        DeleteSurplus();
        Divide();
        DrawminiDFA("node[shape=circle];rankdir=LR;" + str);
    }

    public static void DrawminiDFA(String dotFormat) {
        Graphviz gv = new Graphviz();
        gv.addln(gv.start_graph());
        gv.add(dotFormat);
        gv.addln(gv.end_graph());
        // png为输出格式，还可改为pdf，gif，jpg等
        String type = "png";
        // gv.increaseDpi();
        gv.decreaseDpi();
        gv.decreaseDpi();
        File out = new File("miniDFA" + "." + type);
        gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
    }
}
