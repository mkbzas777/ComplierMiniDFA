package process;

import entity.FromTo;
import gui.MyFrame;

import java.util.ArrayList;
import java.util.Stack;

public class Main {

    public static Stack<FromTo> stack = new Stack<>();

    public static ArrayList<Character> letter = new ArrayList<>();
    public static String input = "";

    //判断是否为字母
    public static boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    //格式化输入
    public static String Format(String str) {
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(str);
        if (isLetter(stringBuffer.charAt(0)))
            letter.add(stringBuffer.charAt(0));
        if ((isLetter(stringBuffer.charAt(0)) && stringBuffer.charAt(1) == '('))
            stringBuffer.insert(1, "·");
        for (int i = 1; i < stringBuffer.length(); i++) {
            if (isLetter(stringBuffer.charAt(i))) {
                if (!letter.contains(stringBuffer.charAt(i)))
                    letter.add(stringBuffer.charAt(i));
                if (isLetter(stringBuffer.charAt(i - 1)) || (stringBuffer.charAt(i - 1) == ')' || stringBuffer.charAt(i - 1) == '*'))
                    stringBuffer.insert(i, "·");
                if (i != stringBuffer.length() - 1 && stringBuffer.charAt(i + 1) == '(')
                    stringBuffer.insert(i + 1, "·");

            } else if (stringBuffer.charAt(i - 1) == '*' && stringBuffer.charAt(i) == '(') {
                stringBuffer.insert(i, "·");
            } else if (stringBuffer.charAt(i - 1) == ')' && stringBuffer.charAt(i) == '(') {
                stringBuffer.insert(i, "·");
            }
        }
        return stringBuffer.toString();
    }

    public static void RemoveAll() {
        input = "";
        stack = new Stack<>();
        letter = new ArrayList<>();
        NFAToDFA.RemoveNFAToDFA();
        DFAToMiniDFA.RemoveDFAToMiniDFA();
        SuffixToNFA.RemoveSuffixToNFA();

    }

    public static void main(String[] args) {
//        "ab|c(d*|a)";
//
        MyFrame frame = new MyFrame();
//        "(a|b)*abb";
//        "a*(abb)*b(b|a)*";
//        "a*|b*";
//        "a*(abb)*";
//        "b*a";
        /*
        四个例子
        (a*|b*)b(ba)*
        (a*|b*)*a*b*
        a(a|c)b|b(ab|bc)(ba|c)
        ((a*)*|(b|a)*)cd*
         */


    }
}
