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

package gov.hhs.fha.nhinc.docquery.passthru.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docquery.passthru.deferred.request.PassthruDocQueryDeferredRequestOrchImpl;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author patlollav
 */
public class PassthruDocQueryDeferredRequestProxyJavaImpl implements PassthruDocQueryDeferredRequestProxy {

    //Logger
    private static final Log logger = LogFactory.getLog(PassthruDocQueryDeferredRequestProxyJavaImpl.class);

    public DocQueryAcknowledgementType crossGatewayQueryRequest(AdhocQueryRequest msg, AssertionType assertion, NhinTargetSystemType target) {
        getLogger().debug("PassthruDocQueryDeferredRequestProxyJavaImpl.respondingGatewayCrossGatewayQuery");

        return getPassthruDocQueryDeferredRequestOrchImpl().crossGatewayQueryRequest(msg, assertion, target);
    }

    protected PassthruDocQueryDeferredRequestOrchImpl getPassthruDocQueryDeferredRequestOrchImpl(){
        return new PassthruDocQueryDeferredRequestOrchImpl();
    }

    protected Log getLogger(){
        return logger;
    }

}
