/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
//
// Non-Export Controlled Information
//
//####################################################################
//## The MIT License
//##
//## Copyright (c) 2010 Harris Corporation
//##
//## Permission is hereby granted, free of charge, to any person
//## obtaining a copy of this software and associated documentation
//## files (the "Software"), to deal in the Software without
//## restriction, including without limitation the rights to use,
//## copy, modify, merge, publish, distribute, sublicense, and/or sell
//## copies of the Software, and to permit persons to whom the
//## Software is furnished to do so, subject to the following conditions:
//## 
//## The above copyright notice and this permission notice shall be
//## included in all copies or substantial portions of the Software.
//## 
//## THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//## EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
//## OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//## NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
//## HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
//## WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//## FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
//## OTHER DEALINGS IN THE SOFTWARE.
//##
//####################################################################
//********************************************************************
// FILE: JaxbUtil.java
//
// Copyright (C) 2010 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: JaxbUtil.java
//
// LIMITATIONS: None
//
// SOFTWARE HISTORY:
//
//
//Feb 24 2010 PTR xxx R.Robinson   Initial Coding.
//
//********************************************************************
package gov.hhs.fha.nhinc.lift.common.util;

import java.io.Reader;
import java.io.StringWriter;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;

public class JaxbUtil {

    /*
     * According to people online, creating the JAXBContext is very slow.  This
     * is way there is a static HashMap to try to prevent identical context's
     * from being created using the class twice.
     */
    private static ConcurrentHashMap<Class<?>, JAXBContext> contextMap = new ConcurrentHashMap<Class<?>, JAXBContext>();
    private static javax.xml.parsers.DocumentBuilderFactory dbf;

    static {
        dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
    }

    public static Object unmarshalMessageFromReader(Reader reader, Class<?> c) throws JAXBException {
        Object result = null;

        JAXBContext jc = getContext(c);
        Unmarshaller um = jc.createUnmarshaller();

        result = um.unmarshal(reader);

        return result;
    }

    public static org.w3c.dom.Element marshal(Object obj) throws JAXBException {
        try {
            Document doc = null;
            JAXBContext jc = getContext(obj.getClass());
            Marshaller marshaller = jc.createMarshaller();
            doc = dbf.newDocumentBuilder().newDocument();
            marshaller.marshal(obj, doc);
            return doc.getDocumentElement();
        } catch (ParserConfigurationException ex) {
            throw new JAXBException("Error in creating document elements: " + ex.getMessage());
        }
    }

    public static String marshalToString(Object obj) throws JAXBException {
        StringWriter writer = new StringWriter();

            JAXBContext jc = getContext(obj.getClass());
            Marshaller marshaller = jc.createMarshaller();
            marshaller.marshal(obj, writer);

        return writer.toString();
    }

    public static Object unmarshal(org.w3c.dom.Element elem, Class<?> c) throws JAXBException {
        Object result = null;

            JAXBContext jc = getContext(c);
            Unmarshaller um = jc.createUnmarshaller();
            result = um.unmarshal(elem);

        return result;
    }

    public static Object unmarshalFromReader(Reader reader, Class<?> c) throws JAXBException {
        Object result = null;

            JAXBContext jc = getContext(c);
            Unmarshaller um = jc.createUnmarshaller();
            result = um.unmarshal(reader);

        return result;
    }

    private static JAXBContext getContext(Class<?> c) throws JAXBException {
        JAXBContext jc = contextMap.get(c);
        if (jc == null) {
            jc = JAXBContext.newInstance(c);
            contextMap.put(c, jc);
        }

        return jc;
    }
}
