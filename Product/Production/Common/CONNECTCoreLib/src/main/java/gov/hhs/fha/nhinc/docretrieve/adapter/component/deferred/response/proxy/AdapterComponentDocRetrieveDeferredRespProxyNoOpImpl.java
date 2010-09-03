/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.adapter.component.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Created by
 * User: ralph
 * Date: Jul 28, 2010
 * Time: 12:36:16 PM
 */
public class AdapterComponentDocRetrieveDeferredRespProxyNoOpImpl implements AdapterComponentDocRetrieveDeferredRespProxy {
    private Log log = null;

     public AdapterComponentDocRetrieveDeferredRespProxyNoOpImpl() {
         log = LogFactory.getLog(getClass());
     }

     public DocRetrieveAcknowledgementType sendToAdapter(RetrieveDocumentSetResponseType body, AssertionType assertion) {
         DocRetrieveAcknowledgementType     response = new DocRetrieveAcknowledgementType();
         RegistryResponseType               resp = new RegistryResponseType();

         resp.setStatus(NhincConstants.DOC_RETRIEVE_DEFERRED_RESP_ACK_STATUS_MSG);
         response.setMessage(resp);

         log.info("AdapterComponentDocRetrieveDeferredRespNoOpImpl.sendToAdapter() - NO OP called");

         return response;
     }
}
