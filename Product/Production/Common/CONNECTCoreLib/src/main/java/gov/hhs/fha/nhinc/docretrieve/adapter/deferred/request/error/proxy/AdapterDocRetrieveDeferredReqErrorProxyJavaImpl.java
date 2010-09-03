/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.adapter.deferred.request.error.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterDocumentRetrieveDeferredRequestErrorType;
import gov.hhs.fha.nhinc.docretrieve.adapter.deferred.request.error.AdapterDocRetrieveDeferredReqErrorOrchImpl;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Created by
 * User: ralph
 * Date: Jul 26, 2010
 * Time: 2:36:53 PM
 */
public class AdapterDocRetrieveDeferredReqErrorProxyJavaImpl implements AdapterDocRetrieveDeferredReqErrorProxy {
    private Log log = null;

     public AdapterDocRetrieveDeferredReqErrorProxyJavaImpl() {
         log = LogFactory.getLog(getClass());
     }

     public DocRetrieveAcknowledgementType sendToAdapter(RetrieveDocumentSetRequestType body, AssertionType assertion, String errMsg) {
         DocRetrieveAcknowledgementType                 response = new DocRetrieveAcknowledgementType();
         AdapterDocRetrieveDeferredReqErrorOrchImpl  errorAdapter = new AdapterDocRetrieveDeferredReqErrorOrchImpl();
         AdapterDocumentRetrieveDeferredRequestErrorType request = new AdapterDocumentRetrieveDeferredRequestErrorType();

         request.setRetrieveDocumentSetRequest(body);
         request.setAssertion(assertion);
         request.setErrorMsg(errMsg);

         response = errorAdapter.respondingGatewayCrossGatewayRetrieve(request, assertion, errMsg);

         return response;
     }

}
