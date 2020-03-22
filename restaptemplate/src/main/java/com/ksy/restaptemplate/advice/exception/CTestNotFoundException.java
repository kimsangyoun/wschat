package com.ksy.restaptemplate.advice.exception;

public class CTestNotFoundException extends RuntimeException {
    public CTestNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public CTestNotFoundException(String msg) {
        super(msg);
    }

    public CTestNotFoundException() {
        super();
    }
}
