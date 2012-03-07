/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.subscription.repository.topicfilter;

import gov.hhs.fha.nhinc.subscription.repository.roottopicextractor.RootTopicExtractor;
import gov.hhs.fha.nhinc.subscription.repository.service.SubscriptionRepositoryException;
import gov.hhs.fha.nhinc.subscription.repository.topicfilter.ITopicComparison;
import gov.hhs.fha.nhinc.subscription.repository.topicfilter.ITopicFilterStrategy;
import gov.hhs.fha.nhinc.subscription.repository.topicfilter.TopicComparisonExactMatchStrategy;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * @author rayj
 */
public class TopicFilterBasicComparisonStrategy implements ITopicFilterStrategy {

    ITopicComparison topicComparisonStrategy;

    public TopicFilterBasicComparisonStrategy(ITopicComparison topicComparisonStrategy) {
        if (topicComparisonStrategy == null) {
            throw new IllegalArgumentException("Must supply a valid ITopicComparison instance.");
        }
        this.topicComparisonStrategy = topicComparisonStrategy;
    }

    public boolean MeetsCriteria(Element subscriptionTopicExpression, Element notificationMessageElement) {
        boolean meetsCriteria = false;
        try {
            RootTopicExtractor rootTopicExtractor = new RootTopicExtractor();
            Element notificationMessageTopic = rootTopicExtractor
                    .extractTopicElementFromNotificationMessageElement(notificationMessageElement);
            meetsCriteria = topicComparisonStrategy
                    .MeetsCriteria(subscriptionTopicExpression, notificationMessageTopic);
        } catch (XPathExpressionException ex) {
            meetsCriteria = true;
        }
        return meetsCriteria;
    }
}
