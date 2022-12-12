package ru.akirakozov.sd.parser.visitor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.akirakozov.sd.parser.exception.CalcVisitorException;
import ru.akirakozov.sd.parser.tokenizer.BraceToken;
import ru.akirakozov.sd.parser.tokenizer.NumberToken;
import ru.akirakozov.sd.parser.tokenizer.OperationToken;
import ru.akirakozov.sd.parser.tokenizer.Token;

import java.util.List;
import java.util.stream.Stream;

import static ru.akirakozov.sd.parser.tokenizer.OperationToken.*;

public class CalcVisitorTest {
    @ParameterizedTest(name = "Test {0}")
    @MethodSource("provideCalcVisitorValidTest")
    public void calcVisitorValidTest(List<Token> tokens, int expected) {
        System.out.println("=== Testing " + tokens + " ===");
        int actual = new CalcVisitor().calculatePostfix(tokens);
        Assertions.assertEquals(expected, actual);
    }

    private static Stream<Arguments> provideCalcVisitorValidTest() {
        return Stream.of(
                Arguments.of(List.of(new NumberToken(1)), 1),
                Arguments.of(List.of(new NumberToken(2), new NumberToken(2), ADD), 4),
                Arguments.of(List.of(new NumberToken(5), new NumberToken(12), SUBTRACT), -7),
                Arguments.of(List.of(new NumberToken(11), new NumberToken(9), MULTIPLY), 99),
                Arguments.of(List.of(new NumberToken(7), new NumberToken(5), DIVIDE), 1),
                Arguments.of(List.of(new NumberToken(1000), new NumberToken(7), SUBTRACT), 993),
                Arguments.of(List.of(new NumberToken(2), new NumberToken(3), new NumberToken(4),
                        SUBTRACT, SUBTRACT), 3)
        );
    }

    @ParameterizedTest(name = "Test {0}")
    @MethodSource("provideCalcVisitorInvalidTest")
    public void calcVisitorInvalidTest(List<Token> tokens) {
        System.out.println("=== Testing " + tokens + " ===");
        Assertions.assertThrows(CalcVisitorException.class, () -> {
            try {
                new CalcVisitor().calculatePostfix(tokens);
            } catch (CalcVisitorException e) {
                System.err.println(e.getMessage());
                throw e;
            }
        });
    }

    private static Stream<Arguments> provideCalcVisitorInvalidTest() {
        return Stream.of(
                Arguments.of(List.of(new NumberToken(2), OperationToken.ADD)),
                Arguments.of(List.of()),
                Arguments.of(List.of(OperationToken.DIVIDE)),
                Arguments.of(List.of(new NumberToken(1), new NumberToken(2))),
                Arguments.of(List.of(new NumberToken(1), new NumberToken(2), new NumberToken(3), OperationToken.ADD)),
                Arguments.of(List.of(BraceToken.LEFT, new NumberToken(2), BraceToken.RIGHT))
        );
    }
}
