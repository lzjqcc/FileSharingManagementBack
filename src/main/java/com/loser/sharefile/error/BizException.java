package com.loser.sharefile.error;

/**
 * 业务异常
 */
public class BizException extends RuntimeException {
    private static final long serialVersionUID = 8690568905643222003L;
    private String code;
    private String errorMsg;

    public BizException(String message, String code, String errorMsg) {
        super(message);
        this.code = code;
        this.errorMsg = errorMsg;
    }

    public String getCode() {
        return code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
