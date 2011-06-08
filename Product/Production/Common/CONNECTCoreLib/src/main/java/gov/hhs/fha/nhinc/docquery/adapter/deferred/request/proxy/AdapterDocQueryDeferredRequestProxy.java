/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docquery.adapter.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

/**
 *
 * @author JHOPPESC
 */
public interface AdapterDocQueryDeferredRequestProxy {

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion);

}
