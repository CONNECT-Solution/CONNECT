package gov.hhs.fha.nhinc.orchestration;


public class OrchestrationContext {
	
	 private OrchestrationStrategy strategy;
	 private Orchestratable message;
	 
     public OrchestrationContext( OrchestrationStrategy strategy, Orchestratable message) {
         this.strategy = strategy;
         this.message = message;
     }

     public Orchestratable execute() {
         strategy.execute(message);
         return message;
     }

}
