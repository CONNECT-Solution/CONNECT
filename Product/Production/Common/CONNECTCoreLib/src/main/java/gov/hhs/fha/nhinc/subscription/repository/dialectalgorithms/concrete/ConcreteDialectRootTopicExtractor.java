/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.subscription.repository.dialectalgorithms.concrete;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.subscription.repository.roottopicextractor.IRootTopicExtractionStrategy;
import gov.hhs.fha.nhinc.subscription.repository.roottopicextractor.RootTopicExtractorHelper;
import gov.hhs.fha.nhinc.subscription.repository.service.SubscriptionRepositoryException;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import org.w3c.dom.Node;

/**
 *
 * @author rayj
 */
public class ConcreteDialectRootTopicExtractor implements IRootTopicExtractionStrategy {

    public String extractRootTopicFromTopicExpressionNode(Node topicExpression) throws SubscriptionRepositoryException {
        String rootTopic = null;
        String topicValue = XmlUtility.getNodeValue(topicExpression);

        String[] topicParts = topicValue.split("/");

        for (String topicPart : topicParts) {
            String cleanedTopicPart = RootTopicExtractorHelper.ReplaceNamespacePrefixesWithNamespaces(topicPart, topicExpression);
            if (NullChecker.isNullish(rootTopic)) {
                rootTopic = "";
            } else {
                rootTopic = rootTopic.concat("/");
            }
            rootTopic = rootTopic.concat(cleanedTopicPart);
        }

        return rootTopic;
    }
}
