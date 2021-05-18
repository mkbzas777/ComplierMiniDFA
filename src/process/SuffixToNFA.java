package process;

import entity.DFA;
import entity.FromTo;
import entity.NFA;
import graphviz.Graphviz;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class SuffixToNFA {

    public static Set<NFA> set = new HashSet<>();
    public static String str = "";

    //根据逆波兰表达式求NFA
    public static void Convert(String suffix) {
        int id = 1;
        NFA start, end, newStart, newEnd;
        FromTo ft1, ft2;
        for (int i = 0; i < suffix.length(); i++) {
            switch (InfixToSuffix.order.indexOf(suffix.charAt(i))) {
                case -1 -> {
                    start = new NFA(id++);
                    end = new NFA(id++);
                    start.setNext1(end);
                    start.setState1(String.valueOf(suffix.charAt(i)));
                    NFAToDFA.dfas.add(new DFA(start.getId(), start.getState1().charAt(0), end.getId()));
                    Main.stack.push(new FromTo(start, end));
                }
                case 0 -> {
                    ft2 = Main.stack.pop();
                    ft1 = Main.stack.pop();
                    newStart = new NFA(id++);
                    newEnd = new NFA(id++);
                    newStart.setNext1(ft1.getStart());
                    newStart.setNext2(ft2.getStart());
                    newStart.setState1("ε");
                    newStart.setState2("ε");
                    NFAToDFA.dfas.add(new DFA(newStart.getId(), newStart.getState1().charAt(0), ft1.getStart().getId()));
                    NFAToDFA.dfas.add(new DFA(newStart.getId(), newStart.getState2().charAt(0), ft2.getStart().getId()));
                    ft1.getEnd().setNext1(newEnd);
                    ft2.getEnd().setNext1(newEnd);
                    ft1.getEnd().setState1("ε");
                    ft2.getEnd().setState1("ε");
                    NFAToDFA.dfas.add(new DFA(ft1.getEnd().getId(), ft1.getEnd().getState1().charAt(0), newEnd.getId()));
                    NFAToDFA.dfas.add(new DFA(ft2.getEnd().getId(), ft2.getEnd().getState1().charAt(0), newEnd.getId()));
                    Main.stack.push(new FromTo(newStart, newEnd));
                }
                case 1 -> {
                    ft2 = Main.stack.pop();
                    ft1 = Main.stack.pop();
                    ft1.getEnd().setNext1(ft2.getStart());
                    ft1.getEnd().setState1("ε");
                    NFAToDFA.dfas.add(new DFA(ft1.getEnd().getId(), ft1.getEnd().getState1().charAt(0), ft2.getStart().getId()));
                    Main.stack.push(new FromTo(ft1.getStart(), ft2.getEnd()));
                }
                case 2 -> {
                    ft1 = Main.stack.pop();
                    ft1.getEnd().setNext1(ft1.getStart());
                    ft1.getEnd().setState1("ε");
                    NFAToDFA.dfas.add(new DFA(ft1.getEnd().getId(), ft1.getEnd().getState1().charAt(0), ft1.getStart().getId()));
                    start = new NFA(id++);
                    end = new NFA(id++);
                    start.setNext1(ft1.getStart());
                    start.setState1("ε");
                    NFAToDFA.dfas.add(new DFA(start.getId(), start.getState1().charAt(0), ft1.getStart().getId()));
                    start.setNext2(end);
                    start.setState2("ε");
                    NFAToDFA.dfas.add(new DFA(start.getId(), start.getState2().charAt(0), end.getId()));
                    ft1.getEnd().setNext2(end);
                    ft1.getEnd().setState2("ε");
                    NFAToDFA.dfas.add(new DFA(ft1.getEnd().getId(), ft1.getEnd().getState2().charAt(0), end.getId()));
                    Main.stack.push(new FromTo(start, end));
                }
            }
        }

        getDotFormat(Main.stack.peek().getStart());
        String dotFormat = "node[shape=plaintext];" + Main.stack.peek().getStart().getId() + "[shape=circle];\"\"->" + Main.stack.peek().getStart().getId() + "[label = start];node[shape=circle];rankdir=LR;" + Main.stack.peek().getEnd().getId() + "[shape=doublecircle];" + str;
        DrawNFA(dotFormat);

    }

    public static void RemoveSuffixToNFA() {
        str = "";
        set = new HashSet<>();

    }

    //获取画图表达式
    public static void getDotFormat(NFA start) {
        set.add(start);
        if (start.getNext1() != null) {
            str += start.getId() + "->" + start.getNext1().getId() + "[label=" + start.getState1() + "];";
            if (!set.contains(start.getNext1()))
                getDotFormat(start.getNext1());
        }
        if (start.getNext2() != null) {
            str += start.getId() + "->" + start.getNext2().getId() + "[label=" + start.getState2() + "];";
            if (!set.contains(start.getNext2()))
                getDotFormat(start.getNext2());
        }

    }

    //画图
    public static void DrawNFA(String dotFormat) {
        Graphviz gv = new Graphviz();
        gv.addln(gv.start_graph());
        gv.add(dotFormat);
        gv.addln(gv.end_graph());
        // png为输出格式，还可改为pdf，gif，jpg等
        String type = "png";
        // gv.increaseDpi();
        gv.decreaseDpi();
        gv.decreaseDpi();
        File out = new File("NFA" + "." + type);
        gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
    }
}
