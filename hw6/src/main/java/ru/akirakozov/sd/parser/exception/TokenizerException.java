package ru.akirakozov.sd.parser.exception;

public class TokenizerException extends Exception {
    public TokenizerException(String message) {
        super(message);
    }

    public TokenizerException(String message, Throwable cause) {
        super(message, cause);
    }
}
