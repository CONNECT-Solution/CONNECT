package gov.hhs.fha.nhinc.subscription.repository.data;

import java.util.Date;

/**
 * Data storage object used for database interactions
 * 
 * @author Neil Webb
 */
public class SubscriptionStorageItem
{
    private String recordId;
    private String subscriptionId;
    private String subscribeXML;
    private String subscriptionReferenceXML;
    private String rootTopic;
    private String parentSubscriptionId;
    private String parentSubscriptionReferenceXML;
    private String consumer;
    private String producer;
    private String patientId;
    private String patientAssigningAuthority;
    private String targets;
    private Date creationDate;

    public String getRecordId()
    {
        return recordId;
    }

    public void setRecordId(String recordId)
    {
        this.recordId = recordId;
    }

    public String getSubscriptionId()
    {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId)
    {
        this.subscriptionId = subscriptionId;
    }

    public String getSubscribeXML()
    {
        return subscribeXML;
    }

    public void setSubscribeXML(String subscribeXML)
    {
        this.subscribeXML = subscribeXML;
    }

    public String getSubscriptionReferenceXML()
    {
        return subscriptionReferenceXML;
    }

    public void setSubscriptionReferenceXML(String subscriptionReferenceXML)
    {
        this.subscriptionReferenceXML = subscriptionReferenceXML;
    }

    public String getRootTopic()
    {
        return rootTopic;
    }

    public void setRootTopic(String rootTopic)
    {
        this.rootTopic = rootTopic;
    }

    public String getParentSubscriptionId()
    {
        return parentSubscriptionId;
    }

    public void setParentSubscriptionId(String parentSubscriptionId)
    {
        this.parentSubscriptionId = parentSubscriptionId;
    }

    public String getParentSubscriptionReferenceXML()
    {
        return parentSubscriptionReferenceXML;
    }

    public void setParentSubscriptionReferenceXML(String parentSubscriptionReferenceXML)
    {
        this.parentSubscriptionReferenceXML = parentSubscriptionReferenceXML;
    }

    public String getConsumer()
    {
        return consumer;
    }

    public void setConsumer(String consumer)
    {
        this.consumer = consumer;
    }

    public String getProducer()
    {
        return producer;
    }

    public void setProducer(String producer)
    {
        this.producer = producer;
    }

    public String getPatientId()
    {
        return patientId;
    }

    public void setPatientId(String patientId)
    {
        this.patientId = patientId;
    }

    public String getPatientAssigningAuthority()
    {
        return patientAssigningAuthority;
    }

    public void setPatientAssigningAuthority(String patientAssigningAuthority)
    {
        this.patientAssigningAuthority = patientAssigningAuthority;
    }

    public String getTargets()
    {
        return targets;
    }

    public void setTargets(String targets)
    {
        this.targets = targets;
    }

    public Date getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }

    @Override
    public int hashCode()
    {
        // TODO: Potential null condition
        return subscriptionId.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        return ((obj == this) || ((obj instanceof SubscriptionStorageItem) && subscriptionId.equals(((SubscriptionStorageItem)obj).getSubscriptionId())));
    }
    

}
