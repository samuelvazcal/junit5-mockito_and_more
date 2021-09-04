package org.aguzman.test.springboot.app.exceptions;

public class NotEnoughFundsException extends RuntimeException{
    public NotEnoughFundsException(String message) {
        super(message);
    }
}
