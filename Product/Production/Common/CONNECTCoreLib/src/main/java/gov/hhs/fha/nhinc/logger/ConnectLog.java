package gov.hhs.fha.nhinc.logger;

import java.sql.Timestamp;

public abstract class ConnectLog {

	private TransactionType transactionType;
	private Timestamp beginTime;
	
	public ConnectLog(TransactionType transactionType) {
		super();
		this.setTransactionType(transactionType);
	}

	protected long getDuration() {
		long duration = Math.abs(System.currentTimeMillis() - getBeginTime().getTime());
		return duration;
	}

	/**
	 * @return the transactionType
	 */
	protected TransactionType getTransactionType() {
		return transactionType;
	}

	/**
	 * @param transactionType the transactionType to set
	 */
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * @return the beginTime
	 */
	protected Timestamp getBeginTime() {
		return beginTime;
	}

	/**
	 * @param beginTime the beginTime to set
	 */
	protected void setBeginTime() {
		this.beginTime = new Timestamp(System.currentTimeMillis());
	}
}
