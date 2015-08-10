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
package gov.hhs.fha.nhinc.corex12.docsubmission.audit.ejb;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.local.AuditRepositoryLoggerLocal;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import java.util.Date;
import java.util.Properties;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import org.apache.log4j.Logger;

/**
 * Stateless EJB implementation which provides the Asynchronous feature.
 *
 * @author nsubrama
 */
@Stateless
public class AuditLoggerAsyncEJB implements AuditRepositoryLoggerLocal {

    private static final Logger LOG = Logger.getLogger(AuditLoggerAsyncEJB.class);
    private final AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

    /**
     *
     * @param message
     * @param assertion
     * @param target
     * @param direction
     * @param isRequesting
     * @param webContextProperties
     * @param serviceName
     */
    @Asynchronous
    public void auditNhinCoreX12RealtimeMessage(Object message, AssertionType assertion, NhinTargetSystemType target,
        String direction, boolean isRequesting, Properties webContextProperties, String serviceName) {
        LOG.info("Inside the method AuditLoggerAsyncEJB.auditNhinCoreX12RealtimeMessage (START)-->TimeStamp:" + (new Date()));
        auditLogger.auditNhinCoreX12RealtimeMessage(message, assertion, target, direction, isRequesting, webContextProperties, serviceName);
        LOG.info("Inside the method AuditLoggerAsyncEJB.auditNhinCoreX12RealtimeMessage (END)-->TimeStamp:" + (new Date()));
    }

    /**
     *
     * @param message
     * @param assertion
     * @param target
     * @param direction
     * @param isRequesting
     * @param webContextProperties
     * @param serviceName
     */
    @Asynchronous
    public void auditNhinCoreX12BatchMessage(Object message, AssertionType assertion, NhinTargetSystemType target,
        String direction, boolean isRequesting, Properties webContextProperties, String serviceName) {
        LOG.trace("---Begin AuditLoggerAsyncEJB.auditNhinCoreX12BatchMessage()---");
        auditLogger.auditNhinCoreX12BatchMessage(message, assertion, target, direction, isRequesting, webContextProperties, serviceName);
        LOG.trace("---End AuditLoggerAsyncEJB.auditNhinCoreX12BatchMessage()---");
    }

}
