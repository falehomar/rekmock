package com.rekmock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Faleh Omar on 01/25/2015.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RekMock {
    String testCaseName();

    MockConfig.Record record() default MockConfig.Record.Default;

    Class<? extends DataSetStore> store() default CWDDataSetStore.class;
}
