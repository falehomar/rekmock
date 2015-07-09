package com.rekmock;

import java.io.InputStream;

/**
 * Created by Faleh Omar on 01/18/2015.
 */
public abstract class DataSetStore {
    public abstract void save(String testCaseName, Class type, String methodName, InputStream inputStream, String fileType);

    public abstract InputStream load(String testCaseName, Class type, String methodName);
}
