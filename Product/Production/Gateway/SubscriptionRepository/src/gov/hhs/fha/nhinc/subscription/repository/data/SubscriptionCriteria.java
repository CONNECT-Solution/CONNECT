package gov.hhs.fha.nhinc.subscription.repository.data;

import gov.hhs.fha.nhinc.subscription.repository.SubscriptionRepositoryException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Data class for subscription criteria
 * 
 * @author Neil Webb
 */
public class SubscriptionCriteria implements Serializable
{
    private static final long serialVersionUID = 2035932112955401949L;
    private Patient subscriberPatient;
    private Patient subscribeePatient;
    private List<Criterion> criteria;
    private TopicExpression topicExpression;
    private SubscriptionPolicy subscriptionPolicy;

    public TopicExpression getTopicExpression()
    {
        return topicExpression;
    }

    public void setTopicExpression(TopicExpression topicExpression)
    {
        this.topicExpression = topicExpression;
    }

    public Patient getSubscriberPatient()
    {
        return subscriberPatient;
    }

    public void setSubscriberPatient(Patient patient)
    {
        this.subscriberPatient = patient;
    }

    public Patient getSubscribeePatient()
    {
        return subscribeePatient;
    }

    public void setSubscribeePatient(Patient patient)
    {
        this.subscribeePatient = patient;
    }

    public List<Criterion> getCriteria()
    {
        if (criteria == null)
        {
            criteria = new ArrayList<Criterion>();
        }
        return criteria;
    }

    public void setCriteria(List<Criterion> criteria)
    {
        this.criteria = criteria;
    }

    public SubscriptionPolicy getSubscriptionPolicy()
    {
        return subscriptionPolicy;
    }

    public void setSubscriptionPolicy(SubscriptionPolicy subscriptionPolicy)
    {
        this.subscriptionPolicy = subscriptionPolicy;
    }

    public void addCriterion(Criterion criterion) throws SubscriptionRepositoryException
    {
        // Validate criterion object
        if (criterion == null)
        {
            throw new SubscriptionRepositoryException("Attempted to add null criterion to subscription criteria list");
        }

        // Validate key
        if ((criterion.getKey() == null) || "".equals(criterion.getKey().trim()))
        {
            throw new SubscriptionRepositoryException("Attempted to add criterion with an invalid key to subscription criteria list - key: " + criterion.getKey());
        }
        else
        {
            // Trim key prior to storage
            criterion.setKey(criterion.getKey().trim());
        }

        // Valiedate value
        if ((criterion.getValue() == null) || "".equals(criterion.getValue().trim()))
        {
            throw new SubscriptionRepositoryException("Attempted to add criterion to the subscription criteria list with an invalid value for key: " + criterion.getKey());
        }
        getCriteria().add(criterion);
    }

    public void removeCriterion(Criterion criterion)
    {
        if (criterion != null)
        {
            Iterator<Criterion> iter = getCriteria().iterator();
            while (iter.hasNext())
            {
                Criterion c = iter.next();
                if (c.equals(criterion))
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
        final SubscriptionCriteria other = (SubscriptionCriteria) obj;
        if (this.subscriberPatient != other.subscriberPatient && (this.subscriberPatient == null || !this.subscriberPatient.equals(other.subscriberPatient)))
        {
            return false;
        }
        if (this.subscribeePatient != other.subscribeePatient && (this.subscribeePatient == null || !this.subscribeePatient.equals(other.subscribeePatient)))
        {
            return false;
        }
        if (this.criteria != other.criteria && (this.criteria == null || !this.criteria.equals(other.criteria)))
        {
            return false;
        }
        if (this.topicExpression != other.topicExpression && (this.topicExpression == null || !this.topicExpression.equals(other.topicExpression)))
        {
            return false;
        }
        if (this.subscriptionPolicy != other.subscriptionPolicy && (this.subscriptionPolicy == null || !this.subscriptionPolicy.equals(other.subscriptionPolicy)))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 97 * hash + (this.subscriberPatient != null ? this.subscriberPatient.hashCode() : 0);
        hash = 97 * hash + (this.subscribeePatient != null ? this.subscribeePatient.hashCode() : 0);
        hash = 97 * hash + (this.criteria != null ? this.criteria.hashCode() : 0);
        return hash;
    }
}
