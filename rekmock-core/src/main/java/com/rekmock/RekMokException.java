package com.rekmock;

/**
 * Created by Faleh Omar on 01/23/2015.
 */
public class RekMokException extends RuntimeException {
    public RekMokException() {
        super();
    }

    public RekMokException(String message) {
        super(message);
    }

    public RekMokException(String message, Throwable cause) {
        super(message, cause);
    }

    public RekMokException(Throwable cause) {
        super(cause);
    }
}
