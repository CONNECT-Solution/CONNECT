/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.adapter.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docretrieve.adapter.component.deferred.request.proxy.AdapterComponentDocRetrieveDeferredReqProxy;
import gov.hhs.fha.nhinc.docretrieve.adapter.component.deferred.request.proxy.AdapterComponentDocRetrieveDeferredReqProxyObjectFactory;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Ralph Saunders
 */
public class AdapterDocRetrieveDeferredReqOrchImpl {

    private Log log = null;

    public AdapterDocRetrieveDeferredReqOrchImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    public DocRetrieveAcknowledgementType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType body, AssertionType assertion) {
        DocRetrieveAcknowledgementType result = null;

        log.debug("Enter AdapterDocRetrieveDeferredReqOrchImpl.respondingGatewayCrossGatewayRetrieve()");

        AdapterComponentDocRetrieveDeferredReqProxyObjectFactory factory = new AdapterComponentDocRetrieveDeferredReqProxyObjectFactory();
        AdapterComponentDocRetrieveDeferredReqProxy proxy = factory.getAdapterDocRetrieveDeferredRequestProxy();

        result = proxy.sendToAdapter(body, assertion);

        log.debug("Leaving AdapterDocRetrieveDeferredReqOrchImpl.respondingGatewayCrossGatewayRetrieve()");

        return result;
    }
}
