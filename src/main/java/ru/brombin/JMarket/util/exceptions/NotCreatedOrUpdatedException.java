package ru.brombin.JMarket.util.exceptions;

public class NotCreatedOrUpdatedException extends  RuntimeException {
    public NotCreatedOrUpdatedException(String message) {
        super(message);
    }
}
