package ru.akirakozov.sd.parser.exception;

public class ParserVisitorException extends RuntimeException {
    public ParserVisitorException(String message) {
        super(message);
    }

    public ParserVisitorException(String message, Throwable cause) {
        super(message, cause);
    }
}
