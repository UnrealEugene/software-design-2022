package ru.akirakozov.sd.parser.visitor;

import ru.akirakozov.sd.parser.exception.CalcVisitorException;
import ru.akirakozov.sd.parser.tokenizer.BraceToken;
import ru.akirakozov.sd.parser.tokenizer.NumberToken;
import ru.akirakozov.sd.parser.tokenizer.OperationToken;
import ru.akirakozov.sd.parser.tokenizer.Token;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class CalcVisitor implements TokenVisitor {
    private final Deque<Integer> numberStack = new ArrayDeque<>();

    @Override
    public void visit(BraceToken token) {
        throw new CalcVisitorException("Postfix notation can't contain braces");
    }

    @Override
    public void visit(NumberToken token) {
        numberStack.add(token.getNumber());
    }

    @Override
    public void visit(OperationToken token) {
        if (numberStack.size() < 2) {
            throw new CalcVisitorException("Invalid postfix expression is given");
        }
        int a = numberStack.removeLast();
        int b = numberStack.removeLast();
        numberStack.add(token.operate(b, a));
    }

    public int calculatePostfix(List<Token> tokens) {
        tokens.forEach(token -> token.accept(this));
        if (numberStack.size() != 1) {
            throw new CalcVisitorException("Invalid postfix expression is given");
        }
        return numberStack.remove();
    }
}
