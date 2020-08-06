package com.atguigu.gmall.common.exception;

/**
 * @author Lee_engineer
 * @create 2020-08-04 19:55
 */
public class GmallException extends RuntimeException {

    public GmallException() {
        super();
    }

    public GmallException(String message) {
        super(message);
    }
}
