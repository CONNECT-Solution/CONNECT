package gov.hhs.fha.nhinc.orchestration;

public interface Delegate {
	public Orchestratable process(Orchestratable message);
}
