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
