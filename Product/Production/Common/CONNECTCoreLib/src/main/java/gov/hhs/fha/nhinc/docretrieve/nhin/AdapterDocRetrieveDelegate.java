/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docretrieve.nhin;

import gov.hhs.fha.nhinc.orchestration.AdapterDelegate;
import gov.hhs.fha.nhinc.orchestration.NhinOrchestratable;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author mweaver
 */
public class AdapterDocRetrieveDelegate implements AdapterDelegate {

    private static Log log = LogFactory.getLog(AdapterDocRetrieveDelegate.class);

    public AdapterDocRetrieveDelegate() {
    }

    @Override
	public Orchestratable process(Orchestratable message) {
    	if (message instanceof NhinDocRetrieveOrchestratable) {
    		process((NhinOrchestratable) message);
    	  } else {
              getLogger().error("message is not an instance of NhinDocRetrieveOrchestratable!");
          }
		return null;
	}
    
    
    public NhinOrchestratable process(NhinOrchestratable message) {
   
            // TODO: check connection manager for which endpoint to use

            // if we are using _a0
            if (true) {
                AdapterDocRetrieveStrategyContext context = new AdapterDocRetrieveStrategyContext(new AdapterDocRetrieveStrategyImpl_a0());
                context.executeStrategy((NhinDocRetrieveOrchestratable) message);
            } else { // if we are using _a1
                // TODO: implement _a1 strategy
            }
            
            return null;
    }

    public void createErrorResponse(NhinOrchestratable message, String error) {
        if (message == null) {
            getLogger().debug("NhinOrchestratable was null");
            return;
        }

        if (message instanceof NhinDocRetrieveOrchestratableImpl_g0) {
            RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
            RegistryResponseType responseType = new RegistryResponseType();
            response.setRegistryResponse(responseType);
            responseType.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
            RegistryErrorList regErrList = new RegistryErrorList();
            responseType.setRegistryErrorList(regErrList);
            RegistryError regErr = new RegistryError();
            regErrList.getRegistryError().add(regErr);
            regErr.setCodeContext(error);
            regErr.setErrorCode("XDSRepositoryError");
            regErr.setSeverity("Error");
            ((NhinDocRetrieveOrchestratableImpl_g0) message).setResponse(response);
        } else /* if(message instanceof NhinDocRetrieveOrchestratableImpl_g1) */ {

        }
    }

    private Log getLogger() {
        return log;
    }

    private class AdapterDocRetrieveStrategyContext {

        private AdapterDocRetrieveStrategy strategy;

        // Constructor
        public AdapterDocRetrieveStrategyContext(AdapterDocRetrieveStrategy strategy) {
            this.strategy = strategy;
        }

        public void executeStrategy(NhinDocRetrieveOrchestratable message) {
            strategy.execute(message);
        }
    }

	
}
