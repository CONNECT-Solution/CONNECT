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
package gov.hhs.fha.nhinc.docrepository.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.docrepository.adapter.model.Document;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentMetadata;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentQueryParams;
import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCode;
import gov.hhs.fha.nhinc.docrepository.adapter.service.DocumentService;
import gov.hhs.fha.nhinc.largefile.LargeFileUtils;
import gov.hhs.fha.nhinc.util.format.UTCDateUtil;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.RegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.activation.DataHandler;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AssociationType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.InternationalStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.LocalizedStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * @author achidamb / jsmith
 *
 */
public class AdapterComponentDocRepositoryOrchImplTest {

    private final DocumentService docService = mock(DocumentService.class);
    private final LargeFileUtils largeFileUtils = mock(LargeFileUtils.class);
    private final UTCDateUtil utcDateUtil = mock(UTCDateUtil.class);

    @Test
    public void testSetDocumentResponse_Failure() {
        AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl();
        DocumentMetadata doc = new DocumentMetadata();
        assertFalse(docRepo.setDocumentResponse(doc, null));
    }

    @Test
    public void testSetDocumentResponse_Success_File() throws IOException {
        Boolean result;
        String url = "file:///FILE_NAME";
        AdapterComponentDocRepositoryOrchImpl docRepo = getDocRepoWithMockLargeFileUtils();

        DocumentMetadata doc = mock(DocumentMetadata.class);
        DocumentResponse oDocResponse = mock(DocumentResponse.class);
        DataHandler dataHandler = mock(DataHandler.class);

        when(doc.getRawData()).thenReturn(url.getBytes());
        when(largeFileUtils.convertToDataHandler(any(File.class))).thenReturn(dataHandler);

        result = docRepo.setDocumentResponse(doc, oDocResponse);

        verify(oDocResponse).setDocument(dataHandler);

        assertTrue(result);
    }

    @Test
    public void testSetDocumentResponse_Success_String() {
        String url = "file:///FILE_NAME^";

        AdapterComponentDocRepositoryOrchImpl docRepo = getDocRepoWithMockLargeFileUtils();

        DocumentMetadata doc = new DocumentMetadata();
        Document document = new Document(doc);
        document.setRawData(url.getBytes());

        DocumentResponse oDocResponse = mock(DocumentResponse.class);
        DataHandler dataHandler = mock(DataHandler.class);

        when(largeFileUtils.convertToDataHandler(any(byte[].class))).thenReturn(dataHandler);

        Boolean result = docRepo.setDocumentResponse(doc, oDocResponse);

        verify(oDocResponse).setDocument(dataHandler);

        assertTrue(result);
    }

    @Test
    public void testDocumentRepositoryRetrieveDocumentSet_Success() {
        AdapterComponentDocRepositoryOrchImpl docRepo = getDocRepoWithEmptyRetrieveDocuments();
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        response = docRepo.documentRepositoryRetrieveDocumentSet(createDocumentRequest_Success());
        assertSame("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success",
            response.getRegistryResponse().getStatus());

    }

    @Test
    public void testDocumentRepositoryRetrieveDocumentSet_Failure() {
        AdapterComponentDocRepositoryOrchImpl docRepo = getDocRepoWithEmptyRetrieveDocuments();
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        response = docRepo.documentRepositoryRetrieveDocumentSet(createDocumentRequest_Failure());
        assertSame("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure",
            response.getRegistryResponse().getStatus());

    }

    @Test
    public void testDocumentRepositoryRetrieveDocumentSet_PartialSuccess() {
        AdapterComponentDocRepositoryOrchImpl docrepo = new AdapterComponentDocRepositoryOrchImpl() {
            @Override
            protected void retrieveDocuments(boolean repositoryIdMatched, List<String> documentUniqueIds,
                RetrieveDocumentSetResponseType response, String homeCommunityId,
                RegistryErrorList regerrList) {
                DocumentMetadata doc1 = new DocumentMetadata();
                doc1.setDocumentUniqueId("2.531.777");
                DocumentMetadata doc2 = new DocumentMetadata();
                doc2.setDocumentUniqueId("");
                ArrayList<DocumentMetadata> docs = new ArrayList<>();
                docs.add(doc1);
                docs.add(doc2);
                loadDocumentResponses(response, docs, homeCommunityId, documentUniqueIds, regerrList);
            }

            @Override
            protected boolean setDocumentResponse(DocumentMetadata doc, DocumentResponse oDocResponse) {
                return true;
            }

        };
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        response = docrepo.documentRepositoryRetrieveDocumentSet(createDocumentRequest_PartialSuccess());
        assertSame("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:PartialSuccess",
            response.getRegistryResponse().getStatus());

    }

    @Test
    public void testRetrieveDocuments_RepositoryIdMatched() {
        AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl() {
            @Override
            protected void loadDocumentResponses(RetrieveDocumentSetResponseType response,
                List<DocumentMetadata> docs, String homeCommunityId, List<String> documentUniqueId,
                RegistryErrorList regerrList) {
                assertTrue(true);
            }

            @Override
            public DocumentService getDocumentService() {
                return docService;
            }

        };

        boolean repositoryIdMatched = true;
        docRepo.retrieveDocuments(repositoryIdMatched, null, null, null, null);
        verify(docService).documentQuery(Mockito.isA(DocumentQueryParams.class));
    }

    @Test
    public void testDocumentRepositoryProvideAndRegisterDocumentSet_Success() {
        ProvideAndRegisterDocumentSetRequestType body = mock(ProvideAndRegisterDocumentSetRequestType.class);
        final HashMap<String, DataHandler> docMap = new HashMap<String, DataHandler>();
        SubmitObjectsRequest submitObjectsRequest = mock(SubmitObjectsRequest.class);
        RegistryObjectListType regObjectList = mock(RegistryObjectListType.class);
        List<JAXBElement<? extends IdentifiableType>> identifiableObjectList = new ArrayList<JAXBElement<? extends IdentifiableType>>();

        AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl();

        when(body.getSubmitObjectsRequest()).thenReturn(submitObjectsRequest);
        when(submitObjectsRequest.getRegistryObjectList()).thenReturn(regObjectList);
        when(regObjectList.getIdentifiable()).thenReturn(identifiableObjectList);

        RegistryResponseType registryResponse = docRepo.documentRepositoryProvideAndRegisterDocumentSet(body);

        assertEquals(DocRepoConstants.XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS, registryResponse.getStatus());
    }

    @Test
    public void testDocumentRepositoryProvideAndRegisterDocumentSet_Failure() {
        ProvideAndRegisterDocumentSetRequestType body = null;

        AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl();
        RegistryResponseType registryResponse = docRepo.documentRepositoryProvideAndRegisterDocumentSet(body);

        RegistryErrorList errorList = registryResponse.getRegistryErrorList();
        RegistryError error = errorList.getRegistryError().get(0);

        assertEquals(DocRepoConstants.XDS_RETRIEVE_RESPONSE_STATUS_FAILURE, registryResponse.getStatus());
        assertEquals(DocRepoConstants.XDS_MISSING_REQUEST_MESSAGE_DATA
            + " ProvideAndRegisterDocumentSetRequestType element is null.", error.getValue());
    }

    @Test
    public void testCheckForReplacementAssociation_Success() {
        RegistryErrorList errorList = new RegistryErrorList();
        List<JAXBElement<? extends IdentifiableType>> identifiableObjectList = mock(ArrayList.class);
        final AssociationType1 associationType1 = new AssociationType1();
        associationType1.setAssociationType("urn:oasis:names:tc:ebxml-regrep:AssociationType:RPLC");
        AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl() {
            @Override
            protected Object getIdentifiableObjectValue(
                List<JAXBElement<? extends IdentifiableType>> identifiableObjectList, int i) {
                return associationType1;
            }
        };

        when(identifiableObjectList.size()).thenReturn(1);
        boolean result = docRepo.checkForReplacementAssociation(identifiableObjectList, errorList);
        assertTrue(result);
    }

    @Test
    public void testCheckForReplacementAssociation_Failure() {
        RegistryErrorList errorList = new RegistryErrorList();
        List<JAXBElement<? extends IdentifiableType>> identifiableObjectList = mock(ArrayList.class);
        final AssociationType1 associationType1 = new AssociationType1();

        AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl() {
            @Override
            protected Object getIdentifiableObjectValue(
                List<JAXBElement<? extends IdentifiableType>> identifiableObjectList, int i) {
                return associationType1;
            }
        };

        when(identifiableObjectList.size()).thenReturn(1);
        boolean result = docRepo.checkForReplacementAssociation(identifiableObjectList, errorList);
        assertFalse(result);
        assertEquals(DocRepoConstants.XDS_MISSING_DOCUMENT_METADATA + " associationType element is null.",
            errorList.getRegistryError().get(0).getValue());
    }

    @Test
    public void testSaveExtrinsicObject_SuccessALL() throws IOException {
        final ExtrinsicObjectType extrinsicObject = mock(ExtrinsicObjectType.class);

        List<ExternalIdentifierType> externalIdentifierList = new ArrayList<>();
        ExternalIdentifierType externalIdentifier = mock(ExternalIdentifierType.class);
        externalIdentifierList.add(externalIdentifier);

        InternationalStringType name = mock(InternationalStringType.class);
        List<LocalizedStringType> nameValueList = mock(ArrayList.class);
        LocalizedStringType nameValue = mock(LocalizedStringType.class);

        List<SlotType1> documentSlots = new ArrayList<>();
        SlotType1 slot = mock(SlotType1.class);
        documentSlots.add(slot);

        ValueListType valueListType = mock(ValueListType.class);
        List<String> valueList = mock(ArrayList.class);
        RegistryErrorList errorList = mock(RegistryErrorList.class);

        final String DOC_UNIQUE_ID = "Doc_ID_1";
        final String DOC_UNIQUE_PERSISTED_ID = "CONNECT0";

        final String PATIENT_ID = "'Patient_ID_1'";
        final String PATIENT_ID_NO_QUOTES = "Patient_ID_1";
        final String DOCUMENT_TITLE = "Document_Title";
        final String DOCUMENT_DESCRIPTION = "Document Description";
        final String MIME_TYPE = "Mime Type";
        final String XDS_INTENDED_RECIPIENT = "Organization|Person";
        final String XDS_INTENDED_ORG = "Organization";
        final String XDS_INTENDED_PERSON = "Person";
        final String XDS_LANGUAGE_CODE = "Language Code";
        final String XDS_LEGAL_AUTHENTICATOR = "Legal Authenticator";

        final String CREATION_TIME_STRING = "100000";
        final long CREATION_TIME = 100000L;
        Date createTimeDate = new Date();
        createTimeDate.setTime(CREATION_TIME);
        final String START_TIME_STRING = "120000";
        final String STOP_TIME_STRING = "240000";
        final long START_TIME = 120000L;
        Date startTimeDate = new Date();
        startTimeDate.setTime(START_TIME);
        final long STOP_TIME = 240000L;
        Date stopTimeDate = new Date();
        stopTimeDate.setTime(STOP_TIME);

        final String XDS_PATIENT_ID = "'Patient_ID_2'";
        final String XDS_PATIENT_ID_NO_QUOTES = "Patient_ID_2";
        HashMap<String, DataHandler> docMap = mock(HashMap.class);
        DataHandler dataHandler = mock(DataHandler.class);
        final String RAW_DATA = "Raw Data";
        final String STATUS = "Status";

        AdapterComponentDocRepositoryOrchImpl docRepo = getDocRepoWithMutipleOverrides();

        when(extrinsicObject.getExternalIdentifier()).thenReturn(externalIdentifierList);
        when(externalIdentifier.getName()).thenReturn(name);
        when(name.getLocalizedString()).thenReturn(nameValueList);
        when(nameValueList.get(0)).thenReturn(nameValue);
        when(nameValue.getValue()).thenReturn(DocRepoConstants.XDS_DOCUMENT_UNIQUE_ID, DocRepoConstants.XDS_PATIENT_ID,
            DOCUMENT_TITLE, DOCUMENT_DESCRIPTION);
        when(externalIdentifier.getValue()).thenReturn(DOC_UNIQUE_ID, PATIENT_ID);

        when(extrinsicObject.getName()).thenReturn(name);

        when(extrinsicObject.getDescription()).thenReturn(name);

        when(extrinsicObject.getMimeType()).thenReturn(MIME_TYPE);

        when(extrinsicObject.getSlot()).thenReturn(documentSlots);
        when(slot.getName()).thenReturn(DocRepoConstants.XDS_INTENDED_RECIPIENT_SLOT,
            DocRepoConstants.XDS_LANGUAGE_CODE_SLOT, DocRepoConstants.XDS_LEGAL_AUTHENTICATOR_SLOT,
            DocRepoConstants.XDS_CREATION_TIME_SLOT, DocRepoConstants.XDS_START_TIME_SLOT,
            DocRepoConstants.XDS_STOP_TIME_SLOT, DocRepoConstants.XDS_SOURCE_PATIENT_ID_SLOT);

        when(slot.getValueList()).thenReturn(valueListType);
        when(valueListType.getValue()).thenReturn(valueList);
        when(valueList.size()).thenReturn(1);
        when(valueList.get(0)).thenReturn(XDS_INTENDED_RECIPIENT, XDS_LANGUAGE_CODE, XDS_LEGAL_AUTHENTICATOR,
            CREATION_TIME_STRING, START_TIME_STRING, STOP_TIME_STRING, XDS_PATIENT_ID);

        when(utcDateUtil.parseUTCDateOptionalTimeZone(CREATION_TIME_STRING)).thenReturn(createTimeDate);
        when(utcDateUtil.parseUTCDateOptionalTimeZone(START_TIME_STRING)).thenReturn(startTimeDate);
        when(utcDateUtil.parseUTCDateOptionalTimeZone(STOP_TIME_STRING)).thenReturn(stopTimeDate);

        when(extrinsicObject.getId()).thenReturn(DOC_UNIQUE_ID);
        when(docMap.get(DOC_UNIQUE_ID)).thenReturn(dataHandler);
        when(largeFileUtils.convertToBytes(dataHandler)).thenReturn(RAW_DATA.getBytes());

        when(extrinsicObject.getStatus()).thenReturn(STATUS);
        when(docService.getNextID()).thenReturn(DOC_UNIQUE_PERSISTED_ID);

        DocumentMetadata doc = docRepo.saveExtrinsicObject(extrinsicObject, errorList, docMap, true);

        assertEquals(PATIENT_ID_NO_QUOTES, doc.getPatientId());
        assertEquals(DOCUMENT_TITLE, doc.getDocumentTitle());
        assertEquals(DOCUMENT_DESCRIPTION, doc.getComments());
        assertEquals(MIME_TYPE, doc.getMimeType());
        assertEquals(XDS_INTENDED_ORG, doc.getIntendedRecipientOrganization());
        assertEquals(XDS_INTENDED_PERSON, doc.getIntendedRecipientPerson());
        assertEquals(XDS_LANGUAGE_CODE, doc.getLanguageCode());
        assertEquals(XDS_LEGAL_AUTHENTICATOR, doc.getLegalAuthenticator());
        assertEquals(CREATION_TIME, doc.getCreationTime().getTime());
        assertEquals(START_TIME, doc.getServiceStartTime().getTime());
        assertEquals(STOP_TIME, doc.getServiceStopTime().getTime());
        assertEquals(XDS_PATIENT_ID_NO_QUOTES, doc.getSourcePatientId());
        assertTrue(Arrays.equals(doc.getRawData(), RAW_DATA.getBytes()));
        assertEquals(STATUS, doc.getAvailablityStatus());
        assertEquals(DocRepoConstants.XDS_STATUS, doc.getStatus());
        assertEquals(DOC_UNIQUE_PERSISTED_ID, doc.getDocumentUniqueId());
        assertEquals((Integer) RAW_DATA.getBytes().length, doc.getSize());
    }

    protected RetrieveDocumentSetRequestType createDocumentRequest_Success() {

        RetrieveDocumentSetRequestType requestBody = new RetrieveDocumentSetRequestType();
        DocumentRequest req1 = new DocumentRequest();
        req1.setDocumentUniqueId("1.546.678");
        req1.setHomeCommunityId("1.1");
        req1.setRepositoryUniqueId("1");
        requestBody.getDocumentRequest().add(req1);
        DocumentRequest req2 = new DocumentRequest();
        req2.setDocumentUniqueId("2.531.777");
        req2.setHomeCommunityId("1.1");
        req2.setRepositoryUniqueId("1");
        requestBody.getDocumentRequest().add(req2);
        return requestBody;

    }

    @Test
    public void testSaveDocument_Success() {
        final String PATIENT_ID = "ID_1";
        final String DOC_UNIQUE_ID = "DOC_ID_1";
        final String CLASS_CODE = "CLASS_CODE";
        final String STATUS = "STATUS";
        final long DOC_ID = 101;
        DocumentMetadata doc = mock(DocumentMetadata.class);
        RegistryErrorList errorList = new RegistryErrorList();

        List<DocumentMetadata> docList = new ArrayList<>();

        AdapterComponentDocRepositoryOrchImpl docRepo = getDocRepoWithMockDocService();

        when(doc.getPatientId()).thenReturn(PATIENT_ID);
        when(doc.getDocumentUniqueId()).thenReturn(DOC_UNIQUE_ID);
        when(doc.getClassCode()).thenReturn(CLASS_CODE);
        when(doc.getStatus()).thenReturn(STATUS);
        when(docService.documentQuery(any(DocumentQueryParams.class))).thenReturn(docList);
        when(doc.getDocumentid()).thenReturn(DOC_ID);

        docRepo.saveDocument(doc, true, DOC_UNIQUE_ID, errorList);

        verify(docService).saveDocument(doc);
        assertEquals(0, errorList.getRegistryError().size());

    }

    @Test
    public void testSaveDocument_Failure() {
        final String PATIENT_ID = "ID_1";
        final String DOC_UNIQUE_ID = "DOC_ID_1";
        final String CLASS_CODE = "CLASS_CODE";
        final String STATUS = "STATUS";
        final long DOC_ID = 0;
        DocumentMetadata doc = mock(DocumentMetadata.class);
        RegistryErrorList errorList = new RegistryErrorList();
        List<DocumentMetadata> docList = new ArrayList<>();

        AdapterComponentDocRepositoryOrchImpl docRepo = getDocRepoWithMockDocService();

        when(doc.getPatientId()).thenReturn(PATIENT_ID);
        when(doc.getDocumentUniqueId()).thenReturn(DOC_UNIQUE_ID);
        when(doc.getClassCode()).thenReturn(CLASS_CODE);
        when(doc.getStatus()).thenReturn(STATUS);
        when(docService.documentQuery(any(DocumentQueryParams.class))).thenReturn(docList);
        when(doc.getDocumentid()).thenReturn(DOC_ID);

        docRepo.saveDocument(doc, true, DOC_UNIQUE_ID, errorList);

        RegistryError error = errorList.getRegistryError().get(0);

        verify(docService).saveDocument(doc);
        assertEquals(DocRepoConstants.XDS_REPOSITORY_ERROR + " DocumentUniqueId: " + DOC_UNIQUE_ID, error.getValue());

    }

    @Test
    public void testSetDocumentPidObjects() {
        final AdapterComponentDocRepositoryHelper docRepoHelper = mock(AdapterComponentDocRepositoryHelper.class);
        AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl() {
            @Override
            protected AdapterComponentDocRepositoryHelper getHelper() {
                return docRepoHelper;
            }
        };
        final List<SlotType1> documentSlots = mock(ArrayList.class);
        DocumentMetadata doc = new DocumentMetadata();

        final String PID3 = "PID3";
        final String PID5 = "PID5";
        final String PID7 = "PID7";
        final String PID8 = "PID8";
        final String PID11 = "PID11";

        when(docRepoHelper.extractPatientInfo(documentSlots, DocRepoConstants.XDS_SOURCE_PATIENT_INFO_PID3))
        .thenReturn(PID3);
        when(docRepoHelper.extractPatientInfo(documentSlots, DocRepoConstants.XDS_SOURCE_PATIENT_INFO_PID5))
        .thenReturn(PID5);
        when(docRepoHelper.extractPatientInfo(documentSlots, DocRepoConstants.XDS_SOURCE_PATIENT_INFO_PID7))
        .thenReturn(PID7);
        when(docRepoHelper.extractPatientInfo(documentSlots, DocRepoConstants.XDS_SOURCE_PATIENT_INFO_PID8))
        .thenReturn(PID8);
        when(docRepoHelper.extractPatientInfo(documentSlots, DocRepoConstants.XDS_SOURCE_PATIENT_INFO_PID11))
        .thenReturn(PID11);

        docRepo.setDocumentPidObjects(doc, documentSlots);

        assertEquals(PID3, doc.getPid3());
        assertEquals(PID5, doc.getPid5());
        assertEquals(PID7, doc.getPid7());
        assertEquals(PID8, doc.getPid8());
        assertEquals(PID11, doc.getPid11());
    }

    @Test
    public void testSetDocumentObjectsFromClassifications() {
        final AdapterComponentDocRepositoryHelper docRepoHelper = mock(AdapterComponentDocRepositoryHelper.class);
        AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl() {
            @Override
            protected AdapterComponentDocRepositoryHelper getHelper() {
                return docRepoHelper;
            }
        };
        DocumentMetadata doc = new DocumentMetadata();
        List<ClassificationType> classifications = mock(ArrayList.class);

        final String AUTHOR_PERSON = "author";
        final String AUTHOR_INSTITUTION = "author institution";
        final String AUTHOR_ROLE = "author role";
        final String AUTHOR_SPECIALTY = "author specialty";
        final String CLASS_CODE_SCHEME = "code scheme";
        final String CLASS_CODE = "class code";
        final String CONFIDENTIALITY_CODE_SCHEME = "conf code scheme";
        final String FORMAT_CODE_SCHEME = "format code scheme";
        final String HC_CODE_SCHEME = "facility code scheme";
        final String PRACTICE_SETTING_SCHEME = "practice setting scheme";
        final String TYPE_CODE_SCHEME = "type code scheme";
        final String CLASS_CODE_NAME = "class code name";
        final String CONFIDENTIALITY_CODE = "confidentiality code";
        final String CONFIDENTIALITY_CODE_NAME = "confidentiality code name";
        final String FORMAT_CODE = "format code";
        final String FORMAT_CODE_NAME = "formant code name";
        final String HC_CODE = "hc code";
        final String HC_CODE_NAME = "hc code name";
        final String PRACTICE_SETTING = "practice setting";
        final String PRACTICE_SETTING_NAME = "practice setting name";
        final String TYPE_CODE = "type code";
        final String TYPE_CODE_NAME = "type code name";

        when(docRepoHelper.extractClassificationMetadata(any(List.class), anyString(), anyString(), anyInt()))
        .thenReturn(AUTHOR_PERSON, AUTHOR_INSTITUTION, AUTHOR_ROLE, AUTHOR_SPECIALTY, CLASS_CODE_SCHEME,
            CONFIDENTIALITY_CODE_SCHEME, FORMAT_CODE_SCHEME, HC_CODE_SCHEME, PRACTICE_SETTING_SCHEME,
            TYPE_CODE_SCHEME);
        when(docRepoHelper.extractClassificationMetadata(any(List.class), anyString(), anyString())).thenReturn(
            CLASS_CODE, CLASS_CODE_NAME, CONFIDENTIALITY_CODE, CONFIDENTIALITY_CODE_NAME, FORMAT_CODE,
            FORMAT_CODE_NAME, HC_CODE, HC_CODE_NAME, PRACTICE_SETTING, PRACTICE_SETTING_NAME, TYPE_CODE,
            TYPE_CODE_NAME);

        docRepo.setDocumentObjectsFromClassifications(doc, classifications);

        assertEquals(AUTHOR_INSTITUTION, doc.getAuthorInstitution());
        assertEquals(AUTHOR_PERSON, doc.getAuthorPerson());
        assertEquals(AUTHOR_ROLE, doc.getAuthorRole());
        assertEquals(AUTHOR_SPECIALTY, doc.getAuthorSpecialty());
        assertEquals(CLASS_CODE, doc.getClassCode());
        assertEquals(CLASS_CODE_NAME, doc.getClassCodeDisplayName());
        assertEquals(CLASS_CODE_SCHEME, doc.getClassCodeScheme());
        assertEquals(CONFIDENTIALITY_CODE, doc.getConfidentialityCode());
        assertEquals(CONFIDENTIALITY_CODE_NAME, doc.getConfidentialityCodeDisplayName());
        assertEquals(CONFIDENTIALITY_CODE_SCHEME, doc.getConfidentialityCodeScheme());
        assertEquals(HC_CODE, doc.getFacilityCode());
        assertEquals(HC_CODE_NAME, doc.getFacilityCodeDisplayName());
        assertEquals(HC_CODE_SCHEME, doc.getFacilityCodeScheme());
        assertEquals(FORMAT_CODE, doc.getFormatCode());
        assertEquals(FORMAT_CODE_NAME, doc.getFormatCodeDisplayName());
        assertEquals(FORMAT_CODE_SCHEME, doc.getFormatCodeScheme());
        assertEquals(PRACTICE_SETTING, doc.getPracticeSetting());
        assertEquals(PRACTICE_SETTING_NAME, doc.getPracticeSettingDisplayName());
        assertEquals(PRACTICE_SETTING_SCHEME, doc.getPracticeSettingScheme());
        assertEquals(TYPE_CODE, doc.getTypeCode());
        assertEquals(TYPE_CODE_NAME, doc.getTypeCodeDisplayName());
        assertEquals(TYPE_CODE_SCHEME, doc.getTypeCodeScheme());
    }

    @Test
    public void testExtractEventCodes() {
        final AdapterComponentDocRepositoryHelper docRepoHelper = mock(AdapterComponentDocRepositoryHelper.class);
        AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl() {
            @Override
            protected AdapterComponentDocRepositoryHelper getHelper() {
                return docRepoHelper;
            }
        };
        List<ClassificationType> classifications = new ArrayList<>();
        ClassificationType classificationType = mock(ClassificationType.class);
        classifications.add(classificationType);
        SlotType1 slot = mock(SlotType1.class);
        List<SlotType1> slotList = new ArrayList<SlotType1>();
        slotList.add(slot);
        final String EVENT_SCHEME = "event scheme";
        final String NODE_REP = "node rep";
        final String EVENT_CODE_DISPLAY_NAME = "event display name";
        InternationalStringType internationalString = mock(InternationalStringType.class);
        LocalizedStringType localizedString = new LocalizedStringType();
        localizedString.setValue(EVENT_CODE_DISPLAY_NAME);
        List<LocalizedStringType> eventCodeDisplayNameList = new ArrayList<>();
        eventCodeDisplayNameList.add(localizedString);
        DocumentMetadata doc = new DocumentMetadata();

        when(classificationType.getClassificationScheme())
        .thenReturn(DocRepoConstants.XDS_EVENT_CODE_LIST_CLASSIFICATION);
        when(classificationType.getSlot()).thenReturn(slotList);
        when(classificationType.getNodeRepresentation()).thenReturn(NODE_REP);
        when(docRepoHelper.extractMetadataFromSlots(slotList, DocRepoConstants.XDS_CODING_SCHEME_SLOT, 0))
        .thenReturn(EVENT_SCHEME);
        when(classificationType.getName()).thenReturn(internationalString);
        when(internationalString.getLocalizedString()).thenReturn(eventCodeDisplayNameList);

        docRepo.extractEventCodes(doc, classifications);

        EventCode event = doc.getEventCodes().iterator().next();

        assertEquals(NODE_REP, event.getEventCode());
        assertEquals(EVENT_CODE_DISPLAY_NAME, event.getEventCodeDisplayName());
        assertEquals(EVENT_SCHEME, event.getEventCodeScheme());
    }

    @Test
    public void testRegisterDocument_BlankPayload() {
        AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl();
        RegisterDocumentSetRequestType body = makeBlankRegisterPayload(
            new ArrayList<JAXBElement<? extends IdentifiableType>>());

        RegistryResponseType result = docRepo.registerDocumentSet(body);
        assertNotNull(result);
        assertEquals(DocRepoConstants.XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS, result.getStatus());
    }

    @Test
    public void testRegisterDocument_MissingExternalId() {
        AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl();
        ArrayList<JAXBElement<? extends IdentifiableType>> documentList = new ArrayList<JAXBElement<? extends IdentifiableType>>();
        ExtrinsicObjectType extrinsicObject = new ExtrinsicObjectType();
        JAXBElement<ExtrinsicObjectType> xmlElement = new JAXBElement<ExtrinsicObjectType>(
            new QName("ExtrinsicObjectType"), ExtrinsicObjectType.class, extrinsicObject);
        documentList.add(xmlElement);
        RegisterDocumentSetRequestType body = makeBlankRegisterPayload(documentList);

        RegistryResponseType result = docRepo.registerDocumentSet(body);
        assertNotNull(result);
        assertEquals(DocRepoConstants.XDS_RETRIEVE_RESPONSE_STATUS_FAILURE, result.getStatus());

        List<RegistryError> registryErrorList = result.getRegistryErrorList().getRegistryError();

        assertEquals(1, registryErrorList.size());
        assertEquals(DocRepoConstants.XDS_ERROR_CODE_MISSING_DOCUMENT_METADATA,
            registryErrorList.get(0).getErrorCode());
        assertEquals(
            DocRepoConstants.XDS_MISSING_DOCUMENT_METADATA
            + " extrinsicObject.getExternalIdentifier() element is null or empty.",
            registryErrorList.get(0).getValue());
    }

    @Test
    public void testRegisterDocument_MissingDocIdAndPatientId() {
        AdapterComponentDocRepositoryOrchImpl docRepo = getDocRepoWithMockDocService();
        ArrayList<JAXBElement<? extends IdentifiableType>> documentList = new ArrayList<JAXBElement<? extends IdentifiableType>>();
        ExtrinsicObjectType extrinsicObject = new ExtrinsicObjectType();
        JAXBElement<ExtrinsicObjectType> xmlElement = new JAXBElement<ExtrinsicObjectType>(
            new QName("ExtrinsicObjectType"), ExtrinsicObjectType.class, extrinsicObject);
        documentList.add(xmlElement);
        RegisterDocumentSetRequestType body = makeBlankRegisterPayload(documentList);

        ExternalIdentifierType identifier = new ExternalIdentifierType();
        InternationalStringType name = new InternationalStringType();
        LocalizedStringType localizedName = new LocalizedStringType();
        localizedName.setValue("1");
        name.getLocalizedString().add(localizedName);
        identifier.setName(name);

        extrinsicObject.getExternalIdentifier().add(identifier);

        RegistryResponseType result = docRepo.registerDocumentSet(body);
        assertNotNull(result);
        assertEquals(DocRepoConstants.XDS_RETRIEVE_RESPONSE_STATUS_FAILURE, result.getStatus());

        List<RegistryError> registryErrorList = result.getRegistryErrorList().getRegistryError();

        assertEquals(2, registryErrorList.size());
        assertEquals(DocRepoConstants.XDS_ERROR_CODE_MISSING_DOCUMENT_METADATA,
            registryErrorList.get(0).getErrorCode());
        assertEquals(
            DocRepoConstants.XDS_MISSING_DOCUMENT_METADATA
            + " DocumentUniqueId was missing.",
            registryErrorList.get(0).getValue());

        assertEquals(DocRepoConstants.XDS_ERROR_CODE_MISSING_DOCUMENT_METADATA,
            registryErrorList.get(1).getErrorCode());
        assertEquals(DocRepoConstants.XDS_MISSING_DOCUMENT_METADATA + " PatientId was missing.",
            registryErrorList.get(1).getValue());
    }

    @Test
    public void testRegisterDocument_Success() {
        AdapterComponentDocRepositoryOrchImpl docRepo = getDocRepoWithMockDocService();
        ArrayList<JAXBElement<? extends IdentifiableType>> documentList = new ArrayList<JAXBElement<? extends IdentifiableType>>();
        ExtrinsicObjectType extrinsicObject = new ExtrinsicObjectType();
        JAXBElement<ExtrinsicObjectType> xmlElement = new JAXBElement<ExtrinsicObjectType>(
            new QName("ExtrinsicObjectType"), ExtrinsicObjectType.class, extrinsicObject);
        documentList.add(xmlElement);
        RegisterDocumentSetRequestType body = makeBlankRegisterPayload(documentList);

        ExternalIdentifierType docId = new ExternalIdentifierType();
        InternationalStringType name = new InternationalStringType();
        LocalizedStringType localizedName = new LocalizedStringType();
        localizedName.setValue(DocRepoConstants.XDS_DOCUMENT_UNIQUE_ID);
        name.getLocalizedString().add(localizedName);
        docId.setName(name);
        docId.setValue("1");
        extrinsicObject.getExternalIdentifier().add(docId);

        ExternalIdentifierType patientId = new ExternalIdentifierType();
        InternationalStringType patName = new InternationalStringType();
        LocalizedStringType patLocalizedName = new LocalizedStringType();
        patLocalizedName.setValue(DocRepoConstants.XDS_PATIENT_ID);
        patName.getLocalizedString().add(patLocalizedName);
        patientId.setName(patName);
        patientId.setValue("1");
        extrinsicObject.getExternalIdentifier().add(patientId);

        when(docService.saveDocument(Mockito.isA(DocumentMetadata.class))).thenAnswer(new Answer<DocumentMetadata>() {
            @Override
            public DocumentMetadata answer(InvocationOnMock invocation) throws Throwable {
                // Set the ID to act like Hibernate persisted the entity, since its a mock.
                DocumentMetadata metadata = (DocumentMetadata) invocation.getArguments()[0];
                metadata.setDocumentid(1L);
                return metadata;
            }
        });

        RegistryResponseType result = docRepo.registerDocumentSet(body);
        assertNotNull(result);
        assertEquals(DocRepoConstants.XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS, result.getStatus());
        assertNull(result.getRegistryErrorList());

        Mockito.verify(docService, Mockito.times(1)).saveDocument(Mockito.isA(DocumentMetadata.class));

    }

    private static RegisterDocumentSetRequestType makeBlankRegisterPayload(
        List<JAXBElement<? extends IdentifiableType>> identifiableList) {
        RegisterDocumentSetRequestType body = new RegisterDocumentSetRequestType();
        SubmitObjectsRequest submittedObjects = new SubmitObjectsRequest();
        RegistryObjectListType registryList = new RegistryObjectListType();
        registryList.getIdentifiable().addAll(identifiableList);
        submittedObjects.setRegistryObjectList(registryList);
        body.setSubmitObjectsRequest(submittedObjects);
        return body;
    }

    protected RetrieveDocumentSetRequestType createDocumentRequest_Failure() {
        RetrieveDocumentSetRequestType requestBody = new RetrieveDocumentSetRequestType();
        DocumentRequest req1 = new DocumentRequest();
        req1.setDocumentUniqueId("");
        req1.setHomeCommunityId("1.1");
        req1.setRepositoryUniqueId("1");
        requestBody.getDocumentRequest().add(req1);
        return requestBody;
    }

    protected RetrieveDocumentSetRequestType createDocumentRequest_PartialSuccess() {
        RetrieveDocumentSetRequestType requestBody = new RetrieveDocumentSetRequestType();
        DocumentRequest req1 = new DocumentRequest();
        req1.setDocumentUniqueId("");
        req1.setHomeCommunityId("1.1");
        req1.setRepositoryUniqueId("1");
        requestBody.getDocumentRequest().add(req1);
        DocumentRequest req2 = new DocumentRequest();
        req2.setDocumentUniqueId("2.531.777");
        req2.setHomeCommunityId("1.1");
        req2.setRepositoryUniqueId("1");
        requestBody.getDocumentRequest().add(req2);
        return requestBody;
    }

    private AdapterComponentDocRepositoryOrchImpl getDocRepoWithMockDocService() {
        AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl() {
            @Override
            public DocumentService getDocumentService() {
                return docService;
            }
        };
        return docRepo;
    }

    private AdapterComponentDocRepositoryOrchImpl getDocRepoWithMockLargeFileUtils() {
        AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl() {
            @Override
            public LargeFileUtils getLargeFileUtils() {
                return largeFileUtils;
            }
        };
        return docRepo;
    }

    private AdapterComponentDocRepositoryOrchImpl getDocRepoWithEmptyRetrieveDocuments() {
        AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl() {
            @Override
            protected void retrieveDocuments(boolean repositoryIdMatched, List<String> documentUniqueIds,
                RetrieveDocumentSetResponseType response, String homeCommunityId,
                RegistryErrorList regerrList) {
            }

        };
        return docRepo;
    }

    private AdapterComponentDocRepositoryOrchImpl getDocRepoWithMutipleOverrides() {
        AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl() {

            @Override
            protected void setDocumentPidObjects(DocumentMetadata doc,
                List<SlotType1> documentSlots) {
                // Do nothing
            }

            @Override
            protected void setDocumentObjectsFromClassifications(DocumentMetadata doc,
                List<ClassificationType> classifications) {
                // Do nothing
            }

            @Override
            protected void extractEventCodes(DocumentMetadata doc, List<ClassificationType> classifications) {
                // Do nothing
            }


            @Override
            public LargeFileUtils getLargeFileUtils() {
                return largeFileUtils;
            }

            @Override
            protected void saveDocument(DocumentMetadata doc, boolean requestHasReplacementAssociation, String documentUniqueId,
                RegistryErrorList errorList) {
                // Do nothing
            }

            @Override
            public UTCDateUtil getDateUtil() {
                return utcDateUtil;
            }

            @Override
            public DocumentService getDocumentService() {
                return docService;
            }
        };

        return docRepo;
    }
}
