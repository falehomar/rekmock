package com.rekmock;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Faleh Omar on 02/12/2015.
 */
public class JSONSerializer extends ObjectSerializer {

    /**
     * issues:
     * 1. conflicting getters of one property e.g isProperty vs getProperty
     */

    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
    }
    @Override
    public InputStream serialize(Object o) {
        try {
            return new ByteArrayInputStream(mapper.writeValueAsBytes(o));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object deSerialize(InputStream inputStream, Class clazz) {
        try {
            return mapper.readValue(inputStream,clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getType() {
        return "json";
    }
}
