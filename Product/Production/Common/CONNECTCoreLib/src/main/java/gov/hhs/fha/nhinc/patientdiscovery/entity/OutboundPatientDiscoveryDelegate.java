package gov.hhs.fha.nhinc.patientdiscovery.entity;

import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Patient Discovery implementation of OutboundDelegate
 * Note that all exceptions
 * should just throw out and will be caught by NhinCallableRequest and handled
 * in this exception trap
 * 
 * @author paul.eftis
 */
public class OutboundPatientDiscoveryDelegate implements OutboundDelegate{

    private static Log log = LogFactory.getLog(OutboundPatientDiscoveryDelegate.class);

    public OutboundPatientDiscoveryDelegate(){
    }


    @Override
    public Orchestratable process(Orchestratable message){
        getLogger().debug("NhinPatientDiscoveryDelegate::process Orchestratable");
        if(message instanceof OutboundPatientDiscoveryOrchestratable){
            return process((OutboundOrchestratable) message);
        }
        return null;
    }


    @Override
    public OutboundOrchestratable process(OutboundOrchestratable message){
        if(message instanceof OutboundPatientDiscoveryOrchestratable){
            return process((OutboundPatientDiscoveryOrchestratable) message);
        }
        getLogger().error("NhinPatientDiscoveryDelegate message is not an instance of EntityPatientDiscoveryOrchestratable!");
        return null;
    }


    public OutboundPatientDiscoveryOrchestratable process(OutboundPatientDiscoveryOrchestratable message){
        getLogger().debug("NhinPatientDiscoveryDelegate::process EntityPatientDiscoveryOrchestratable");

        OutboundPatientDiscoveryOrchestrationContextBuilder contextBuilder =
                (OutboundPatientDiscoveryOrchestrationContextBuilder) OrchestrationContextFactory
                .getInstance().getBuilder(
                    message.getAssertion().getHomeCommunity(),
                    message.getServiceName());

        contextBuilder.setAssertionType(message.getAssertion());
        contextBuilder.setRequest(message.getRequest());
        contextBuilder.setTarget(message.getTarget());
        contextBuilder.setServiceName(message.getServiceName());
        contextBuilder.setPolicyTransformer(message.getPolicyTransformer());
        contextBuilder.setAuditTransformer(message.getAuditTransformer());
        contextBuilder.setProcessor(message.getResponseProcessor());

        OutboundPatientDiscoveryOrchestratable response =
                (OutboundPatientDiscoveryOrchestratable)contextBuilder.build().execute();

        if(response instanceof OutboundPatientDiscoveryOrchestratable_a0){
            getLogger().debug("NhinPatientDiscoveryDelegate::process returning a0 result");
        }else if(response instanceof OutboundPatientDiscoveryOrchestratable_a0){
            getLogger().debug("NhinPatientDiscoveryDelegate::process returning a1 result");
        }else{
            getLogger().error("NhinPatientDiscoveryDelegate::process has unknown response!!!");
        }

        return response;
    }


    private Log getLogger(){
        return log;
    }

}
