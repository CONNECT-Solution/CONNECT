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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentMetadata;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentQueryParams;
import gov.hhs.fha.nhinc.docrepository.adapter.service.DocumentService;
import gov.hhs.fha.nhinc.largefile.LargeFileUtils;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.activation.DataHandler;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.InternationalStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.LocalizedStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import org.junit.Test;

/**
 * @author jsmith
 *
 */
public class AdapterComponentDocRepositoryHelperTest {

    @Test
    public void testGetDocumentMap() throws IOException {
        AdapterComponentDocRepositoryHelper docRepoHelper = new AdapterComponentDocRepositoryHelper();
        ProvideAndRegisterDocumentSetRequestType body = mock(ProvideAndRegisterDocumentSetRequestType.class);
        List<ProvideAndRegisterDocumentSetRequestType.Document> docList = new ArrayList<>();
        ProvideAndRegisterDocumentSetRequestType.Document doc = new ProvideAndRegisterDocumentSetRequestType.Document();
        final String ID = "MOCK_ID";
        final String VALUE = "MOCK_VALUE";
        DataHandler dataHandler = LargeFileUtils.getInstance().convertToDataHandler(VALUE);
        doc.setId(ID);
        doc.setValue(dataHandler);

        docList.add(doc);

        when(body.getDocument()).thenReturn(docList);

        HashMap<String, DataHandler> docMap = docRepoHelper.getDocumentMap(body);

        assertEquals(docMap.get(ID), dataHandler);
    }

    /**
     * Test for extractClassifcationMetadata(List<ClassificationType, String, String, int).
     */
    @Test
    public void testExtractClassificationMetadata() {
        final String CLASS_VALUE = "Classfication value";
        final String CLASS_SCHEME_NAME = "classificationSchemeName";
        AdapterComponentDocRepositoryHelper docRepoHelper = new AdapterComponentDocRepositoryHelper() {
            @Override
            String extractMetadataFromSlots(List<SlotType1> documentSlots, String slotName, int valueIndex) {
                return CLASS_VALUE;
            }
        };
        List<ClassificationType> classifications = new ArrayList<>();
        ClassificationType classType = mock(ClassificationType.class);

        classifications.add(classType);

        when(classType.getClassificationScheme()).thenReturn(CLASS_SCHEME_NAME);

        String result = docRepoHelper.extractClassificationMetadata(classifications, CLASS_SCHEME_NAME, "slotName", 0);
        assertEquals(result, CLASS_VALUE);
    }

    @Test
    public void testExtractPatientInfo() {
        final String PATIENT_NAME = "John Doe";
        final String SLOT_PATIENT_NAME = "John Doe <Patient Information>";
        final String EXPECTED_RESULT = "<Patient Information>";
        List<SlotType1> documentSlots = new ArrayList<>();
        SlotType1 slot = mock(SlotType1.class);
        documentSlots.add(slot);
        ValueListType valueListType = mock(ValueListType.class);
        List<String> valueList = new ArrayList<>();
        valueList.add(SLOT_PATIENT_NAME);

        when(slot.getName()).thenReturn(DocRepoConstants.XDS_SOURCE_PATIENT_INFO_SLOT);
        when(slot.getValueList()).thenReturn(valueListType);
        when(valueListType.getValue()).thenReturn(valueList);

        AdapterComponentDocRepositoryHelper docRepoHelper = new AdapterComponentDocRepositoryHelper();

        String result = docRepoHelper.extractPatientInfo(documentSlots, PATIENT_NAME);

        assertEquals(result, EXPECTED_RESULT);
    }

    /**
     * Test for extractClassificationMetadata(List<ClassificationType>, String, String).
     */
    @Test
    public void testExtractClassificationMetadata2() {
        AdapterComponentDocRepositoryHelper docRepoHelper = new AdapterComponentDocRepositoryHelper();
        String result;
        List<ClassificationType> classifications = new ArrayList();
        ClassificationType classificationType = mock(ClassificationType.class);
        classifications.add(classificationType);
        InternationalStringType internationalString = mock(InternationalStringType.class);
        LocalizedStringType localizedString = new LocalizedStringType();
        final String VALUE = "classification Value";
        localizedString.setValue(VALUE);
        List<LocalizedStringType> localizedList = new ArrayList();
        localizedList.add(localizedString);
        final String CLASSIFICATION_SCHEME = "classificationScheme";

        when(classificationType.getClassificationScheme()).thenReturn(CLASSIFICATION_SCHEME);
        when(classificationType.getName()).thenReturn(internationalString);
        when(internationalString.getLocalizedString()).thenReturn(localizedList);

        result = docRepoHelper.extractClassificationMetadata(classifications, CLASSIFICATION_SCHEME,
                DocRepoConstants.XDS_NAME);
        assertEquals(result, VALUE);

        when(classificationType.getNodeRepresentation()).thenReturn(VALUE);
        result = docRepoHelper.extractClassificationMetadata(classifications, CLASSIFICATION_SCHEME,
                DocRepoConstants.XDS_NODE_REPRESENTATION);
        assertEquals(result, VALUE);

        when(classificationType.getClassifiedObject()).thenReturn(VALUE);
        result = docRepoHelper.extractClassificationMetadata(classifications, CLASSIFICATION_SCHEME,
                DocRepoConstants.XDS_CLASSIFIED_OBJECT);
        assertEquals(result, VALUE);

        result = docRepoHelper.extractClassificationMetadata(classifications, CLASSIFICATION_SCHEME,
                DocRepoConstants.XDS_CLASSIFICATION_ID);
        assertEquals(result, VALUE);
    }

    @Test
    public void testQueryRepositoryByPatientId() {
        AdapterComponentDocRepositoryHelper docRepoHelper = new AdapterComponentDocRepositoryHelper();
        final String SPATID = "sPatId";
        final String SDOCID = "Doc ID 1";
        final String SCLASSCODE = "sClassCode";
        final String ESTATUS = "sStatus";
        final String DOC_UNIQUE_ID = "Doc ID 1";
        final long DOC_ID = 12345;
        DocumentService docService = mock(DocumentService.class);
        List<DocumentMetadata> documents = new ArrayList<>();
        DocumentMetadata doc = mock(DocumentMetadata.class);
        documents.add(doc);

        when(docService.documentQuery(any(DocumentQueryParams.class))).thenReturn(documents);
        when(doc.getDocumentUniqueId()).thenReturn(DOC_UNIQUE_ID);
        when(doc.getDocumentid()).thenReturn(DOC_ID);

        long result = docRepoHelper.queryRepositoryByPatientId(SPATID, SDOCID, SCLASSCODE, ESTATUS, docService);

        assertEquals(result, DOC_ID);
    }
}
