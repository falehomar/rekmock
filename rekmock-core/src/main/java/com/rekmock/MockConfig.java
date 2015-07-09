package com.rekmock;

/**
 * Created by x153520 on 12/15/2014.
 */
public class MockConfig {

    private String testCaseName;
    private Record record;
    private DataSetStore store;


    public DataSetStore getStore() {
        return store;
    }

    public void setStore(Class<? extends DataSetStore> store) {
        try {
            this.store = store.newInstance();
        } catch (InstantiationException e) {
            throw new RekMokException(e);
        } catch (IllegalAccessException e) {
            throw new RekMokException(e);
        }
    }

    public String getTestCaseName() {
        return testCaseName;
    }


    public enum Record {Overwrite, None,Default;}


    public Record getRecord() {
        return record;
    }


    public void setRecord(Record record) {
        this.record = record;
    }


    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }

}
