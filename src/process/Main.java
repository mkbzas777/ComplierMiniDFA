package process;

import entity.FromTo;
import process.DFAToMiniDFA;
import process.InfixToSuffix;
import process.NFAToDFA;
import process.SuffixToNFA;

import java.util.ArrayList;

import java.util.Stack;

public class Main {

    public static Stack<FromTo> stack = new Stack<>();

    public static ArrayList<Character> letter = new ArrayList<>();

    //判断是否为字母
    public static boolean isLetter(char c){
        if((c >= 'a' && c <='z')||(c >= 'A' && c <='Z'))
            return true;
        return false;
    }

    //格式化输入
    public static String Format(String str){
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(str);
        if(isLetter(stringBuffer.charAt(0)))
            letter.add(stringBuffer.charAt(0));
        for(int i = 1;i < stringBuffer.length() ; i++)
        {
            if(isLetter(stringBuffer.charAt(i))){
                if (!letter.contains(stringBuffer.charAt(i)))
                    letter.add(stringBuffer.charAt(i));
                if(isLetter(stringBuffer.charAt(i-1)) ||(stringBuffer.charAt(i-1) == ')'||stringBuffer.charAt(i-1) == '*'))
                    stringBuffer.insert(i,"·");
                if(i !=stringBuffer.length()-1 && stringBuffer.charAt(i+1) == '(')
                    stringBuffer.insert(i+1,"·");
            }else if(stringBuffer.charAt(i-1) == '*' && stringBuffer.charAt(i) == '('){
                stringBuffer.insert(i,"·");
            }
        }
        return stringBuffer.toString();
    }
    public static String input ="";
    public static void main(String[] args) {
//        input = "ab|c(d*|a)";
        input = "(a*|b*)b(ba)*";
//        input = "a*|b*";
//        String str = "b*a";
        input = Format(input);
        System.out.println(input);
        String suffix = InfixToSuffix.Convert(input);
        System.out.println(suffix);
        SuffixToNFA.Convert(suffix);
        NFAToDFA.Convert();
        DFAToMiniDFA.Convert();
    }
}
