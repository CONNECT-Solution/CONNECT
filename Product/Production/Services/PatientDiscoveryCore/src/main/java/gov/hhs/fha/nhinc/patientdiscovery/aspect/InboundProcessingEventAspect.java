package gov.hhs.fha.nhinc.patientdiscovery.aspect;

import gov.hhs.fha.nhinc.event.ContextEventBuilder;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType;

@Aspect
public class InboundProcessingEventAspect extends PatientDiscoveryEventAspect {

    public InboundProcessingEventAspect() {
        super();
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.patientdiscovery.*.deferred.request.NhinPatientDiscoveryAsyncReqImpl.respondingGatewayPRPAIN201305UV02(..)) ||"
            + "execution(* gov.hhs.fha.nhinc.patientdiscovery.NhinPatientDiscoveryImpl.respondingGatewayPRPAIN201305UV02(..)) &&"
            + " args(body)")
    public void processInboundMessage(PRPAIN201305UV02 body) {
    }

    @Before("processInboundMessage(org.hl7.v3.PRPAIN201305UV02) && args(requestType)")
    public void beginEvent(PRPAIN201305UV02 body) {
        recordEvent(beginEventBuilder, body);
    }

    @After("processInboundMessage(org.hl7.v3.PRPAIN201305UV02) && args(requestType)")
    public void endEvent(PRPAIN201305UV02 body) {
        recordEvent(endEventbuilder, body);
    }

    ContextEventBuilder beginEventBuilder = new ContextEventBuilder() {
        @Override
        public void createNewEvent() {
            event = eventFactory.createBeginInboundProcessing();
        }
    };

    ContextEventBuilder endEventbuilder = new ContextEventBuilder() {
        @Override
        public void createNewEvent() {
            event = eventFactory.createEndInboundProcessing();
        }
    };

}