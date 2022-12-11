package ru.akirakozov.sd.parser;

import ru.akirakozov.sd.parser.exception.TokenizerException;
import ru.akirakozov.sd.parser.tokenizer.Token;
import ru.akirakozov.sd.parser.tokenizer.Tokenizer;
import ru.akirakozov.sd.parser.visitor.CalcVisitor;
import ru.akirakozov.sd.parser.visitor.ParserVisitor;
import ru.akirakozov.sd.parser.visitor.PrintVisitor;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws TokenizerException, IOException {
        Scanner sc = new Scanner(System.in);

        List<Token> tokens = new Tokenizer(sc.nextLine()).getTokens();

        PrintVisitor printVisitor = new PrintVisitor(System.out, System.out.charset());

        printVisitor.printTokens(tokens);
        System.out.println();

        List<Token> postfixTokens = new ParserVisitor().infixToPostfix(tokens);

        printVisitor.printTokens(postfixTokens);
        System.out.println();
        System.out.println(new CalcVisitor().calculatePostfix(postfixTokens));
    }
}