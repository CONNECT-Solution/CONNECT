/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.subscription.repository.topicfilter;

import gov.hhs.fha.nhinc.subscription.repository.dialectalgorithms.full.FullDialectTopicFilterStrategy;
import gov.hhs.fha.nhinc.subscription.repository.roottopicextractor.RootTopicExtractor;
import gov.hhs.fha.nhinc.subscription.repository.service.SubscriptionRepositoryException;

/**
 *
 * @author rayj
 */
public class TopicFilterFactory {
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(TopicFilterFactory.class);

    public static ITopicFilterStrategy getTopicFilterStrategy(String dialect) throws SubscriptionRepositoryException {
        ITopicFilterStrategy topicFilterStategy = null;
        if (dialect.contentEquals(RootTopicExtractor.DIALECT_SIMPLE)) {
            ITopicComparison topicComparison = TopicComparisonFactory.getTopicComparisonStrategy(dialect);
            topicFilterStategy = new TopicFilterBasicComparisonStrategy(topicComparison);
        } else if (dialect.contentEquals(RootTopicExtractor.DIALECT_SIMPLE_MISSPELLED)) {
            log.warn("Dialect unknown ('" + dialect + ", but assumed to be '" + RootTopicExtractor.DIALECT_SIMPLE + "'");
            ITopicComparison topicComparison = TopicComparisonFactory.getTopicComparisonStrategy(dialect);
            topicFilterStategy = new TopicFilterBasicComparisonStrategy(topicComparison);
        } else if (dialect.contentEquals(RootTopicExtractor.DIALECT_CONCRETE)) {
            ITopicComparison topicComparison = TopicComparisonFactory.getTopicComparisonStrategy(dialect);
            topicFilterStategy = new TopicFilterBasicComparisonStrategy(topicComparison);
        } else if (dialect.contentEquals(RootTopicExtractor.DIALECT_CONCRETE_MISSPELLED)) {
            log.warn("Dialect unknown ('" + dialect + ", but assumed to be '" + RootTopicExtractor.DIALECT_CONCRETE + "'");
            ITopicComparison topicComparison = TopicComparisonFactory.getTopicComparisonStrategy(dialect);
            topicFilterStategy = new TopicFilterBasicComparisonStrategy(topicComparison);
        } else if (dialect.contentEquals(RootTopicExtractor.DIALECT_FULL)) {
            topicFilterStategy = new FullDialectTopicFilterStrategy();
        } else if (dialect.contentEquals(RootTopicExtractor.DIALECT_FULL_MISSPELLED)) {
            log.warn("Dialect unknown ('" + dialect + ", but assumed to be '" + RootTopicExtractor.DIALECT_FULL + "'");
            topicFilterStategy = new FullDialectTopicFilterStrategy();
        } else {
            throw new SubscriptionRepositoryException("Unknown dialect + '" + dialect + "'");
        }
        return topicFilterStategy;
    }
}
