/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docquery.adapter.component.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docquery.adapter.component.deferred.request.AdapterComponentDocQueryDeferredRequestOrchImpl;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class AdapterComponentDocQueryDeferredRequestProxyJavaImpl implements AdapterComponentDocQueryDeferredRequestProxy {
    private static Log log = LogFactory.getLog(AdapterComponentDocQueryDeferredRequestProxyJavaImpl.class);

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion) {
        log.debug("Using Java Implementation for Adapter Component Doc Query Deferred Request Service");
        return new AdapterComponentDocQueryDeferredRequestOrchImpl().respondingGatewayCrossGatewayQuery(msg, assertion);
    }

}
