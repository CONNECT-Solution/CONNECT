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
package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.document.DocumentConstants;
import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * @author bhumphrey
 *
 */
public class OutboundDocQueryAggregatorTest {

    OutboundDocQueryAggregator nhinAggregator = new OutboundDocQueryAggregator();

    @Test
    public void testPartialSuccess() {
        assertTrue(nhinAggregator.isEitherParitalSuccess(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS,
                DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS));
        assertTrue(nhinAggregator.isEitherParitalSuccess(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_SUCCESS,
                DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS));
        assertTrue(nhinAggregator.isEitherParitalSuccess(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE,
                DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS));
    }

    @Test
    public void testNotPartialSuccess() {
        assertFalse(nhinAggregator.isEitherParitalSuccess(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE,
                DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE));
        assertFalse(nhinAggregator.isEitherParitalSuccess(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_SUCCESS,
                DocumentConstants.XDS_QUERY_RESPONSE_STATUS_SUCCESS));
        assertFalse(nhinAggregator.isEitherParitalSuccess(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_SUCCESS,
                DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE));

    }

    @Test
    public void testAreTheyDifferent() {
        assertFalse(nhinAggregator.areTheStatusesDifferent(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE,
                DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE));
        assertFalse(nhinAggregator.areTheStatusesDifferent(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_SUCCESS,
                DocumentConstants.XDS_QUERY_RESPONSE_STATUS_SUCCESS));
        assertFalse(nhinAggregator.areTheStatusesDifferent(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS,
                DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS));
        assertTrue(nhinAggregator.areTheStatusesDifferent(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE,
                DocumentConstants.XDS_QUERY_RESPONSE_STATUS_SUCCESS));
        assertTrue(nhinAggregator.areTheStatusesDifferent(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_SUCCESS,
                DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS));
        assertTrue(nhinAggregator.areTheStatusesDifferent(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS,
                DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE));
    }

    @Test
    public void testCasesForPartialSuccess() {

        assertEquals(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS,
                nhinAggregator.determineAggregateStatus(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS,
                        null));

        assertEquals(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS,
                nhinAggregator.determineAggregateStatus(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS,
                        DocumentConstants.XDS_QUERY_RESPONSE_STATUS_SUCCESS));

        assertEquals(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS,
                nhinAggregator.determineAggregateStatus(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE,
                        DocumentConstants.XDS_QUERY_RESPONSE_STATUS_SUCCESS));

        assertEquals(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS,
                nhinAggregator.determineAggregateStatus(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_SUCCESS,
                        DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE));
    }

    @Test
    public void testCasesForSuccess() {

        assertEquals(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_SUCCESS,
                nhinAggregator.determineAggregateStatus(null, DocumentConstants.XDS_QUERY_RESPONSE_STATUS_SUCCESS));

        assertEquals(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_SUCCESS, nhinAggregator.determineAggregateStatus(
                DocumentConstants.XDS_QUERY_RESPONSE_STATUS_SUCCESS,
                DocumentConstants.XDS_QUERY_RESPONSE_STATUS_SUCCESS));

    }

    @Test
    public void testCasesForFailure() {

        assertEquals(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE,
                nhinAggregator.determineAggregateStatus(null, DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE));

        assertEquals(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE, nhinAggregator.determineAggregateStatus(
                DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE,
                DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE));

    }

    @Test
    public void testRegistryCollectionSingle() {
        AdhocQueryResponse aggregatedResponse = new AdhocQueryResponse();
        aggregatedResponse.setRegistryObjectList(new RegistryObjectListType());

        nhinAggregator.collectRegistryObjectResponses(aggregatedResponse,
                createRegistryObjectListType(new RegistryObjectType()));

        assertEquals(1, aggregatedResponse.getRegistryObjectList().getIdentifiable().size());
    }

    @Test
    public void testRegistryCollectionSingleMultiple() {
        AdhocQueryResponse aggregatedResponse = new AdhocQueryResponse();
        aggregatedResponse.setRegistryObjectList(new RegistryObjectListType());

        nhinAggregator.collectRegistryObjectResponses(aggregatedResponse,
                createRegistryObjectListType(new RegistryObjectType()));
        nhinAggregator.collectRegistryObjectResponses(aggregatedResponse,
                createRegistryObjectListType(new RegistryObjectType()));
        nhinAggregator.collectRegistryObjectResponses(aggregatedResponse,
                createRegistryObjectListType(new RegistryObjectType()));
        nhinAggregator.collectRegistryObjectResponses(aggregatedResponse,
                createRegistryObjectListType(new RegistryObjectType()));
        assertEquals(4, aggregatedResponse.getRegistryObjectList().getIdentifiable().size());

    }

    public RegistryObjectListType createRegistryObjectListType(RegistryObjectType... registryObjects) {
        RegistryObjectListType registryObjectListType = new RegistryObjectListType();
        for (RegistryObjectType registryObject : registryObjects) {
            registryObjectListType.getIdentifiable().add(createIdentifiable(registryObject));
        }
        return registryObjectListType;
    }

    /**
     * @param registryObjectType
     * @return
     */
    public JAXBElement<RegistryObjectType> createIdentifiable(RegistryObjectType registryObjectType) {
        return new JAXBElement<>(new QName(
                "urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "Identifiable"), RegistryObjectType.class,
                registryObjectType);
    }

    @Test
    public void testAggregateRegistryCollectionNullRegistry() {
        AdhocQueryResponse aggregatedResponse = new AdhocQueryResponse();
        aggregatedResponse.setRegistryObjectList(new RegistryObjectListType());

        AdhocQueryResponse individualResponse = new AdhocQueryResponse();

        nhinAggregator.aggregateRegistryObjectList(aggregatedResponse, individualResponse);
        assertNotNull(aggregatedResponse.getRegistryObjectList());
        assertEquals(0, aggregatedResponse.getRegistryObjectList().getIdentifiable().size());
    }

    @Test
    public void testAggregateRegistryCollection() {
        AdhocQueryResponse aggregatedResponse = new AdhocQueryResponse();

        AdhocQueryResponse individualResponse = new AdhocQueryResponse();

        aggregatedResponse.setRegistryObjectList(new RegistryObjectListType());

        individualResponse.setRegistryObjectList(createRegistryObjectListType(new RegistryObjectType()));

        nhinAggregator.aggregateRegistryObjectList(aggregatedResponse, individualResponse);

        assertEquals(1, aggregatedResponse.getRegistryObjectList().getIdentifiable().size());
    }

    @Test
    public void testAggregateNullResponse() {

        OutboundDocQueryOrchestratable aggregatedOrchestrable = new OutboundDocQueryOrchestratable();

        OutboundDocQueryOrchestratable individualOrchestrable = new OutboundDocQueryOrchestratable();
        individualOrchestrable.setResponse(null);

        nhinAggregator.aggregate(aggregatedOrchestrable, individualOrchestrable);

        assertEquals(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE, aggregatedOrchestrable.getResponse()
                .getStatus());

        assertNotNull(aggregatedOrchestrable.getResponse().getRegistryErrorList());

        assertEquals(1, aggregatedOrchestrable.getResponse().getRegistryErrorList().getRegistryError().size());

        assertEquals("Null response.", aggregatedOrchestrable.getResponse().getRegistryErrorList().getRegistryError()
                .get(0).getCodeContext());
    }

    @Test
    public void testIncreasingCount() {
        AdhocQueryResponse aggregatedResponse = new AdhocQueryResponse();
        aggregatedResponse.setTotalResultCount(BigInteger.ZERO);

        nhinAggregator.increaseCount(aggregatedResponse);
        nhinAggregator.increaseCount(aggregatedResponse);

        assertTrue(aggregatedResponse.getTotalResultCount().equals(new BigInteger("2")));
    }

    @Test
    public void testAggregateRegistryErrors() {
        AdhocQueryResponse aggregatedResponse = new AdhocQueryResponse();
        AdhocQueryResponse individualResponse = new AdhocQueryResponse();

        nhinAggregator.aggregateRegistryErrors(aggregatedResponse, individualResponse);

        assertNull(aggregatedResponse.getRegistryErrorList());

        individualResponse.setRegistryErrorList(new RegistryErrorList());
        individualResponse.getRegistryErrorList().getRegistryError().add(new RegistryError());

        nhinAggregator.aggregateRegistryErrors(aggregatedResponse, individualResponse);

        assertEquals(1, aggregatedResponse.getRegistryErrorList().getRegistryError().size());
    }

    @Test
    public void testAggregateSlotlistResponse() {
        AdhocQueryResponse aggregatedResponse = new AdhocQueryResponse();
        AdhocQueryResponse individualResponse = new AdhocQueryResponse();
        aggregatedResponse.setResponseSlotList(new SlotListType());

        nhinAggregator.aggregateSlotlistResponse(aggregatedResponse, individualResponse);
        assertEquals(0, aggregatedResponse.getResponseSlotList().getSlot().size());

        individualResponse.setResponseSlotList(new SlotListType());
        individualResponse.getResponseSlotList().getSlot().add(new SlotType1());

        nhinAggregator.aggregateSlotlistResponse(aggregatedResponse, individualResponse);
        assertEquals(1, aggregatedResponse.getResponseSlotList().getSlot().size());

    }
}
