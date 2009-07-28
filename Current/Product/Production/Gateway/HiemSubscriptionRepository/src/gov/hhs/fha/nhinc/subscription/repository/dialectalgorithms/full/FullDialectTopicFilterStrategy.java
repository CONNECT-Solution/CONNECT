/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.subscription.repository.dialectalgorithms.full;

import gov.hhs.fha.nhinc.subscription.repository.topicfilter.ITopicFilterStrategy;
import gov.hhs.fha.nhinc.xmlCommon.NamespaceContextMapperFromNode;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author rayj
 */
public class FullDialectTopicFilterStrategy implements ITopicFilterStrategy {

    public boolean MeetsCriteria(Element subscriptionTopicExpression, Element notificationMessageElement) {
//        boolean meetsCriteria = false;
//        NamespaceContext namespaceContext = new NamespaceContextMapperFromNode(subscriptionTopicExpression);
//
//        String xpathQuery = XmlUtility.getNodeValue(subscriptionTopicExpression);
//        try {
//            Node result = XmlUtility.performXpathQuery(notificationMessageElement, xpathQuery, namespaceContext);
//            meetsCriteria = (result != null);
//        } catch (XPathExpressionException ex) {
//            Logger.getLogger(FullDialectTopicFilterStrategy.class.getName()).log(Level.SEVERE, null, ex);
//            meetsCriteria = false;
//        }
//        return meetsCriteria;
        return true;

    }
}
