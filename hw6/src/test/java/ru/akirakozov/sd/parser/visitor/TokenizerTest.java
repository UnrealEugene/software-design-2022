package ru.akirakozov.sd.parser.visitor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.akirakozov.sd.parser.exception.TokenizerException;
import ru.akirakozov.sd.parser.tokenizer.NumberToken;
import ru.akirakozov.sd.parser.tokenizer.Token;
import ru.akirakozov.sd.parser.tokenizer.Tokenizer;

import java.util.List;
import java.util.stream.Stream;

import static ru.akirakozov.sd.parser.tokenizer.BraceToken.LEFT;
import static ru.akirakozov.sd.parser.tokenizer.BraceToken.RIGHT;
import static ru.akirakozov.sd.parser.tokenizer.OperationToken.*;
import static ru.akirakozov.sd.parser.tokenizer.OperationToken.SUBTRACT;

public class TokenizerTest {
    @ParameterizedTest(name = "Test ''{0}''")
    @MethodSource("provideTokenizerValidTest")
    public void tokenizerValidTest(String input, List<Token> expected) throws TokenizerException {
        System.out.println("=== Testing '" + input + "' ===");
        List<Token> actual = new Tokenizer(input).getTokens();
        Assertions.assertEquals(expected, actual);
    }

    private static Stream<Arguments> provideTokenizerValidTest() {
        return Stream.of(
                Arguments.of(
                        "2 + 2 * 2",
                        List.of(new NumberToken(2), ADD, new NumberToken(2), MULTIPLY, new NumberToken(2))
                ),
                Arguments.of(
                        "  0  (  )  +  -  *  /  ",
                        List.of(new NumberToken(0), LEFT, RIGHT, ADD, SUBTRACT, MULTIPLY, DIVIDE)
                ),
                Arguments.of(
                        "(()())())))(()",
                        List.of(LEFT, LEFT, RIGHT, LEFT, RIGHT, RIGHT, LEFT, RIGHT, RIGHT, RIGHT, RIGHT, LEFT, LEFT, RIGHT)
                ),
                Arguments.of(
                        "//123",
                        List.of(DIVIDE, DIVIDE, new NumberToken(123))
                )
        );
    }

    @ParameterizedTest(name = "Test ''{0}''")
    @MethodSource("provideTokenizerInvalidTest")
    public void tokenizerInvalidTest(String input) {
        System.out.println("=== Testing '" + input + "' ===");
        Assertions.assertThrows(TokenizerException.class, () -> {
            try {
                new Tokenizer(input).getTokens();
            } catch (TokenizerException e) {
                System.err.println(e.getMessage());
                throw e;
            }
        });
    }

    private static Stream<Arguments> provideTokenizerInvalidTest() {
        return Stream.of(
                Arguments.of("sin cos"),
                Arguments.of("sqrt(1)"),
                Arguments.of("2147483648"),
                Arguments.of("3 % 2"),
                Arguments.of("10!"),
                Arguments.of("(10 - 4)^2")
        );
    }
}
