import java.util.Scanner;

/* Idea programu
 *  Program konwertuje wyrazenia INF na ONP (RPN), na zasadzie:
 * - jesli operanda to na wyjscie
 * - jesli operator to na stos, przy czym jesli jego priorytet jest mniejszy niz wyrazenia na gorze stosu, to wyrzucamy
 * elementy ze stosu tak dlugo, az ten warunek nie bedzie falszywy
 * - dla nawiasow nawias ( traktowany jako znacznik; po napotkaniu na ) nalezy wyrzucic na wyjscie stos w obrebie tych
 * nawiasow
 * - na koniec wszystko ze stosu na wyjscie
 *
 * Program konwertuje wyrazenia ONP (RPN) na INF, na zasadzie:
 * - jesli operanda, to na stos
 * - jesli operator, to zdejmujemy ze stosu dwa gorne wyrazenia i stosujemy na nich operator, po czym odkladamy na gore
 * stosu, przy czym jesli ktores z wyrazen ma priorytet mniejszy niz operatora, wyrazenie bierzemy w nawias
 * - na koniec wszystko ze stosu na wyjscie
 * */


/* Stos */
class Stack<Type> {
    private Stack<Type> previous;
    private Type value;

    Stack() {
    }

    Stack(Type value) {
        this.value = value;
    }

    Stack(Stack<Type> previous, Type value) {
        this.previous = previous;
        this.value = value;
    }

    public void push(Type value) {
        this.previous = new Stack<>(this.previous, this.value);
        this.value = value;
    }

    public Type pop() throws IllegalAccessException {
        if (this.isEmpty()) {
            throw new IllegalAccessException("error");
        }
        Type top = this.value;
        this.value = this.previous.value;
        this.previous = this.previous.previous;
        return top;
    }

    public Type peek() {
        return this.value;
    }

    public boolean isEmpty() {
        return this.previous == null;
    }

    public int size() {
        return this.isEmpty() ? 0 : 1 + this.previous.size();
    }
}

public class Source {

    public static Scanner scanner = new Scanner(System.in);
    public static String operators = "()!~^*/%+-<>?&|=";
    public static String operatorsRPN = "!~^*/%+-<>?&|=";
    public static String operands = "qwertyuiopasdfghjklzxcvbnm";
    public static String leftAssociative = "()*/%+-<>?&|";
    public static String rightAssociative = "!~^=";

    /* Priorytet */
    public static int priority(String input) {
        if (input.equals("")) return -1;
        String allowedInput = operators + operands;
        if (allowedInput.contains(input)) {
            if (input.equals("=")) return 0;
            if (input.equals("|")) return 1;
            if (input.equals("&")) return 2;
            if (input.equals("?")) return 3;
            if (input.equals("<") || input.equals(">")) return 4;
            if (input.equals("+") || input.equals("-")) return 5;
            if (input.equals("*") || input.equals("/") || input.equals("%")) return 6;
            if (input.equals("^")) return 7;
            if (input.equals("~") || input.equals("!")) return 8;
            if (operands.contains(input)) return 9;
        }
        return -1;
    }

    /* Lacznosc */
    public static int associativity(String input) {
        if (input.equals("")) return -1;
        if (leftAssociative.contains(input)) return 0;
        if (rightAssociative.contains(input)) return 1;
        return -1;
    }

    /* Czyszczenie inputu */
    public static String reduce(String input, String allowedInput) {
        String output = "";
        for (int i = 0; i < input.length(); i++) {
            if (allowedInput.contains(String.valueOf(input.charAt(i)))) {
                output += String.valueOf(input.charAt(i));
            }
        }
        return output;
    }

    /* Konwersja INF->ONP */
    public static String INFtoRPN(String in) throws IllegalAccessException {
        in = reduce(in, operators + operands);
        String out = "";
        Stack<String> stack = new Stack<>();
        int state = 0;
        int parenthesis = 0;
        int noOperands = 0;
        int noOperatorsO2 = 0;
        for (int i = 0; i < in.length(); i++) {
            String token = String.valueOf(in.charAt(i));
            if (operators.contains(token)) {
                if (priority(token) == 8) { //o1
                    if (state == 1) {
                        return "error";
                    }
                    if (state == 0) state = 2;
                }
                if ((priority(token) > -1 && priority(token) < 8) && (!token.equals("(") && !token.equals(")"))) { //o2
                    noOperatorsO2++;
                    if (state == 0 || state == 2) {

                        return "error";
                    }
                    state = 0;
                }

                if (token.equals("(")) { // (
                    if (state == 1) {
                        return "error";
                    }
                    if (state == 2) state = 0;
                    parenthesis++;
                    stack.push(token);
                    continue;
                }
                if (token.equals(")")) { // )
                    if (state == 0 || state == 2) {
                        return "error";
                    }
                    if (parenthesis > 0) parenthesis--;
                    while (!stack.isEmpty() && !stack.peek().equals("(")) {
                        out += stack.pop() + " ";
                    }
                    if (!stack.isEmpty() && stack.peek().equals("(")) stack.pop(); //wywalamy '('
                    continue;
                }

                int tokenAssoc = associativity(token);
                while (!stack.isEmpty() && operators.contains(String.valueOf(stack.peek())) &&
                        ((tokenAssoc == 0 && priority(token) <= priority(String.valueOf(stack.peek()))) ||
                                (tokenAssoc == 1 && priority(token) < priority(String.valueOf(stack.peek()))))) {
                    out += stack.pop() + " ";
                }
                stack.push(token);
                continue;
            }

            if (operands.contains(token)) { //z
                noOperands++;
                if (state == 1 && i < (in.length() - 1)) {
                    return "error";
                }
                if (state == 0 || state == 2) state = 1;
                out += token + " ";
                while (!stack.isEmpty() && priority(String.valueOf(stack.peek())) == 8) {
                    String temp = String.valueOf(stack.pop());
                    out += temp + " ";
                }
            }
        }
        if (state != 1 || parenthesis != 0 || noOperands <= noOperatorsO2 || (noOperands > 1 && noOperatorsO2 == 0)) {
            //czy automat dziala, czy dobre nawiasy, czy dobry stosunek operatorow i operandow
            return "error";
        }
        if (!stack.isEmpty()) out += stack.pop();
        while (!stack.isEmpty()) {
            out += " " + stack.pop();
        }
        return out;
    }


    /* Konwersja ONP->INF */
    public static String RPNtoINF(String in) throws IllegalAccessException {
        in = reduce(in, operands + operatorsRPN);
        String out = "";
        int noOperatorsO2 = 0;
        int noOperands = 0;
        Stack<String> stack = new Stack();
        Stack<Integer> priorityStack = new Stack();
        for (int i = 0; i < in.length(); i++) {
            String token = String.valueOf(in.charAt(i));

            if (operands.contains(token)) {
                noOperands++;
                stack.push(token);
                priorityStack.push(priority(token));
            }

            if (operators.contains(token)) {
                if (priority(token) < 8) noOperatorsO2++;
                if (!stack.isEmpty()) {
                    String temp1 = stack.pop();
                    int temp2 = priorityStack.pop();
                    if (temp2 <= priority(token) && temp2 != 8) { //gdy trzeba oblozyc nawiasami
                        temp1 = "( " + temp1 + " )";
                    }
                    if (token.equals("~") || token.equals("!")) {
                        stack.push(token + " " + temp1);
                        priorityStack.push(priority(token));
                        continue;
                    }
                    if (!stack.isEmpty()) {
                        String temp3 = stack.pop();
                        int temp4 = priorityStack.pop();
                        if (temp4 < priority(token)) { //gdy trzeba oblozyc nawiasami
                            temp3 = "( " + temp3 + " )";
                        }
                        stack.push(temp3 + " " + token + " " + temp1);
                        priorityStack.push(priority(token));
                    } else {
                        return "error";
                    }
                } else {
                    return "error";
                }
            }
        }
        while (!stack.isEmpty()) {
            out += stack.pop();
        }
        if (noOperands != (noOperatorsO2 + 1)) { //czy poprawny stosunek operatorow do operandow
            return "error";
        }
        return out;
    }


    public static void main(String[] args) throws IllegalAccessException {
        /* Input */
        if (!scanner.hasNextInt()) return;
        int HowManyLines = scanner.nextInt();
        for (int line = 0; line < HowManyLines; line++) {
            String type = scanner.next();
            String input = scanner.nextLine();
            if (input.length() < 2 || input.length() > 252) continue;
            if (type.equals("INF:")) {
                System.out.println("ONP: " + INFtoRPN(input));
            }
            if (type.equals("ONP:")) {
                System.out.println("INF: " + RPNtoINF(input));
            }
        }
    }
}



/* test0.in
8
INF: ~~~~(a*b)
INF: a*(b*c)
INF: a*(b*(c*d))
INF: a=b=c=d
INF: a=b^c^d
INF: y-a*(b + x^v - e) / c + d / (~ p)
INF: b=(a+b)*c^d
INF: ((m+n+(b-v+(c*x+z/a)))^s^d=f)^g=h
*
* test0.out
ONP: a b * ~ ~ ~ ~
ONP: a b c * *
ONP: a b c d * * *
ONP: a b c d = = =
ONP: a b c d ^ ^ =
ONP: y a b x v ^ + e - * c / - d p ~ / +
ONP: b a b + c d ^ * =
ONP: m n + b v - c x * z a / + + + s d ^ ^ f = g ^ h =
*
* */
