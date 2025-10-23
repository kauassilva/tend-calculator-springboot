package com.learning.tend_calculator.parse;

import com.learning.tend_calculator.expression.*;

import java.util.*;

import static com.learning.tend_calculator.parse.TokenType.ADD;

public class ExpressionParser {

    private static final Set<String> FUNCTIONS = Set.of("sen", "cos", "tg", "exp", "log", "abs");

    public Expression parse(String input) {
        String cleaned = input.replace(" ", "");
        List<Token> tokens = tokenize(cleaned);
        return buildAST(tokens);
    }

    private List<Token> tokenize(String cleaned) {
        List<Token> output = new ArrayList<>();
        int i = 0;
        int n = cleaned.length();

        while (i < n) {
            char ch = cleaned.charAt(i);

            if (Character.isDigit(ch) || ch == ',') {
                int j = i + 1;

                while (j < n && (Character.isDigit(cleaned.charAt(j)) || cleaned.charAt(j) == ',')) {
                    j++;
                }

                output.add(new Token(TokenType.NUMBER, cleaned.substring(i, j)));
                i = j;
                continue;
            }
            if (Character.isLetter(ch)) {
                int j = i + 1;

                while (j < n && Character.isLetterOrDigit(cleaned.charAt(j))) {
                    j++;
                }

                String name = cleaned.substring(i, j);
                output.add(new Token(TokenType.IDENTIFIER, name));
                i = j;
                continue;
            }

            switch (ch) {
                case '+':
                    output.add(new Token(ADD, "+"));
                    break;
                case '-':
                    output.add(new Token(TokenType.SUBTRACT, "-"));
                    break;
                case '*':
                    output.add(new Token(TokenType.MULTIPLY, "*"));
                    break;
                case '/':
                    output.add(new Token(TokenType.DIVIDE, "/"));
                    break;
                case '^':
                    output.add(new Token(TokenType.POWER, "^"));
                    break;
                case '(':
                    output.add(new Token(TokenType.LEFT_PARENTHESES, "("));
                    break;
                case ')':
                    output.add(new Token(TokenType.RIGHT_PARENTHESES, ")"));
                    break;
                default:
                    throw new IllegalArgumentException("Caractere inválido: " + ch);
            }

            i++;
        }

        return output;
    }

    // Shunting Yard
    private Expression buildAST(List<Token> tokens) {
        Deque<Token> opStack = new ArrayDeque<>();
        Deque<Expression> outStack = new ArrayDeque<>();

        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);

            switch (token.type()) {
                case NUMBER -> outStack.push(new Constant(Double.parseDouble(token.text())));
                case IDENTIFIER -> {
                    if (isFunction(token.text()) && i + 2 < tokens.size() && tokens.get(i + 1).type() == TokenType.LEFT_PARENTHESES) {
                        int start = i + 2;
                        int paren = 1;
                        int j = start;

                        while (j < tokens.size() && paren > 0) {
                            if (tokens.get(j).type() == TokenType.LEFT_PARENTHESES)
                                paren++;
                            else if (tokens.get(j).type() == TokenType.RIGHT_PARENTHESES)
                                paren--;

                            j++;
                        }

                        if (paren != 0)
                            throw new IllegalArgumentException("Parênteses não balanceados em função");

                        List<Token> argTokens = tokens.subList(start, j - 1);
                        Expression argExpr = buildAST(argTokens);
                        outStack.push(new FunctionCall(token.text(), List.of(argExpr)));
                        i = j - 1;
                    } else {
                        outStack.push(new Variable(token.text()));
                    }
                }
                case ADD, SUBTRACT, MULTIPLY, DIVIDE, POWER -> {
                    while (!opStack.isEmpty() && precedence(opStack.peek()) >= precedence(token)) {
                        popOperator(opStack, outStack);
                    }

                    opStack.push(token);
                }
                case LEFT_PARENTHESES -> opStack.push(token);
                case RIGHT_PARENTHESES -> {
                    while (!opStack.isEmpty() && opStack.peek().type() != TokenType.LEFT_PARENTHESES)
                        popOperator(opStack, outStack);

                    if (opStack.isEmpty())
                        throw new IllegalArgumentException("Parênteses não balanceados");

                    opStack.pop();
                }
            }
        }

        while (!opStack.isEmpty()) {
            if (opStack.peek().type() == TokenType.LEFT_PARENTHESES)
                throw new IllegalArgumentException("Parênteses não balanceados");

            popOperator(opStack, outStack);
        }

        if (outStack.size() != 1)
            throw new IllegalArgumentException("Expressão inválida");

        return outStack.pop();
    }

    private void popOperator(Deque<Token> opStack, Deque<Expression> outStack) {
        Token op = opStack.pop();

        if (outStack.size() < 2)
            throw new IllegalArgumentException("Operador sem operandos suficientes");

        Expression right = outStack.pop();
        Expression left = outStack.pop();

        Operator operator = switch (op.type()) {
            case ADD -> Operator.ADD;
            case SUBTRACT -> Operator.SUBTRACT;
            case MULTIPLY -> Operator.MULTIPLY;
            case DIVIDE -> Operator.DIVIDE;
            case POWER -> Operator.POWER;
            default -> throw new IllegalArgumentException("Operador inválido");
        };

        outStack.push(new BinaryOperator(left, right, operator));
    }

    private Integer precedence(Token token) {
        return switch (token.type()) {
            case POWER -> 4;
            case MULTIPLY, DIVIDE -> 3;
            case ADD, SUBTRACT -> 2;
            default -> 0;
        };
    }

    private boolean isFunction(String text) {
        return FUNCTIONS.contains(text.toLowerCase());
    }

}
