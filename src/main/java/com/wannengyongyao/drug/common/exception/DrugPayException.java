package com.wannengyongyao.drug.common.exception;

public class DrugPayException extends RuntimeException {
    public DrugPayException(String message, Throwable cause) {
        super(message, cause);
    }

    public DrugPayException(String message) {
        super(message);
    }
}
