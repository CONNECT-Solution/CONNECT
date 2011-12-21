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

package gov.hhs.fha.nhinc.docquery.adapter.deferred.request.error.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docquery.adapter.deferred.request.error.AdapterDocQueryDeferredRequestErrorOrchImpl;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class AdapterDocQueryDeferredRequestErrorProxyJavaImpl implements AdapterDocQueryDeferredRequestErrorProxy {
    private static Log log = LogFactory.getLog(AdapterDocQueryDeferredRequestErrorProxyJavaImpl.class);

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion, String errMsg) {
        log.debug("Using Java Implementation for Adapter Doc Query Deferred Request Error Service");
        return new AdapterDocQueryDeferredRequestErrorOrchImpl().respondingGatewayCrossGatewayQuery(msg, assertion, errMsg);
    }

}
