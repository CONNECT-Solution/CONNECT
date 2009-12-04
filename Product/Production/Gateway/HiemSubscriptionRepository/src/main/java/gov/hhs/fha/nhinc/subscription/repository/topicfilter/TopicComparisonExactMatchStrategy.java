/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.subscription.repository.topicfilter;

import gov.hhs.fha.nhinc.subscription.repository.roottopicextractor.RootTopicExtractor;
import gov.hhs.fha.nhinc.subscription.repository.roottopicextractor.RootTopicExtractorHelper;
import gov.hhs.fha.nhinc.subscription.repository.service.SubscriptionRepositoryException;
import gov.hhs.fha.nhinc.subscription.repository.topicfilter.ITopicComparison;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author rayj
 */
public class TopicComparisonExactMatchStrategy implements ITopicComparison {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(TopicComparisonExactMatchStrategy.class);

    public boolean MeetsCriteria(Element subscriptionTopicExpression, Element notificationMessageTopic) {
        boolean meetsCriteria = false;
        try {
            //same if topics are the same.. could just test the root topics for this
            RootTopicExtractor rootTopicExtractor = new RootTopicExtractor();
            String notifyRootTopic = rootTopicExtractor.extractRootTopicFromTopicExpressionNode(notificationMessageTopic);
            String subscribeRootTopic = rootTopicExtractor.extractRootTopicFromTopicExpressionNode(subscriptionTopicExpression);
            meetsCriteria = subscribeRootTopic.contentEquals(notifyRootTopic);

            if (!meetsCriteria && supportNonNamespaceMatch()) {
                String notifyRootTopicWithoutPrefix = RootTopicExtractorHelper.removeNamespaceHolder(notifyRootTopic);
                String subscribeRootTopicWithoutPrefix = RootTopicExtractorHelper.removeNamespaceHolder(subscribeRootTopic);
                meetsCriteria = subscribeRootTopicWithoutPrefix.contentEquals(notifyRootTopicWithoutPrefix);
                if (meetsCriteria) {
                    log.debug("using supportNonNamespaceMatch able to make match. [notifyRootTopic=" + notifyRootTopic + "][subscribeRootTopic=" + subscribeRootTopic + "]");
                }
            }

        } catch (SubscriptionRepositoryException ex) {
            log.error("Error occurred processing topic comparison strategy TopicComparisonExactMatchStrategy.MeetsCriteria", ex);
            meetsCriteria = false;
        }
        return meetsCriteria;
    }

    private boolean supportNonNamespaceMatch() {
        return true;
    }
}
