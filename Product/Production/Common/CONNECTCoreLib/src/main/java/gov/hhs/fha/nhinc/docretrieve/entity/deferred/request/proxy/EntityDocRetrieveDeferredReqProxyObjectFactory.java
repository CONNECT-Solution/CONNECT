/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.entity.deferred.request.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredReqProxyObjectFactory extends ComponentProxyObjectFactory {

    private static final String CONFIG_FILE_NAME = "DocumentRetrieveDeferredProxyConfig.xml";
    private static final String BEAN_NAME_ENTITY_DOCRETRIEVE_DEFERRED_REQUEST = "entitydocretrievedeferredreq";

    /**
     * Retrieve an Entity Document Retrieve Deferred Request implementation
     * using the IOC framework.
     * This method retrieves the object from the framework that has an
     * identifier of "entitydocretrievedeferredreq".
     * @return EntityDocRetrieveDeferredReqProxy
     */
    public EntityDocRetrieveDeferredReqProxy getEntityDocRetrieveDeferredReqProxy() {
        return getBean(BEAN_NAME_ENTITY_DOCRETRIEVE_DEFERRED_REQUEST, EntityDocRetrieveDeferredReqProxy.class);
    }

    /**
     * 
     * @return String
     */
    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }
}
