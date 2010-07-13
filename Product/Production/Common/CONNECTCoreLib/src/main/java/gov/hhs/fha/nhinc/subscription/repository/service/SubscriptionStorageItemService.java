package gov.hhs.fha.nhinc.subscription.repository.service;

import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.subscription.repository.dao.SubscriptionStorageItemDao;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionStorageItem;
import java.io.ByteArrayInputStream;
import java.util.List;
import org.xml.sax.InputSource;

/**
 * SubscriptionStorageItem persistence service
 *
 * NOTE: Do not access this class directly. Use the SubscriptionItemService instead.
 * 
 * @author Neil Webb
 */
public class SubscriptionStorageItemService {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(SubscriptionStorageItemService.class);

    /**
     * Save a subscription storage item
     * 
     * @param subscriptionItem Object to save
     */
    public void save(SubscriptionStorageItem subscriptionItem) {
        SubscriptionStorageItemDao dao = new SubscriptionStorageItemDao();
        if ((subscriptionItem != null) && (subscriptionItem.getSubscriptionId() == null)) {
            //subscriptionItem.setSubscriptionId(generateSubscriptionId());
            //TODO: Set subscription reference
        }
        dao.save(subscriptionItem);
    }

    /**
     * Retrieve a subscription storage item by identifier
     *
     * @param subscriptionId Subscription identifier
     * @return Retrieved subscription
     */
    public SubscriptionStorageItem findById(String subscriptionId) {
        SubscriptionStorageItemDao dao = new SubscriptionStorageItemDao();
        return dao.findById(subscriptionId);
    }

    /**
     * Retrieve a subscription storage item by subscription identifier
     *
     * @param subscriptionId Subscription identifier
     * @return Retrieved subscriptions
     */
    public List<SubscriptionStorageItem> findBySubscriptionId(String subscriptionId) {
        SubscriptionStorageItemDao dao = new SubscriptionStorageItemDao();
        return dao.findBySubscriptionId(subscriptionId);
    }

    public List<SubscriptionStorageItem> findByRootTopic(String rootTopic, String producer) {
        SubscriptionStorageItemDao dao = new SubscriptionStorageItemDao();
        return dao.findByRootTopic(rootTopic, producer);
    }

    public List<SubscriptionStorageItem> findByProducer(String producer) {
        SubscriptionStorageItemDao dao = new SubscriptionStorageItemDao();
        return dao.findByProducer(producer);
    }

    /**
     * Retrieve a subscription storage item by parent subscription identifier
     *
     * @param parentSubscriptionId Parent subscription identifier
     * @return Retrieved subscriptions
     */
    public List<SubscriptionStorageItem> findByParentSubscriptionId(String parentSubscriptionId) {
        SubscriptionStorageItemDao dao = new SubscriptionStorageItemDao();
        return dao.findByParentSubscriptionId(parentSubscriptionId);
    }

    public SubscriptionStorageItem retrieveByLocalSubscriptionReferenceParameters(ReferenceParametersElements referenceParametersElements) throws SubscriptionRepositoryException {
        SubscriptionStorageItem subscriptionItem = null;
        String subscriptionId = SubscriptionIdHelper.extractSubscriptionIdFromReferenceParametersElements(referenceParametersElements);
        if (subscriptionId != null) {
            List<SubscriptionStorageItem> subscriptionItems = findBySubscriptionId(subscriptionId);
            if ((subscriptionItems != null) && !subscriptionItems.isEmpty()) {
                // TODO: What if there are multiple returned - possible???
                subscriptionItem = subscriptionItems.get(0);
            }
        } else {
            //todo: throw proper fault (unable to locate resource or something..
            throw new SubscriptionRepositoryException("Unable to find subscription");
        }
        return subscriptionItem;
    }

    public SubscriptionStorageItem retrieveByLocalSubscriptionReference(String subscriptionReferenceXML) throws SubscriptionRepositoryException {
        SubscriptionStorageItem subscriptionItem = null;
        String subscriptionId = SubscriptionIdHelper.extractSubscriptionIdFromSubscriptionReferenceXml(subscriptionReferenceXML);
        if (subscriptionId != null) {
            List<SubscriptionStorageItem> subscriptionItems = findBySubscriptionId(subscriptionId);
            if ((subscriptionItems != null) && !subscriptionItems.isEmpty()) {
                // TODO: What if there are multiple returned - possible???
                subscriptionItem = subscriptionItems.get(0);
            }
        } else {
            // TODO: Perform query on full sub ref
            throw new SubscriptionRepositoryException("Retreive by full subscription reference not yet implemented.");
        }
        return subscriptionItem;
    }

    /**
     * Retrieve subscriptions from parent subscription reference. The parent
     * subscription reference will always be generated by this system so
     * efficient matching may be performed by extracting the subscription id.
     * No support for a variant form of subscription reference is required.
     *
     * @param parentSubscriptionReferenceXML Parent subscription reference
     * @return Matching subscriptions
     * @throws SubscriptionRepositoryException 
     */
    public List<SubscriptionStorageItem> retrieveByParentSubscriptionReference(String parentSubscriptionReferenceXML) throws SubscriptionRepositoryException {
        List<SubscriptionStorageItem> subscriptionItems = null;
        if(NullChecker.isNotNullish(parentSubscriptionReferenceXML))
        {
            String parentSubscriptionId = SubscriptionIdHelper.extractSubscriptionIdFromSubscriptionReferenceXml(parentSubscriptionReferenceXML);

            if (parentSubscriptionId != null) {
                SubscriptionStorageItemService storageService = new SubscriptionStorageItemService();
                subscriptionItems = storageService.findByParentSubscriptionId(parentSubscriptionId);
            } else {
                // TODO: Perform query on full sub ref
                throw new SubscriptionRepositoryException("Retreive by full parent subscription reference not yet implemented.");
            }
        }

        return subscriptionItems;
    }

    /**
     * Delete a subscription storage item
     * 
     * @param subscriptionItem Subscription storage item to delete
     */
    public void delete(SubscriptionStorageItem subscriptionItem) {
        SubscriptionStorageItemDao dao = new SubscriptionStorageItemDao();
        dao.delete(subscriptionItem);
    }

    private String generateSubscriptionId() {

        java.rmi.server.UID uid = new java.rmi.server.UID();
        return uid.toString();
    }

    /**
     * Retrieve the number of subscriptions in the repository.
     *
     * @return Subscription count
     */
    public int subscriptionCount() {
        SubscriptionStorageItemDao dao = new SubscriptionStorageItemDao();
        return dao.subscriptionCount();
    }

    /**
     * Empty the subscription repository.
     */
    public void emptyRepository() {
        SubscriptionStorageItemDao dao = new SubscriptionStorageItemDao();
        dao.emptyRepository();
    }
}
