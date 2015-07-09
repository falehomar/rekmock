package com.rekmock;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Faleh Omar on 01/24/2015.
 */
public class ObjectJAXBSerializer extends ObjectSerializer {

    @Override
    public InputStream serialize(Object o) {
        // look for an ObjectFactory

        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(o.getClass().getPackage().getName());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
                    Boolean.TRUE);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            jaxbMarshaller.marshal(new JAXBElement(new QName("uri", "local"), o.getClass(), o), byteArrayOutputStream);
            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return super.serialize(o);
    }

    @Override
    public Object deSerialize(InputStream inputStream, Class clazz) {
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(clazz.getPackage().getName());
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            Object value = jaxbUnmarshaller.unmarshal(new StreamSource(inputStream), clazz).getValue();
            return value;
        } catch (JAXBException e) {
            e.printStackTrace();
        }


        return super.deSerialize(inputStream, clazz);
    }

    @Override
    public String getType() {
        return "xml";
    }
}
