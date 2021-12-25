package advent.of.code;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class day10 {

    public static Character inverseMethod(Character c) {
        switch(c) {
            case '[':
                return ']';
            case '{':
                return '}';
            case '<':
                return '>';
            case '(':
                return ')';
        }
        return null;
    }

    // L2 language with grammar
    // S -> # (epsilon symbol)
    // S -> SS
    // S -> <S>
    // S -> (S)
    // S -> {S}
    // S -> [S]
    public static void main(String[] args) {
        String input = Utils.constructStringFromFile("day10.txt");

        System.out.println(input);

        Stack<Character> stack = new Stack<>();
        List<Character> errors = new ArrayList<>();

        // lines
        String[] lines = input.split("\n");

        for (int i = 0; i < lines.length; i++) {
            // line
            for (int j = 0; j < lines[i].length(); j++) {
                char token = lines[i].charAt(j);

                switch(token) {
                    case '[':
                        stack.push('[');
                        continue;
                    case '{':
                        stack.push('{');
                        continue;
                    case '<':
                        stack.push('<');
                        continue;
                    case '(':
                        stack.push('(');
                        continue;
                }

                // 1.case token='[' <-> stack.peek()=']' -> reduce
                // 2.case token='(' <-> stack.peek()=')' -> reduce
                // 3.case token='<' <-> stack.peek()='>' -> reduce
                // 4.case token='{' <-> stack.peek()='}' -> reduce
                if (token == ']' && stack.peek() == '[') {
                    stack.pop();
                } else if (token == ')' && stack.peek() == '(') {
                    stack.pop();
                } else if (token == '>' && stack.peek() == '<') {
                    stack.pop();
                } else if (token == '}' && stack.peek() == '{') {
                    stack.pop();
                } else {
                    System.out.println("Syntax error - Token: " + token + " error. Excepted: " + inverseMethod(stack.peek()) + " -----|i:" + i + "),(j:" + j + ")|");
                    errors.add(token);
                    break;
                }
            }
        }

        // counting
        // ): 3 points.
        // ]: 57 points.
        // }: 1197 points.
        // >: 25137 points.
        int sum = 0;
        for (Character error : errors) {
            switch (error) {
                case ']':
                    sum += 57;
                    break;
                case '}':
                    sum += 1197;
                    break;
                case '>':
                    sum += 25137;
                    break;
                case ')':
                    sum += 3;
            }
        }
        System.out.println("Result:" + sum);
    }
}
