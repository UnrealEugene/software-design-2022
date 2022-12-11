package ru.akirakozov.sd.parser.tokenizer;

import ru.akirakozov.sd.parser.visitor.TokenVisitor;

import java.util.function.IntBinaryOperator;

public enum OperationToken implements Token {
    ADD("+", 1, (x, y) -> x + y),
    SUBTRACT("-", 1, (x, y) -> x - y),
    MULTIPLY("*", 2, (x, y) -> x * y),
    DIVIDE("/", 2, (x, y) -> x / y);

    private final String str;
    private final int priority;
    private final IntBinaryOperator operation;

    OperationToken(String str, int priority, IntBinaryOperator operation) {
        this.str = str;
        this.priority = priority;
        this.operation = operation;
    }

    public int getPriority() {
        return priority;
    }

    public int operate(int left, int right) {
        return operation.applyAsInt(left, right);
    }

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return str;
    }
}
