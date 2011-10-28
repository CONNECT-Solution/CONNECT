package gov.hhs.fha.nhinc.logger;

import java.sql.Timestamp;

public abstract class ConnectLog {

	private String transactionId;
	private TransactionType transactionType;
	private Timestamp beginTime;
	
	public ConnectLog(String transactionId, TransactionType transactionType) {
		super();
		this.setTransactionId(transactionId);
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

	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	public String getLogInfo() {
		return "\t" + transactionId + "\t" + transactionType + "\t";
	}
}
