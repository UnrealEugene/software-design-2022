package ru.akirakozov.sd.parser.tokenizer;

import ru.akirakozov.sd.parser.visitor.TokenVisitor;

public interface Token {
    void accept(TokenVisitor visitor);
}
