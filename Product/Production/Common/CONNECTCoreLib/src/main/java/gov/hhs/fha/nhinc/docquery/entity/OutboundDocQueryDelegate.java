package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Doc Query implementation of OutboundDelegate
 * 
 * @author paul.eftis
 */
public class OutboundDocQueryDelegate implements OutboundDelegate{

    private static Log log = LogFactory.getLog(OutboundDocQueryDelegate.class);

    public OutboundDocQueryDelegate(){
    }


    @Override
    public Orchestratable process(Orchestratable message){
        getLogger().debug("NhinDocQueryDelegate::process Orchestratable");
        if(message == null){
            getLogger().error("NhinDocQueryDelegate Orchestratable was null!!!");
            return null;
        }
        if(message instanceof OutboundDocQueryOrchestratable){
            return process((OutboundOrchestratable) message);
        }
        return null;
    }


    @Override
    public OutboundOrchestratable process(OutboundOrchestratable message){
        if(message instanceof OutboundDocQueryOrchestratable){
            return process((OutboundDocQueryOrchestratable) message);
        }
        getLogger().error("NhinDocQueryDelegate message is not an instance of EntityDocQueryOrchestratable!");
        return null;
    }


    public OutboundDocQueryOrchestratable process(OutboundDocQueryOrchestratable message){
        getLogger().debug("NhinDocQueryDelegate::process EntityDocQueryOrchestratable");

        OutboundDocQueryOrchestrationContextBuilder contextBuilder =
                (OutboundDocQueryOrchestrationContextBuilder) OrchestrationContextFactory
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

        OutboundDocQueryOrchestratable response =
                (OutboundDocQueryOrchestratable)contextBuilder.build().execute();

        if(response instanceof OutboundDocQueryOrchestratable_a0){
            getLogger().debug("NhinDocQueryDelegate::process returning a0 result");
        }else if(response instanceof OutboundDocQueryOrchestratable_a0){
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
