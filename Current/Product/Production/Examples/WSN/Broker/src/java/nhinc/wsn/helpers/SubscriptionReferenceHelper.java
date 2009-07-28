/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nhinc.wsn.helpers;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import javax.xml.ws.wsaddressing.W3CEndpointReferenceBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class SubscriptionReferenceHelper {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(SubscriptionReferenceHelper.class);
    private static String subscriptionManagerUrl = "http://myaddress/SubscriptionManager";

    public static W3CEndpointReference CreateSubscriptionReference() {
        log.info("begin CreateSubscriptionReference");
        W3CEndpointReference subRef;

        W3CEndpointReferenceBuilder resultBuilder = new W3CEndpointReferenceBuilder();

        log.info("subscriptionManagerUrl=" + subscriptionManagerUrl);
        resultBuilder.address(subscriptionManagerUrl);

        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            doc = docBuilderFactory.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException ex) {
            throw new RuntimeException(ex);
        }

        doc.setXmlStandalone(true);

        //todo: this needs to become dynamic
        Element subscriptionElem = doc.createElementNS("urn:nhin", "SubscriptionId");
        subscriptionElem.setTextContent("some random value");
        resultBuilder.referenceParameter(subscriptionElem);


        log.info("building.. resultBuilder.build()");
        subRef = resultBuilder.build();
        log.info("end CreateSubscriptionReference");
        return subRef;
    }
}
