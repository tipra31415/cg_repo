package com.cg.exception;

public class InvalidLoanAmountException extends RuntimeException {
    public InvalidLoanAmountException(String msg) {
        super(msg);
    }
}

