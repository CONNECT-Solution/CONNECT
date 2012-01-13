/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.orchestration.EntityOrchestratable;
import gov.hhs.fha.nhinc.orchestration.NhinDelegate;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContext;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextFactory;
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

    @Override
    public Orchestratable process(Orchestratable message) {
        if (message instanceof EntityOrchestratable) {
            return process((EntityOrchestratable) message);
        }
        return null;
    }

    public EntityOrchestratable process(EntityOrchestratable message) {
        EntityOrchestratable resp = null;
        if (message instanceof EntityDocRetrieveOrchestratable) {
            EntityDocRetrieveOrchestratable DRMessage = (EntityDocRetrieveOrchestratable) message;
            // TODO: check connection manager for which endpoint to use

            EntityDocRetrieveOrchestrationContextBuilder contextBuilder = ((EntityDocRetrieveOrchestrationContextBuilder) OrchestrationContextFactory.getInstance().getBuilder(
                    DRMessage.getAssertion().getHomeCommunity(), DRMessage.getServiceName()));
            contextBuilder.init(DRMessage);

            OrchestrationContext context = contextBuilder.build();

            resp = context.execute();
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
        } else /*
         * if(message instanceof NhinDocRetrieveOrchestratableImpl_g1)
         */ {
        }
    }

    private Log getLogger() {
        return log;
    }
}
