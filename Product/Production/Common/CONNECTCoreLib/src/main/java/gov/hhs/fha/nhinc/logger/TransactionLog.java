package gov.hhs.fha.nhinc.logger;

public interface TransactionLog {
	
	public void begin ();
	
	public void end ();
}
