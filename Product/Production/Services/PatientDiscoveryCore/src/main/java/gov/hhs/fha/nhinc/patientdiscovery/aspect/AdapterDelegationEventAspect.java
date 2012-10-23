package gov.hhs.fha.nhinc.patientdiscovery.aspect;

import gov.hhs.fha.nhinc.event.ContextEventBuilder;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hl7.v3.PRPAIN201305UV02;

@Aspect
public class AdapterDelegationEventAspect extends PatientDiscoveryEventAspect {

    public AdapterDelegationEventAspect() {
        super();
    }

    @Pointcut("execution(* gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.proxy.AdapterPatientDiscovery*.processPatientDiscoveryAsyncReq(..)) ||"
            + "execution(* gov.hhs.fha.nhinc.patientdiscovery.adapter.proxy.AdapterPatientDiscoveryProxy*.respondingGatewayPRPAIN201305UV02(..)) &&"
            + "args(body)")
    private void adapterDelegation(org.hl7.v3.PRPAIN201305UV02 body) {
    }

    @Before("adapterDelegation(org.hl7.v3.PRPAIN201305UV02) && args(body)")
    public void beginEvent(PRPAIN201305UV02 body) {
        recordEvent(beginBuilder, body);
    }


    @After("adapterDelegation(org.hl7.v3.PRPAIN201305UV02) && args(body)")
    public void endEvent(PRPAIN201305UV02 body) {
        recordEvent(endBuilder, body);
    }

    ContextEventBuilder beginBuilder = new ContextEventBuilder() {
        @Override
        public void createNewEvent() {
            event = eventFactory.createBeginAdapterDelegation();
        }
    };

    ContextEventBuilder endBuilder = new ContextEventBuilder() {
        @Override
        public void createNewEvent() {
            event = eventFactory.createEndAdapterDelegation();
        }
    };

}
