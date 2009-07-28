/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.subscription.repository.dialectalgorithms.simple;

import gov.hhs.fha.nhinc.subscription.repository.roottopicextractor.IRootTopicExtractionStrategy;
import gov.hhs.fha.nhinc.subscription.repository.roottopicextractor.RootTopicExtractorHelper;
import gov.hhs.fha.nhinc.subscription.repository.service.SubscriptionRepositoryException;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import org.w3c.dom.Node;

/**
 *
 * @author rayj
 */
public class SimpleDialectRootTopicExtractor   implements IRootTopicExtractionStrategy  {
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(SimpleDialectRootTopicExtractor.class);

    public String extractRootTopicFromTopicExpressionNode(Node topicExpression) throws SubscriptionRepositoryException {
        log.debug("begin SimpleDialectRootTopicExtractor.extractRootTopicFromTopicExpressionNode topicExpression='" + XmlUtility.serializeNodeIgnoreFaults(topicExpression) + "'");
        String rootTopic = null;
        String topicValue = XmlUtility.getNodeValue(topicExpression);
        topicValue = RootTopicExtractorHelper.ReplaceNamespacePrefixesWithNamespaces(topicValue, topicExpression);
        rootTopic = topicValue;
        return rootTopic;
    }




}
