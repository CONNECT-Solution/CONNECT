package gov.hhs.fha.nhinc.subscription.repository.data;

import gov.hhs.fha.nhinc.subscription.repository.SubscriptionRepositoryException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Data class for a subscription reference
 * 
 * @author Neil Webb
 */
public class SubscriptionReference implements Serializable
{
    private static final long serialVersionUID = -5693688804432186068L;
    private String subscriptionManagerEndpointAddress;
    private List<ReferenceParameter> referenceParameters;

    public String getSubscriptionManagerEndpointAddress()
    {
        return subscriptionManagerEndpointAddress;
    }

    public void setSubscriptionManagerEndpointAddress(String subscriptionManagerEndpointAddress)
    {
        this.subscriptionManagerEndpointAddress = subscriptionManagerEndpointAddress;
    }

    public List<ReferenceParameter> getReferenceParameters()
    {
        if (referenceParameters == null)
        {
            referenceParameters = new ArrayList<ReferenceParameter>();
        }
        return referenceParameters;
    }

    public void setReferenceParameters(List<ReferenceParameter> referenceParameters)
    {
        this.referenceParameters = referenceParameters;
    }
    
    public void addReferenceParameter(ReferenceParameter refParam) throws SubscriptionRepositoryException
    {
        if (refParam == null)
        {
            throw new SubscriptionRepositoryException("Attempted to add null reference parameter");
        }
        getReferenceParameters().add(refParam);
    }

    public void removeReferenceParameter(ReferenceParameter refParam)
    {
        if (refParam != null)
        {
            Iterator<ReferenceParameter> iter = getReferenceParameters().iterator();
            while (iter.hasNext())
            {
                ReferenceParameter rp = iter.next();
                if (refParam.equals(rp))
                {
                    iter.remove();
                    break;
                }
            }
        }
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
        final SubscriptionReference other = (SubscriptionReference) obj;
        if (this.subscriptionManagerEndpointAddress != other.subscriptionManagerEndpointAddress && (this.subscriptionManagerEndpointAddress == null || !this.subscriptionManagerEndpointAddress.equals(other.subscriptionManagerEndpointAddress)))
        {
            System.out.println("Subscription manager endpoint address did not equal");
            System.out.println("This subscription manager endpoint address: " + this.subscriptionManagerEndpointAddress);
            System.out.println("Other subscription manager endpoint address: " + other.subscriptionManagerEndpointAddress);
            return false;
        }
        if (this.referenceParameters != other.referenceParameters && (this.referenceParameters == null || !this.referenceParameters.equals(other.referenceParameters)))
        {
            System.out.println("Reference parameters did not equal");
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 97 * hash + (this.subscriptionManagerEndpointAddress != null ? this.subscriptionManagerEndpointAddress.hashCode() : 0);
        hash = 97 * hash + (this.referenceParameters != null ? this.referenceParameters.hashCode() : 0);
        return hash;
    }
}
