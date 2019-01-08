/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.transform.marshallers;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.util.JAXBUnmarshallingUtil;
import gov.hhs.fha.nhinc.util.StreamUtils;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class Marshaller {

    private static final Logger LOG = LoggerFactory.getLogger(Marshaller.class);

    public Element marshal(Object object, String contextPath) {
        Element element = null;
        LOG.debug("begin marshal");
        if (object == null) {
            LOG.warn("object to marshall is null");
        } else if (NullChecker.isNullish(contextPath)) {
            LOG.warn("no contextPath supplied");
        } else {
            try {
                LOG.debug("get instance of JAXBContext [contextPath='" + contextPath + "']");
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext(contextPath);
                LOG.debug("get instance of marshaller");
                javax.xml.bind.Marshaller marshaller = jc.createMarshaller();
                StringWriter stringWriter = new StringWriter();
                LOG.debug("Calling marshal");
                marshaller.marshal(object, stringWriter);
                LOG.debug("string writer writing to string");
                String xml = stringWriter.toString();
                LOG.debug("Marshaled xml=[" + xml + "]");
                if (NullChecker.isNotNullish(xml)) {
                    element = XmlUtility.convertXmlToElement(xml);
                }
            } catch (Exception e) {
                // "java.security.PrivilegedActionException: java.lang.ClassNotFoundException:
                // com.sun.xml.bind.v2.ContextFactory"
                LOG.error("Failed to marshall: {}", e.getLocalizedMessage(), e);
                element = null;
            }
        }
        return element;
    }

    /**
     * Marshalls the passed in XML Bean object into an XML Element.
     *
     * @param object - the XML bean to be marshalled
     * @param contextPath - The name of the context. (i.e. "org.hl7.v3").
     * @param qname - the qualified name of the XML (i.e. "urn:org:hl7:v3")
     * @return the XML Element
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Element marshal(Object object, String contextPath, QName qname) {
        Element element = null;
        StringWriter stringWriter = new StringWriter();

        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext(contextPath);

            javax.xml.bind.Marshaller marshaller = jc.createMarshaller();

            marshaller.marshal(new JAXBElement(qname, object.getClass(), object), stringWriter);

            element = XmlUtility.convertXmlToElement(stringWriter.toString());
        } catch (Exception e) {
            LOG.error("Failed to marshall: {}", e.getLocalizedMessage(), e);
            element = null;
        } finally {
            try {
                stringWriter.close();
            } catch (IOException ioe) {
                // close quietly(ish)
                LOG.warn("Could not close writer: {}", ioe.getLocalizedMessage(), ioe);
            }
        }

        return element;
    }

    public Object unmarshallJaxbElement(Element element, String contextPath) {
        Object unmarshalledObject = unmarshal(element, contextPath);
        if (unmarshalledObject instanceof JAXBElement) {
            JAXBElement jaxb = (JAXBElement) unmarshalledObject;
            unmarshalledObject = jaxb.getValue();
        }
        return unmarshalledObject;
    }

    public Object unmarshal(Element element, String contextPath) {
        Object unmarshalledObject = null;
        LOG.debug("begin unmarshal");

        if (element == null) {
            LOG.warn("element to unmarshal is null");
        } else if (contextPath == null) {
            LOG.warn("no contextPath supplied");
        } else {
            ByteArrayInputStream is = null;
            try {
                LOG.debug("desializing element");
                String serializedElement = XmlUtility.serializeElement(element);
                LOG.debug("serializedElement=[" + serializedElement + "]");
                LOG.debug("get instance of JAXBContext [contextPath='" + contextPath + "']");
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext(contextPath);
                LOG.debug("get instance of unmarshaller");
                javax.xml.bind.Unmarshaller unmarshaller = jc.createUnmarshaller();
                is = new ByteArrayInputStream(serializedElement.getBytes());
                LOG.debug("Calling unmarshal");
                JAXBUnmarshallingUtil util = new JAXBUnmarshallingUtil();
                unmarshalledObject = unmarshaller.unmarshal(util.getSafeStreamReaderFromInputStream(is));
                LOG.debug("end unmarshal");
            } catch (Exception e) {
                // "java.security.PrivilegedActionException: java.lang.ClassNotFoundException:
                // com.sun.xml.bind.v2.ContextFactory"
                // use jaxb element
                LOG.error("Failed to unmarshall: {}", e.getLocalizedMessage(), e);
                unmarshalledObject = null;
            } finally {
                StreamUtils.closeStreamSilently(is);
            }
        }

        return unmarshalledObject;
    }

    public Object unmarshal(String xml, String contextPath) {
        Element element = null;
        try {
            element = XmlUtility.convertXmlToElement(xml);
        } catch (Exception ex) {
            LOG.warn("failed to parse xml: {}", ex.getLocalizedMessage(), ex);
        }
        return unmarshal(element, contextPath);
    }
}
