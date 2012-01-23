package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

public interface PatientDiscoveryAuditor {

	/**
	 * Creates an audit log for a NHIN 201305 message.
	 * @param request 201305 message to log
	 * @param direction Indicates whether the message is going out or comming in
	 * @param _interface Indicates which interface component is being logged??
	 * @return Returns an acknowledgement object indicating whether the audit was successfully completed.
	 */
	public AcknowledgementType auditNhin201305(PRPAIN201305UV02 request,
			AssertionType assertion, String direction);

	/**
	 * Creates an audit log for a NHIN Deferred 201305 message.
	 * @param request 201305 message to log
	 * @param direction Indicates whether the message is going out or comming in
	 * @param _interface Indicates which interface component is being logged??
	 * @return Returns an acknowledgement object indicating whether the audit was successfully completed.
	 */
	public AcknowledgementType auditNhinDeferred201305(
			PRPAIN201305UV02 request, AssertionType assertion, String direction);

	/**
	 * Creates an audit log for an Adapter 201305 message.
	 * @param request 201305 message to log
	 * @param direction Indicates whether the message is going out or comming in
	 * @param _interface Indicates which interface component is being logged??
	 * @return Returns an acknowledgement object indicating whether the audit was successfully completed.
	 */
	public AcknowledgementType auditAdapter201305(PRPAIN201305UV02 request,
			AssertionType assertion, String direction);

	/**
	 * Creates an audit log for an Adapter Deferred 201305 message.
	 * @param request 201305 message to log
	 * @param direction Indicates whether the message is going out or comming in
	 * @param _interface Indicates which interface component is being logged??
	 * @return Returns an acknowledgement object indicating whether the audit was successfully completed.
	 */
	public AcknowledgementType auditAdapterDeferred201305(
			PRPAIN201305UV02 request, AssertionType assertion, String direction);

	/**
	 * Creates an audit log for a NHIN 201306 message.
	 * @param request 201306 message to log
	 * @param direction Indicates whether the message is going out or comming in
	 * @param _interface Indicates which interface component is being logged??
	 * @return Returns an acknowledgement object indicating whether the audit was successfully completed.
	 */
	public AcknowledgementType auditNhin201306(PRPAIN201306UV02 request,
			AssertionType assertion, String direction);

	/**
	 * Creates an audit log for a NHIN Deferred 201306 message.
	 * @param request 201306 message to log
	 * @param direction Indicates whether the message is going out or comming in
	 * @param _interface Indicates which interface component is being logged??
	 * @return Returns an acknowledgement object indicating whether the audit was successfully completed.
	 */
	public AcknowledgementType auditNhinDeferred201306(
			PRPAIN201306UV02 request, AssertionType assertion, String direction);

	/**
	 * Creates an audit log for an Adapter 201306 message.
	 * @param request 201306 message to log
	 * @param direction Indicates whether the message is going out or comming in
	 * @param _interface Indicates which interface component is being logged??
	 * @return Returns an acknowledgement object indicating whether the audit was successfully completed.
	 */
	public AcknowledgementType auditAdapter201306(PRPAIN201306UV02 request,
			AssertionType assertion, String direction);

	/**
	 * Creates an audit log for an Adapter Deferred 201306 message.
	 * @param request 201306 message to log
	 * @param direction Indicates whether the message is going out or comming in
	 * @param _interface Indicates which interface component is being logged??
	 * @return Returns an acknowledgement object indicating whether the audit was successfully completed.
	 */
	public AcknowledgementType auditAdapterDeferred201306(
			PRPAIN201306UV02 request, AssertionType assertion, String direction);

	/**
	 * Creates an audit log for an Entity 201305 message.
	 * @param request 201305 message to log
	 * @param direction Indicates whether the message is going out or comming in
	 * @param _interface Indicates which interface component is being logged??
	 * @return Returns an acknowledgement object indicating whether the audit was successfully completed.
	 */
	public AcknowledgementType auditEntity201305(
			RespondingGatewayPRPAIN201305UV02RequestType request,
			AssertionType assertion, String direction);

	/**
	 * Creates an audit log for an Entity Deferred 201305 message.
	 * @param request 201305 message to log
	 * @param direction Indicates whether the message is going out or comming in
	 * @param _interface Indicates which interface component is being logged??
	 * @param _process
	 * @return Returns an acknowledgement object indicating whether the audit was successfully completed.
	 */
	public AcknowledgementType auditEntityDeferred201305(
			RespondingGatewayPRPAIN201305UV02RequestType request,
			AssertionType assertion, String direction, String _process);

	/**
	 * Creates an audit log for an Entity 201306 message.
	 * @param request 201306 message to log
	 * @param direction Indicates whether the message is going out or comming in
	 * @param _interface Indicates which interface component is being logged??
	 * @return Returns an acknowledgement object indicating whether the audit was successfully completed.
	 */
	public AcknowledgementType auditEntity201306(
			RespondingGatewayPRPAIN201306UV02ResponseType request,
			AssertionType assertion, String direction);

	/**
	 * Creates an audit log for an Entity Deferred 201306 message.
	 * @param request 201306 message to log
	 * @param direction Indicates whether the message is going out or comming in
	 * @param _interface Indicates which interface component is being logged??
	 * @return Returns an acknowledgement object indicating whether the audit was successfully completed.
	 */
	public AcknowledgementType auditEntity201306(
			RespondingGatewayPRPAIN201306UV02RequestType request,
			AssertionType assertion, String direction);

	public AcknowledgementType auditAck(MCCIIN000002UV01 request,
			AssertionType assertion, String direction, String _interface);

}