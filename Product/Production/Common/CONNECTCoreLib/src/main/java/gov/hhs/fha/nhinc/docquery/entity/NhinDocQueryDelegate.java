package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.orchestration.EntityOrchestratable;
import gov.hhs.fha.nhinc.orchestration.NhinDelegate;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;
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
        if(message instanceof EntityOrchestratable){
            return process((EntityOrchestratable) message);
        }
        return null;
    }


    public EntityOrchestratable process(EntityOrchestratable message){
        getLogger().debug("NhinDocQueryDelegate::process EntityOrchestratable");

        EntityOrchestratable response = null;
        if(message instanceof EntityDocQueryOrchestratable){
            EntityDocQueryOrchestratable DQMessage = (EntityDocQueryOrchestratable)message;
            OrchestrationContextBuilder contextBuilder = OrchestrationContextFactory.getInstance().getBuilder(
                DQMessage.getAssertion().getHomeCommunity(), DQMessage.getServiceName());
            if(contextBuilder instanceof EntityDocQueryOrchestrationContextBuilder_g0){
                ((EntityDocQueryOrchestrationContextBuilder_g0) contextBuilder).init(DQMessage);
            }else if(contextBuilder instanceof EntityDocQueryOrchestrationContextBuilder_g1){
                ((EntityDocQueryOrchestrationContextBuilder_g1) contextBuilder).init(DQMessage);
            }else{
                return null;
            }

            Orchestratable resp = contextBuilder.build().execute();

            if(resp instanceof EntityDocQueryOrchestratable_a0){
                response = (EntityDocQueryOrchestratable_a0)resp;
                getLogger().debug("NhinDocQueryDelegate::process returning EntityDocQueryOrchestratable_a0");
            }else if(resp instanceof EntityDocQueryOrchestratable_a1){
                response = (EntityDocQueryOrchestratable_a1)resp;
                getLogger().debug("NhinDocQueryDelegate::process returning EntityDocQueryOrchestratable_a1");
            }else{
                getLogger().error("NhinDocQueryDelegate::process has unknown response!!!");
            }
        }else{
            getLogger().error("NhinDocQueryDelegate message is not an instance of NhinDocQueryOrchestratable!");
        }
        return response;
    }


    private Log getLogger() {
        return log;
    }

}
