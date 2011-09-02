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

package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.patientdiscovery.adapter.proxy.AdapterPatientDiscoveryProxy;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.proxy.AdapterPatientDiscoveryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.error.proxy.AdapterPatientDiscoveryDeferredReqErrorProxy;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.error.proxy.AdapterPatientDiscoveryDeferredReqErrorProxyObjectFactory;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.proxy.AdapterPatientDiscoveryDeferredReqProxy;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.proxy.AdapterPatientDiscoveryDeferredReqProxyObjectFactory;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.response.proxy.AdapterPatientDiscoveryDeferredRespProxy;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.response.proxy.AdapterPatientDiscoveryDeferredRespProxyObjectFactory;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;
import org.hl7.v3.AsyncAdapterPatientDiscoveryErrorRequestType;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;

/**
 *
 * @author jhoppesc
 */
public class PatientDiscoveryAdapterSender {

    public PRPAIN201306UV02 send201305ToAgency(PRPAIN201305UV02 request, AssertionType assertion) {
        RespondingGatewayPRPAIN201305UV02RequestType adapterReq = new RespondingGatewayPRPAIN201305UV02RequestType();

        AdapterPatientDiscoveryProxyObjectFactory factory = new AdapterPatientDiscoveryProxyObjectFactory();
        AdapterPatientDiscoveryProxy proxy = factory.getAdapterPatientDiscoveryProxy();

        adapterReq.setAssertion(assertion);
        adapterReq.setPRPAIN201305UV02(request);
        PRPAIN201306UV02 adapterResp = proxy.respondingGatewayPRPAIN201305UV02(adapterReq.getPRPAIN201305UV02(), adapterReq.getAssertion());

        return adapterResp;
    }

    public MCCIIN000002UV01 sendDeferredReqErrorToAgency(PRPAIN201305UV02 request, AssertionType assertion, String errMsg) {
        AsyncAdapterPatientDiscoveryErrorRequestType adapterReq = new AsyncAdapterPatientDiscoveryErrorRequestType();

        AdapterPatientDiscoveryDeferredReqErrorProxyObjectFactory factory = new AdapterPatientDiscoveryDeferredReqErrorProxyObjectFactory();
        AdapterPatientDiscoveryDeferredReqErrorProxy proxy = factory.getAdapterPatientDiscoveryDeferredReqErrorProxy();

        adapterReq.setAssertion(assertion);
        adapterReq.setPRPAIN201305UV02(request);
        adapterReq.setErrorMsg(errMsg);
        PRPAIN201306UV02 response = new HL7PRPA201306Transforms().createPRPA201306ForPatientNotFound(request);

        MCCIIN000002UV01 adapterResp = proxy.processPatientDiscoveryAsyncReqError(request, response, assertion, errMsg);

        return adapterResp;
    }

    public MCCIIN000002UV01 sendDeferredRespToAgency(PRPAIN201306UV02 request, AssertionType assertion) {
        RespondingGatewayPRPAIN201306UV02RequestType adapterReq = new RespondingGatewayPRPAIN201306UV02RequestType();

        AdapterPatientDiscoveryDeferredRespProxyObjectFactory factory = new AdapterPatientDiscoveryDeferredRespProxyObjectFactory();
        AdapterPatientDiscoveryDeferredRespProxy proxy = factory.getAdapterPatientDiscoveryDeferredRespProxy();

        adapterReq.setAssertion(assertion);
        adapterReq.setPRPAIN201306UV02(request);
        MCCIIN000002UV01 adapterResp = proxy.processPatientDiscoveryAsyncResp(request, assertion);

        return adapterResp;
    }

}
