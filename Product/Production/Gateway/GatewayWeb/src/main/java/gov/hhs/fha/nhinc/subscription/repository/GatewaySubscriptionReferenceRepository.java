package gov.hhs.fha.nhinc.subscription.repository;

import javax.jws.WebService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "NhincComponentSubscriptionReferenceRepositoryService", portName = "NhincComponentSubscriptionReferenceRepositoryPortTypeBindingPort", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentsubscriptionreferencerepository.NhincComponentSubscriptionReferenceRepositoryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhinccomponentsubscriptionreferencerepository", wsdlLocation = "WEB-INF/wsdl/GatewaySubscriptionReferenceRepository/NhincComponentSubscriptionReferenceRepository.wsdl")
public class GatewaySubscriptionReferenceRepository
{
    private static Log log = LogFactory.getLog(GatewaySubscriptionReferenceRepository.class);

    public gov.hhs.fha.nhinc.common.subscription.SubscriptionReferenceType storeSubscriptionReference(gov.hhs.fha.nhinc.common.subscription.SubscriptionItemType subscriptionItem)
    {
        gov.hhs.fha.nhinc.common.subscription.SubscriptionReferenceType subscriptionReference = null;
        try
        {
            subscriptionReference = new SubscriptionReferenceRepositoryHelper().storeSubscription(subscriptionItem);
        }
        catch (SubscriptionRepositoryException ex)
        {
            log.error(ex.getMessage(), ex);
            // Return empty reference until fault handling is implemented
            subscriptionReference = new gov.hhs.fha.nhinc.common.subscription.SubscriptionReferenceType();
        }
        return subscriptionReference;
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType deleteSubscriptionReference(gov.hhs.fha.nhinc.common.subscription.DeleteSubscriptionMessageRequestType deleteSubscriptionMessage)
    {
        gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType ack = null;
        try
        {
            if(deleteSubscriptionMessage != null)
            {
                ack = new SubscriptionReferenceRepositoryHelper().deleteSubscription(deleteSubscriptionMessage.getSubscriptionReference());
            }
        }
        catch (SubscriptionRepositoryException ex)
        {
            log.error(ex.getMessage(), ex);
            // Create an ack here until proper fault handling is established.
            ack = new gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType();
        }
        return ack;
    }

    public gov.hhs.fha.nhinc.common.subscription.SubscriptionItemsType retrieveByCriteria(gov.hhs.fha.nhinc.common.subscription.SubscriptionCriteriaType subscriptionCriteria)
    {
        gov.hhs.fha.nhinc.common.subscription.SubscriptionItemsType subscriptionItems = null;
        try
        {
            subscriptionItems = new SubscriptionReferenceRepositoryHelper().retrieveByCriteria(subscriptionCriteria);
        }
        catch (SubscriptionRepositoryException ex)
        {
            log.error(ex.getMessage(), ex);
            // Create empty response until fault handling is added
            subscriptionItems = new gov.hhs.fha.nhinc.common.subscription.SubscriptionItemsType();
        }
        return subscriptionItems;
    }

    public gov.hhs.fha.nhinc.common.subscription.SubscriptionItemType retrieveBySubscriptionReference(gov.hhs.fha.nhinc.common.subscription.RetrieveBySubscriptionReferenceRequestMessageType retrieveBySubscriptionReferenceMessage)
    {
        gov.hhs.fha.nhinc.common.subscription.SubscriptionItemType subscriptionItem = null;
        try
        {
            if(retrieveBySubscriptionReferenceMessage != null)
            {
                subscriptionItem = new SubscriptionReferenceRepositoryHelper().retrieveBySubscriptionReference(retrieveBySubscriptionReferenceMessage.getSubscriptionReference());
            }
        }
        catch (SubscriptionRepositoryException ex)
        {
            log.error(ex.getMessage(), ex);
            // Create empty response until fault handling is added
            subscriptionItem = new gov.hhs.fha.nhinc.common.subscription.SubscriptionItemType();
        }
        return subscriptionItem;
    }

    public gov.hhs.fha.nhinc.common.subscription.SubscriptionItemsType retrieveByParentSubscriptionReference(gov.hhs.fha.nhinc.common.subscription.RetrieveByParentSubscriptionReferenceMessageType retrieveByParentSubscriptionReferenceMessage)
    {
        gov.hhs.fha.nhinc.common.subscription.SubscriptionItemsType subscriptionItems = null;
        try
        {
            if(retrieveByParentSubscriptionReferenceMessage != null)
            {
                subscriptionItems = new SubscriptionReferenceRepositoryHelper().retrieveByParentSubscriptionReference(retrieveByParentSubscriptionReferenceMessage.getSubscriptionReference());
            }
        }
        catch (SubscriptionRepositoryException ex)
        {
            log.error(ex.getMessage(), ex);
            // Create empty response until fault handling is added
            subscriptionItems = new gov.hhs.fha.nhinc.common.subscription.SubscriptionItemsType();
        }
        return subscriptionItems;
    }

}
