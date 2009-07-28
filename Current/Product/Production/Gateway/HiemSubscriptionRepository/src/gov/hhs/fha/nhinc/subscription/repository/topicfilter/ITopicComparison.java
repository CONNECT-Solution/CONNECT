/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.subscription.repository.topicfilter;

import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public interface ITopicComparison {
    boolean MeetsCriteria(Element subscriptionTopicExpression, Element notificationMessageTopic);
}
