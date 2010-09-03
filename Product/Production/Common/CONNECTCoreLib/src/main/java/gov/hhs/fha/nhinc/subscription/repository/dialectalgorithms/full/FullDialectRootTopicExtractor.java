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

package gov.hhs.fha.nhinc.subscription.repository.dialectalgorithms.full;

import gov.hhs.fha.nhinc.subscription.repository.roottopicextractor.IRootTopicExtractionStrategy;
import gov.hhs.fha.nhinc.subscription.repository.roottopicextractor.RootTopicExtractorHelper;
import gov.hhs.fha.nhinc.subscription.repository.service.SubscriptionRepositoryException;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import org.w3c.dom.Node;

/**
 *
 * @author rayj
 */
public class FullDialectRootTopicExtractor implements IRootTopicExtractionStrategy {

    public String extractRootTopicFromTopicExpressionNode(Node topicExpression) throws SubscriptionRepositoryException {
        String rootTopic = "";
        String topicValue = XmlUtility.getNodeValue(topicExpression);
        String[] topicParts = topicValue.split("/");

        if (topicParts.length >= 1) {
            rootTopic = RootTopicExtractorHelper.ReplaceNamespacePrefixesWithNamespaces(topicParts[0], topicExpression);
        }

        return rootTopic;
    }

}
