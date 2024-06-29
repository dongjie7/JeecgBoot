package org.jeecg.modules.estar.nd.exception;

import org.jeecg.modules.estar.nd.file.ResultCodeEnum;

import lombok.Data;

/**
 * 自定义全局异常类
 */
@Data
public class EstarException extends RuntimeException {
    private Integer code;

    public EstarException(String message) {
        super(message);
        this.code = ResultCodeEnum.UNKNOWN_ERROR.getCode();
    }

    public EstarException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public EstarException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "EstarException{" + "code=" + code + ", message=" + this.getMessage() + '}';
    }
}