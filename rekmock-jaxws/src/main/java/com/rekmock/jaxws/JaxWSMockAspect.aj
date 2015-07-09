package com.rekmock.jaxws;

import com.rekmock.MockConfig;
import com.rekmock.ObjectJAXBSerializer;
import com.rekmock.RekMockAPI;
import com.rekmock.RekMockProvider;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Created by x153520 on 12/16/2014.
 */
public aspect JaxWSMockAspect {

    pointcut servicecall(Object t, javax.jws.WebMethod method):call(@javax.jws.WebMethod * *(..)) && @annotation(method) && target(t);



    Object around(Object t, javax.jws.WebMethod method):servicecall(t,method)
            {
                MethodSignature signature = (MethodSignature) thisJoinPoint.getSignature();

                final Class returnType = signature.getReturnType();

                RekMockAPI rekMockAPI = RekMockProvider.getInstance().getRekMockAPI();
                if (rekMockAPI == null) {
                    return proceed(t, method);
                }
                rekMockAPI.registerTypeSerializer(returnType, new ObjectJAXBSerializer());


                String name = "".equals(method.action()) ? signature.getMethod().getName() : method.action();

                final Object result = rekMockAPI.getCallResult(t.getClass(), name, returnType);

                if (result != null) {
                    return result;
                }
                Object o = proceed(t, method);
                if (rekMockAPI.getRecord() != MockConfig.Record.None) {
                    rekMockAPI.recordCallResult(t.getClass(), name, returnType, o);
                }
                return o;
            }


    /*after(Object t,javax.jws.WebMethod method) returning (Object o):servicecall(t,method)
            {

                final String className = t.getClass().getName();
                final int dotIndex = className.lastIndexOf(".");
                String name = "".equals(method.action()) ? className.substring(dotIndex + 1) : method.action();
                final RekMock rekMock = MockConfig.getRekMockSingleton();
                if (rekMock.getRecord() != MockConfig.Record.None) {
                    rekMock.recordCallResult(name, o);
                }

            }*/
}
