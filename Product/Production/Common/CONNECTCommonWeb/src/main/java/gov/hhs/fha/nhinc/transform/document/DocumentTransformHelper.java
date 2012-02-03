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
package gov.hhs.fha.nhinc.transform.document;

import gov.hhs.fha.nhinc.common.nhinccommondocumenttransform.ReplaceAdhocQueryPatientIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommondocumenttransform.ReplaceAdhocQueryPatientIdRequestType;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class to perform transform operations for document query messages.
 * 
 * @author Neil Webb
 */
public class DocumentTransformHelper 
{
    private static Log log = LogFactory.getLog(DocumentTransformHelper.class);
    
    /**
     * Replace the patient identifier information in an AdhocQuery message with the information
     * provided in the request. The information replaced includes the community id, assigning 
     * authority, and patient identifier.
     * 
     * @param replaceAdhocQueryPatientIdRequest Request message containing the AdhocQuery message
     * and patient identifier information
     * @return Altered AdhocQuery Message
     */
    public ReplaceAdhocQueryPatientIdResponseType replaceAdhocQueryPatientId(ReplaceAdhocQueryPatientIdRequestType replaceAdhocQueryPatientIdRequest)
    {
        log.debug("DocumentTransformHelper.replaceAdhocQueryPatientId() -- Begin");
        ReplaceAdhocQueryPatientIdResponseType response = new ReplaceAdhocQueryPatientIdResponseType();
        
        if ((replaceAdhocQueryPatientIdRequest != null) &&
            (replaceAdhocQueryPatientIdRequest.getAdhocQueryRequest() != null))
        {
            // Collect input data
            String homeCommunityId = replaceAdhocQueryPatientIdRequest.getHomeCommunityId();
            String assigningAuthorityId = null;
            String patientId = null;
            if(replaceAdhocQueryPatientIdRequest.getQualifiedSubjectIdentifier() != null)
            {
                assigningAuthorityId = replaceAdhocQueryPatientIdRequest.getQualifiedSubjectIdentifier().getAssigningAuthorityIdentifier();
                patientId = replaceAdhocQueryPatientIdRequest.getQualifiedSubjectIdentifier().getSubjectIdentifier();
            }
            
            // Call transform
            DocumentQueryTransform transform = new DocumentQueryTransform();
            AdhocQueryRequest adhocQueryRequest = transform.replaceAdhocQueryPatientId(replaceAdhocQueryPatientIdRequest.getAdhocQueryRequest(), homeCommunityId, assigningAuthorityId, patientId);
            response.setAdhocQueryRequest(adhocQueryRequest);
            
        }

        log.debug("DocumentQueryTransform.replaceAdhocQueryPatientId() -- End");
        return response;
    }
    
}
