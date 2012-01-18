package gov.hhs.fha.nhinc.orchestration;

public final class OrchestrationContextBuilderBase implements OrchestrationContextBuilder {

    private OrchestrationStrategy strategy;
    private Orchestratable message;

    public OrchestrationContextBuilder setStrategy(OrchestrationStrategy strategy) {
        this.strategy = strategy;
        return this;
    }

    public OrchestrationStrategy getStrategy() {
        return strategy;
    }

    public Orchestratable getOrchestratable() {
        return message;
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.orchestration.OrchestraionContextBuilder#build()
     */
    public OrchestrationContext build() {
        return new OrchestrationContext(getStrategy(), getOrchestratable());
    }

    public OrchestrationContextBuilder setOrchestratable(Orchestratable message) {
        this.message = message;
        return this;
    }
}
