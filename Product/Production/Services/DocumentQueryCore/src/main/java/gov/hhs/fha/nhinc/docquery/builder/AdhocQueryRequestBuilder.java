/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docquery.builder;

import gov.hhs.fha.nhinc.docquery.xdsb.helper.XDSbConstants.ReturnType;
import gov.hhs.fha.nhinc.docquery.xdsb.helper.XDSbConstants.XDSbStoredQuery;
import gov.hhs.fha.nhinc.messaging.builder.Builder;
import java.util.Date;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

/**
 *
 * @author tjafri
 */
public interface AdhocQueryRequestBuilder extends Builder {

    /**
     * Gets the message.
     *
     * @return the message
     */
    public AdhocQueryRequest getMessage();

    /**
     * Gets the query type.
     *
     * @return the query type
     */
    public XDSbStoredQuery getQueryId();

    /**
     * Sets the return composed objects.
     *
     * @param bool the new return composed objects
     */
    public void setReturnComposedObjects(boolean bool);

    /**
     * Sets the return type.
     *
     * @param returnType the new return type
     */
    public void setReturnType(ReturnType returnType);

    /**
     * @param patientId
     */
    public void setPatientId(String patientId);

    /**
     * @param documentTypeCode
     */
    public void setDocumentTypeCode(List<String> documentTypeCode);

    /**
     * @param startTime
     */
    public void setCreationTimeFrom(Date startTime);

    /**
     * @param endTime
     */
    public void setCreationTimeTo(Date endTime);

    /**
     * @param patientIdRoot
     */
    public void setPatientIdRoot(String patientIdRoot);

}
