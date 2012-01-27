package gov.hhs.fha.nhinc.orchestration;

public final class BaseOrchestrationContextBuilder implements OrchestrationContextBuilder {

	private OrchestrationStrategy strategy;
	
	private OutboundOrchestratable message;
	
	public OrchestrationContextBuilder setStrategy(OrchestrationStrategy strategy) {
		this.strategy = strategy;
		return this;
	}
	
	public OrchestrationStrategy getStrategy() {
		return strategy;
	}

	
	public OrchestrationContextBuilder setMessage(OutboundOrchestratable message) {
		this.message = message;
		return this;
	}
	
	public OutboundOrchestratable getMessage() {
		return message;
	}

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.orchestration.OrchestraionContextBuilder#build()
	 */
	@Override
	public OrchestrationContext build() {
		return new OrchestrationContext(getStrategy(), getMessage());
	}

}
