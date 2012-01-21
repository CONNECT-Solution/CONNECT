package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.orchestration.EntityOrchestratable;
import gov.hhs.fha.nhinc.orchestration.NhinDelegate;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Doc Query implementation of NhinDelegate
 * Note that all exceptions should just throw out and will be caught
 * by NhinCallableRequest and handled in this exception trap
 * @author paul.eftis
 */
public class NhinDocQueryDelegate implements NhinDelegate{

    private static Log log = LogFactory.getLog(NhinDocQueryDelegate.class);

    public NhinDocQueryDelegate(){
    }


    @Override
    public Orchestratable process(Orchestratable message){
        getLogger().debug("NhinDocQueryDelegate::process Orchestratable");
        if(message instanceof EntityDocQueryOrchestratable){
            return process((EntityOrchestratable) message);
        }
        return null;
    }


    @Override
    public EntityOrchestratable process(EntityOrchestratable message){
        if(message instanceof EntityDocQueryOrchestratable){
            return process((EntityDocQueryOrchestratable) message);
        }
        getLogger().error("NhinDocQueryDelegate message is not an instance of EntityDocQueryOrchestratable!");
        return null;
    }


    public EntityDocQueryOrchestratable process(EntityDocQueryOrchestratable message){
        getLogger().debug("NhinDocQueryDelegate::process EntityDocQueryOrchestratable");

        EntityDocQueryOrchestrationContextBuilder contextBuilder =
                (EntityDocQueryOrchestrationContextBuilder) OrchestrationContextFactory
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

        EntityDocQueryOrchestratable response =
                (EntityDocQueryOrchestratable)contextBuilder.build().execute();

        if(response instanceof EntityDocQueryOrchestratable_a0){
            getLogger().debug("NhinDocQueryDelegate::process returning a0 result");
        }else if(response instanceof EntityDocQueryOrchestratable_a0){
            getLogger().debug("NhinDocQueryDelegate::process returning a1 result");
        }else{
            getLogger().error("NhinDocQueryDelegate::process has unknown response!!!");
        }
        return response;
    }


    private Log getLogger(){
        return log;
    }

}
