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

package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.response.AdapterPatientDiscoveryDeferredRespOrchImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;

/**
 *
 * @author JHOPPESC
 */
public class AdapterPatientDiscoveryDeferredRespProxyJavaImpl implements AdapterPatientDiscoveryDeferredRespProxy {
    private static Log log = LogFactory.getLog(AdapterPatientDiscoveryDeferredRespProxyJavaImpl.class);

    public MCCIIN000002UV01 processPatientDiscoveryAsyncResp(PRPAIN201306UV02 request, AssertionType assertion) {
        log.debug("Using Java Implementation for Adapter Patient Discovery Deferred Response Service");
        return new AdapterPatientDiscoveryDeferredRespOrchImpl().processPatientDiscoveryAsyncResp(request, assertion);
    }

}
