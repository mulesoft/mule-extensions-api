/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal;

import org.mule.apache.xml.serialize.OutputFormat;
import org.mule.apache.xml.serialize.XMLSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

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
