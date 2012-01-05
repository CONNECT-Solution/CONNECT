package gov.hhs.fha.nhinc.orchestration;


public class OrchestrationContext {
	
	 private OrchestrationStrategy strategy;
	 private EntityOrchestratable message;
	 
     public OrchestrationContext( OrchestrationStrategy strategy, EntityOrchestratable message) {
         this.strategy = strategy;
         this.message = message;
     }

     public EntityOrchestratable execute() {
         strategy.execute(message);
         return message;
     }

}
