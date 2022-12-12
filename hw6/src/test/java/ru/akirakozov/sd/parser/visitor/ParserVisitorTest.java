package ru.akirakozov.sd.parser.visitor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.akirakozov.sd.parser.exception.CalcVisitorException;
import ru.akirakozov.sd.parser.exception.ParserVisitorException;
import ru.akirakozov.sd.parser.tokenizer.NumberToken;
import ru.akirakozov.sd.parser.tokenizer.Token;

import java.util.List;
import java.util.stream.Stream;

import static ru.akirakozov.sd.parser.tokenizer.BraceToken.LEFT;
import static ru.akirakozov.sd.parser.tokenizer.BraceToken.RIGHT;
import static ru.akirakozov.sd.parser.tokenizer.OperationToken.*;

public class ParserVisitorTest {
    @ParameterizedTest(name = "Test {0}")
    @MethodSource("provideParserVisitorValidTest")
    public void calcVisitorValidTest(List<Token> tokens, List<Token> expected) {
        System.out.println("=== Testing " + tokens + " ===");
        List<Token> actual = new ParserVisitor().infixToPostfix(tokens);
        Assertions.assertEquals(expected, actual);
    }

    private static Stream<Arguments> provideParserVisitorValidTest() {
        return Stream.of(
                Arguments.of(
                        List.of(new NumberToken(1)),
                        List.of(new NumberToken(1))
                ),
                Arguments.of(
                        List.of(new NumberToken(2), ADD, new NumberToken(2)),
                        List.of(new NumberToken(2), new NumberToken(2), ADD)
                ),
                Arguments.of(
                        List.of(new NumberToken(2), ADD, new NumberToken(2), MULTIPLY, new NumberToken(2)),
                        List.of(new NumberToken(2), new NumberToken(2), new NumberToken(2), MULTIPLY, ADD)
                ),
                Arguments.of(
                        List.of(new NumberToken(1), SUBTRACT, LEFT, new NumberToken(2), SUBTRACT, new NumberToken(3), RIGHT),
                        List.of(new NumberToken(1), new NumberToken(2), new NumberToken(3), SUBTRACT, SUBTRACT)
                ),
                Arguments.of(List.of(), List.of())
        );
    }

    @ParameterizedTest(name = "Test {0}")
    @MethodSource("provideParserVisitorInvalidTest")
    public void calcVisitorInvalidTest(List<Token> tokens) {
        System.out.println("=== Testing " + tokens + " ===");
        Assertions.assertThrows(ParserVisitorException.class, () -> {
            try {
                new ParserVisitor().infixToPostfix(tokens);
            } catch (ParserVisitorException e) {
                System.err.println(e.getMessage());
                throw e;
            }
        });
    }

    private static Stream<Arguments> provideParserVisitorInvalidTest() {
        return Stream.of(
                Arguments.of(List.of(LEFT, new NumberToken(2), ADD, new NumberToken(3))),
                Arguments.of(List.of(SUBTRACT, RIGHT)),
                Arguments.of(List.of(RIGHT, LEFT)),
                Arguments.of(List.of(LEFT, RIGHT, ADD, RIGHT, LEFT))
        );
    }
}

