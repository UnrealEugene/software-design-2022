package ru.akirakozov.sd.parser.tokenizer;

import ru.akirakozov.sd.parser.visitor.TokenVisitor;

public class NumberToken implements Token {
    private final int number;

    public NumberToken(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return Integer.toString(number);
    }
}
