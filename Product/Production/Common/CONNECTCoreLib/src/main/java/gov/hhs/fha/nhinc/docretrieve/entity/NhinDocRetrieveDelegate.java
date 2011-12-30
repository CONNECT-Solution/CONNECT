/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.orchestration.EntityOrchestratable;
import gov.hhs.fha.nhinc.orchestration.NhinDelegate;
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
public class NhinDocRetrieveDelegate implements NhinDelegate {

    private static Log log = LogFactory.getLog(NhinDocRetrieveDelegate.class);

    public NhinDocRetrieveDelegate() {
    }

    public EntityOrchestratable process(EntityOrchestratable message) {
        EntityOrchestratable resp = null;
        if (message instanceof EntityDocRetrieveOrchestratable) {
            EntityDocRetrieveOrchestratable DRMessage = (EntityDocRetrieveOrchestratable) message;
            // TODO: check connection manager for which endpoint to use

            // if we are using _g0
            if (true) {
                NhinDocRetrieveStrategyContext context = new NhinDocRetrieveStrategyContext(new NhinDocRetrieveStrategyImpl_g0());
                EntityDocRetrieveOrchestratableImpl_a0 impl_a0 = new EntityDocRetrieveOrchestratableImpl_a0(DRMessage.getRequest(), DRMessage.getAssertion(), null, DRMessage.getAuditTransformer(), null, null);
                impl_a0.setTarget(DRMessage.getTarget());
                context.executeStrategy((EntityDocRetrieveOrchestratable) impl_a0);
                resp = impl_a0;
            } else { // if we are using _a1
                // TODO: implement _g1 strategy
            }
        } else {
            getLogger().error("message is not an instance of NhinDocRetrieveOrchestratable!");
        }
        return resp;
    }

    public void createErrorResponse(EntityOrchestratable message, String error) {
        if (message == null) {
            getLogger().debug("NhinOrchestratable was null");
            return;
        }

        if (message instanceof EntityDocRetrieveOrchestratableImpl_a0) {
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
            ((EntityDocRetrieveOrchestratableImpl_a0) message).setResponse(response);
        } else /* if(message instanceof NhinDocRetrieveOrchestratableImpl_g1) */ {

        }
    }

    private Log getLogger() {
        return log;
    }

    private class NhinDocRetrieveStrategyContext {

        private NhinDocRetrieveStrategy strategy;

        // Constructor
        public NhinDocRetrieveStrategyContext(NhinDocRetrieveStrategy strategy) {
            this.strategy = strategy;
        }

        public void executeStrategy(EntityDocRetrieveOrchestratable message) {
            strategy.execute(message);
        }
    }
}
