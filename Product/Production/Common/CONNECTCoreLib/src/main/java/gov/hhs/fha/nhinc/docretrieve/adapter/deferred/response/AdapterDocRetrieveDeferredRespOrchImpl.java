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

package gov.hhs.fha.nhinc.docretrieve.adapter.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docretrieve.adapter.component.deferred.request.proxy.AdapterComponentDocRetrieveDeferredReqProxy;
import gov.hhs.fha.nhinc.docretrieve.adapter.component.deferred.request.proxy.AdapterComponentDocRetrieveDeferredReqProxyObjectFactory;
import gov.hhs.fha.nhinc.docretrieve.adapter.component.deferred.response.proxy.AdapterComponentDocRetrieveDeferredRespProxy;
import gov.hhs.fha.nhinc.docretrieve.adapter.component.deferred.response.proxy.AdapterComponentDocRetrieveDeferredRespProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * @author Ralph Saunders
 */
public class AdapterDocRetrieveDeferredRespOrchImpl {
    private Log log = null;

    public AdapterDocRetrieveDeferredRespOrchImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    public DocRetrieveAcknowledgementType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetResponseType body, AssertionType assertion)
    {
        DocRetrieveAcknowledgementType      result = null;

        log.debug("Enter AdapterDocRetrieveDeferredRespOrchImpl.respondingGatewayCrossGatewayRetrieve()");
        AdapterComponentDocRetrieveDeferredRespProxyObjectFactory factory = new AdapterComponentDocRetrieveDeferredRespProxyObjectFactory();
        AdapterComponentDocRetrieveDeferredRespProxy proxy = factory.getAdapterDocRetrieveDeferredResponseProxy();

        result = proxy.sendToAdapter(body, assertion);

        log.debug("Leaving AdapterDocRetrieveDeferredRespOrchImpl.respondingGatewayCrossGatewayRetrieve()");

        return result;
    }

}
