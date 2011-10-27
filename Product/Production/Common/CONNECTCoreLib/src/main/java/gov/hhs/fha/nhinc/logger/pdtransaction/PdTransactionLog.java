package gov.hhs.fha.nhinc.logger.pdtransaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.logger.ConnectLog;
import gov.hhs.fha.nhinc.logger.TransactionLog;
import gov.hhs.fha.nhinc.logger.TransactionType;

public class PdTransactionLog extends ConnectLog implements TransactionLog {

	private Log log;
	private String remoteGateway;
	
	public PdTransactionLog(TransactionType transactionType) {
		super(transactionType);
		log = LogFactory.getLog(PdTransactionLog.class);
	}
	
	public void begin () {
		setBeginTime();
		log.info (" Start remoteGateway::" + remoteGateway);
	}
	
	public void end () {
		log.info (" End in " + getDuration() + " msec remoteGateway::" + remoteGateway);
	}

	/**
	 * @param remoteGateway the remoteGateway to set
	 */
	public void setRemoteGateway(String remoteGateway) {
		this.remoteGateway = remoteGateway;
	}
}
