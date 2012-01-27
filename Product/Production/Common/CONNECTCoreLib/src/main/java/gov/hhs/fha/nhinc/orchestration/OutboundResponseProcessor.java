package gov.hhs.fha.nhinc.orchestration;


/**
 * Used for concurrent task executor based orchestrator implementations
 * @author paul.eftis
 */
public interface OutboundResponseProcessor extends InboundAggregator{

    public OutboundOrchestratableMessage processNhinResponse(
            OutboundOrchestratableMessage individualResponse,
            OutboundOrchestratableMessage cumulativeResponse);

    public OutboundOrchestratableMessage processErrorResponse(OutboundOrchestratableMessage request,
            String error);
}
