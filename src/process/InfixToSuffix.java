package process;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

//转换为逆波兰表达式
public class InfixToSuffix {

    public static final String order = "|·*";//符号优先级

    public static int GetPriority(char a) {
        return order.indexOf(a);
    }

    public static String Convert(@NotNull String s) {
        Queue<Character> letter = new LinkedList<>();
        Stack<Character> operator = new Stack<>();
        int i = 0;
        /*
        逐个读取输入的字符串
         */
        while (i < s.length()) {

            char c = s.charAt(i);

            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) { //如果是字母直接入栈
                letter.add(c);
                ++i;
            } else if (c == '(') {//如果是左括号，直接入栈
                operator.push(c);
                ++i;
            } else if (c == '|' || c == '*' || c == '·') {//如果是运算符号
                /*
                1.判断栈是否为空，如果是空，则直接入栈
                2.判断栈顶是否是左括号，如果是左括号，则直接入栈
                3.如果当前运算符比栈顶运算符优先级高，则当前运算符入栈
                4.如果以上都不是，则出栈栈顶运算符
                 */
                if (operator.isEmpty() || operator.peek() == '(' || GetPriority(c) > GetPriority(operator.peek())) {
                    operator.push(c);
                    ++i;
                } else
                    letter.add(operator.pop());
            } else if (c == ')') {//如果是右括号，则一直弹出运算符，直到碰到左括号
                while (operator.peek() != '(')
                    letter.add(operator.pop());
                operator.pop();
                ++i;
            }
        }
        //将栈中剩余运算符取出
        while (!operator.isEmpty()) {
            letter.add(operator.pop());
        }
        StringBuilder result = new StringBuilder();
        while (!letter.isEmpty()) {
            result.append(letter.poll());
        }
        return result.toString();
    }
}
