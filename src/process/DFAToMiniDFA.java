package process;

import entity.DFA;
import graphviz.Graphviz;

import java.io.File;
import java.util.*;

public class DFAToMiniDFA {

    private static ArrayList<DFA> miniDFA = NFAToDFA.DFAs;
    public static HashMap<Integer,HashMap<Character,Integer>> stateChange = new HashMap<>();
    public static ArrayList<Integer> finalState = NFAToDFA.finalState;
    //删除多余
    public static void DeleteSurplus(){
        ArrayList<DFA> surplus;
        do {
            surplus = new ArrayList<>();
            for (DFA dfaI : miniDFA) {
                boolean flag = false;
                for (DFA dfaJ : miniDFA)
                    if (dfaI.getSt() == 0 || dfaI.getSt() == dfaJ.getEd()) {
                        flag = true;
                        break;
                    }
                if (!flag)
                    surplus.add(dfaI);
            }
        }while(surplus.size()!=0);
        miniDFA.removeAll(surplus);
    }
//获取状态转换图
    public static void GetStateChange(){
        System.out.println(miniDFA);
        System.out.println(NFAToDFA.allState);
        for (DFA dfaI:miniDFA) {
            HashMap<Character, Integer> hm = new HashMap<>();
            for(char x: Main.letter)
                hm.put(x,-1);
            for (DFA dfaJ:miniDFA) {
                if(dfaI.getSt()==dfaJ.getSt())
                    hm.put(dfaJ.getW(),dfaJ.getEd());
            }
            NFAToDFA.allState.remove(dfaI.getSt());
            stateChange.put(dfaI.getSt(),hm);
        }
        for(int i:NFAToDFA.allState)
        {
            HashMap<Character, Integer> hm = new HashMap<>();
            for(char x: Main.letter)
                hm.put(x,-1);
            stateChange.put(i,hm);
        }

    }

    public static void ShowState(){

        System.out.println("");
        System.out.print("  ");
        for (char x: Main.letter) {
            System.out.print("  "+x);
        }
        System.out.println();
        int n = stateChange.size();
        for (int i =0;i< n;i++){
            System.out.print(i+":");
            for (char x: Main.letter) {
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
    public static int Move(int i,char x){
        for (DFA DFA:miniDFA) {
            if(DFA.getSt() == i && DFA.getW() == x)
                return DFA.getEd();
        }
        return -1;
    }

    //  [a][b][group][ga][gb][state]
    public static void InitArray(int[][] array){
        System.out.println(stateChange);
        int group = Main.letter.size();
        for(int i=0;i<stateChange.size();i++){
            for (int j = 0; j< Main.letter.size(); j++) {
                if(stateChange.get(i)!=null)
                    array[i][j] = stateChange.get(i).get(Main.letter.get(j));
                else
                    array[i][j]=-1;
            }
            if(finalState.contains(i))
                array[i][Main.letter.size()]=0;
            else
                array[i][Main.letter.size()]=1;

        }
        //设置标志位
        System.out.println(Arrays.deepToString(array));
        System.out.println(stateChange);
        ArrayList<ArrayList<Integer>> arrayLists = new ArrayList<>();
        for(int i=0;i<stateChange.size();i++){
            ArrayList<Integer>arrayList = new ArrayList<>();
            for(int j = Main.letter.size()+1, n = 0; j<2* Main.letter.size()+1||n< Main.letter.size(); j++,n++)
            {

                if(array[i][n]!=-1)
                    array[i][j]=array[array[i][n]][Main.letter.size()];
                else
                    array[i][j]=-1;
                if(array[i][group]==1)
                    arrayList.add(array[i][j]);

            }
            arrayLists.add(arrayList);
        }
        System.out.println(arrayLists);
        ArrayList<Integer> simple = new ArrayList<>();
        for(int i=0;i<arrayLists.size();i++)
        {
            if(!arrayLists.get(i).isEmpty() )
            {
                simple = arrayLists.get(i);
                break;
            }

        }

        System.out.println(simple);
        int id = 2;
        for(int i=0;i<arrayLists.size();i++)
        {
            //System.out.println(arrayLists.get(i));
            //System.out.println(arrayLists.get(i).equals(simple));
            if(!arrayLists.get(i).isEmpty() && (!arrayLists.get(i).equals(simple))){
                array[i][2*group+2-1]=1;
            }
        }
        System.out.println(Arrays.deepToString(array));
    }

    public static void RenewArray(int[][] array,int groupID) {
        int group = Main.letter.size();
        int groupNum = groupID;
        boolean flag = true;
        for (int i = 0; i < stateChange.size(); i++) {
            if(array[i][2*group+1]==1)
            {
                array[i][group]=groupID;
                array[i][2*group+1]=0;
                flag=false;
            }
        }

        for (int g = 0; g < groupNum; g++) {
            ArrayList<ArrayList<Integer>> arrayLists = new ArrayList<>();
            for(int i=0;i<stateChange.size();i++) {
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
            for(int x=0;x<arrayLists.size();x++)
            {
                if(!arrayLists.get(x).isEmpty() )
                {
                    simple = arrayLists.get(x);
                    break;
                }

            }
            //System.out.println(simple);
            for(int x=0;x<arrayLists.size();x++)
            {
                if(!arrayLists.get(x).isEmpty() && (!arrayLists.get(x).equals(simple))){
                    array[x][2*group+2-1]=1;
                }
            }
        }
        if(!flag)
            RenewArray(array,++groupID);

    }

    private static String str = "";
    public static void Divide(){
        GetStateChange();
//        ShowState();
        System.out.println("FinalState:"+finalState);
        int id = 2;
        int[][] array = new int[stateChange.size()][2* Main.letter.size()+2];
        InitArray(array);
        RenewArray(array,id);
        System.out.println(Arrays.deepToString(array));
        int group = Main.letter.size();
        ArrayList<ArrayList<Integer>> arrayLists = new ArrayList<>();
        for(int i=0;i<stateChange.size();i++) {
            for (int j = group+1; j < 2*group+1; j++) {
                ArrayList<Integer> arrayList = new ArrayList<>();
                arrayList.add(array[i][group]);
                arrayList.add(array[i][j]);
                arrayList.add(j);
                System.out.println(arrayList);
                if (array[i][j] != -1&&!arrayLists.contains(arrayList)){
                    str+=array[i][group]+"->"+array[i][j]+"[label="+ Main.letter.get(j-group-1)+"];";
                    arrayLists.add(arrayList);
                    if(finalState.contains(i))
                        str+=array[i][group]+"[shape=doublecircle];";
                }
            }


        }


    }

    public static void Convert()
    {
        DeleteSurplus();
        Divide();
        DrawminiDFA("node[shape=circle];rankdir=LR;"+str);
    }
    public static void DrawminiDFA(String dotFormat){
        Graphviz gv=new Graphviz();
        gv.addln(gv.start_graph());
        gv.add(dotFormat);
        gv.addln(gv.end_graph());
        // png为输出格式，还可改为pdf，gif，jpg等
        String type = "png";
        // gv.increaseDpi();
        gv.decreaseDpi();
        gv.decreaseDpi();
        File out = new File("miniDFA"+"."+ type);
        gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );
    }
}
