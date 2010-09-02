/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docrepositoryadapter.proxy;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 *
 *
 * @author Neil Webb
 */
public class AdapterDocumentRepositoryProxyObjectFactory
{
    private static final String CONFIG_FILE_NAME = "AdapterDocumentRepositoryProxyConfig.xml";
    private static final String BEAN_NAME_ADAPTER_DOCUMENT_REPOSITORY = "adapterdocumentrepository";
    private static ApplicationContext context = null;

    static {
        context = new FileSystemXmlApplicationContext(PropertyAccessor.getPropertyFileURL() + CONFIG_FILE_NAME);
    }


    /**
     * Retrieve an adapter Document Registry implementation using the IOC framework.
     * This method retrieves the object from the framework that has an
     * identifier of "adapterdocumentregistry."
     *
     * @return AdapterDocumentRegistryProxy
     */
    public AdapterDocumentRepositoryProxy getAdapterDocumentRepositoryProxy() {
        AdapterDocumentRepositoryProxy adapterDocumentRepositoryProxy = null;
        if (context != null) {
            adapterDocumentRepositoryProxy = (AdapterDocumentRepositoryProxy) context.getBean(BEAN_NAME_ADAPTER_DOCUMENT_REPOSITORY);
        }
        return adapterDocumentRepositoryProxy;
    }
}
