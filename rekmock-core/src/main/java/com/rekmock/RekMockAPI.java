package com.rekmock;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Faleh Omar on 01/18/2015.
 */
public class RekMockAPI {

    private String testCaseName;

    class
            CompositeKey {
        Object[] os;

        CompositeKey(Object... o) {
            this.os = o;
        }

        @Override
        public int hashCode() {
            int hash = 0;
            for (Object o : this.os)
                hash ^= o.hashCode();
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof CompositeKey)) {
                return false;
            }
            CompositeKey k = (CompositeKey) obj;
            for (int i = 0; i < this.os.length; i++) {
                if (!k.os[i].equals(os[i])) {
                    return false;
                }
            }

            return true;
        }
    }

    private Map<CompositeKey, ObjectSerializer> objectSerializers = new HashMap<CompositeKey, ObjectSerializer>();
    private DataSetStore dataSetStore;
    private MockConfig.Record record;

    public <R> void recordCallResult(Class type, String methodName, Class<R> returnType, R returnVal) {

        // serialize object
        ObjectSerializer serializer = getTypeSerializer(type, returnType);


        if (serializer == null)
            throw new RekMokException("serializer not registered for type");

        String fileType = serializer.getType();

        InputStream inputStream = serializer.serialize(returnVal);

        dataSetStore.save(getTestCaseName(), type, methodName, inputStream,fileType);


    }

    public <R> void recordExceptionResult(Class type, String methodName, Class<R> returnType, Throwable throwable) {

        // serialize object
        /*ObjectSerializer serializer = getTypeSerializer(throwable.getClass());

        if (serializer == null)
            serializer = new ObjectJavaSerializer();

        InputStream inputStream = serializer.serialize(throwable);

        dataSetStore.save(getTestCaseName(), type, methodName, inputStream);*/


    }

    private ObjectSerializer getTypeSerializer(Class type, Class returnType) {

        ObjectSerializer objectSerializer = objectSerializers.get(new CompositeKey(type, returnType));

        if (objectSerializer == null) {
            objectSerializer = objectSerializers.get(new CompositeKey(returnType));
        }


        return objectSerializer;
    }

    /**
     * returns mock data. null if nothing found
     *
     * @param type
     * @param methodName
     * @param returnType
     * @param <R>
     * @return
     */
    public <R> R getCallResult(Class type, String methodName, Class<R> returnType) {

        InputStream inputStream = dataSetStore.load(getTestCaseName(), type, methodName);
        if (inputStream == null)
            return null;

        ObjectSerializer serializer = getTypeSerializer(type, returnType);

        return returnType.cast(serializer.deSerialize(inputStream, returnType));
    }

    public DataSetStore getDataSetStore() {
        return dataSetStore;
    }

    public void setDataSetStore(DataSetStore dataSetStore) {
        this.dataSetStore = dataSetStore;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }

    public MockConfig.Record getRecord() {
        return record;
    }

    public void setRecord(MockConfig.Record record) {
        this.record = record;
    }

    synchronized public void registerTypeSerializer(Class type, Class returnType, ObjectSerializer objectSerializer) {
        assert objectSerializer != null;

        CompositeKey compositeKey = null;
        if (type != null && returnType != null) {
            compositeKey = new CompositeKey(type, returnType);
            if (serializerExists(compositeKey)) {
                return;
            }


        } else if (returnType != null) {
            compositeKey = new CompositeKey(returnType);
            if (serializerExists(compositeKey)) {
                return;
            }

        } else
            throw new RekMokException("a type has to be provided");

        objectSerializers.put(compositeKey, objectSerializer);
    }

    public void registerTypeSerializer(Class returnType, ObjectSerializer objectSerializer) {
        registerTypeSerializer(null, returnType, objectSerializer);
    }

    protected boolean serializerExists(CompositeKey compositeKey) {
        return objectSerializers.containsKey(compositeKey);
    }
}
