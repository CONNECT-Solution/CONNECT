package gov.hhs.fha.nhinc.patientdiscovery.entity;

import gov.hhs.fha.nhinc.orchestration.EntityOrchestratable;
import gov.hhs.fha.nhinc.orchestration.NhinDelegate;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContext;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Note that all exceptions should just throw out and will be caught
 * by NhinCallableRequest and handled in this exception trap
 * @author paul.eftis
 */
public class NhinPatientDiscoveryDelegate implements NhinDelegate{

    private static Log log = LogFactory.getLog(NhinPatientDiscoveryDelegate.class);


    public NhinPatientDiscoveryDelegate(){
    }


    @Override
    public Orchestratable process(Orchestratable message){
        getLogger().debug("NhinPatientDiscoveryDelegate::process Orchestratable");
        if(message instanceof EntityOrchestratable){
            return process((EntityOrchestratable) message);
        }
        return null;
    }


    public EntityOrchestratable process(EntityOrchestratable message){
        getLogger().debug("NhinPatientDiscoveryDelegate::process EntityOrchestratable");

        EntityOrchestratable resp = null;
        EntityPatientDiscoveryOrchestratable_a0 response_a0 = null;
        EntityPatientDiscoveryOrchestratable_a0 response_a1 = null;
        if(message instanceof EntityPatientDiscoveryOrchestratable){
            EntityPatientDiscoveryOrchestratable PDMessage = (EntityPatientDiscoveryOrchestratable)message;

            OrchestrationContextBuilder contextBuilder = OrchestrationContextFactory.getInstance().getBuilder(
                PDMessage.getAssertion().getHomeCommunity(), PDMessage.getServiceName());
            if(contextBuilder instanceof EntityPatientDiscoveryOrchestrationContextBuilder_g0){
                ((EntityPatientDiscoveryOrchestrationContextBuilder_g0) contextBuilder).init(PDMessage);
//            }else if(contextBuilder instanceof EntityPatientDiscoveryOrchestrationContextBuilder_g1){
//                ((EntityPatientDiscoveryOrchestrationContextBuilder_g1) contextBuilder).init(DQMessage);
            }else{
                return null;
            }
            resp = contextBuilder.build().execute();

            if(resp instanceof EntityPatientDiscoveryOrchestratable_a0){
                response_a0 = (EntityPatientDiscoveryOrchestratable_a0)resp;
            }else{
                response_a1 = (EntityPatientDiscoveryOrchestratable_a0)resp;
            }
        }else{
            getLogger().error("NhinPatientDiscoveryDelegate message is not an instance of NhinPatientDiscoveryOrchestratable!");
        }
        if(response_a0 != null){
            return response_a0;
        }else{
            return response_a1;
        }


//        EntityPatientDiscoveryOrchestratable message = (EntityPatientDiscoveryOrchestratable)m;
//        EntityPatientDiscoveryOrchestratable_a0 response_a0 = null;
//        EntityDocQueryOrchestratable_a1 response_a1 = null;
//
//        boolean useSpecA0 = true;
//
//
//        if(useSpecA0){
//            // if we are using _a0
//            // create the associated a0 request and response objects
//            // and set the associated doc query strategy impl
//            NhinDocQueryStrategyContext context = new NhinDocQueryStrategyContext(
//                    new NhinPatientDiscoveryStrategyImpl_g0());
//            EntityPatientDiscoveryOrchestratable_a0 request_a0
//                    = new EntityPatientDiscoveryOrchestratable_a0(
//                    null, null, null, null,
//                    message.getAssertion(), message.getServiceName(),
//                    (NhinTargetSystemType)message.getTarget(),
//                    (PRPAIN201305UV02)message.getRequest());
//            response_a0 = (EntityPatientDiscoveryOrchestratable_a0)context.executeStrategy(request_a0);
//        }else{
//            // if we are using _a1
//            NhinDocQueryStrategyContext context = new NhinDocQueryStrategyContext(
//                    new NhinPatientDiscoveryStrategyImpl_a1());
//            EntityPatientDiscoveryOrchestratable_a1 request_a1
//                    = new EntityPatientDiscoveryOrchestratable_a1(
//                    null, null, null, null,
//                    message.getAssertion(), message.getServiceName(),
//                    (NhinTargetSystemType)message.getTarget(),
//                    (PRPAIN201305UV02)message.getRequest());
//            response_a1 = (EntityPatientDiscoveryOrchestratable_a1)context.executeStrategy(request_a1);
//
//
//            // currently for patient discovery, a0 and a1 are the same (i.e. spec hasn't changed)
//            // so just create the associated a0 request and response objects
//            // and set the associated doc query strategy impl
//            NhinDocQueryStrategyContext context = new NhinDocQueryStrategyContext(
//                    new NhinPatientDiscoveryStrategyImpl_g0());
//            EntityPatientDiscoveryOrchestratable_a0 request_a0
//                    = new EntityPatientDiscoveryOrchestratable_a0(
//                    null, null, null, null,
//                    message.getAssertion(), message.getServiceName(),
//                    (NhinTargetSystemType)message.getTarget(),
//                    (PRPAIN201305UV02)message.getRequest());
//            response_a0 = (EntityPatientDiscoveryOrchestratable_a0)context.executeStrategy(request_a0);
//        }
//
//        if(response_a0 != null){
//            return response_a0;
//        }else{
//            return response_a1;
//            return response_a0;
//        }
    }


    private Log getLogger() {
        return log;
    }
}
