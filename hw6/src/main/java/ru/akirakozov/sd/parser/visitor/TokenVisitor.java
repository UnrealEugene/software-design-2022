package ru.akirakozov.sd.parser.visitor;

import ru.akirakozov.sd.parser.tokenizer.*;

public interface TokenVisitor {
    void visit(BraceToken token);
    void visit(NumberToken token);
    void visit(OperationToken token);
}
