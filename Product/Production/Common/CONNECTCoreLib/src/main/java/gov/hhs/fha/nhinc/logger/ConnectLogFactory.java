package gov.hhs.fha.nhinc.logger;

import gov.hhs.fha.nhinc.logger.defaulttransaction.DefaultTransactionLog;
import gov.hhs.fha.nhinc.logger.pdtransaction.PdTransactionLog;

public class ConnectLogFactory {

	public static ConnectLog getTransactionLog (TransactionType transactionType) {
		switch (transactionType) {
		case PD_GATEWAY_TRANSACTION:
			return new PdTransactionLog(transactionType);

		default:
			return new DefaultTransactionLog(transactionType);
		}
	}
}
