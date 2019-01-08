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
package gov.hhs.fha.nhinc.docregistry.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentMetadata;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentQueryParams;
import gov.hhs.fha.nhinc.docrepository.adapter.service.DocumentService;
import gov.hhs.fha.nhinc.document.DocumentConstants;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

/**
 * This class is used to test the AdapterComponentDocRegistryOrchImplTest class
 *
 * @author akong
 */
public class AdapterComponentDocRegistryOrchImplTest {

    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final DocumentService mockDocumentService = context.mock(DocumentService.class);

    /**
     * Default constructor
     */
    public AdapterComponentDocRegistryOrchImplTest() {
    }

    private AdapterComponentDocRegistryOrchImpl createAdapterComponentDocRegistryOrchImpl() {
        return new AdapterComponentDocRegistryOrchImpl() {

            @Override
            protected DocumentService getDocumentService() {
                return createDocumentService();
            }

            @Override
            protected String retrieveHomeCommunityId() {
                return "1.1";
            }
        };
    }

    private DocumentService createDocumentService() {
        return new DocumentService() {
            @Override
            public List<DocumentMetadata> documentQuery(DocumentQueryParams params) {

                ArrayList<DocumentMetadata> docs = new ArrayList<>();
                if (params.getOnDemand() == null || params.getOnDemand() != null && params.getOnDemand()) {
                    DocumentMetadata onDemandDoc = new DocumentMetadata();
                    onDemandDoc.setDocumentUniqueId("12345.11111");

                    docs.add(onDemandDoc);
                }

                if (params.getOnDemand() == null || !params.getOnDemand()) {
                    DocumentMetadata stableDoc = new DocumentMetadata();
                    stableDoc.setDocumentUniqueId("12345.22222");

                    DocumentMetadata stableDoc2 = new DocumentMetadata();
                    stableDoc2.setDocumentUniqueId("12345.33333");

                    docs.add(stableDoc);
                    docs.add(stableDoc2);
                }
                return docs;
            }
        };
    }

    private AdhocQueryRequest createAdhocQueryRequest(String documentEntryTypeValue) {
        AdhocQueryRequest request = new AdhocQueryRequest();
        AdhocQueryType query = new AdhocQueryType();
        request.setAdhocQuery(query);
        request.getAdhocQuery().setId("urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d");
        SlotType1 slot = new SlotType1();
        slot.setName("$XDSDocumentEntryType");
        ValueListType valList = new ValueListType();
        valList.getValue().add(documentEntryTypeValue);
        slot.setValueList(valList);
        query.getSlot().add(slot);
        SlotType1 slot2 = new SlotType1();
        slot2.setName("$XDSDocumentEntryPatientId");
        ValueListType valList2 = new ValueListType();
        valList2.getValue().add("D123401^^^&1.1&ISO");
        slot2.setValueList(valList2);
        query.getSlot().add(slot2);
        return request;
    }

    private AdhocQueryRequest createAdhocQueryRequestForOnDemandDocuments() {
        return createAdhocQueryRequest("('urn:uuid:34268e47-fdf5-41a6-ba33-82133c465248')");
    }

    private AdhocQueryRequest createAdhocQueryRequestForStableDocuments() {
        return createAdhocQueryRequest("('urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1')");
    }

    private AdhocQueryRequest createAdhocQueryRequestForStableAndOnDemandDocuments() {
        return createAdhocQueryRequest("('urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1', "
                + "'urn:uuid:34268e47-fdf5-41a6-ba33-82133c465248')");
    }

    @Test
    public void testRegistryStoredQuery_OnDemandDocs() {
        AdapterComponentDocRegistryOrchImpl orchImpl = createAdapterComponentDocRegistryOrchImpl();

        AdhocQueryRequest request = createAdhocQueryRequestForOnDemandDocuments();

        AdhocQueryResponse response = orchImpl.registryStoredQuery(request);

        List<JAXBElement<? extends IdentifiableType>> objectList = response.getRegistryObjectList().getIdentifiable();
        assertEquals(1, objectList.size());

        ExtrinsicObjectType docExtrinsic = (ExtrinsicObjectType) objectList.get(0).getValue();
        List<ExternalIdentifierType> externalIdentifierList = docExtrinsic.getExternalIdentifier();
        assertEquals(1, externalIdentifierList.size());
        assertEquals("12345.11111", externalIdentifierList.get(0).getValue());
    }

    @Test
    public void testRegistryStoredQuery_StableDocs() {
        AdapterComponentDocRegistryOrchImpl orchImpl = createAdapterComponentDocRegistryOrchImpl();

        AdhocQueryRequest request = createAdhocQueryRequestForStableDocuments();

        AdhocQueryResponse response = orchImpl.registryStoredQuery(request);

        List<JAXBElement<? extends IdentifiableType>> objectList = response.getRegistryObjectList().getIdentifiable();
        assertEquals(2, objectList.size());

        ExtrinsicObjectType docExtrinsic = (ExtrinsicObjectType) objectList.get(0).getValue();
        List<ExternalIdentifierType> externalIdentifierList = docExtrinsic.getExternalIdentifier();
        assertEquals(1, externalIdentifierList.size());
        assertEquals("12345.22222", externalIdentifierList.get(0).getValue());

        docExtrinsic = (ExtrinsicObjectType) objectList.get(1).getValue();
        externalIdentifierList = docExtrinsic.getExternalIdentifier();
        assertEquals(1, externalIdentifierList.size());
        assertEquals("12345.33333", externalIdentifierList.get(0).getValue());
    }

    @Test
    public void testRegistryStoredQuery_StableAndOnDemandDocs() {
        AdapterComponentDocRegistryOrchImpl orchImpl = createAdapterComponentDocRegistryOrchImpl();

        AdhocQueryRequest request = createAdhocQueryRequestForStableAndOnDemandDocuments();
        AdhocQueryResponse response = orchImpl.registryStoredQuery(request);

        List<JAXBElement<? extends IdentifiableType>> objectList = response.getRegistryObjectList().getIdentifiable();
        assertEquals(3, objectList.size());
    }

    @Test
    public void testRegistryStoredQuery_unknownRegistryQueryId() {
        AdapterComponentDocRegistryOrchImpl orchImpl = createAdapterComponentDocRegistryOrchImpl();

        AdhocQueryRequest request = createAdhocQueryRequestForunknownStoredQuery();
        AdhocQueryResponse response = orchImpl.registryStoredQuery(request);
        assertSame(response.getStatus(), DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE);
    }

    private AdhocQueryRequest createAdhocQueryRequestForunknownStoredQuery() {
        return createAdhocQueryRequestUnknownStoredQuery("('urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1')");
    }

    private AdhocQueryRequest createAdhocQueryRequestUnknownStoredQuery(String documentEntryTypeValue) {
        AdhocQueryRequest request = new AdhocQueryRequest();
        AdhocQueryType query = new AdhocQueryType();
        request.setAdhocQuery(query);
        request.getAdhocQuery().setId("9a74-a90016b0af0d");
        SlotType1 slot = new SlotType1();
        slot.setName("$XDSDocumentEntryType");
        ValueListType valList = new ValueListType();
        valList.getValue().add(documentEntryTypeValue);
        slot.setValueList(valList);
        SlotType1 slot2 = new SlotType1();
        slot2.setName("$XDSDocumentEntryPatientId");
        query.getSlot().add(slot);
        // query.getSlot().add(slot2);
        return request;
    }

    @Test
    public void testRegistryStoredQuery_RegistryQueryIdMissingParam() {
        AdapterComponentDocRegistryOrchImpl orchImpl = createAdapterComponentDocRegistryOrchImpl();

        AdhocQueryRequest request = createAdhocQueryRequestforMissingParam();
        AdhocQueryResponse response = orchImpl.registryStoredQuery(request);
        assertSame(response.getRegistryErrorList().getRegistryError().get(0).getErrorCode(),
                "XDSStoredQueryMissingParam");
    }

    /**
     * @return
     */
    private AdhocQueryRequest createAdhocQueryRequestforMissingParam() {

        return createAdhocQueryRequestMissingParam("('urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1')");
    }

    /**
     * @param string
     * @return
     */
    private AdhocQueryRequest createAdhocQueryRequestMissingParam(String documentEntryTypeValue) {
        AdhocQueryRequest request = new AdhocQueryRequest();
        AdhocQueryType query = new AdhocQueryType();
        request.setAdhocQuery(query);
        request.getAdhocQuery().setId("urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d");
        SlotType1 slot = new SlotType1();
        slot.setName("$XDSDocumentEntryType");
        ValueListType valList = new ValueListType();
        valList.getValue().add(documentEntryTypeValue);
        slot.setValueList(valList);
        query.getSlot().add(slot);
        return request;
    }

    @Test
    public void testRegistryStoredQuery_XDSStoredQueryParamNumberError() {
        AdapterComponentDocRegistryOrchImpl orchImpl = createAdapterComponentDocRegistryOrchImpl();

        AdhocQueryRequest request = createAdhocQueryRequestForXDSStoredQueryParamNumberError();
        AdhocQueryResponse response = orchImpl.registryStoredQuery(request);
        assertSame(response.getRegistryErrorList().getRegistryError().get(0).getErrorCode(),
                "XDSStoredQueryParamNumber");
    }

    private AdhocQueryRequest createAdhocQueryRequestForXDSStoredQueryParamNumberError() {
        return createAdhocQueryRequestXDSStoredQueryParamNumberError(
                "('urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1')");
    }

    private AdhocQueryRequest createAdhocQueryRequestXDSStoredQueryParamNumberError(String documentEntryTypeValue) {
        AdhocQueryRequest request = new AdhocQueryRequest();
        AdhocQueryType query = new AdhocQueryType();
        request.setAdhocQuery(query);
        request.getAdhocQuery().setId("urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d");
        SlotType1 slot = new SlotType1();
        slot.setName("$XDSDocumentEntryType");
        ValueListType valList = new ValueListType();
        valList.getValue().add(documentEntryTypeValue);
        slot.setValueList(valList);
        query.getSlot().add(slot);
        SlotType1 slot2 = new SlotType1();
        slot2.setName("$XDSDocumentEntryPatientId");
        ValueListType valList2 = new ValueListType();
        valList.getValue().add("D123401^^^&1.1&ISO");
        slot2.setValueList(valList2);
        query.getSlot().add(slot2);
        SlotType1 slot3 = new SlotType1();
        slot3.setName("$XDSDocumentEntryPatientId");
        ValueListType valList3 = new ValueListType();
        valList.getValue().add("D123402^^^&2.2&ISO");
        slot3.setValueList(valList3);
        query.getSlot().add(slot3);
        return request;
    }

}
