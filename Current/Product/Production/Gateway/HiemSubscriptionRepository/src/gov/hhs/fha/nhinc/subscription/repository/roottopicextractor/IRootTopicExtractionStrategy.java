/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.subscription.repository.roottopicextractor;

import gov.hhs.fha.nhinc.subscription.repository.service.SubscriptionRepositoryException;


/**
 *
 * @author rayj
 */
public interface IRootTopicExtractionStrategy  {
    String extractRootTopicFromTopicExpressionNode(org.w3c.dom.Node topicExpression) throws SubscriptionRepositoryException;
}
