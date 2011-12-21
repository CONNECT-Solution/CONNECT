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

package gov.hhs.fha.nhinc.docquery.adapter.component.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import gov.hhs.fha.nhinc.docquery.adapter.component.deferred.response.AdapterComponentDocQueryDeferredResponseOrchImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class AdapterComponentDocQueryDeferredResponseProxyJavaImpl implements AdapterComponentDocQueryDeferredResponseProxy {
    private static Log log = LogFactory.getLog(AdapterComponentDocQueryDeferredResponseProxyJavaImpl.class);

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryResponse msg, AssertionType assertion) {
        log.debug("Using Java Implementation for Adapter Component Doc Query Deferred Response Service");
        return new AdapterComponentDocQueryDeferredResponseOrchImpl().respondingGatewayCrossGatewayQuery(msg, assertion);
    }

}
