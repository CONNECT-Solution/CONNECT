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

import org.apache.log4j.Logger;

/**
 * 
 * @author rayj
 */
public class TopicComparisonFactory {

    private static final Logger LOG = Logger.getLogger(TopicComparisonFactory.class);

    public static ITopicComparison getTopicComparisonStrategy(String dialect) throws SubscriptionRepositoryException {
        ITopicComparison topicComparisonStategy = null;
        if (dialect.contentEquals(RootTopicExtractor.DIALECT_SIMPLE)) {
            topicComparisonStategy = new TopicComparisonExactMatchStrategy();
        } else if (dialect.contentEquals(RootTopicExtractor.DIALECT_SIMPLE_MISSPELLED)) {
            LOG.warn("Dialect unknown ('" + dialect + ", but assumed to be '" + RootTopicExtractor.DIALECT_SIMPLE + "'");
            topicComparisonStategy = new TopicComparisonExactMatchStrategy();
        } else if (dialect.contentEquals(RootTopicExtractor.DIALECT_CONCRETE)) {
            topicComparisonStategy = new TopicComparisonExactMatchStrategy();
        } else if (dialect.contentEquals(RootTopicExtractor.DIALECT_CONCRETE_MISSPELLED)) {
            LOG.warn("Dialect unknown ('" + dialect + ", but assumed to be '" + RootTopicExtractor.DIALECT_CONCRETE
                    + "'");
            topicComparisonStategy = new TopicComparisonExactMatchStrategy();
        } else if (dialect.contentEquals(RootTopicExtractor.DIALECT_FULL)) {
            // temp only
            topicComparisonStategy = new TopicComparisonExactMatchStrategy();
        } else if (dialect.contentEquals(RootTopicExtractor.DIALECT_FULL_MISSPELLED)) {
            LOG.warn("Dialect unknown ('" + dialect + ", but assumed to be '" + RootTopicExtractor.DIALECT_FULL + "'");
            // temp only
            topicComparisonStategy = new TopicComparisonExactMatchStrategy();
        } else {
            throw new SubscriptionRepositoryException("Unknown dialect + '" + dialect + "'");
        }
        return topicComparisonStategy;
    }
}
