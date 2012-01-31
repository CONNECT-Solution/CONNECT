/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docsubmission.passthru;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.entity.OutboundDocSubmissionDelegate;
import gov.hhs.fha.nhinc.docsubmission.entity.OutboundDocSubmissionOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author JHOPPESC
 */
public class PassthruDocSubmissionOrchImpl {
    private Log log = null;

    public PassthruDocSubmissionOrchImpl() {
        log = createLogger();
    }

    public RegistryResponseType provideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion, NhinTargetSystemType targetSystem) {
        RegistryResponseType response = null;
        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType body = new RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
        body.setNhinTargetSystem(targetSystem);
        body.setProvideAndRegisterDocumentSetRequest(request);
        
        XDRAuditLogger auditLog = getAuditLogger();
        AcknowledgementType ack = auditLog.auditXDR(body, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
        log.debug("ack: " + ack.getMessage());

        OutboundDocSubmissionDelegate dsDelegate = new OutboundDocSubmissionDelegate();
        OutboundDocSubmissionOrchestratable dsOrchestratable = new OutboundDocSubmissionOrchestratable(dsDelegate);
        dsOrchestratable.setAssertion(assertion);
        dsOrchestratable.setRequest(body.getProvideAndRegisterDocumentSetRequest());
        dsOrchestratable.setTarget(body.getNhinTargetSystem());
        response = ((OutboundDocSubmissionOrchestratable) dsDelegate.process(dsOrchestratable)).getResponse();

        ack = auditLog.auditNhinXDRResponse(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
        log.debug("ack: " + ack.getMessage());
        
        return response;
    }

    protected XDRAuditLogger getAuditLogger() {
        return new XDRAuditLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
}
