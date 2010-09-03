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
package gov.hhs.fha.nhinc.subscription.repository.roottopicextractor;

import gov.hhs.fha.nhinc.subscription.repository.service.SubscriptionRepositoryException;


/**
 *
 * @author rayj
 */
public interface IRootTopicExtractionStrategy  {
    String extractRootTopicFromTopicExpressionNode(org.w3c.dom.Node topicExpression) throws SubscriptionRepositoryException;
}
