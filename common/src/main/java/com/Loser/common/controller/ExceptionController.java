package com.Loser.common.controller;

import com.Loser.common.error.BizException;
import com.Loser.common.error.ErrorEntity;
import com.Loser.common.error.SystemError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionController {
    @ResponseBody
    @ExceptionHandler(SystemError.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object systemError(SystemError systemError) {
        return new ErrorEntity(systemError.getMessage(), systemError.getCode());
    }
    @ResponseBody
    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object bizException(BizException bizException) {
        return new ErrorEntity(bizException.getMessage(), bizException.getCode());
    }

}
