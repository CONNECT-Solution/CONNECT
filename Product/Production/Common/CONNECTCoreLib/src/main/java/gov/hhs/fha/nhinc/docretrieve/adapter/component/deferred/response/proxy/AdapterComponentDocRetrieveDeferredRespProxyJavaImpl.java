/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.adapter.component.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docretrieve.adapter.component.deferred.response.AdapterComponentDocRetrieveDeferredRespOrchImpl;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Created by
 * User: ralph
 * Date: Jul 28, 2010
 * Time: 12:36:16 PM
 */
public class AdapterComponentDocRetrieveDeferredRespProxyJavaImpl implements AdapterComponentDocRetrieveDeferredRespProxy {
    private Log log = null;

     public AdapterComponentDocRetrieveDeferredRespProxyJavaImpl() {
         log = LogFactory.getLog(getClass());
     }

     public DocRetrieveAcknowledgementType sendToAdapter(RetrieveDocumentSetResponseType body, AssertionType assertion) {
         DocRetrieveAcknowledgementType                     response = new DocRetrieveAcknowledgementType();
         AdapterComponentDocRetrieveDeferredRespOrchImpl    adapter = new AdapterComponentDocRetrieveDeferredRespOrchImpl();

         log.info("AdapterComponentDocRetrieveDeferredRespJavaImpl.sendToAdapter() - JavaImpl called");

         response = adapter.respondingGatewayCrossGatewayRetrieve(body, assertion);

         return response;
     }
}
