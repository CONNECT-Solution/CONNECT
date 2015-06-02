/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

/**
 *
 * @author jhoppesc
 */
public class PatientDiscoveryAuditLogger implements PatientDiscoveryAuditor {

	private final AuditRepositoryLogger auditLogger;
	private AuditRepositoryProxyObjectFactory auditRepoFactory;
	private final AuditRepositoryProxy proxy;



	public PatientDiscoveryAuditLogger() {
		auditLogger = getAuditRepositoryLogger();
		auditRepoFactory = getAuditRepositoryProxyObjectFactory();
		 proxy = auditRepoFactory.getAuditRepositoryProxy();
	}

    protected AuditRepositoryLogger getAuditRepositoryLogger() {
        if (auditLogger == null){
            return new AuditRepositoryLogger();
        }
        return auditLogger;
    }

    protected AuditRepositoryProxyObjectFactory getAuditRepositoryProxyObjectFactory(){
        if(auditRepoFactory == null){
            return new AuditRepositoryProxyObjectFactory();
        }
        return auditRepoFactory;
    }

	public PatientDiscoveryAuditLogger(
			AuditRepositoryLogger auditRepositoryLogger,AuditRepositoryProxy proxy) {
		this.auditLogger  = auditRepositoryLogger;
		this.proxy = proxy;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor#auditNhin201305
	 * (org.hl7.v3.PRPAIN201305UV02,
	 * gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String)
	 */
	@Override
	public AcknowledgementType auditNhin201305(PRPAIN201305UV02 request,
			AssertionType assertion, String direction) {
		return audit(auditLogger.logNhinPatientDiscReq(
					request, assertion, direction,
					NhincConstants.AUDIT_LOG_SYNC_TYPE), assertion);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor#
	 * auditNhinDeferred201305(org.hl7.v3.PRPAIN201305UV02,
	 * gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String)
	 */
	@Override
	public AcknowledgementType auditNhinDeferred201305(
			PRPAIN201305UV02 request, AssertionType assertion, String direction) {
		return audit(auditLogger.logNhinPatientDiscReq(
					request, assertion, direction,
					NhincConstants.AUDIT_LOG_DEFERRED_TYPE), assertion);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor#auditAdapter201305
	 * (org.hl7.v3.PRPAIN201305UV02,
	 * gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String)
	 */
	@Override
	public AcknowledgementType auditAdapter201305(PRPAIN201305UV02 request,
			AssertionType assertion, String direction) {
		return audit(auditLogger.logAdapterPatientDiscReq(
					request, assertion, direction,
					NhincConstants.AUDIT_LOG_SYNC_TYPE), assertion);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor#
	 * auditAdapterDeferred201305(org.hl7.v3.PRPAIN201305UV02 ,
	 * gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String)
	 */
	@Override
	public AcknowledgementType auditAdapterDeferred201305(
			PRPAIN201305UV02 request, AssertionType assertion, String direction) {
		return audit(auditLogger.logAdapterPatientDiscReq(
					request, assertion, direction,
					NhincConstants.AUDIT_LOG_DEFERRED_TYPE), assertion);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor#auditNhin201306
	 * (org.hl7.v3.PRPAIN201306UV02,
	 * gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String)
	 */
	@Override
	public AcknowledgementType auditNhin201306(PRPAIN201306UV02 request,
			AssertionType assertion, String direction) {
		return audit(auditLogger.logNhinPatientDiscResp(
					request, assertion, direction,
					NhincConstants.AUDIT_LOG_SYNC_TYPE), assertion);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor#
	 * auditNhinDeferred201306(org.hl7.v3.PRPAIN201306UV02,
	 * gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String)
	 */
	@Override
	public AcknowledgementType auditNhinDeferred201306(
			PRPAIN201306UV02 request, AssertionType assertion, String direction) {
		return audit(auditLogger.logNhinPatientDiscResp(
					request, assertion, direction,
					NhincConstants.AUDIT_LOG_DEFERRED_TYPE), assertion);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor#auditAdapter201306
	 * (org.hl7.v3.PRPAIN201306UV02,
	 * gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String)
	 */
	@Override
	public AcknowledgementType auditAdapter201306(PRPAIN201306UV02 request,
			AssertionType assertion, String direction) {
		return audit(auditLogger
					.logAdapterPatientDiscResp(request, assertion, direction,
							NhincConstants.AUDIT_LOG_SYNC_TYPE),  assertion);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor#
	 * auditAdapterDeferred201306(org.hl7.v3.PRPAIN201306UV02 ,
	 * gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String)
	 */
	@Override
	public AcknowledgementType auditAdapterDeferred201306(
			PRPAIN201306UV02 request, AssertionType assertion, String direction) {

		// Set up the audit logging request message
		return  audit(auditLogger
					.logAdapterPatientDiscResp(request, assertion, direction,
							NhincConstants.AUDIT_LOG_DEFERRED_TYPE), assertion);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor#auditEntity201305
	 * (org.hl7.v3. RespondingGatewayPRPAIN201305UV02RequestType,
	 * gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String)
	 */
	@Override
	public AcknowledgementType auditEntity201305(
			RespondingGatewayPRPAIN201305UV02RequestType request,
			AssertionType assertion, String direction) {

		// Set up the audit logging request message
		return audit(auditLogger.logEntityPatientDiscReq(
					request, assertion, direction,
					NhincConstants.AUDIT_LOG_SYNC_TYPE, null), assertion);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor#
	 * auditEntityDeferred201305(org.hl7.v3.
	 * RespondingGatewayPRPAIN201305UV02RequestType,
	 * gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public AcknowledgementType auditEntityDeferred201305(
			RespondingGatewayPRPAIN201305UV02RequestType request,
			AssertionType assertion, String direction, String _process) {

		// Set up the audit logging request message
		return audit(auditLogger.logEntityPatientDiscReq(
				request, assertion, direction,
				NhincConstants.AUDIT_LOG_DEFERRED_TYPE, _process), assertion);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor#auditEntity201306
	 * (org.hl7.v3. RespondingGatewayPRPAIN201306UV02ResponseType,
	 * gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String)
	 */
	@Override
	public AcknowledgementType auditEntity201306(
			RespondingGatewayPRPAIN201306UV02ResponseType request,
			AssertionType assertion, String direction) {

		// Set up the audit logging request message
		return audit(auditLogger.logEntityPatientDiscResp(
				request, assertion, direction,
				NhincConstants.AUDIT_LOG_SYNC_TYPE), assertion);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor#auditEntity201306
	 * (org.hl7.v3. RespondingGatewayPRPAIN201306UV02RequestType,
	 * gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String)
	 */
	@Override
	public AcknowledgementType auditEntityDeferred201306(
			RespondingGatewayPRPAIN201306UV02RequestType request,
			AssertionType assertion, String direction) {
		return audit(auditLogger.logEntityPatientDiscAsyncResp(request,
				assertion, direction, NhincConstants.AUDIT_LOG_DEFERRED_TYPE), assertion);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor#auditAck(org
	 * .hl7.v3.MCCIIN000002UV01,
	 * gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public AcknowledgementType auditAck(MCCIIN000002UV01 request,
			AssertionType assertion, String direction, String _interface) {

		return audit(auditLogger.logNhinPatientDiscAck(request, assertion,
				direction, _interface), assertion);

	}

	/**
	 * Creates an audit log for an AdhocQueryRequest or AdhocQueryResponse
	 *
	 * @param auditLogMsg
	 *            AdhocQueryRequest or AdhocQueryResponse message to log.
	 * @return Returns an AcknowledgementType object indicating whether the
	 *         audit message was successfully stored.
	 */
	protected AcknowledgementType audit(LogEventRequestType auditLogMsg, AssertionType assertion) {
		AcknowledgementType ack = new AcknowledgementType();
		if (auditLogMsg != null) {
			ack = sendToProxy(auditLogMsg, assertion);
		}
		return ack;
	}

	protected AcknowledgementType sendToProxy(
			LogEventRequestType auditLogMsg, AssertionType assertion) {

		AcknowledgementType ack  = proxy.auditLog(auditLogMsg, assertion);
		return ack;
	}

}
