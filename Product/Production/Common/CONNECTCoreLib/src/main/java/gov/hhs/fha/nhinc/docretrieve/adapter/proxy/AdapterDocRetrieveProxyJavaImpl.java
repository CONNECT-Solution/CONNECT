/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.adapter.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docretrieve.adapter.AdapterDocRetrieveOrchImpl;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is the java implementation of the Adapter Doc Retrieve component proxy.
 *
 * @author Les Westberg
 */
public class AdapterDocRetrieveProxyJavaImpl implements AdapterDocRetrieveProxy {

    private Log log = null;

    /**
     * Default constructor.
     */
    public AdapterDocRetrieveProxyJavaImpl() {
        log = createLogger();
    }

    /**
     * Creates the log object for logging.
     *
     * @return The log object.
     */
    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    /**
     * Retrieve the specified document.
     *
     * @param request The identifier(s) if the document(s) to be retrieved.
     * @param assertion The assertion information.
     * @return The retrieved documents.
     */
    public RetrieveDocumentSetResponseType retrieveDocumentSet(RetrieveDocumentSetRequestType request, AssertionType assertion) {
        log.debug("Entering AdapterDocRetrieveProxyJavaImpl.retrieveDocumentSet");

        AdapterDocRetrieveOrchImpl oOrchestrator = new AdapterDocRetrieveOrchImpl();
        RetrieveDocumentSetResponseType oResponse = oOrchestrator.respondingGatewayCrossGatewayRetrieve(request, assertion);

        log.debug("Leaving AdapterDocRetrieveProxyJavaImpl.retrieveDocumentSet");

        return oResponse;
    }
}
