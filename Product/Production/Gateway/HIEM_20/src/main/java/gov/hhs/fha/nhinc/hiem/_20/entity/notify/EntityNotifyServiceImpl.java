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
package gov.hhs.fha.nhinc.hiem._20.entity.notify;

import javax.xml.ws.WebServiceContext;

import org.oasis_open.docs.wsn.b_2.Notify;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.NotifyRequestType;
import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import gov.hhs.fha.nhinc.hiem.processor.entity.EntityNotifyProcessor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;

/**
 *
 *
 * @author Neil Webb
 */
public class EntityNotifyServiceImpl {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
            .getLog(EntityNotifyServiceImpl.class);

    /**
     * Returns the performance manager.
     * @return the instance
     */
    protected PerformanceManager getPerformanceManager(){
    	return PerformanceManager.getPerformanceManagerInstance();
    }
    
    /**
     * Return a new SoapUtil instance.
     * @return the instance
     */
    protected SoapUtil getSoapUtil(){
    	return new SoapUtil();
    }
    
    /**
     * Return the notify processor.
     * @return the processor
     */
    protected EntityNotifyProcessor getEntityNotifyProcessor(){
    	return new EntityNotifyProcessor();
    }
    
    /**
     * The implementation of the Entity notify interface.
     * @param notifyRequest the request to send
     * @param context the contect of the request
     * @return an acknowledgment
     */
    public AcknowledgementType notify(NotifyRequestType notifyRequest, WebServiceContext context) {
        log.debug("EntityNotifyServiceImpl.notify");
        AcknowledgementType ack = new AcknowledgementType();

        try {
            // Log the start of the entity performance record
        	getPerformanceManager().logPerformanceStart(
                    NhincConstants.HIEM_NOTIFY_ENTITY_SERVICE_NAME, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE,
                    NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, HomeCommunityMap.getLocalHomeCommunityId());

            String rawNotifyXml = getSoapUtil().extractSoapMessage(context, "notifySoapMessage");

            EntityNotifyProcessor processor = getEntityNotifyProcessor();
            processor.processNotify(notifyRequest.getNotify(), notifyRequest.getAssertion(), rawNotifyXml);

            // Log the end of the entity performance record
            getPerformanceManager().logPerformanceStop(
                    NhincConstants.HIEM_NOTIFY_ENTITY_SERVICE_NAME, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE,
                    NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, HomeCommunityMap.getLocalHomeCommunityId());
        } catch (Throwable t) {
            log.error("Exception encountered processing notify message: " + t.getMessage(), t);
        }

        return ack;
    }

    /**
     * The implementation of the Entity notify interface.
     * @param notifyRequest the request to send
     * @param context the contect of the request
     * @return an acknowledgment
     */
    public AcknowledgementType notify(Notify notifyRequest, WebServiceContext context) {
        log.debug("EntityNotifyServiceImpl.notify");
        AcknowledgementType ack = new AcknowledgementType();

        try {
            // Log the start of the entity performance record
        	getPerformanceManager().logPerformanceStart(
                    NhincConstants.HIEM_NOTIFY_ENTITY_SERVICE_NAME, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE,
                    NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, HomeCommunityMap.getLocalHomeCommunityId());
            
            String rawNotifyXml = getSoapUtil().extractSoapMessage(context, "notifySoapMessage");

            EntityNotifyProcessor processor = getEntityNotifyProcessor();
            processor.processNotify(notifyRequest, SamlTokenExtractor.GetAssertion(context), rawNotifyXml);

            // Log the end of the entity performance record
            getPerformanceManager().logPerformanceStop(
                    NhincConstants.HIEM_NOTIFY_ENTITY_SERVICE_NAME, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE,
                    NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, HomeCommunityMap.getLocalHomeCommunityId());
        } catch (Throwable t) {
            log.error("Exception encountered processing notify message: " + t.getMessage(), t);
        }

        return ack;
    }

}
