package gov.hhs.fha.nhinc.orchestration;


/**
 * Used for concurrent task executor based orchestrator implementations
 * @author paul.eftis
 */
public interface NhinResponseProcessor extends NhinAggregator{

    public EntityOrchestratableMessage processNhinResponse(
            EntityOrchestratableMessage individualResponse,
            EntityOrchestratableMessage cumulativeResponse);

    public EntityOrchestratableMessage processErrorResponse(EntityOrchestratableMessage request,
            String error);
}
