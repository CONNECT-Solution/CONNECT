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
package gov.hhs.fha.nhinc.docsubmission.aspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;

import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.junit.Test;

import com.google.common.base.Optional;

import gov.hhs.fha.nhinc.event.BaseDescriptionBuilderTest;
import gov.hhs.fha.nhinc.event.EventDescription;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocumentConstants;

/**
 * @author akong
 * 
 */
public class RegistryResponseDescriptionBuilderTest extends BaseDescriptionBuilderTest {

    @Test
    public void emptyBuild() {
        RegistryResponseDescriptionBuilder builder = new RegistryResponseDescriptionBuilder(null);
        EventDescription eventDescription = getEventDescription(builder);
        assertNotNull(eventDescription);
    }

    @Test
    public void emptyResponse() {
        RegistryResponseType response = new RegistryResponseType();

        RegistryResponseDescriptionBuilder builder = new RegistryResponseDescriptionBuilder(response);
        EventDescription eventDescription = getEventDescription(builder);

        assertEquals(null, eventDescription.getStatuses());
        assertEquals(null, eventDescription.getErrorCodes());
    }
    
    @Test
    public void errorResponse() {
        RegistryResponseType response = new RegistryResponseType();
        
        RegistryError error = new RegistryError();
        error.setErrorCode(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE);
        addError(response, error);

        error = new RegistryError();
        error.setErrorCode(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE);
        addError(response, error);

        error = new RegistryError();
        error.setErrorCode(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS);
        addError(response, error);
               
        RegistryResponseDescriptionBuilder builder = new RegistryResponseDescriptionBuilder(response);
        EventDescription eventDescription = getEventDescription(builder);
        
        assertEquals(3, eventDescription.getErrorCodes().size());
        assertEquals(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE, eventDescription.getErrorCodes().get(0));
        assertEquals(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE, eventDescription.getErrorCodes().get(1));
        assertEquals(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS,
                eventDescription.getErrorCodes().get(2));
    }
    
    private void addError(RegistryResponseType response, RegistryError error) {
        RegistryErrorList registryErrorList = response.getRegistryErrorList();
        if (registryErrorList == null) {
            registryErrorList = new RegistryErrorList();
            response.setRegistryErrorList(registryErrorList);
        }
        List<RegistryError> registryError = registryErrorList.getRegistryError();
        registryError.add(error);
    }
}
