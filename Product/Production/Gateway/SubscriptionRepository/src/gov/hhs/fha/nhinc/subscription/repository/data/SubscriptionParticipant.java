package gov.hhs.fha.nhinc.subscription.repository.data;

import java.io.Serializable;

/**
 * Class for participants in a subscription
 * 
 * @author Neil Webb
 */
public class SubscriptionParticipant implements Serializable
{
    private static final long serialVersionUID = 6464393602956231914L;
    private Community community;
    private String notificationEndpointAddress;
    private String userAddress;

    public Community getCommunity()
    {
        return community;
    }

    public void setCommunity(Community community)
    {
        this.community = community;
    }

    public String getNotificationEndpointAddress()
    {
        return notificationEndpointAddress;
    }

    public void setNotificationEndpointAddress(String notificationEndpointAddress)
    {
        this.notificationEndpointAddress = notificationEndpointAddress;
    }

    public String getUserAddress()
    {
        return userAddress;
    }

    public void setUserAddress(String userAddress)
    {
        this.userAddress = userAddress;
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
        final SubscriptionParticipant other = (SubscriptionParticipant) obj;
        if (this.community != other.community && (this.community == null || !this.community.equals(other.community)))
        {
            return false;
        }
        if (this.notificationEndpointAddress != other.notificationEndpointAddress && (this.notificationEndpointAddress == null || !this.notificationEndpointAddress.equals(other.notificationEndpointAddress)))
        {
            return false;
        }
        if (this.userAddress != other.userAddress && (this.userAddress == null || !this.userAddress.equals(other.userAddress)))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 19 * hash + (this.community != null ? this.community.hashCode() : 0);
        hash = 19 * hash + (this.notificationEndpointAddress != null ? this.notificationEndpointAddress.hashCode() : 0);
        hash = 19 * hash + (this.userAddress != null ? this.userAddress.hashCode() : 0);
        return hash;
    }
    
    
}
