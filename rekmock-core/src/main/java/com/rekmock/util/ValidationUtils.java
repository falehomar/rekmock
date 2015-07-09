package com.rekmock.util;

/**
 * Created by Faleh Omar on 01/18/2015.
 */
public abstract class ValidationUtils {
    public static boolean empty(String s) {
        return s == null || s.trim().isEmpty();
    }

}
