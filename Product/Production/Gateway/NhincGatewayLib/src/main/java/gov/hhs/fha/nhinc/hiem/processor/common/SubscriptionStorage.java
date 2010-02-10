package gov.hhs.fha.nhinc.hiem.processor.common;

import gov.hhs.fha.nhinc.subscription.repository.data.HiemSubscriptionItem;
import gov.hhs.fha.nhinc.subscription.repository.service.SubscriptionRepositoryException;
import gov.hhs.fha.nhinc.subscription.repository.service.HiemSubscriptionRepositoryService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3._2005._08.addressing.EndpointReferenceType;

/**
 * Store a subscription item to the subscription repository
 * 
 * @author Neil Webb
 */
public class SubscriptionStorage
{
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(SubscriptionStorage.class);

    /**
     * Store a subscription item to the subscription repository.
     *
     * @param subscriptionItem Subscription item containing subscription information
     * @return Subscription reference
     */
    public EndpointReferenceType storeSubscriptionItem(HiemSubscriptionItem subscriptionItem)
    {
        EndpointReferenceType epr = null;
        if(subscriptionItem != null)
        {
            try
            {
                HiemSubscriptionRepositoryService service = new HiemSubscriptionRepositoryService();
                log.debug("Calling SubscriptionRepositoryService.saveSubscriptionToConnect");
                epr = service.saveSubscriptionToConnect(subscriptionItem);
            }
            catch (Exception ex)
            {
                Logger.getLogger(SubscriptionStorage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            log.debug("Subscription item was null in storeSubscriptionItem");
        }
        return epr;
    }

    /**
     * Store a child subscription item to the subscription repository.
     *
     * @param subscriptionItem Subscription item containing subscription information
     */
    public void storeExternalSubscriptionItem(HiemSubscriptionItem subscriptionItem)
    {
        if(subscriptionItem != null)
        {
            try
            {
                HiemSubscriptionRepositoryService service = new HiemSubscriptionRepositoryService();
                log.debug("Calling SubscriptionRepositoryService.saveSubscriptionToExternal");
                service.saveSubscriptionToExternal(subscriptionItem);
            }
            catch (SubscriptionRepositoryException ex)
            {
                Logger.getLogger(SubscriptionStorage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            log.debug("Subscription item was null in storeExternalSubscriptionItem");
        }
    }
}
