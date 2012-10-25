package gov.hhs.fha.nhinc.patientdiscovery.aspect;

import gov.hhs.fha.nhinc.event.ContextEventBuilder;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hl7.v3.PRPAIN201305UV02;

@Aspect
public class InboundMessageEventAspect extends PatientDiscoveryEventAspect {

    public InboundMessageEventAspect() {
        super();
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.patientdiscovery.*.gateway.ws.NhinPatientDiscoveryAsyncReq.respondingGatewayDeferredPRPAIN201305UV02(..)) ||"
            + "execution(* gov.hhs.fha.nhinc.patientdiscovery.*.gateway.ws.NhinPatientDiscovery.respondingGatewayPRPAIN201305UV02(..)) &&"
            + "args(body)")
    private void inboundMessage(org.hl7.v3.PRPAIN201305UV02 body) {
    }

    @Before("inboundMessage(org.hl7.v3.PRPAIN201305UV02) && args(body)")
    public void beginEvent(PRPAIN201305UV02 body) {
        recordEvent(beginEventBuilder, body);
    }

    @After("inboundMessage(org.hl7.v3.PRPAIN201305UV02) &&args(body)")
    public void endEvent(PRPAIN201305UV02 body) {

        recordEvent(endEventBuilder, body);
    }
    
    ContextEventBuilder beginEventBuilder = new ContextEventBuilder() {
        @Override
        public void createNewEvent() {
            event = eventFactory.createBeginInboundMessage();
        }
    };

    ContextEventBuilder endEventBuilder = new ContextEventBuilder() {
        @Override
        public void createNewEvent() {
            event = eventFactory.createEndInboundMessage();
        }
    };

}
