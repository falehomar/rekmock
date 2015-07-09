package com.rekmock;

import java.io.InputStream;

/**
 * Created by Faleh Omar on 01/18/2015.
 */
public abstract class ObjectSerializer {
    public InputStream serialize(Object o) {
        return null;
    }

    public Object deSerialize(InputStream inputStream, Class clazz) {
        return null;
    }

    public abstract String getType();
}
