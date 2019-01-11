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
package gov.hhs.fha.nhinc.docretrieve.aspect;

import gov.hhs.fha.nhinc.document.DocumentConstants;
import gov.hhs.fha.nhinc.event.BaseDescriptionBuilderTest;
import gov.hhs.fha.nhinc.event.EventDescription;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

public class RetrieveDocumentSetResponseTypeDescriptionBuilderTest extends BaseDescriptionBuilderTest {

    @Test
    public void emptyBuild() {
        RetrieveDocumentSetResponseTypeDescriptionBuilder builder = new RetrieveDocumentSetResponseTypeDescriptionBuilder();
        EventDescription eventDescription = getEventDescription(builder);
        assertNotNull(eventDescription);
    }

    @Test
    public void basicBuild() {
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        addStatus(response, DocumentConstants.XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS);
        addDocumentResponse(response, "homeCommunityId");

        RetrieveDocumentSetResponseTypeDescriptionBuilder builder = new RetrieveDocumentSetResponseTypeDescriptionBuilder();
        builder.setReturnValue(response);
        EventDescription eventDescription = getEventDescription(builder);
        assertEquals(1, eventDescription.getStatuses().size());
        assertEquals(DocumentConstants.XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS, eventDescription.getStatuses().get(0));
        assertEquals(1, eventDescription.getRespondingHCIDs().size());
        assertEquals("homeCommunityId", eventDescription.getRespondingHCIDs().get(0));
        assertTrue(CollectionUtils.isEmpty(eventDescription.getErrorCodes()));
        assertAlwaysNullAttributes(eventDescription);
    }

    @Test
    public void twoRespondingHCIds() {
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        addStatus(response, DocumentConstants.XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS);
        addDocumentResponse(response, "homeCommunityId");
        addDocumentResponse(response, "otherHomeCommunityId");

        RetrieveDocumentSetResponseTypeDescriptionBuilder builder = new RetrieveDocumentSetResponseTypeDescriptionBuilder();
        builder.setReturnValue(response);
        EventDescription eventDescription = getEventDescription(builder);
        assertEquals(1, eventDescription.getStatuses().size());
        assertEquals(DocumentConstants.XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS, eventDescription.getStatuses().get(0));
        assertEquals(2, eventDescription.getRespondingHCIDs().size());
        assertEquals("homeCommunityId", eventDescription.getRespondingHCIDs().get(0));
        assertEquals("otherHomeCommunityId", eventDescription.getRespondingHCIDs().get(1));
        assertTrue(CollectionUtils.isEmpty(eventDescription.getErrorCodes()));
        assertAlwaysNullAttributes(eventDescription);
    }

    @Test
    public void errorNoDocsInResponse() {
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        addStatus(response, DocumentConstants.XDS_RETRIEVE_RESPONSE_STATUS_FAILURE);
        addError(response);

        RetrieveDocumentSetResponseTypeDescriptionBuilder builder = new RetrieveDocumentSetResponseTypeDescriptionBuilder();
        builder.setReturnValue(response);
        EventDescription eventDescription = getEventDescription(builder);
        assertEquals(1, eventDescription.getStatuses().size());
        assertEquals(DocumentConstants.XDS_RETRIEVE_RESPONSE_STATUS_FAILURE, eventDescription.getStatuses().get(0));
        assertTrue(eventDescription.getRespondingHCIDs() == null || eventDescription.getRespondingHCIDs().isEmpty());
        assertEquals(1, eventDescription.getErrorCodes().size());
        assertEquals(DocumentConstants.XDS_RETRIEVE_ERRORCODE_REPOSITORY_ERROR, eventDescription.getErrorCodes().get(0));
        assertAlwaysNullAttributes(eventDescription);
    }

    @Test
    public void keepDupErrorList() {
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        addStatus(response, DocumentConstants.XDS_RETRIEVE_RESPONSE_STATUS_FAILURE);
        addError(response);
        addError(response);

        RetrieveDocumentSetResponseTypeDescriptionBuilder builder = new RetrieveDocumentSetResponseTypeDescriptionBuilder();
        builder.setReturnValue(response);
        EventDescription eventDescription = getEventDescription(builder);
        assertEquals(2, eventDescription.getErrorCodes().size());
        assertEquals(DocumentConstants.XDS_RETRIEVE_ERRORCODE_REPOSITORY_ERROR, eventDescription.getErrorCodes().get(0));
        assertEquals(DocumentConstants.XDS_RETRIEVE_ERRORCODE_REPOSITORY_ERROR, eventDescription.getErrorCodes().get(1));
    }

    private void addError(RetrieveDocumentSetResponseType response) {
        RegistryResponseType registryResponse = response.getRegistryResponse();
        RegistryErrorList registryErrorList = getOrCreateErrorList(registryResponse);
        RegistryError registryError = new RegistryError();
        registryError.setCodeContext("error context"); // not an enumerated value
        registryError.setErrorCode(DocumentConstants.XDS_RETRIEVE_ERRORCODE_REPOSITORY_ERROR);
        registryErrorList.getRegistryError().add(registryError);
    }

    private RegistryResponseType getOrCreateRegistryResponse(RetrieveDocumentSetResponseType response) {
        RegistryResponseType registryResponse = response.getRegistryResponse();
        if (registryResponse == null) {
            registryResponse = new RegistryResponseType();
            response.setRegistryResponse(registryResponse);
        }
        return registryResponse;
    }

    private RegistryErrorList getOrCreateErrorList(RegistryResponseType registryResponse) {
        RegistryErrorList registryErrorList = registryResponse.getRegistryErrorList();
        if (registryErrorList == null) {
            registryErrorList = new RegistryErrorList();
            registryResponse.setRegistryErrorList(registryErrorList);
        }
        return registryErrorList;
    }

    private void addDocumentResponse(RetrieveDocumentSetResponseType response, String communityId) {
        DocumentResponse documentResponse = new DocumentResponse();
        documentResponse.setHomeCommunityId(communityId);
        response.getDocumentResponse().add(documentResponse);
    }

    private void addStatus(RetrieveDocumentSetResponseType response, String status) {
        RegistryResponseType value = getOrCreateRegistryResponse(response);
        value.setStatus(status);
        response.setRegistryResponse(value);
    }

    private void assertAlwaysNullAttributes(EventDescription eventDescription) {
        assertNull(eventDescription.getTimeStamp());
        assertNull(eventDescription.getNPI());
        assertNull(eventDescription.getInitiatingHCID());
        assertTrue(CollectionUtils.isEmpty(eventDescription.getPayloadSizes()));
        assertTrue(CollectionUtils.isEmpty(eventDescription.getPayloadTypes()));
    }
}
