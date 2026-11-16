package com.example.LogiTrack.exception;

public class DatabaseRuleException extends RuntimeException {
    public DatabaseRuleException(String mensaje) {
        super(mensaje);
    }
}
