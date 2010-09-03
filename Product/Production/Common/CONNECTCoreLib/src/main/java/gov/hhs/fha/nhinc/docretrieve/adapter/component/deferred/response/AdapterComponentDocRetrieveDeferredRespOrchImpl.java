/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.adapter.component.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 *
 * @author Ralph Saunders
 */
public class AdapterComponentDocRetrieveDeferredRespOrchImpl {
    private Log log = null;

    public AdapterComponentDocRetrieveDeferredRespOrchImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    public DocRetrieveAcknowledgementType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetResponseType body, AssertionType assertion)
    {
        log.debug("Enter AdapterComponentDocRetrieveDeferredRespImpl.respondingGatewayCrossGatewayRetrieve()");
        DocRetrieveAcknowledgementType response = null;
        RegistryResponseType           responseType;

        response = new DocRetrieveAcknowledgementType();
        responseType = new RegistryResponseType();
        response.setMessage(responseType);
        responseType.setStatus(NhincConstants.DOC_RETRIEVE_DEFERRED_RESP_ACK_STATUS_MSG);

        log.debug("Leaving AdapterComponentDocRetrieveDeferredRespImpl.respondingGatewayCrossGatewayRetrieve()");
        return response;
    }

}
