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
package gov.hhs.fha.nhinc.hiem.processor.entity.handler;

import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.hiem.configuration.topicconfiguration.TopicConfigurationEntry;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;

/**
 * Factory to create the correct entity subscribe handler based on a topic
 * configuration.
 *
 * @author Neil Webb
 */
public class EntitySubscribeHandlerFactory
{
    /**
     * Create the appropriate entity subscription message handler based on the 
     * topic configuration for the topic of the subscribe message.
     * 
     * @param topicConfig Topic configuration
     * @param patientIdentifier Optional patient identifier
     * @return Entity subscribe message handler
     */
    public EntitySubscribeHandler getEntitySubscribeHandler(TopicConfigurationEntry topicConfig, QualifiedSubjectIdentifierType patientIdentifier)
    {
        /*
         * The only initial processors will be patient-centric and non-patient-centric.
         * Additional processors may be useful in the future such as document based.
         */
        if(patientIdentifier != null)
        {
            EntitySubscribeHandler handler = new PatientCentricEntitySubscribeHandler();
            handler.setPatientIdentifier(patientIdentifier);
            handler.setPatientIdentiferLocation(topicConfig.getPatientIdentifierSubscribeLocation());
            return handler;
        }
        else
        {
            return new TargetedEntitySubscribeHandler();
        }
    }
}
