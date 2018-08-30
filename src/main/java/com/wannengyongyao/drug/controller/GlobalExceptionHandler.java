package com.wannengyongyao.drug.controller;

import com.wannengyongyao.drug.common.ResultCode;
import com.wannengyongyao.drug.common.ResultObject;
import com.wannengyongyao.drug.common.exception.DrugSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value=MethodArgumentNotValidException.class)
    @ResponseBody
    public ResultObject handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String errorMsg = exception.getBindingResult().getFieldErrors().stream()
                .map(this::getErrorMessage)
                .findFirst()
                .orElse(exception.getMessage());
        logger.error(errorMsg);
        return ResultObject.fail(ResultCode.BAD_REQUEST, errorMsg);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseBody
    public ResultObject handleIllegalArgumentException(IllegalArgumentException e){
        return ResultObject.fail(ResultCode.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = DrugSQLException.class)
    @ResponseBody
    public ResultObject handleDrugSQLException(Exception e){
        logger.error("error", e);
        return ResultObject.fail(ResultCode.FAILED, e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultObject handleAllException(Exception e){
        logger.error("error", e);
        return ResultObject.fail(ResultCode.FAILED, e.getMessage());
    }

    private String getErrorMessage(FieldError fieldError){
        StringBuilder errorMessage = new StringBuilder(fieldError.getField());
        errorMessage.append("-");
        errorMessage.append(fieldError.getDefaultMessage());
        return  errorMessage.toString();
    }
}