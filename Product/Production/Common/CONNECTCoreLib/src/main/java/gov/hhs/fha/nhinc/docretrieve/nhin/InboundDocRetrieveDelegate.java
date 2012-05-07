/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docretrieve.nhin;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocumentConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.InboundDelegate;
import gov.hhs.fha.nhinc.orchestration.InboundOrchestratable;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContext;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextFactory;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
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
public class InboundDocRetrieveDelegate implements InboundDelegate {

    private static Log log = LogFactory.getLog(InboundDocRetrieveDelegate.class);

    public InboundDocRetrieveDelegate() {
    }

    @Override
    public Orchestratable process(Orchestratable message) {
        if (message instanceof InboundOrchestratable) {
            return process((InboundOrchestratable) message);
        } else {
            getLogger().error("message is not an instance of NhinDocRetrieveOrchestratable!");
        }
        return null;
    }

    public InboundOrchestratable process(InboundOrchestratable message) {

        HomeCommunityType hcid = new HomeCommunityType();
        try {
            hcid.setHomeCommunityId(PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                    NhincConstants.HOME_COMMUNITY_ID_PROPERTY));
        } catch (PropertyAccessException ex) {
            ex.printStackTrace();
        }
        InboundDocRetrieveContextBuilder contextBuilder = (InboundDocRetrieveContextBuilder) OrchestrationContextFactory
                .getInstance().getBuilder(hcid, NhincConstants.ADAPTER_DOC_RETRIEVE_SERVICE_NAME);

        contextBuilder.setContextMessage(message);
        OrchestrationContext context = ((OrchestrationContextBuilder) contextBuilder).build();

        return (InboundOrchestratable) context.execute();
    }

    public void createErrorResponse(InboundOrchestratable message, String error) {
        if (message == null) {
            getLogger().debug("NhinOrchestratable was null");
            return;
        }

        if (message instanceof InboundDocRetrieveOrchestratableImpl) {
            RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
            RegistryResponseType responseType = new RegistryResponseType();
            response.setRegistryResponse(responseType);
            responseType.setStatus(DocumentConstants.XDS_RETRIEVE_RESPONSE_STATUS_FAILURE);
            RegistryErrorList regErrList = new RegistryErrorList();
            responseType.setRegistryErrorList(regErrList);
            RegistryError regErr = new RegistryError();
            regErrList.getRegistryError().add(regErr);
            regErr.setCodeContext(error);
            regErr.setErrorCode("XDSRepositoryError");
            regErr.setSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
            ((InboundDocRetrieveOrchestratableImpl) message).setResponse(response);
        }
    }

    private Log getLogger() {
        return log;
    }
}
