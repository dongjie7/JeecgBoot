package org.jeecg.modules.estar.nd.exception;

public class NDException extends RuntimeException {
    public NDException(Throwable cause) {
        super("统一文件操作平台（ND）出现异常", cause);
    }

    public NDException(String message) {
        super(message);
    }

    public NDException(String message, Throwable cause) {
        super(message, cause);
    }

}
