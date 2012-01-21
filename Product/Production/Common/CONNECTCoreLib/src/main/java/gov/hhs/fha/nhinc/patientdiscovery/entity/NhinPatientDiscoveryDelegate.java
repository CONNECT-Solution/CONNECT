package gov.hhs.fha.nhinc.patientdiscovery.entity;

import gov.hhs.fha.nhinc.orchestration.EntityOrchestratable;
import gov.hhs.fha.nhinc.orchestration.NhinDelegate;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Patient Discovery implementation of NhinDelegate
 * Note that all exceptions
 * should just throw out and will be caught by NhinCallableRequest and handled
 * in this exception trap
 * 
 * @author paul.eftis
 */
public class NhinPatientDiscoveryDelegate implements NhinDelegate{

    private static Log log = LogFactory.getLog(NhinPatientDiscoveryDelegate.class);

    public NhinPatientDiscoveryDelegate(){
    }


    @Override
    public Orchestratable process(Orchestratable message){
        getLogger().debug("NhinPatientDiscoveryDelegate::process Orchestratable");
        if(message instanceof EntityPatientDiscoveryOrchestratable){
            return process((EntityOrchestratable) message);
        }
        return null;
    }


    @Override
    public EntityOrchestratable process(EntityOrchestratable message){
        if(message instanceof EntityPatientDiscoveryOrchestratable){
            return process((EntityPatientDiscoveryOrchestratable) message);
        }
        getLogger().error("NhinPatientDiscoveryDelegate message is not an instance of EntityPatientDiscoveryOrchestratable!");
        return null;
    }


    public EntityPatientDiscoveryOrchestratable process(EntityPatientDiscoveryOrchestratable message){
        getLogger().debug("NhinPatientDiscoveryDelegate::process EntityPatientDiscoveryOrchestratable");

        EntityPatientDiscoveryOrchestrationContextBuilder contextBuilder =
                (EntityPatientDiscoveryOrchestrationContextBuilder) OrchestrationContextFactory
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

        EntityPatientDiscoveryOrchestratable response =
                (EntityPatientDiscoveryOrchestratable)contextBuilder.build().execute();

        if(response instanceof EntityPatientDiscoveryOrchestratable_a0){
            getLogger().debug("NhinPatientDiscoveryDelegate::process returning a0 result");
        }else if(response instanceof EntityPatientDiscoveryOrchestratable_a0){
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
