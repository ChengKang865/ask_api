package com.autoask.common.exception;

/**
 * @author hyy
 * @craete 2016/7/22 16:58
 */
public class ApiException extends Exception {
    private static final long serialVersionUID = 1L;

    public ApiException() {
        super();
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ApiException(Throwable throwable) {
        super(throwable);
    }

}
