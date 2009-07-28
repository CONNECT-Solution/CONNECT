package gov.hhs.fha.nhinc.subscription.repository;

import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionType;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionRecord;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionRecordList;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionReference;

/**
 * Helper class for subscription reference repository operations
 * 
 * @author Neil Webb
 */
public class SubscriptionReferenceRepositoryHelper extends BaseSubscriptionRepositoryHelper
{
    public SubscriptionReferenceRepositoryHelper() throws SubscriptionRepositoryException
    {
        super();
    }

    public gov.hhs.fha.nhinc.common.subscription.SubscriptionItemsType retrieveByParentSubscriptionReference(gov.hhs.fha.nhinc.common.subscription.SubscriptionReferenceType parentSubscriptionReferenceType)
    {
        gov.hhs.fha.nhinc.common.subscription.SubscriptionItemsType subscriptionItemsType = new gov.hhs.fha.nhinc.common.subscription.SubscriptionItemsType();

        // Transform to subscription reference
        SubscriptionReference subscriptionReference = loadSubscriptionReference(parentSubscriptionReferenceType);
        
        // Retrieve the subscription item
        SubscriptionRecordList subscriptionRecords = subscriptionRepositoryService.retrieveByParentSubscriptionReference(subscriptionReference, getSubscriptionType());
        
        // Transform to subscription item type
        if(subscriptionRecords != null)
        {
            for(SubscriptionRecord record : subscriptionRecords)
            {
                if((record != null) && (record.getSubscription() != null))
                {
                    gov.hhs.fha.nhinc.common.subscription.SubscriptionItemType subscriptionItemType = loadSubscriptionItemType(record.getSubscription());
                    if(subscriptionItemType != null)
                    {
                        subscriptionItemsType.getSubscriptionItem().add(subscriptionItemType);
                    }
                }
            }
        }
        return subscriptionItemsType;
    }

    @Override
    protected SubscriptionType getSubscriptionType()
    {
        return SubscriptionType.SUBSCRIPTION_REFERENCE;
    }
}
