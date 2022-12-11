package ru.akirakozov.sd.parser.tokenizer;

import ru.akirakozov.sd.parser.visitor.TokenVisitor;

public enum BraceToken implements Token {
    LEFT("("), RIGHT(")");

    private final String str;

    BraceToken(String str) {
        this.str = str;
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
