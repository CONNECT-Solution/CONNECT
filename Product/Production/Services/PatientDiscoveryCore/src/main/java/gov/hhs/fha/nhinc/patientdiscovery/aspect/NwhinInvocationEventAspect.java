package gov.hhs.fha.nhinc.patientdiscovery.aspect;

import gov.hhs.fha.nhinc.event.ContextEventBuilder;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hl7.v3.PRPAIN201305UV02;

@Aspect
public class NwhinInvocationEventAspect extends PatientDiscoveryEventAspect {

    public NwhinInvocationEventAspect() {
        super();
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.response.proxy.NhinPatientDiscoveryDeferredRespProxy*.respondingGatewayPRPAIN201306UV02(..)) ||"
            + "execution(* gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.request.proxy.NhinPatientDiscoveryDeferredReqProxy*.respondingGatewayPRPAIN201305UV02(..)) ||"
            + "execution(* gov.hhs.fha.nhinc.patientdiscovery.nhin.proxy.NhinPatientDiscoveryProxy*.respondingGatewayPRPAIN201305UV02(..))&&"
            + "args(body)")
    private void nwhinInvocation(PRPAIN201305UV02 body) {
    }

    @Before("nwhinInvocation(org.hl7.v3.PRPAIN201305UV02) && args(body)")
    public void beginEvent(PRPAIN201305UV02 body) {
        recordEvent(beginBuilder, body);
    }

    @After("nwhinInvocation(org.hl7.v3.PRPAIN201305UV02) && args(body)")
    public void endEvent(PRPAIN201305UV02 body) {
        recordEvent(endBuilder, body);
    }

    ContextEventBuilder endBuilder = new ContextEventBuilder() {
        @Override
        public void createNewEvent() {
            event = eventFactory.createEndNwhinInvocation();
        }
    };

    ContextEventBuilder beginBuilder = new ContextEventBuilder() {
        @Override
        public void createNewEvent() {
            event = eventFactory.createBeginNwhinInvocation();
        }
    };
}
