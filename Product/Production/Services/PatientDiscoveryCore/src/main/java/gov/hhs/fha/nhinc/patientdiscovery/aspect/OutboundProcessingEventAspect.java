package gov.hhs.fha.nhinc.patientdiscovery.aspect;

import gov.hhs.fha.nhinc.event.ContextEventBuilder;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;

@Aspect
public class OutboundProcessingEventAspect extends PatientDiscoveryEventAspect {

    public OutboundProcessingEventAspect() {
        super();
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.patientdiscovery.*.entity.deferred.response.EntityPatientDiscoveryDeferredResponseImpl.processPatientDiscoveryAsyncResp(..)) ||"
            + "execution(* gov.hhs.fha.nhinc.patientdiscovery._10.entity.deferred.request.EntityPatientDiscoveryDeferredRequestImpl.processPatientDiscoveryAsyncRequest*(..)) ||"
            + "execution(* gov.hhs.fha.nhinc.patientdiscovery._10.entity.EntityPatientDiscoveryImpl.respondingGatewayPRPAIN201305UV02(..))&&"
            + "args(requestType)")
    private void processOutboundMessage(RespondingGatewayPRPAIN201305UV02RequestType requestType) {
    }

    @Before("processOutboundMessage(org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType) && args(requestType)")
    public void beginEvent(RespondingGatewayPRPAIN201305UV02RequestType requestType) {
        recordEvent(beginBuilder, requestType);
    }

    

    @After("processOutboundMessage((org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType)  && args(requestType)")
    public void endEvent(RespondingGatewayPRPAIN201305UV02RequestType requestType) {
        recordEvent(endBuilder, requestType);
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.patientdiscovery._10.passthru.deferred.request.NhincProxyPatientDiscoveryDeferredRequestImpl.processPatientDiscoveryAsyncRequest*(..)) ||"
            + "execution(* gov.hhs.fha.nhinc.patientdiscovery._10.passthru.NhincProxyPatientDiscoveryImpl.proxyPRPAIN201305UV(..)) &&"
            + "args(requestType)")
    private void processPassthruOutboundMessage(ProxyPRPAIN201305UVProxyRequestType requestType) {
    }

    @Before("processPassthruOutboundMessage(org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType) && args(requestType)")
    public void beginEvent(ProxyPRPAIN201305UVProxyRequestType requestType) {
        recordEvent(beginBuilder, requestType);
    }


    @After("processPassthruOutboundMessage(org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType) && args(body)")
    public void endEvent(ProxyPRPAIN201305UVProxyRequestType requestType) {

        recordEvent(endBuilder, requestType);
    }

    ContextEventBuilder beginBuilder = new ContextEventBuilder() {
        @Override
        public void createNewEvent() {
            event = eventFactory.createBeginOutboundProcessing();
        }
    };

    ContextEventBuilder endBuilder = new ContextEventBuilder() {
        @Override
        public void createNewEvent() {
            event = eventFactory.createEndOutboundProcessing();
        }
    };

}
