/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.subscription.repository.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Data object for a subscription policy
 * 
 * @author Neil Webb
 */
public class SubscriptionPolicy
{
    private List<SubscriptionPolicyItem> policyItems;

    public List<SubscriptionPolicyItem> getPolicyItems()
    {
        if(policyItems == null)
        {
            policyItems = new ArrayList<SubscriptionPolicyItem>();
        }
        return policyItems;
    }

    public void setPolicyItems(List<SubscriptionPolicyItem> policyItems)
    {
        this.policyItems = policyItems;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final SubscriptionPolicy other = (SubscriptionPolicy) obj;
        if (this.policyItems != other.policyItems && (this.policyItems == null || !this.policyItems.equals(other.policyItems)))
        {
            return false;
        }
        return true;
    }

}
