package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.orchestration.OrchestrationContext;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;

public class OutboundDocRetrieveOrchestrationContextBuilder_g1 implements OrchestrationContextBuilder,
        OutboundDocRetrieveContextBuilder {

    private OutboundDocRetrieveOrchestratable message;

    @Override
    public OrchestrationContext build() {
        return new OrchestrationContext(new OutboundDocRetrieveStrategyImpl_g1(), message);
    }

    @Override
    public void setContextMessage(OutboundOrchestratable message) {
        if (message instanceof OutboundDocRetrieveOrchestratable) {
            setContextMessage((OutboundDocRetrieveOrchestratable) message);
        }
    }

    public void setContextMessage(OutboundDocRetrieveOrchestratable message) {
        this.message = message;
    }
}
