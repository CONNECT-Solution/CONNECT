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

package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.error.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.error.AdapterPatientDiscoveryDeferredReqErrorOrchImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;

/**
 *
 * @author jhoppesc
 */
public class AdapterPatientDiscoveryDeferredReqErrorProxyJavaImpl implements AdapterPatientDiscoveryDeferredReqErrorProxy {
    private static Log log = LogFactory.getLog(AdapterPatientDiscoveryDeferredReqErrorProxyJavaImpl.class);

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReqError(PRPAIN201305UV02 request, PRPAIN201306UV02 response, AssertionType assertion, String errMsg) {
        log.debug("Using Java Implementation for Adapter Patient Discovery Deferred Request Error Service");
        return new AdapterPatientDiscoveryDeferredReqErrorOrchImpl().processPatientDiscoveryAsyncReqError(request, response, assertion, errMsg);
    }

}
