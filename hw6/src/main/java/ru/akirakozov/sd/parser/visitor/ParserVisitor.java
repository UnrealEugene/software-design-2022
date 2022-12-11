package ru.akirakozov.sd.parser.visitor;

import ru.akirakozov.sd.parser.exception.ParserVisitorException;
import ru.akirakozov.sd.parser.tokenizer.BraceToken;
import ru.akirakozov.sd.parser.tokenizer.NumberToken;
import ru.akirakozov.sd.parser.tokenizer.OperationToken;
import ru.akirakozov.sd.parser.tokenizer.Token;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ParserVisitor implements TokenVisitor {
    private final Deque<Token> stack = new ArrayDeque<>();
    private final List<Token> result = new ArrayList<>();

    private void popStackToResult() {
        result.add(stack.removeLast());
    }

    @Override
    public void visit(BraceToken token) {
        switch (token) {
            case LEFT -> stack.add(token);
            case RIGHT -> {
                while (!stack.isEmpty()
                        && (!(stack.peekLast() instanceof BraceToken) || stack.peekLast() != BraceToken.LEFT)) {
                    popStackToResult();
                }
                if (stack.isEmpty()) {
                    throw new ParserVisitorException("Mismatched parenthesis found");
                }
                stack.removeLast();
            }
        }
    }

    @Override
    public void visit(NumberToken token) {
        result.add(token);
    }

    @Override
    public void visit(OperationToken token) {
        while (!stack.isEmpty() && stack.peekLast() instanceof OperationToken
                && ((OperationToken) stack.peekLast()).getPriority() >= token.getPriority()) {
            popStackToResult();
        }
        stack.add(token);
    }

    public List<Token> infixToPostfix(List<Token> tokens) {
        tokens.forEach(token -> token.accept(this));
        while (!stack.isEmpty()) {
            if (stack.peekLast() instanceof BraceToken) {
                throw new ParserVisitorException("Mismatched parenthesis found");
            }
            popStackToResult();
        }
        return result;
    }
}
