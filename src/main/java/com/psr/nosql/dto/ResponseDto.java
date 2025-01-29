package com.psr.nosql.dto;

import com.psr.nosql.constant.MessageConstant;
import lombok.Getter;

@Getter
public class ResponseDto<T> {

    private Boolean isSuccess;
    private String message;
    private T data;

    public ResponseDto() {
        this.isSuccess = true;
        this.message = MessageConstant.SUCCESS;
    }

    public ResponseDto(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public ResponseDto(T data) {
        this.isSuccess = true;
        this.message = MessageConstant.SUCCESS;
        this.data = data;
    }
}
