package gov.hhs.fha.nhinc.patientdiscovery.aspect;

import gov.hhs.fha.nhinc.event.ContextEventBuilder;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;

@Aspect
public class OutboundMessageEventAspect extends PatientDiscoveryEventAspect {

    public OutboundMessageEventAspect() {
        super();
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.patientdiscovery.*.gateway.ws.EntityPatientDiscoveryDeferredResponse*.processPatientDiscoveryAsyncResp(..)) ||"
            + "execution(* gov.hhs.fha.nhinc.patientdiscovery.*.gateway.ws.EntityPatientDiscoveryDeferredRequest*.processPatientDiscoveryAsyncReq(..)) ||"
            + "execution(* gov.hhs.fha.nhinc.patientdiscovery.*.gateway.ws.EntityPatientDiscovery*.respondingGatewayPRPAIN201305UV02(..)) &&"
            + "args(requestType)")
    private void outboundMessage(RespondingGatewayPRPAIN201305UV02RequestType requestType) {
    }

    @Before("outboundMessage() && args(requestType)")
    public void beginOutboundMessageEvent(RespondingGatewayPRPAIN201305UV02RequestType requestType) {
        ContextEventBuilder builder = new ContextEventBuilder() {
            @Override
            public void createNewEvent() {
                event = eventFactory.createBeginOutboundMessage();
            }
        };
        recordEvent(builder, requestType.getPRPAIN201305UV02());
    }

    @After("outboundMessage()  && args(requestType)")
    public void endOutboundMessageEvent(RespondingGatewayPRPAIN201305UV02RequestType requestType) {
        ContextEventBuilder builder = new ContextEventBuilder() {
            @Override
            public void createNewEvent() {
                event = eventFactory.createEndOutboundMessage();
            }
        };
        recordEvent(builder, requestType.getPRPAIN201305UV02());
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.patientdiscovery.*.gateway.ws.NhincProxyPatientDiscoveryDeferredRequest*.proxyProcessPatientDiscoveryAsyncReq(..)) || "
            + "execution(* gov.hhs.fha.nhinc.patientdiscovery.*.gateway.ws.NhincProxyPatientDiscovery*.proxyPRPAIN201305UV(..)) &&"
            + "args(requestType)")
    private void passthruOutboundMessage(ProxyPRPAIN201305UVProxyRequestType requestType) {
    }

    @Before("passthruOutboundMessage(org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType) && args(requestType)")
    public void beginOutboundMessageEvent(ProxyPRPAIN201305UVProxyRequestType requestType) {
        ContextEventBuilder builder = new ContextEventBuilder() {
            @Override
            public void createNewEvent() {
                event = eventFactory.createBeginOutboundMessage();
            }
        };
        recordEvent(builder, requestType.getPRPAIN201305UV02());
    }

    @After("passthruOutboundMessage(org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType) && args(requestType)")
    public void endOutboundMessageEvent(ProxyPRPAIN201305UVProxyRequestType requestType) {
        ContextEventBuilder builder = new ContextEventBuilder() {
            @Override
            public void createNewEvent() {
                event = eventFactory.createEndOutboundMessage();
            }
        };
        recordEvent(builder, requestType.getPRPAIN201305UV02());
    }

}
