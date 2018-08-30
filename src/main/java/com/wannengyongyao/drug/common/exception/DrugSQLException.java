package com.wannengyongyao.drug.common.exception;

public class DrugSQLException extends RuntimeException {
    public DrugSQLException(String message, Throwable cause) {
        super(message, cause);
    }

    public DrugSQLException(String message) {
        super(message);
    }
}
