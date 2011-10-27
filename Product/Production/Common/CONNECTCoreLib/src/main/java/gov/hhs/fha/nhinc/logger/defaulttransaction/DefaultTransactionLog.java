package gov.hhs.fha.nhinc.logger.defaulttransaction;

import gov.hhs.fha.nhinc.logger.ConnectLog;
import gov.hhs.fha.nhinc.logger.TransactionLog;
import gov.hhs.fha.nhinc.logger.TransactionType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DefaultTransactionLog extends ConnectLog implements TransactionLog {

	private Log log;
	
	public DefaultTransactionLog(TransactionType transactionType) {
		super(transactionType);
		log = LogFactory.getLog(DefaultTransactionLog.class);
	}
	
	public void begin () {
		setBeginTime();
		log.info (" Start.");
	}
	
	public void end () {
		log.info (" End in " + getDuration() + " msec");
	}
}
