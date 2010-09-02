/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.subscription.repository.data;

import java.io.Serializable;

/**
 * Data class for a subscription item
 * 
 * @author Neil Webb
 */
public class SubscriptionItem implements Serializable
{
    private static final long serialVersionUID = 7601908766151278860L;
    private SubscriptionParticipant subscriber;
    private SubscriptionParticipant subscribee;
    private SubscriptionCriteria subscriptionCriteria;
    private SubscriptionReference subscriptionReference;
    private SubscriptionReference parentSubscriptionReference;

    public SubscriptionParticipant getSubscriber()
    {
        return subscriber;
    }

    public void setSubscriber(SubscriptionParticipant subscriber)
    {
        this.subscriber = subscriber;
    }

    public SubscriptionParticipant getSubscribee()
    {
        return subscribee;
    }

    public void setSubscribee(SubscriptionParticipant subscribee)
    {
        this.subscribee = subscribee;
    }

    public SubscriptionCriteria getSubscriptionCriteria()
    {
        return subscriptionCriteria;
    }

    public void setSubscriptionCriteria(SubscriptionCriteria subscriptionCriteria)
    {
        this.subscriptionCriteria = subscriptionCriteria;
    }

    public SubscriptionReference getSubscriptionReference()
    {
        return subscriptionReference;
    }

    public void setSubscriptionReference(SubscriptionReference subscriptionReference)
    {
        this.subscriptionReference = subscriptionReference;
    }

    public SubscriptionReference getParentSubscriptionReference()
    {
        return parentSubscriptionReference;
    }

    public void setParentSubscriptionReference(SubscriptionReference parentSubscriptionReference)
    {
        this.parentSubscriptionReference = parentSubscriptionReference;
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
        final SubscriptionItem other = (SubscriptionItem) obj;
        if (this.subscriber != other.subscriber && (this.subscriber == null || !this.subscriber.equals(other.subscriber)))
        {
            return false;
        }
        if (this.subscribee != other.subscribee && (this.subscribee == null || !this.subscribee.equals(other.subscribee)))
        {
            return false;
        }
        if (this.subscriptionCriteria != other.subscriptionCriteria && (this.subscriptionCriteria == null || !this.subscriptionCriteria.equals(other.subscriptionCriteria)))
        {
            return false;
        }
        if (this.subscriptionReference != other.subscriptionReference && (this.subscriptionReference == null || !this.subscriptionReference.equals(other.subscriptionReference)))
        {
            return false;
        }
        if (this.parentSubscriptionReference != other.parentSubscriptionReference && (this.parentSubscriptionReference == null || !this.parentSubscriptionReference.equals(other.parentSubscriptionReference)))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 83 * hash + (this.subscriber != null ? this.subscriber.hashCode() : 0);
        hash = 83 * hash + (this.subscribee != null ? this.subscribee.hashCode() : 0);
        hash = 83 * hash + (this.subscriptionCriteria != null ? this.subscriptionCriteria.hashCode() : 0);
        hash = 83 * hash + (this.subscriptionReference != null ? this.subscriptionReference.hashCode() : 0);
        hash = 83 * hash + (this.parentSubscriptionReference != null ? this.parentSubscriptionReference.hashCode() : 0);
        return hash;
    }
    
    
}
