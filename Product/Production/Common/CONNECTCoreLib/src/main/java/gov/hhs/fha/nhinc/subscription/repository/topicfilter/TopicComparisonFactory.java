/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.subscription.repository.topicfilter;

import gov.hhs.fha.nhinc.subscription.repository.roottopicextractor.RootTopicExtractor;
import gov.hhs.fha.nhinc.subscription.repository.service.SubscriptionRepositoryException;

/**
 *
 * @author rayj
 */
public class TopicComparisonFactory {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(TopicComparisonFactory.class);

    public static ITopicComparison getTopicComparisonStrategy(String dialect) throws SubscriptionRepositoryException {
        ITopicComparison topicComparisonStategy = null;
        if (dialect.contentEquals(RootTopicExtractor.DIALECT_SIMPLE)) {
            topicComparisonStategy = new TopicComparisonExactMatchStrategy();
        } else if (dialect.contentEquals(RootTopicExtractor.DIALECT_SIMPLE_MISSPELLED)) {
            log.warn("Dialect unknown ('" + dialect + ", but assumed to be '" + RootTopicExtractor.DIALECT_SIMPLE + "'");
            topicComparisonStategy = new TopicComparisonExactMatchStrategy();
        } else if (dialect.contentEquals(RootTopicExtractor.DIALECT_CONCRETE)) {
            topicComparisonStategy = new TopicComparisonExactMatchStrategy();
        } else if (dialect.contentEquals(RootTopicExtractor.DIALECT_CONCRETE_MISSPELLED)) {
            log.warn("Dialect unknown ('" + dialect + ", but assumed to be '" + RootTopicExtractor.DIALECT_CONCRETE + "'");
            topicComparisonStategy = new TopicComparisonExactMatchStrategy();
        } else if (dialect.contentEquals(RootTopicExtractor.DIALECT_FULL)) {
            //temp only
            topicComparisonStategy = new TopicComparisonExactMatchStrategy();
        } else if (dialect.contentEquals(RootTopicExtractor.DIALECT_FULL_MISSPELLED)) {
            log.warn("Dialect unknown ('" + dialect + ", but assumed to be '" + RootTopicExtractor.DIALECT_FULL + "'");
            //temp only
            topicComparisonStategy = new TopicComparisonExactMatchStrategy();
        } else {
            throw new SubscriptionRepositoryException("Unknown dialect + '" + dialect + "'");
        }
        return topicComparisonStategy;
    }
}
