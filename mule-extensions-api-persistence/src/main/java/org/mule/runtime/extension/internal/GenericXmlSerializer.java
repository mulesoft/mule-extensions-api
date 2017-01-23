/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class GenericXmlSerializer<T> {

  private Class<T> serializedType;

  public GenericXmlSerializer(Class<T> serializedType) {
    this.serializedType = serializedType;
  }

  public String serialize(T dto) {
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(serializedType);
      Marshaller marshaller = jaxbContext.createMarshaller();

      ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
      marshaller.marshal(dto, getXmlSerializer(out).asContentHandler());

      return out.toString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public <T> T deserialize(String xml) {
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(serializedType);
      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
      return (T) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));
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
