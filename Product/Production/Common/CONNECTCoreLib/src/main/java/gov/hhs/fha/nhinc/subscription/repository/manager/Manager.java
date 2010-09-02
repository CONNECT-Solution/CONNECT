/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.subscription.repository.manager;

import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.subscription.repository.SubscriptionRepositoryException;
import gov.hhs.fha.nhinc.subscription.repository.data.ReferenceParameter;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionItem;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionRecord;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionRecordList;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionReference;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessEntity;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessService;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Data manager for file-based subscriptions.
 * 
 * @author dunnek
 */
public class Manager {

    private static Log log = LogFactory.getLog(DataSaver.class);
    public static final String CONST_DEFAULT_SUBSCRIBE_LIST = "subscriptionList.xml";
    private static final String CONST_UNSUBSCRIBE_SERVICE_NAME = "subscriptionmanager";
            
    // Initalize to defalut list
    private static String dataSource = CONST_DEFAULT_SUBSCRIBE_LIST;

    /**
     * Default constructor - the default subscription list is used.
     */
    public Manager() {
        super();
    }

    /**
     * Parameterized constructor
     * 
     * @param dataSourceName File name to use for the subscription list
     */
    public Manager(String dataSourceName) {
        dataSource = dataSourceName;
    }

    /**
     * Load the default subscription list
     * 
     * @return Subscription list
     */
    public SubscriptionRecordList loadSubscriptionList() {
        return new DataSaver().loadList(dataSource);
    }

    /**
     * Load a subscription list using the provided source name
     * 
     * @param dataSourceName Name of the file containing the subscriptions
     * @return Subscription list
     */
    public SubscriptionRecordList loadSubscriptionList(String dataSourceName) {
        return new DataSaver().loadList(dataSourceName);
    }

    /**
     * Add a subscription to the default subscription list
     * 
     * @param subscriptionRecord New subscription record to add
     */
    public void addSubscription(SubscriptionRecord subscriptionRecord) {
        SubscriptionRecordList subscriptions;

        subscriptions = loadSubscriptionList(dataSource);

        addSubscription(subscriptionRecord, subscriptions);
    }

    /**
     * Add a subscription to the provided subscription list
     * 
     * @param subscriptionRecord New subscription record
     * @param subscriptionRecordList Subscription record list
     */
    public void addSubscription(SubscriptionRecord subscriptionRecord, SubscriptionRecordList subscriptionRecordList) {
        String subscriptionId = null;
        CMBusinessEntity oCMBusinessEntity = null;
        String url = null;
        String myCommunityId = null;

        if ((subscriptionRecord != null) && (subscriptionRecord.getSubscription() != null)) {
            SubscriptionItem subscriptionItem = subscriptionRecord.getSubscription();

            SubscriptionReference subscriptionReference = subscriptionItem.getSubscriptionReference();
            if (subscriptionReference == null) {
                subscriptionReference = new SubscriptionReference();
                subscriptionItem.setSubscriptionReference(subscriptionReference);
                subscriptionId = generateSubscriptionId();
                ReferenceParameter param = new ReferenceParameter();
                param.setNamespacePrefix("nhin");
                param.setNamespace("http://www.hhs.gov/healthit/nhin");
                param.setElementName("SubscriptionId");
                param.setValue(subscriptionId);
                try {
                    subscriptionReference.addReferenceParameter(param);
                } catch (SubscriptionRepositoryException ex) {
                    log.error("Error adding subscription reference parameter to list", ex);
                }

                try {
                    myCommunityId = PropertyAccessor.getProperty("gateway", "localHomeCommunityId");
                    if (myCommunityId != null) {
                        try {
                            oCMBusinessEntity = ConnectionManagerCache.getBusinessEntityByServiceName(myCommunityId, CONST_UNSUBSCRIBE_SERVICE_NAME);
                            if (oCMBusinessEntity != null &&
                                    oCMBusinessEntity.getBusinessServices() != null &&
                                    oCMBusinessEntity.getBusinessServices().getBusinessService() != null &&
                                    oCMBusinessEntity.getBusinessServices().getBusinessService().size() > 0 &&
                                    oCMBusinessEntity.getBusinessServices().getBusinessService().get(0) != null) {
                                CMBusinessService busService = oCMBusinessEntity.getBusinessServices().getBusinessService().get(0);

                                if (busService != null &&
                                        busService.getBindingTemplates() != null &&
                                        busService.getBindingTemplates().getBindingTemplate() != null &&
                                        busService.getBindingTemplates().getBindingTemplate().size() > 0 &&
                                        busService.getBindingTemplates().getBindingTemplate().get(0) != null &&
                                        busService.getBindingTemplates().getBindingTemplate().get(0).getEndpointURL() != null &&
                                        busService.getBindingTemplates().getBindingTemplate().get(0).getEndpointURL().length() > 0) {
                                    url = busService.getBindingTemplates().getBindingTemplate().get(0).getEndpointURL();
                                }
                            }
                        } catch (Throwable t) {
                            String sErrorMessage = "Failed to retrieve business entity.  Error: " + t.getMessage();
                            log.error(sErrorMessage, t);
                        }
                    }
                    if (url != null &&
                            url.length() > 0) {
                        log.info("Returning Endpoint URL: " + url);
                        subscriptionReference.setSubscriptionManagerEndpointAddress(url);
                    } else {
                        log.error("URL not defined for service " + CONST_UNSUBSCRIBE_SERVICE_NAME);
                        subscriptionReference.setSubscriptionManagerEndpointAddress("https://www.somewhere.org/SubscriptionManager");
                    }
                } catch (PropertyAccessException ex) {
                    log.error("Failed to retrieve the subscription manager endpoint address: " + ex.getMessage(), ex);
                    subscriptionReference.setSubscriptionManagerEndpointAddress("https://www.somewhere.org/SubscriptionManager");
                }
            }
        }

        subscriptionRecordList.add(subscriptionRecord);

        saveSubscriptions(subscriptionRecordList);
    }

    /**
     * Remove a subscription from a given subscription list
     * 
     * @param subRef Subscription reference
     * @param subscriptionRecordList Subscription record list
     */
    public void removeSubscription(SubscriptionReference subRef, SubscriptionRecordList subscriptionRecordList) {
        if ((subscriptionRecordList != null) && (!subscriptionRecordList.isEmpty()) && (subRef != null) && (!subRef.getReferenceParameters().isEmpty())) {
            SubscriptionRecordList removeList = new SubscriptionRecordList();

            for (SubscriptionRecord subscriptionRecord : subscriptionRecordList) {
                if ((subscriptionRecord != null) && (subscriptionRecord.getSubscription() != null) && (subscriptionRecord.getSubscription().getSubscriptionReference() != null) && (subRef.getReferenceParameters().containsAll(subscriptionRecord.getSubscription().getSubscriptionReference().getReferenceParameters()))) {
                    removeList.add(subscriptionRecord);
                }
            }

            for (Iterator removeIter = removeList.iterator(); removeIter.hasNext();) {
                subscriptionRecordList.remove(removeIter.next());
            }

            saveSubscriptions(subscriptionRecordList);
        }
    }

    /**
     * Remove a subscription from the default file repository
     * 
     * @param subRef Subscription reference
     */
    public void removeSubscription(SubscriptionReference subRef) {
        SubscriptionRecordList subscriptions;

        subscriptions = loadSubscriptionList(dataSource);

        removeSubscription(subRef, subscriptions);
    }

    private void saveSubscriptions(SubscriptionRecordList subscriptionList) {
        new DataSaver().saveList(subscriptionList, dataSource);
    }

    private String generateSubscriptionId() {

        java.rmi.server.UID uid = new java.rmi.server.UID();
        return uid.toString();
    }
}
