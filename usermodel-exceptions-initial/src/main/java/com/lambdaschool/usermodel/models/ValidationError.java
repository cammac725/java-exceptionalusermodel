package com.lambdaschool.usermodel.models;

public class ValidationError {

    // string or variable or column that cause the problem
    private String code;

    // explains what the problem is
    private String message;

    public ValidationError() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
