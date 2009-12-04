package gov.hhs.fha.nhinc.subscription.repository.data;

import java.io.Serializable;

/**
 * Storage object for a subscription record
 * 
 * @author Neil Webb
 */
public class SubscriptionRecord implements Serializable
{
    private static final long serialVersionUID = 2604328369853548393L;
    private Long subscriptionId;
    private SubscriptionType type;
    private SubscriptionItem subscription;

    public SubscriptionItem getSubscription()
    {
        return subscription;
    }

    public void setSubscription(SubscriptionItem subscription)
    {
        this.subscription = subscription;
    }

    public Long getSubscriptionId()
    {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId)
    {
        this.subscriptionId = subscriptionId;
    }

    public SubscriptionType getType()
    {
        return type;
    }

    public void setType(SubscriptionType type)
    {
        this.type = type;
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
        final SubscriptionRecord other = (SubscriptionRecord) obj;
        if (this.subscriptionId != other.subscriptionId && (this.subscriptionId == null || !this.subscriptionId.equals(other.subscriptionId)))
        {
            return false;
        }
        if (this.type != other.type)
        {
            return false;
        }
        if (this.subscription != other.subscription && (this.subscription == null || !this.subscription.equals(other.subscription)))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 97 * hash + (this.subscriptionId != null ? this.subscriptionId.hashCode() : 0);
        hash = 97 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 97 * hash + (this.subscription != null ? this.subscription.hashCode() : 0);
        return hash;
    }
    
    
}
