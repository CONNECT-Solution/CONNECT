/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.nhin.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;

/**
 * Created by
 * User: ralph
 * Date: Jul 26, 2010
 * Time: 1:34:33 PM
 */
public interface NhinDocRetrieveDeferredReqProxy {

    public DocRetrieveAcknowledgementType sendToRespondingGateway(RetrieveDocumentSetRequestType body, AssertionType assertion, NhinTargetSystemType target);
}
