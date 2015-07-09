package com.rekmock;

import org.aspectj.lang.reflect.MethodSignature;

/**
 * Created by x153520 on 12/16/2014.
 */
public aspect MockAspect {
    pointcut commandrun(Object t, Mock mock):execution(@com.rekmock.Mock * *(..)) && @annotation(mock) && target(t);

    Object around(Object t, Mock mock):commandrun(t,mock)
            {
                MethodSignature signature = (MethodSignature) thisJoinPoint.getSignature();

                final Class returnType = signature.getReturnType();


                RekMockAPI rekMockAPI = RekMockProvider.getInstance().getRekMockAPI();
                if (rekMockAPI == null) {
                    return proceed(t, mock);
                }

                try {
                    rekMockAPI.registerTypeSerializer(t.getClass(),returnType, mock.serializer().newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                String name = "".equals(mock.value()) ? signature.getMethod().getName() : mock.value();

                final Object result = rekMockAPI.getCallResult(t.getClass(), name, returnType);

                if (result != null) {
                    return result;
                }
                Object o = proceed(t, mock);
                if (rekMockAPI.getRecord() != MockConfig.Record.None) {
                    rekMockAPI.recordCallResult(t.getClass(), name, returnType, o);
                }
                return o;
            }
}
