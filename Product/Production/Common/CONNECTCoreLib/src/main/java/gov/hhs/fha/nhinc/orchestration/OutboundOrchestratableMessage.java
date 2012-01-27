package gov.hhs.fha.nhinc.orchestration;

/**
 * Used for concurrent task executor based orchestrator implementations
 * @author paul.eftis
 */
public interface OutboundOrchestratableMessage extends OutboundOrchestratable{
    
    public OutboundResponseProcessor getResponseProcessor();
    
}
