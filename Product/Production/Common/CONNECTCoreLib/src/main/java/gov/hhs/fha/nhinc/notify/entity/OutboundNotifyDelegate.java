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
package gov.hhs.fha.nhinc.notify.entity;

import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextFactory;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class OutboundNotifyDelegate implements OutboundDelegate {

    private static Log log = LogFactory.getLog(OutboundNotifyDelegate.class);

    /**
     * The generic process method to build and execute the Orchestratable.
     * @param message the message to process
     * @return the Orchestratable
     */
    public Orchestratable process(Orchestratable message) {
        if (message instanceof OutboundOrchestratable) {
            return process((OutboundOrchestratable) message);
        }
        return null;
    }

    /**
     * The process method to build and execute the Orchestratable.
     * @param message the message to use
     * @return the orchestratable
     */
    @Override
    public OutboundOrchestratable process(OutboundOrchestratable message) {
        getLogger().debug("begin process");
        if (message instanceof OutboundNotifyOrchestratable) {
            getLogger().debug("processing Notify orchestratable ");
            OutboundNotifyOrchestratable dsMessage = (OutboundNotifyOrchestratable) message;

            //Notify does not get sent based on UDDI or spec version
            OutboundNotifyOrchestrationContextBuilder_g0 contextBuilder = 
            		new OutboundNotifyOrchestrationContextBuilder_g0();
            ((OutboundNotifyOrchestrationContextBuilder_g0) contextBuilder).init(message);
            
            return (OutboundOrchestratable) contextBuilder.build().execute();
        }
        getLogger().error("message is not an instance of OutboundNotifyOrchestratable!");
        return null;
    }

    /**
     * Returns an instance of the OrchestrationContextFactory.
     * @return the instance
     */
    protected OrchestrationContextFactory getOrchestrationContextFactory() {
        return OrchestrationContextFactory.getInstance();
    }

    /**
     * Return the logger.
     * @return the logger
     */
    protected Log getLogger() {
        return log;
    }

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.orchestration.OutboundDelegate#createErrorResponse(
	 * 		gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable, java.lang.String)
	 */
	@Override
	public void createErrorResponse(OutboundOrchestratable message, String error) {
		// TODO Auto-generated method stub
		
	}
}
