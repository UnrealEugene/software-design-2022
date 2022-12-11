package ru.akirakozov.sd.parser.exception;

public class CalcVisitorException extends RuntimeException {
    public CalcVisitorException(String message) {
        super(message);
    }

    public CalcVisitorException(String message, Throwable cause) {
        super(message, cause);
    }
}
