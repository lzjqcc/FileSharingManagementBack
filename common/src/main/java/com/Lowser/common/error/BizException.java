package com.Lowser.common.error;

/**
 * 业务异常
 */
public class BizException extends RuntimeException implements AbstractException{
    private static final long serialVersionUID = 8690568905643222003L;
    private String message;
    private String code;
    public BizException(String message, String code) {
        super(message);
        this. message = message;
        this.code = code;
    }
    public BizException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return message;
    }
}
