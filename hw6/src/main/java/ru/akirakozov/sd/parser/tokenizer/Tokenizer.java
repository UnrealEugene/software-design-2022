package ru.akirakozov.sd.parser.tokenizer;

import ru.akirakozov.sd.parser.exception.TokenizerException;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private final String input;
    private final List<Token> tokens;

    private int index;
    private State state;

    public Tokenizer(String input) {
        this.input = input;
        this.state = new StartState();
        this.tokens = new ArrayList<>();
    }

    private void changeState(State state) {
        this.state = state;
    }

    private void createToken(Token token) {
        tokens.add(token);
    }

    private void next() {
        index++;
    }

    public List<Token> getTokens() throws TokenizerException {
        while (!(state instanceof EOFState)) {
            state.action();
        }
        return tokens;
    }

    private interface State {
        default void action() throws TokenizerException {
            // No operation
        }
    }

    private class StartState implements State {
        @Override
        public void action() {
            if (index >= input.length()) {
                changeState(new EOFState());
                return;
            }

            char curChar = input.charAt(index);
            if (Character.isWhitespace(curChar)) {
                next();
                return;
            }
            if (Character.isDigit(curChar)) {
                changeState(new NumberState());
                return;
            }
            switch (curChar) {
                case '+' -> createToken(OperationToken.ADD);
                case '-' -> createToken(OperationToken.SUBTRACT);
                case '*' -> createToken(OperationToken.MULTIPLY);
                case '/' -> createToken(OperationToken.DIVIDE);
                case '(' -> createToken(BraceToken.LEFT);
                case ')' -> createToken(BraceToken.RIGHT);
                default -> {
                    changeState(new ErrorState(new TokenizerException(
                            String.format("Unexpected symbol '%c' at position %d", curChar, index))));
                    return;
                }
            }
            next();
        }
    }

    private class NumberState implements State {
        private final StringBuffer buffer = new StringBuffer();

        @Override
        public void action() {
            if (index < input.length()) {
                char curChar = input.charAt(index);
                if (Character.isDigit(curChar)) {
                    buffer.append(curChar);
                    next();
                    return;
                }
            }
            try {
                createToken(new NumberToken(Integer.parseInt(buffer.toString())));
                changeState(new StartState());
            } catch (NumberFormatException e) {
                changeState(new ErrorState(new TokenizerException(
                        String.format("Number '%s' is too big at position %d", buffer, index - buffer.length()), e)));
            }
        }
    }

    private static class EOFState implements State {

    }

    private static class ErrorState implements State {
        private final TokenizerException exception;

        private ErrorState(TokenizerException exception) {
            this.exception = exception;
        }

        @Override
        public void action() throws TokenizerException {
            throw exception;
        }
    }
}
