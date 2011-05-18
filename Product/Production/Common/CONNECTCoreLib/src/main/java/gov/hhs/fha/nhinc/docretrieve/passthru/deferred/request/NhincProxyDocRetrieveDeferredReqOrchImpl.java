/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.passthru.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docretrieve.nhin.deferred.request.proxy.NhinDocRetrieveDeferredReqProxy;
import gov.hhs.fha.nhinc.docretrieve.nhin.deferred.request.proxy.NhinDocRetrieveDeferredReqProxyObjectFactory;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Passthru Proxy Orchestration Request implementation class
 *
 * @author Sai Valluripalli
 * @author richard.ettema
 */
public class NhincProxyDocRetrieveDeferredReqOrchImpl {

    //Logger
    private static final Log log = LogFactory.getLog(NhincProxyDocRetrieveDeferredReqOrchImpl.class);

    /**
     * Pass the processing on to the NHIN layer
     *
     * @param request
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RetrieveDocumentSetRequestType request, AssertionType assertion, NhinTargetSystemType target) {
        log.debug("Begin NhincProxyDocRetrieveDeferredReqOrchImpl.crossGatewayRetrieveRequest(...)");

        DocRetrieveAcknowledgementType ack = null;

        //Call Nhin component proxy
        NhinDocRetrieveDeferredReqProxyObjectFactory objFactory = new NhinDocRetrieveDeferredReqProxyObjectFactory();
        NhinDocRetrieveDeferredReqProxy docRetrieveProxy = objFactory.getNhinDocRetrieveDeferredRequestProxy();
        ack = docRetrieveProxy.sendToRespondingGateway(request, assertion, target);

        log.debug("End NhincProxyDocRetrieveDeferredReqOrchImpl.crossGatewayRetrieveRequest(...)");

        return ack;
    }

}
