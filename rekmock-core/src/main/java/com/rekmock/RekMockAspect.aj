package com.rekmock;

/**
 * Created by Faleh Omar on 01/26/2015.
 */
public aspect RekMockAspect {
    pointcut rekmock_class(RekMock rekMock):staticinitialization(*) && @annotation(rekMock);
    pointcut rekmock_method(RekMock rekMock):execution(@com.rekmock.RekMock * *(..)) && @annotation(rekMock);

    after(RekMock rekMock):rekmock_class(rekMock)
            {
                MockConfig mockConfig = new MockConfig();
                mockConfig.setTestCaseName(rekMock.testCaseName());
                mockConfig.setRecord(rekMock.record());
                mockConfig.setStore(rekMock.store());

                RekMockProvider.getInstance().newTestCaseContext(mockConfig);
            }

    /*before(RekMock rekMock):rekmock_method(rekMock)
            {

                RekMockAPI rekMockAPI = null;

                rekMockAPI = MockConfig.getInstance().testCaseName(rekMock.testCaseName()).record(rekMock.record())
                        .dataSetStore(rekMock.store()).build();

                MockConfig.setRekMockSingleton(rekMockAPI);

            }*/

}
