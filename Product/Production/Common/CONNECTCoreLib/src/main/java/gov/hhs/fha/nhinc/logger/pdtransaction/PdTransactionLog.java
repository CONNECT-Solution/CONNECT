package gov.hhs.fha.nhinc.logger.pdtransaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.logger.ConnectLog;
import gov.hhs.fha.nhinc.logger.TransactionLog;
import gov.hhs.fha.nhinc.logger.TransactionType;

public class PdTransactionLog extends ConnectLog implements TransactionLog {

	private Log log;
	private String sourceGateway;
	
	public PdTransactionLog(String transactionId, TransactionType transactionType) {
		super(transactionId, transactionType);
		log = LogFactory.getLog(PdTransactionLog.class);
	}
	
	public void init(String sourceGateway) {
		this.sourceGateway = sourceGateway;
	}
	
	public void begin() {
		setBeginTime();
		log.info ("Start" + getLogInfo() + "sourceGateway::" + sourceGateway);
	}
	
	public void end(String status) {
		log.info (status + getLogInfo() + getDuration() + " msec sourceGateway::" + sourceGateway);
	}

	/**
	 * @param sourceGateway the sourceGateway to set
	 */
	public void setSourceGateway(String sourceGateway) {
		this.sourceGateway = sourceGateway;
	}
}
