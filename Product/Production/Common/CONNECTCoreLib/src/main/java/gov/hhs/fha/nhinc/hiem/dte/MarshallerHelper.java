/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
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

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
            .getLog(MarshallerHelper.class);

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
