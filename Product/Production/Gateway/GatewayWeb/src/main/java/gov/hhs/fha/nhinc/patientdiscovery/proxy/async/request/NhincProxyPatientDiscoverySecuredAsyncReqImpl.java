/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.proxy.async.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinpatientdiscovery.async.request.proxy.NhinPatientDiscoveryAsyncReqProxy;
import gov.hhs.fha.nhinc.nhinpatientdiscovery.async.request.proxy.NhinPatientDiscoveryAsyncReqProxyObjectFactory;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;

/**
 *
 * @author jhoppesc
 */
public class NhincProxyPatientDiscoverySecuredAsyncReqImpl {

    public MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncReq(ProxyPRPAIN201305UVProxySecuredRequestType request, WebServiceContext context) {
        ProxyPRPAIN201305UVProxyRequestType unsecureRequest = new ProxyPRPAIN201305UVProxyRequestType();
        unsecureRequest.setNhinTargetSystem(request.getNhinTargetSystem());
        unsecureRequest.setPRPAIN201305UV02(request.getPRPAIN201305UV02());
        unsecureRequest.setAssertion(SamlTokenExtractor.GetAssertion(context));

        MCCIIN000002UV01 ack = proxyProcessPatientDiscoveryAsyncReq(unsecureRequest);

        return ack;
    }

    public MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncReq(ProxyPRPAIN201305UVProxyRequestType request) {
        MCCIIN000002UV01 resp = new MCCIIN000002UV01();

        // Audit the Patient Discovery Request Message sent on the Nhin Interface
        PatientDiscoveryAuditLogger auditLog = new PatientDiscoveryAuditLogger();
        AcknowledgementType ack = auditLog.auditNhin201305(request.getPRPAIN201305UV02(), request.getAssertion(), NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

        NhinPatientDiscoveryAsyncReqProxyObjectFactory patientDiscoveryFactory = new NhinPatientDiscoveryAsyncReqProxyObjectFactory();
        NhinPatientDiscoveryAsyncReqProxy proxy = patientDiscoveryFactory.getNhinPatientDiscoveryAsyncReqProxy();

        resp = proxy.respondingGatewayPRPAIN201305UV02(request.getPRPAIN201305UV02(), request.getAssertion(), request.getNhinTargetSystem());

        // Audit the Patient Discovery Response Message received on the Nhin Interface
        ack = auditLog.auditAck(resp, request.getAssertion(), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        return resp;
    }

}
