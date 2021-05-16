import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

//转换为逆波兰表达式
public class InfixToSuffix {
    public static final String order = "|·*";
    public static int GetPriority(char a) {
        return order.indexOf(a);
    }

    public static String Convert(@NotNull String s) {
        Queue<Character> letter = new LinkedList<>();
        Stack<Character> operator = new Stack<>();
        int i = 0;
        while (i < s.length()) {

            char c = s.charAt(i);
            if ((c >= 'a' && c <= 'z')||(c >= 'A' && c <='Z')) {
                letter.add(c);
                ++i;
            } else if (c == '(') {
                operator.push(c);
                ++i;
            } else if (c == '|' || c == '*' || c == '·') {
                if(operator.isEmpty()||operator.peek()=='('||GetPriority(c)>GetPriority(operator.peek())){
                    operator.push(c);
                    ++i;
                } else
                    letter.add(operator.pop());
            } else if(c == ')'){
                while (operator.peek() != '(')
                    letter.add(operator.pop());
                operator.pop();
                ++i;
            }
        }
        while (!operator.isEmpty()){
            letter.add(operator.pop());
        }
        String result = "";
        while (!letter.isEmpty()) {
            result += letter.poll();
        }
        return result;
    }
}
