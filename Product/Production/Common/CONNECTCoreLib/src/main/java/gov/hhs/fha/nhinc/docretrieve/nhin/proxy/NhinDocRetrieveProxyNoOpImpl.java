/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.nhin.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;

/**
 *
 *
 * @author Neil Webb
 */
public class NhinDocRetrieveProxyNoOpImpl implements NhinDocRetrieveProxy
{

    /**
     * Retrieve the document(s) specified in the request.
     *
     * @param request The identifier(s) of the document(s) to be retrieved.
     * @param targetSystem The target system where the message is being sent to.
     * @return The document(s) that were retrieved.
     */
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType request,
                                                                                 AssertionType assertion,
                                                                                 NhinTargetSystemType targetSystem)
    {
        return new RetrieveDocumentSetResponseType();
    }
    
}
