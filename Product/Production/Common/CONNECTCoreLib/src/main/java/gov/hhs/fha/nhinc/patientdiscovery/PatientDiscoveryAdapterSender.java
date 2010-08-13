/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.adapter.patientdiscovery.async.request.error.proxy.AdapterPatientDiscoveryAsyncReqErrorProxy;
import gov.hhs.fha.nhinc.adapter.patientdiscovery.async.request.error.proxy.AdapterPatientDiscoveryAsyncReqErrorProxyObjectFactory;
import gov.hhs.fha.nhinc.adapter.patientdiscovery.async.request.proxy.AdapterPatientDiscoveryAsyncReqProxy;
import gov.hhs.fha.nhinc.adapter.patientdiscovery.async.request.proxy.AdapterPatientDiscoveryAsyncReqProxyObjectFactory;
import gov.hhs.fha.nhinc.adapter.patientdiscovery.async.request.queue.proxy.AdapterPatientDiscoveryAsyncReqQueueProxy;
import gov.hhs.fha.nhinc.adapter.patientdiscovery.async.request.queue.proxy.AdapterPatientDiscoveryAsyncReqQueueProxyObjectFactory;
import gov.hhs.fha.nhinc.adapter.patientdiscovery.async.response.proxy.AdapterPatientDiscoveryAsyncRespProxy;
import gov.hhs.fha.nhinc.adapter.patientdiscovery.async.response.proxy.AdapterPatientDiscoveryAsyncRespProxyObjectFactory;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.proxy.AdapterPatientDiscoveryProxy;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.proxy.AdapterPatientDiscoveryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
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

    public MCCIIN000002UV01 sendAsyncReqToAgency(PRPAIN201305UV02 request, AssertionType assertion) {
        RespondingGatewayPRPAIN201305UV02RequestType adapterReq = new RespondingGatewayPRPAIN201305UV02RequestType();

        AdapterPatientDiscoveryAsyncReqProxyObjectFactory factory = new AdapterPatientDiscoveryAsyncReqProxyObjectFactory();
        AdapterPatientDiscoveryAsyncReqProxy proxy = factory.getAdapterPatientDiscoveryAsyncReqProxy();

        adapterReq.setAssertion(assertion);
        adapterReq.setPRPAIN201305UV02(request);
        MCCIIN000002UV01 adapterResp = proxy.processPatientDiscoveryAsyncReq(adapterReq);

        return adapterResp;
    }

    public MCCIIN000002UV01 sendAsyncReqToAgencyQueue(PRPAIN201305UV02 request, AssertionType assertion) {
        RespondingGatewayPRPAIN201305UV02RequestType adapterReq = new RespondingGatewayPRPAIN201305UV02RequestType();

        AdapterPatientDiscoveryAsyncReqQueueProxyObjectFactory factory = new AdapterPatientDiscoveryAsyncReqQueueProxyObjectFactory();
        AdapterPatientDiscoveryAsyncReqQueueProxy proxy = factory.getAdapterPatientDiscoveryAsyncReqQueueProxy();

        adapterReq.setAssertion(assertion);
        adapterReq.setPRPAIN201305UV02(request);

        MCCIIN000002UV01 adapterResp = proxy.addPatientDiscoveryAsyncReq(adapterReq);

        return adapterResp;
    }

    public MCCIIN000002UV01 sendAsyncReqErrorToAgency(PRPAIN201305UV02 request, AssertionType assertion, String errMsg) {
        AsyncAdapterPatientDiscoveryErrorRequestType adapterReq = new AsyncAdapterPatientDiscoveryErrorRequestType();

        AdapterPatientDiscoveryAsyncReqErrorProxyObjectFactory factory = new AdapterPatientDiscoveryAsyncReqErrorProxyObjectFactory();
        AdapterPatientDiscoveryAsyncReqErrorProxy proxy = factory.getAdapterPatientDiscoveryAsyncReqErrorProxy();

        adapterReq.setAssertion(assertion);
        adapterReq.setPRPAIN201305UV02(request);
        adapterReq.setErrorMsg(errMsg);
        adapterReq.setPRPAIN201306UV02(new HL7PRPA201306Transforms().createPRPA201306ForPatientNotFound(request));

        MCCIIN000002UV01 adapterResp = proxy.processPatientDiscoveryAsyncReqError(adapterReq);

        return adapterResp;
    }

    public MCCIIN000002UV01 sendAsyncRespToAgency(PRPAIN201306UV02 request, AssertionType assertion) {
        RespondingGatewayPRPAIN201306UV02RequestType adapterReq = new RespondingGatewayPRPAIN201306UV02RequestType();

        AdapterPatientDiscoveryAsyncRespProxyObjectFactory factory = new AdapterPatientDiscoveryAsyncRespProxyObjectFactory();
        AdapterPatientDiscoveryAsyncRespProxy proxy = factory.getAdapterPatientDiscoveryAsyncRespProxy();

        adapterReq.setAssertion(assertion);
        adapterReq.setPRPAIN201306UV02(request);
        MCCIIN000002UV01 adapterResp = proxy.processPatientDiscoveryAsyncResp(adapterReq);

        return adapterResp;
    }

}
