package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.orchestration.EntityOrchestratable;
import gov.hhs.fha.nhinc.orchestration.NhinDelegate;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextFactory;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import javax.xml.bind.JAXBContext;
import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
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

        EntityOrchestratable resp = null;
        EntityDocQueryOrchestratable_a0 response_a0 = null;
        EntityDocQueryOrchestratable_a0 response_a1 = null;
        if(message instanceof EntityDocQueryOrchestratable){
            EntityDocQueryOrchestratable DQMessage = (EntityDocQueryOrchestratable)message;
            OrchestrationContextBuilder contextBuilder = OrchestrationContextFactory.getInstance().getBuilder(
                DQMessage.getAssertion().getHomeCommunity(), DQMessage.getServiceName());
            if(contextBuilder instanceof EntityDocQueryOrchestrationContextBuilder_g0){
                ((EntityDocQueryOrchestrationContextBuilder_g0) contextBuilder).init(DQMessage);
//            }else if(contextBuilder instanceof EntityDocQueryOrchestrationContextBuilder_g1){
//                ((EntityDocQueryOrchestrationContextBuilder_g1) contextBuilder).init(DQMessage);
            }else{
                return null;
            }
            resp = contextBuilder.build().execute();

            if(resp instanceof EntityDocQueryOrchestratable_a0){
                response_a0 = (EntityDocQueryOrchestratable_a0)resp;
                // for debug
                getLogger().debug("NhinDocQueryDelegate has response=" + marshalResponseToString(response_a0.getResponse()));
            }else{
                response_a1 = (EntityDocQueryOrchestratable_a0)resp;
                // for debug
                getLogger().debug("NhinDocQueryDelegate has response=" + marshalResponseToString(response_a1.getResponse()));
            }
        }else{
            getLogger().error("NhinDocQueryDelegate message is not an instance of NhinDocQueryOrchestratable!");
        }
        if(response_a0 != null){
            return response_a0;
        }else{
            return response_a1;
        }
    }



    // for debug
    private String marshalResponseToString(AdhocQueryResponse response){
        // marshall response object to string to output to web page
        String xml = null;
        if(response == null){
            System.out.println("NhinDocQueryDelegate has null AdhocQueryResponse response!!!");
            return null;
        }
        try{
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("oasis.names.tc.ebxml_regrep.xsd.query._3");
            javax.xml.bind.Marshaller marshaller = jc.createMarshaller();
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(response, stringWriter);
            xml = stringWriter.toString();
        }catch(Exception e){}
        return xml;
    }


    private Log getLogger() {
        return log;
    }

}
