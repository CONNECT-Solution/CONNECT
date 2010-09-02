/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hiem.dte;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class MarshallerHelper {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(MarshallerHelper.class);

    public Element marshal(Object object, String contextPath) {
        Element element = null;
        log.debug("begin marshal");
        if (object == null) {
            log.warn("object to marshall is null");
        } else if (NullChecker.isNullish(contextPath)) {
            log.warn("no contextPath supplied");
        } else {
            try {
                log.debug("get instance of JAXBContext [contextPath='" + contextPath + "']");
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext(contextPath);
                log.debug("get instance of marshaller");
                Marshaller marshaller = jc.createMarshaller();
                StringWriter stringWriter = new StringWriter();
                log.debug("Calling marshal");
                marshaller.marshal(object, stringWriter);
                log.debug("string writer writing to string");
                String xml = stringWriter.toString();
                log.debug("Marshaled xml=[" + xml + "]");
                if (NullChecker.isNotNullish(xml)) {
                    element = XmlUtility.convertXmlToElement(xml);
                }
            } catch (Exception e) {
                log.error("Failed to marshall: " + e.getMessage(), e);
                element = null;
            }
        }
        return element;
    }

    public Object unmarshal(Element element, String contextPath) {
        Object unmarshalledObject = null;
        log.debug("begin unmarshal");

        if (element == null) {
            log.warn("element to unmarshal is null");
        } else if (contextPath == null) {
            log.warn("no contextPath supplied");
        } else {
            try {
                log.debug("desializing element");
                String serializedElement = XmlUtility.serializeElement(element);
                log.debug("serializedElement=[" + serializedElement + "]");
                log.debug("get instance of JAXBContext [contextPath='" + contextPath + "']");
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext(contextPath);
                log.debug("get instance of unmarshaller");
                Unmarshaller unmarshaller = jc.createUnmarshaller();
                log.debug("init stringReader");
                StringReader stringReader = new StringReader(serializedElement);
                log.debug("Calling unmarshal");
                unmarshalledObject = unmarshaller.unmarshal(stringReader);
                log.debug("end unmarshal");
            } catch (Exception e) {
                log.error("Failed to unmarshall: " + e.getMessage(), e);
                unmarshalledObject = null;
            }
        }

        return unmarshalledObject;
    }
}
