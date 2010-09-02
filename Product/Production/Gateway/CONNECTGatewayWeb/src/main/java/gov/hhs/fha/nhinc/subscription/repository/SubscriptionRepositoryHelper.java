/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.subscription.repository;

import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionType;

/**
 * Helper class for subscrption repository operations
 * 
 * @author Neil Webb
 */
public class SubscriptionRepositoryHelper extends BaseSubscriptionRepositoryHelper
{
    public SubscriptionRepositoryHelper() throws SubscriptionRepositoryException
    {
        super();
    }

    @Override
    protected SubscriptionType getSubscriptionType()
    {
        return SubscriptionType.SUBSCRIPTION;
    }
}
