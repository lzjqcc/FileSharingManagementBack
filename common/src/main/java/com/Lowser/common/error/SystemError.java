package com.Lowser.common.error;

public class SystemError extends RuntimeException {
    private String code;
    private static final long serialVersionUID = -2820425925941747027L;
    public SystemError(String code, String errorMsg) {
        super(errorMsg);
        this.code = code;
    }
    public SystemError(String errorMsg) {
        super(errorMsg);
    }
    public SystemError(Throwable throwable, String... errorMsg) {
        super(getMessage(errorMsg) , throwable);

    }

    public String getCode() {
        return code;
    }

    private static String getMessage(String... errorMsg) {
        StringBuilder builder = new StringBuilder();
        for (String msg : errorMsg) {
            builder.append(msg);
        }
        return builder.toString();
    }
}
