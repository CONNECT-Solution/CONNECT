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
 * 
 * @author Neil Webb
 */
public class RootTopicExtractor {

    public static final String DIALECT_FULL = "http://docs.oasis-open.org/wsn/t-1/TopicExpression/Full";
    public static final String DIALECT_FULL_MISSPELLED = "http://doc.oasis-open.org/wsn/t-1/TopicExpression/Full";
    public static final String DIALECT_CONCRETE = "http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete";
    public static final String DIALECT_CONCRETE_MISSPELLED = "http://doc.oasis-open.org/wsn/t-1/TopicExpression/Concrete";
    public static final String DIALECT_SIMPLE = "http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple";
    public static final String DIALECT_SIMPLE_MISSPELLED = "http://doc.oasis-open.org/wsn/t-1/TopicExpression/Simple";
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(RootTopicExtractor.class);

    public String extractRootTopicFromSubscribeElement(Element subscribeElement) throws SubscriptionRepositoryException {
        // TODO: Root topic extraction strategy
        String rootTopic = null;
        if (subscribeElement != null) {
            try {
                Element topicExpressionElement = extractTopicExpressionElementFromSubscribeElement(subscribeElement);

                if (topicExpressionElement != null) {
                    rootTopic = extractRootTopicFromTopicExpressionNode(topicExpressionElement);
                } else {
                    log.warn("subscribe does not have topic expression node");
//                    RootTopicExtractorReverseCompat compat = new RootTopicExtractorReverseCompat();
//                    rootTopic = compat.extractReverseCompatRootTopic(subscribeElement);
                }

                log.debug("Extracted root topic: " + rootTopic);
            } catch (Throwable t) {
                log.error("Error extracting root topic: " + t.getMessage(), t);
                throw new SubscriptionRepositoryException("Error extracting root topic: " + t.getMessage(), t);
            }
        }

        return rootTopic;
    }

    public String extractRootTopicFromSubscribeXml(String subscribeXml) throws SubscriptionRepositoryException {
        Element subscribeElement = null;
        try {
            subscribeElement = XmlUtility.convertXmlToElement(subscribeXml);
        } catch (Exception ex) {
            throw new SubscriptionRepositoryException("Failed to parse subscribe xml", ex);
        }
        String rootTopic = extractRootTopicFromSubscribeElement(subscribeElement);
        return rootTopic;
    }

    public String extractRootTopicFromNotificationMessageElement(Element notificationMessageElement) throws SubscriptionRepositoryException {
        // TODO: Root topic extraction strategy
        String rootTopic = null;
        if (notificationMessageElement != null) {
            try {
                Node topic;
                topic = extractTopicElementFromNotificationMessageElement(notificationMessageElement);
                if (topic != null) {
                    rootTopic = extractRootTopicFromTopicExpressionNode(topic);
                } else {
                    log.warn("notify does not have topic node");
                }

                log.debug("Extracted root topic: " + rootTopic);
            } catch (Throwable t) {
                log.error("Error extracting root topic: " + t.getMessage(), t);
                throw new SubscriptionRepositoryException("Error extracting root topic: " + t.getMessage(), t);
            }
        }

        return rootTopic;
    }

    public String extractRootTopicFromNotificationMessageXml(String notificationMessageXml) throws SubscriptionRepositoryException {
        Element notificationMessageElement;
        try {
            notificationMessageElement = XmlUtility.convertXmlToElement(notificationMessageXml);
        } catch (Exception ex) {
            throw new SubscriptionRepositoryException(ex);
        }
        return extractRootTopicFromNotificationMessageElement(notificationMessageElement);
    }

    public String extractRootTopicFromTopicExpressionNode(Node topicExpression) throws SubscriptionRepositoryException {
        log.debug("extractRootTopicFromTopicExpressionNode node='" + XmlUtility.serializeNodeIgnoreFaults(topicExpression) + "'");
        String rootTopic = null;
        if (topicExpression != null) {
            IRootTopicExtractionStrategy extractor = null;
            String dialect = getDialectFromTopicExpression(topicExpression);
            if (dialect.contentEquals(DIALECT_SIMPLE)) {
                extractor = new SimpleDialectRootTopicExtractor();
            } else if (dialect.contentEquals(DIALECT_SIMPLE_MISSPELLED)) {
                log.warn("Dialect unknown ('" + dialect + ", but assumed to be '" + DIALECT_SIMPLE + "'");
                extractor = new SimpleDialectRootTopicExtractor();
            } else if (dialect.contentEquals(DIALECT_CONCRETE)) {
                extractor = new ConcreteDialectRootTopicExtractor();
            } else if (dialect.contentEquals(DIALECT_CONCRETE_MISSPELLED)) {
                log.warn("Dialect unknown ('" + dialect + ", but assumed to be '" + DIALECT_CONCRETE + "'");
                extractor = new ConcreteDialectRootTopicExtractor();
            } else if (dialect.contentEquals(DIALECT_FULL)) {
                extractor = new FullDialectRootTopicExtractor();
            } else if (dialect.contentEquals(DIALECT_FULL_MISSPELLED)) {
                log.warn("Dialect unknown ('" + dialect + ", but assumed to be '" + DIALECT_FULL + "'");
                extractor = new FullDialectRootTopicExtractor();
            } else {
                throw new SubscriptionRepositoryException("Unknown dialect + '" + dialect + "'");
            }
            rootTopic = extractor.extractRootTopicFromTopicExpressionNode(topicExpression);
        }
        log.debug("rootTopic='" + rootTopic + "'");
        return rootTopic;
    }

    public String getDialectFromTopicExpression(Node topicExpression) {
        String dialect = null;
        if (topicExpression != null) {
            Node dialectNode = topicExpression.getAttributes().getNamedItem("Dialect");
            if (dialectNode != null) {
                dialect = dialectNode.getNodeValue();
            }
        }
        return dialect;
    }

    public String getDialectFromTopicExpression(String topicExpression) throws Exception {
        Node node = (Node) XmlUtility.convertXmlToElement(topicExpression);
        return getDialectFromTopicExpression(node);
    }

    public Element extractTopicExpressionElementFromSubscribeXml(String subscribeXml) throws XPathExpressionException {
        Element subscribeElement;
        try {
            subscribeElement = XmlUtility.convertXmlToElement(subscribeXml);
        } catch (Exception ex) {
            throw new XPathExpressionException(ex);
        }
        return extractTopicExpressionElementFromSubscribeElement(subscribeElement);
    }

    public Element extractTopicExpressionElementFromSubscribeElement(Element subscribeElement) throws XPathExpressionException {
        String xpathQuery = "//*[local-name()='Subscribe']/*[local-name()='Filter']/*[local-name()='TopicExpression']";

        Element topicExpressionElement = (Element) XmlUtility.performXpathQuery(subscribeElement, xpathQuery);
        if (topicExpressionElement == null) {
            log.info("subscribe does not have topic expression node.  Will check for reverse compat");
            RootTopicExtractorReverseCompat compat = new RootTopicExtractorReverseCompat();
            topicExpressionElement = compat.buildTopicExpressionFromSubscribe(subscribeElement);
        }

        return topicExpressionElement;
    }

    private Element extractTopicElementFromNotificationMessageXml(String notificationMessageXml) throws XPathExpressionException {
        String xpathQuery = "//*[local-name()='NotificationMessage']/*[local-name()='Topic']";
        return (Element) XmlUtility.performXpathQuery(notificationMessageXml, xpathQuery);
    }

    public Element extractTopicElementFromNotificationMessageElement(Element notificationMessageElement) throws XPathExpressionException {
        String xpathQuery = "//*[local-name()='NotificationMessage']/*[local-name()='Topic']";
        Element topicElement = (Element) XmlUtility.performXpathQuery(notificationMessageElement, xpathQuery);

        if (topicElement == null) {
            log.info("notify does not have topic node.  Will check for reverse compat");
            RootTopicExtractorReverseCompat compat = new RootTopicExtractorReverseCompat();
            topicElement = compat.buildTopicFromNotify(notificationMessageElement);
        }
        return topicElement;

    }
}
