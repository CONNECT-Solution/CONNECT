/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve._30.nhin;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docretrieve.nhin.NhinDocRetrieveOrchImpl;
import gov.hhs.fha.nhinc.messaging.server.BaseService;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import javax.xml.ws.WebServiceContext;

/**
 * This is the Web Service implementation of Nhin Doc Retrieve.
 * 
 * @author vvickers, Les Westberg
 */
public class DocRetrieveImpl extends BaseService {

    private NhinDocRetrieveOrchImpl orchImpl;
    
    public DocRetrieveImpl(NhinDocRetrieveOrchImpl orchImpl) {
        this.orchImpl = orchImpl;
    }
    
    RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType body,
            WebServiceContext context) {
        
        AssertionType assertion = getAssertion(context, null);

        return orchImpl.respondingGatewayCrossGatewayRetrieve(body, assertion);
    }
}
