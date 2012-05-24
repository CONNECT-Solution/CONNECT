/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.orchestration;

import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.properties.IPropertyAcessor;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResultType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author mweaver
 */
public abstract class CONNECTOrchestrationBase implements CONNECTOrchestrator {

	private static final Log logger = LogFactory
			.getLog(CONNECTOrchestrationBase.class);

	private IPropertyAcessor propertyAcessor;

	/**
	 * 
	 */
	public CONNECTOrchestrationBase() {
		propertyAcessor = PropertyAccessor.getInstance(
				NhincConstants.GATEWAY_PROPERTY_FILE);
	}

	/**
		 * 
		 */
	public CONNECTOrchestrationBase(IPropertyAcessor propertyAccesor) {
		propertyAcessor = propertyAccesor;
	}

	public Orchestratable process(Orchestratable message) {
		Orchestratable resp = null;
		getLogger().debug(
				"Entering CONNECTNhinOrchestrator for "
						+ message.getServiceName());
		if (message != null) {
			resp = processNotNullMessage(message);
		}
		getLogger().debug(
				"Returning from CONNECTNhinOrchestrator for "
						+ message.getServiceName());
		return resp;
	}

	public Orchestratable processNotNullMessage(Orchestratable message) {
		Orchestratable resp = null;
		// audit
		getLogger().debug("Calling audit for " + message.getServiceName());
		auditRequest(message);

		if (message.isEnabled()) {
			resp = processEnabledMessage(message);
		} else {
			getLogger().debug(
					message.getServiceName()
							+ " is not enabled. returning a error response");
			resp = createErrorResponse((InboundOrchestratable) message,
					message.getServiceName() + " is not enabled.");
		}
		// audit again
		getLogger().debug(
				"Calling audit response for " + message.getServiceName());
		auditResponse(message);
		getLogger().debug(
				"Returning from CONNECTNhinOrchestrator for "
						+ message.getServiceName());
		return resp;
	}

	public Orchestratable processEnabledMessage(Orchestratable message) {
		getLogger().debug(
				message.getServiceName()
						+ " service is enabled. Procesing message...");
		if (message.isPassthru()) {
			return processPassThruMessage(message);
		} else {
			getLogger()
					.debug(message.getServiceName()
							+ "is not in passthrough mode. Calling internal processing");
			return processIfPolicyIsOk(message);
		}
	}

	protected abstract Orchestratable processIfPolicyIsOk(Orchestratable message);

	public Orchestratable processPassThruMessage(Orchestratable message) {
		getLogger()
				.debug(message.getServiceName()
						+ " is in passthrough mode. Sending directly to adapter");
		return delegate(message);
	}

	public Orchestratable processInboundIfPolicyIsOk(Orchestratable message) {

		if (isPolicyOk(message, PolicyTransformer.Direction.INBOUND)) {
			// if true, sent to adapter
			return delegate(message);
		} else {
			return handleFailedPolicyCheck((InboundOrchestratable)message);
		}
	}

	public Orchestratable processOutboundIfPolicyIsOk(Orchestratable message) {

		if (isPolicyOk(message, PolicyTransformer.Direction.OUTBOUND)) {
			// if true, sent to adapter
			return delegate(message);
		} else {
			return handleFailedPolicyCheck((OutboundOrchestratable)message);
		}
	}

	private Orchestratable handleFailedPolicyCheck(InboundOrchestratable message) {
		getLogger().debug(
				message.getServiceName()
						+ " failed policy check. Returning a error response");
		return createErrorResponse( message,
				message.getServiceName() + " failed policy check.");
	}
	
	private Orchestratable handleFailedPolicyCheck(OutboundOrchestratable message) {
		getLogger().debug(
				message.getServiceName()
						+ " failed policy check. Returning a error response");
		return createErrorResponse( message,
				message.getServiceName() + " failed policy check.");
	}


	protected Log getLogger() {
		return logger;
	}

	/*
	 * Begin Delegate Methods
	 */
	protected Orchestratable createErrorResponse(InboundOrchestratable message,
			String error) {
		if (message != null && message.getAdapterDelegate() != null) {
			InboundDelegate delegate = message.getAdapterDelegate();
			delegate.createErrorResponse(message, error);
		}
		return message;
	}
	
	
	/*
	 * Begin Delegate Methods
	 */
	protected Orchestratable createErrorResponse(OutboundOrchestratable message,
			String error) {
		if (message != null && message.getDelegate() != null) {
			OutboundDelegate delegate = message.getDelegate();
			delegate.createErrorResponse(message, error);
		}
		return message;
	}

	/*
	 * End Delegate Methods
	 */

	/*
	 * Begin Audit Methods
	 */
	protected AcknowledgementType auditRequest(Orchestratable message) {
		AcknowledgementType resp = null;

		if (message != null && message.getAuditTransformer() != null) {
			AuditTransformer transformer = message.getAuditTransformer();
			LogEventRequestType auditLogMsg = transformer
					.transformRequest(message);
			resp = audit(auditLogMsg, message.getAssertion());
		}
		return resp;
	}

	protected AcknowledgementType auditResponse(Orchestratable message) {
		AcknowledgementType resp = null;

		if (message != null && message.getAuditTransformer() != null) {
			AuditTransformer transformer = message.getAuditTransformer();
			LogEventRequestType auditLogMsg = transformer
					.transformResponse(message);
			resp = audit(auditLogMsg, message.getAssertion());
		}
		return resp;
	}

	private AcknowledgementType audit(LogEventRequestType message,
			AssertionType assertion) {
		getLogger().debug("Entering CONNECTNhinOrchestrator.audit(...)");
		AcknowledgementType ack = null;
		try {
			if (isAuditServiceEnabled()) {
				AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
				AuditRepositoryProxy proxy = auditRepoFactory
						.getAuditRepositoryProxy();

				ack = proxy.auditLog(message, assertion);
			}
		} catch (Exception exc) {
			getLogger().error("Error: Failed to Audit message.", exc);
		}
		getLogger()
				.debug("Exiting AuditRCONNECTNhinOrchestratorepositoryLogger.audit(...)");
		return ack;
	}

	protected boolean isAuditServiceEnabled() {
		getLogger().debug(
				"Entering CONNECTNhinOrchestrator.isAuditServiceEnabled(...)");
		boolean serviceEnabled = false;
		try {
			serviceEnabled = propertyAcessor
					.getPropertyBoolean(NhincConstants.AUDIT_LOG_SERVICE_PROPERTY);

		} catch (PropertyAccessException ex) {
			getLogger().error(
					"Error: Failed to retrieve "
							+ NhincConstants.AUDIT_LOG_SERVICE_PROPERTY
							+ " from property file: "
							+ NhincConstants.GATEWAY_PROPERTY_FILE);
			getLogger().error(ex.getMessage(), ex);
		}
		getLogger().debug(
				"Exiting CONNECTNhinOrchestrator.isAuditServiceEnabled(...) with value of: "
						+ serviceEnabled);
		return serviceEnabled;
	}

	/*
	 * End Audit Methods
	 */

	/*
	 * Begin Policy Methods
	 */
	protected boolean isPolicyOk(Orchestratable message,
			PolicyTransformer.Direction direction) {
		getLogger().debug("Entering CONNECTNhinOrchestrator.isPolicyOk(...)");
		boolean policyIsValid = false;

		try {
			PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
			PolicyEngineProxy policyProxy = policyEngFactory
					.getPolicyEngineProxy();

			PolicyTransformer transformer = message.getPolicyTransformer();
			CheckPolicyRequestType policyReq = transformer.transform(message,
					direction);

			if (policyReq != null && message.getAssertion() != null) {
				CheckPolicyResponseType policyResp = policyProxy.checkPolicy(
						policyReq, message.getAssertion());

				if (policyResp.getResponse() != null
						&& policyResp.getResponse().getResult() != null) {
					// we are expecting only 1 result, if we get more we are
					// only paying attention to the first result
					for (ResultType r : policyResp.getResponse().getResult()) {
						if (r.getDecision() == DecisionType.PERMIT) {
							policyIsValid = true;
						}
						break;
					}
				}
			}
		} catch (Exception exc) {
			getLogger().error("Error: Failed to check policy.", exc);
		}
		getLogger().debug(
				"Exiting CONNECTNhinOrchestrator.isPolicyOk(...) with a value of :"
						+ policyIsValid);
		return policyIsValid;
	}

	/*
	 * End Policy Methods
	 */

	/*
	 * Begin Delegate Methods
	 */
	protected Orchestratable delegate(Orchestratable message) {
		Orchestratable resp = null;
		getLogger().debug(
				"Entering CONNECTNhinOrchestrator.delegateToNhin(...)");
		Delegate p = message.getDelegate();
		resp = p.process(message);
		getLogger()
				.debug("Exiting CONNECTNhinOrchestrator.delegateToNhin(...)");
		return resp;
	}
	/*
	 * End Delegate Methods
	 */
}
