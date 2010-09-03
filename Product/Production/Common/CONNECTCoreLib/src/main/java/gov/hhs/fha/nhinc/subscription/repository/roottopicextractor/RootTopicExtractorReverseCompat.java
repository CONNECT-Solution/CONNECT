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
package gov.hhs.fha.nhinc.subscription.repository.roottopicextractor;

//import java.io.ByteArrayOutputStream;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.subscription.repository.dialectalgorithms.concrete.ConcreteDialectRootTopicExtractor;
import gov.hhs.fha.nhinc.subscription.repository.dialectalgorithms.full.FullDialectRootTopicExtractor;
import gov.hhs.fha.nhinc.subscription.repository.dialectalgorithms.simple.SimpleDialectRootTopicExtractor;
import gov.hhs.fha.nhinc.subscription.repository.service.*;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
//import java.io.IOException;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.xpath.XPathConstants;
//import org.apache.xml.serialize.OutputFormat;
//import org.apache.xml.serialize.XMLSerializer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPathExpressionException;
//import org.w3c.dom.DOMException;
//import org.w3c.dom.ls.LSException;
//import org.xml.sax.InputSource;
import org.w3c.dom.*;
//import java.io.ByteArrayInputStream;
//import org.w3c.dom.DOMConfiguration;
//import java.io.StringReader;
//import javax.xml.namespace.QName;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import org.w3c.dom.Document;
//import org.w3c.dom.bootstrap.DOMImplementationRegistry;
//import org.w3c.dom.ls.DOMImplementationLS;
//import org.w3c.dom.ls.LSInput;
//import org.w3c.dom.ls.LSParser;
//import org.w3c.dom.ls.LSSerializer;
//import org.xml.sax.SAXException;

/**
 *
 * @author rayj
 */
public class RootTopicExtractorReverseCompat {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(RootTopicExtractorReverseCompat.class);

    public Element buildTopicExpressionFromSubscribe(Element message) {
        String documentSubscribeXpathQuery = "//*[local-name()='Subscribe']/*[local-name()='AdhocQuery']";

        Element topicExpression = null;
        if (xpathCheckForTopic(documentSubscribeXpathQuery, message)) {
            topicExpression = buildDocumentTopicExpression();
        }
        return topicExpression;
    }

    public Element buildTopicFromNotify(Element message) {
        String documentNotifyXpathQuery = "//*[local-name()='NotificationMessage']/*[local-name()='Message']/*[local-name()='RetrieveDocumentSetRequest']";

        Element topic = null;

        if (xpathCheckForTopic(documentNotifyXpathQuery, message)) {
            topic = buildDocumentTopic();
        }
        return topic;
    }

    private Element buildDocumentTopicExpression() {
        String topicExpression = "<wsnt:TopicExpression xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2' Dialect='http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple' xmlns:nhinc='urn:gov.hhs.fha.nhinc.hiemtopic'>nhinc:document</wsnt:TopicExpression>";
        Element element = null;
        try {
            element = XmlUtility.convertXmlToElement(topicExpression);
        } catch (Exception ex) {
            log.warn("unable to handle reverse compat topic", ex);
        }
        return element;
    }

    private Element buildDocumentTopic() {
        String topicExpression = "<wsnt:Topic xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2' Dialect='http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple' xmlns:nhinc='urn:gov.hhs.fha.nhinc.hiemtopic'>nhinc:document</wsnt:Topic>";
        Element element = null;
        try {
            element = XmlUtility.convertXmlToElement(topicExpression);
        } catch (Exception ex) {
            log.warn("unable to handle reverse compat topic", ex);
        }
        return element;
    }

//    public String extractReverseCompatRootTopic(String message) {
//        Element element = null;
//        try {
//            element = XmlUtility.convertXmlToElement(message);
//        } catch (Exception ex) {
//            log.warn("Failed to check topic for reverse compatibility.", ex);
//        }
//        return extractReverseCompatRootTopic(element);
//    }

//    public String extractReverseCompatRootTopic(Element message) {
//        String documentNotifyXpathQuery = "//*[local-name()='Subscribe']/*[local-name()='AdhocQuery']";
//        String documentSubscribeXpathQuery = "//*[local-name()='NotificationMessage']/*[local-name()='Message']/*[local-name()='RetrieveDocumentSetRequest']";
//        String documentTopic = "{urn:gov.hhs.fha.nhinc.hiemtopic}document";
//
//        String rootTopic = null;
//
//        if (xpathCheckForTopic(documentSubscribeXpathQuery, message) || xpathCheckForTopic(documentNotifyXpathQuery, message)) {
//            rootTopic = documentTopic;
//        }
//        return rootTopic;
//    }

    private boolean xpathCheckForTopic(String xpathQuery, Element message) {
        boolean result = false;
        Node match = null;
        try {
            match = XmlUtility.performXpathQuery(message, xpathQuery);
        } catch (XPathExpressionException ex) {
            log.debug("Failed to perform xpath query against message.  Assume that this is not a match");
        }
        result = (match != null);
        return result;

    }
}
