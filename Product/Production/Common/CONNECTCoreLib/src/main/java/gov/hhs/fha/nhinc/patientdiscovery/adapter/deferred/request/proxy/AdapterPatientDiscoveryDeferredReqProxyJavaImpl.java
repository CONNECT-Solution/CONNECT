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

package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.AdapterPatientDiscoveryDeferredReqOrchImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;

/**
 *
 * @author JHOPPESC
 */
public class AdapterPatientDiscoveryDeferredReqProxyJavaImpl implements AdapterPatientDiscoveryDeferredReqProxy {
    private static Log log = LogFactory.getLog(AdapterPatientDiscoveryDeferredReqProxyJavaImpl.class);

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(PRPAIN201305UV02 request, AssertionType assertion) {
        log.debug("Using Java Implementation for Adapter Patient Discovery Deferred Request Service");
        return new AdapterPatientDiscoveryDeferredReqOrchImpl().processPatientDiscoveryAsyncReq(request, assertion);
    }

}
