package com.rekmock;

/**
 * Created by Faleh Omar on 02/05/2015.
 */
public class RekMockProvider {

    private RekMockProvider() {
    }

    private static final RekMockProvider instance = new RekMockProvider();

    public static RekMockProvider getInstance() {
        return instance;
    }

    private static RekMockAPI currentRekMockAPI;

    public void newTestCaseContext(MockConfig mockConfig) {
        RekMockAPI rekMockAPI = new RekMockAPI();
        rekMockAPI.setRecord(mockConfig.getRecord());
        rekMockAPI.setTestCaseName(mockConfig.getTestCaseName());
        rekMockAPI.setDataSetStore(mockConfig.getStore());

        currentRekMockAPI = rekMockAPI;
    }

    public RekMockAPI getRekMockAPI() {
        return currentRekMockAPI;
    }
}
