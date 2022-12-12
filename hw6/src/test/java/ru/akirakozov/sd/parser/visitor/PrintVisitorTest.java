package ru.akirakozov.sd.parser.visitor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.akirakozov.sd.parser.tokenizer.NumberToken;
import ru.akirakozov.sd.parser.tokenizer.Token;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

import static ru.akirakozov.sd.parser.tokenizer.BraceToken.LEFT;
import static ru.akirakozov.sd.parser.tokenizer.BraceToken.RIGHT;
import static ru.akirakozov.sd.parser.tokenizer.OperationToken.*;

public class PrintVisitorTest {
    @ParameterizedTest(name = "Test {0}")
    @MethodSource("providePrintVisitorTest")
    public void printVisitorValidTest(List<Token> tokens, String expected) throws IOException {
        System.out.println("=== Testing " + tokens + " ===");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new PrintVisitor(out, StandardCharsets.UTF_8).printTokens(tokens);
        String actual = out.toString(StandardCharsets.UTF_8);
        Assertions.assertEquals(expected, actual);
    }

    private static Stream<Arguments> providePrintVisitorTest() {
        return Stream.of(
                Arguments.of(
                        List.of(new NumberToken(2), ADD, new NumberToken(2), MULTIPLY, new NumberToken(2)),
                        "2 + 2 * 2"
                ),
                Arguments.of(
                        List.of(new NumberToken(0), LEFT, RIGHT, ADD, SUBTRACT, MULTIPLY, DIVIDE),
                        "0 ( ) + - * /"
                ),
                Arguments.of(
                        List.of(new NumberToken(1), SUBTRACT, LEFT, new NumberToken(2), SUBTRACT, new NumberToken(3), RIGHT),
                        "1 - ( 2 - 3 )"
                )
        );
    }
}
