/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.runtime.extension.internal.xml;

import org.mule.apache.xml.serialize.OutputFormat;
import org.mule.apache.xml.serialize.XMLSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Moved from org.mule.runtime.extension.internal.GenericXmlSerializer<T>.
 */
public class GenericXmlSerializer<T> {

  private JAXBContext jaxbContext;
  private Marshaller marshaller;
  private Unmarshaller unmarshaller;

  public GenericXmlSerializer(Class<T> serializedType) {
    try {
      jaxbContext = JAXBContext.newInstance(serializedType);
      marshaller = jaxbContext.createMarshaller();
      unmarshaller = jaxbContext.createUnmarshaller();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public synchronized String serialize(T dto) {
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
      marshaller.marshal(dto, getXmlSerializer(out).asContentHandler());

      return out.toString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public synchronized <T> T deserialize(String xml) {
    try {
      return (T) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public synchronized <T> T deserialize(InputStream xml) {
    try {
      return (T) unmarshaller.unmarshal(xml);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private XMLSerializer getXmlSerializer(OutputStream out) {
    OutputFormat of = new OutputFormat();

    of.setCDataElements(new String[] {"^description"});
    of.setIndenting(true);

    XMLSerializer serializer = new XMLSerializer(of);
    serializer.setOutputByteStream(out);

    return serializer;
  }
}
