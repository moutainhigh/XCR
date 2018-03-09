package com.yatang.xc.xcr.exceptions;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * @Author : BobLee
 * @CreateTime : 2017年5月17日 下午6:06:00
 * @Summary :
 */
public class YTBusinessException extends RuntimeException{

    private static final long serialVersionUID = 3264687668928470971L;
    private Throwable exception;
    private String message;
    private String code;

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "{\"exception\":\"" + exception + "\", \"message\":\"" + message + "\", \"code\":\"" + code + "\"} ";
    }

    public YTBusinessException() {
        initCause(null);
    }

    public YTBusinessException(String msg) {
        super(msg);
        initCause(null);
        this.message = msg;
    }

    public YTBusinessException(String msg, String code) {
        super(msg);
        initCause(null);
        this.message = msg;
        this.code = code;
    }

    public YTBusinessException(Throwable thrown) {
        initCause(null);
        this.exception = thrown;
    }

    public YTBusinessException(String msg, String code, Throwable thrown) {
        super(msg);
        initCause(null);
        this.message = msg;
        this.code = code;
        this.exception = thrown;
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }

    public void printStackTrace(PrintStream outStream) {
        printStackTrace(new PrintWriter(outStream));
    }

    public void printStackTrace(PrintWriter writer) {
        super.printStackTrace(writer);
        if (getException() != null) {
            getException().printStackTrace(writer);
        }
        writer.flush();
    }

}
