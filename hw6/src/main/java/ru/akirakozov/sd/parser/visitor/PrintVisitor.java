package ru.akirakozov.sd.parser.visitor;

import ru.akirakozov.sd.parser.tokenizer.BraceToken;
import ru.akirakozov.sd.parser.tokenizer.NumberToken;
import ru.akirakozov.sd.parser.tokenizer.OperationToken;
import ru.akirakozov.sd.parser.tokenizer.Token;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class PrintVisitor implements TokenVisitor {
    private final List<String> strTokens = new ArrayList<>();
    private final BufferedWriter writer;

    public PrintVisitor(OutputStream outputStream, Charset charset) {
        this.writer = new BufferedWriter(new OutputStreamWriter(outputStream, charset));
    }

    public PrintVisitor(OutputStream outputStream) {
        this(outputStream, Charset.defaultCharset());
    }

    @Override
    public void visit(BraceToken token) {
        strTokens.add(token.toString());
    }

    @Override
    public void visit(NumberToken token) {
        strTokens.add(token.toString());
    }

    @Override
    public void visit(OperationToken token) {
        strTokens.add(token.toString());
    }

    public void printTokens(List<Token> tokens) throws IOException {
        tokens.forEach(token -> token.accept(this));
        writer.write(String.join(" ", strTokens));
        writer.flush();
        strTokens.clear();
    }
}
