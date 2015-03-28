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
package gov.hhs.fha.nhinc.corex12.docsubmission.audit;

import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import org.apache.log4j.Logger;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeRequest;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeResponse;

/**
 *
 * @author svalluripalli
 */
public class COREX12AuditRepositoryLogger {

    private static final Logger LOG = Logger.getLogger(COREX12AuditRepositoryLogger.class);
    private final COREX12RealtimeTransforms x12AuditTransformer = new COREX12RealtimeTransforms();

    /**
     *
     * @param message
     * @param assertion
     * @param target
     * @param direction
     * @param _interface
     * @return LogEventRequestType
     */
    public LogEventRequestType logNhinCoreX12RealtimeRequest(COREEnvelopeRealTimeRequest message, AssertionType assertion, NhinTargetSystemType target,
        String direction) {
        LOG.debug("Entering AuditRepositoryLogger.logNhinCoreX12RealtimeRequest(...)");
        LogEventRequestType oAuditMes = null;
        oAuditMes = x12AuditTransformer.transformRequestToAuditMsg(message, assertion, target, direction, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        LOG.debug("End AuditRepositoryLogger.logNhinCoreX12RealtimeRequest(...)");
        return oAuditMes;
    }

    /**
     *
     * @param message
     * @param assertion
     * @param target
     * @param direction
     * @param _interface
     * @param isRequesting
     * @return LogEventRequestType
     */
    public LogEventRequestType logNhinCoreX12RealtimeResponse(COREEnvelopeRealTimeResponse message, AssertionType assertion, NhinTargetSystemType target,
        String direction, String _interface, boolean isRequesting) {
        LOG.debug("Entering AuditRepositoryLogger.logNhinCoreX12RealtimeRequest(...)");
        LogEventRequestType oAuditMes = null;
        oAuditMes = x12AuditTransformer.transformResponseToAuditMsg(message, assertion, target, direction, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, isRequesting);
        LOG.debug("End AuditRepositoryLogger.logNhinCoreX12RealtimeRequest(...)");
        return oAuditMes;
    }

    /**
     *
     * @param message
     * @param assertion
     * @param target
     * @param direction
     * @param _interface
     * @return LogEventRequestType
     */
    public LogEventRequestType logAdapterCoreX12RealtimeRequest(COREEnvelopeRealTimeRequest message, AssertionType assertion, NhinTargetSystemType target,
        String direction) {
        LOG.debug("Entering AuditRepositoryLogger.logAdapterCoreX12RealtimeRequest(...)");
        LogEventRequestType oAuditMes = null;
        oAuditMes = x12AuditTransformer.transformRequestToAuditMsg(message, assertion, target, direction, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);
        LOG.debug("End AuditRepositoryLogger.logAdapterCoreX12RealtimeRequest(...)");
        return oAuditMes;
    }

    /**
     *
     * @param message
     * @param assertion
     * @param target
     * @param direction
     * @param _interface
     * @param isRequesting
     * @return LogEventRequestType
     */
    public LogEventRequestType logAdapterCoreX12RealtimeResponse(COREEnvelopeRealTimeResponse message, AssertionType assertion, NhinTargetSystemType target,
        String direction, String _interface, boolean isRequesting) {
        LOG.debug("Entering AuditRepositoryLogger.logAdapterCoreX12RealtimeResponse(...)");
        LogEventRequestType oAuditMes = null;
        oAuditMes = x12AuditTransformer.transformResponseToAuditMsg(message, assertion, target, direction, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, isRequesting);
        LOG.debug("End AuditRepositoryLogger.logAdapterCoreX12RealtimeResponse(...)");
        return oAuditMes;
    }
}
