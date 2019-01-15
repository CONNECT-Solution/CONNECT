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
package gov.hhs.fha.nhinc.admingui.managed;

import static gov.hhs.fha.nhinc.admingui.util.HelperUtil.addFacesMessageBy;

import gov.hhs.fha.nhinc.admingui.model.loadtestdata.Document;
import gov.hhs.fha.nhinc.admingui.model.loadtestdata.Patient;
import gov.hhs.fha.nhinc.admingui.services.LoadTestDataWSService;
import gov.hhs.fha.nhinc.admingui.services.exception.ValidationException;
import gov.hhs.fha.nhinc.admingui.services.impl.LoadTestDataWSServiceImpl;
import gov.hhs.fha.nhinc.admingui.util.HelperUtil;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.DocumentMetadataType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.DocumentType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.EventCodeType;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentMetadata;
import gov.hhs.fha.nhinc.loadtestdata.LoadTestDataException;
import gov.hhs.fha.nhinc.util.CoreHelpUtils;
import java.io.IOException;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Tran Tang
 *
 */
@ManagedBean(name = "LoadTestDataDocumentBean")
@ViewScoped
@Component
public class LoadTestDataDocumentBean {

    private static final Logger LOG = LoggerFactory.getLogger(LoadTestDataDocumentBean.class);
    private static final String DOCUMENT = "Document Info";
    private static final String EVENT_CODE = " an Eventcode";
    private static final String GROWL_MESSAGE = "msgForGrowl";
    private static final String EDIT = "edit";
    private static final String UNSUCCESSFUL = "Operation was not successful. Please refresh your browser and try again.";

    private String dialogTitle;
    private Document withDocument;
    private EventCodeType withEventCode;

    private Document selectedDocument;
    private EventCodeType selectedEventCode;

    private LoadTestDataWSService wsService = new LoadTestDataWSServiceImpl();
    private Map<String, String> listStatusType;
    private List<Document> documentList;
    Map<Long, Patient> lookupPatient;

    public LoadTestDataDocumentBean() {
        selectedDocument = null;
    }

    // selected-record
    public Document getSelectedDocument() {
        return selectedDocument;
    }

    public void setSelectedDocument(Document selectedDocument) {
        this.selectedDocument = selectedDocument;
    }

    public EventCodeType getSelectedEventCode() {
        return selectedEventCode;
    }

    public void setSelectedEventCode(EventCodeType selectedEventCode) {
        this.selectedEventCode = selectedEventCode;
    }

    // database-methods
    public List<Document> getDocuments() {
        if (MapUtils.isEmpty(lookupPatient)) {
            List<Patient> patients = HelperUtil.convertPatients(wsService.getAllPatients());
            lookupPatient = mapPatientById(patients);
        }
        if (CollectionUtils.isEmpty(documentList)) {
            documentList = HelperUtil.convertDocuments(wsService.getAllDocuments());
            for (Document doc : documentList) {
                if (doc.getPatientRecordId() != null) {
                    Patient idFound = lookupPatient.get(doc.getPatientRecordId());
                    doc.setPatientIdentifier(null != idFound ? idFound.getPatientIdentifier() : null);
                }
            }
        }
        return documentList;

    }

    public boolean saveDocument() {
        boolean actionResult = false;
        try {
            if (withDocument != null) {
                actionResult = wsService.saveDocument(withDocument);
                refreshDocumentList();
                if (actionResult) {
                    addFacesMessageBy(GROWL_MESSAGE, msgForSaveSuccess(DOCUMENT, withDocument.getDocumentId()));
                } else {
                    addFacesMessageBy(GROWL_MESSAGE, HelperUtil.getMsgError(UNSUCCESSFUL));
                }
            } else {
                addFacesMessageBy(GROWL_MESSAGE, HelperUtil.getMsgInfo("Document is null."));
            }
        } catch (LoadTestDataException e) {
            logError(DOCUMENT, e);
        }
        return actionResult;
    }

    public void duplicateDocument() {
        if (selectedDocument != null) {
            dialogTitle = "Edit Document";
            withDocument = new Document(wsService.duplicateDocument(selectedDocument.getDocumentId()));
            if (null != withDocument) {
                selectedDocument = withDocument;
                refreshDocumentList();
            }
        } else {
            new DocumentMetadata();
            addFacesMessageBy(msgForInvalidDocument("document"));
        }
    }

    public boolean deleteDocument() {
        boolean result = false;
        if (selectedDocument != null) {
            result = wsService.deleteDocument(selectedDocument);
            refreshDocumentList();
            selectedDocument = null;
        } else {
            addFacesMessageBy(GROWL_MESSAGE, msgForSelectDelete("Document"));
        }
        return result;
    }

    public void newDocument() {
        dialogTitle = "Create Document";
        withDocument = new Document();
        newEventCode();
    }

    public void editDocument() {
        if (selectedDocument != null) {
            dialogTitle = "Edit Document";
            DocumentMetadataType docMeta = wsService.getDocumentBy(selectedDocument.getDocumentId());
            if (null != docMeta) {
                withDocument = new Document(docMeta);
                newEventCode();
            } else {
                refreshDocumentList();
                addFacesMessageBy(GROWL_MESSAGE, msgForLoadFor(DOCUMENT, EDIT));
            }
        } else {
            newDocument();
            addFacesMessageBy(GROWL_MESSAGE, msgForSelectEdit(DOCUMENT));
        }
    }

    public List<EventCodeType> getEventCodes() {
        if (null != withDocument) {
            return withDocument.getEventCodeList();
        }
        return new ArrayList<>();
    }

    public boolean saveEventCode() {
        boolean actionResult = false;

        if (isValidDocumentId()) {
            try {
                withEventCode.setDocumentid(withDocument.getDocumentId());
                actionResult = wsService.saveEventCode(withEventCode);

                if (actionResult) {
                    refreshDocument();
                    addFacesMessageBy(msgForSaveSuccess(EVENT_CODE, withEventCode.getEventCodeId()));
                    withEventCode = new EventCodeType();
                } else {
                    addFacesMessageBy(HelperUtil.getMsgError(UNSUCCESSFUL));
                }
            } catch (LoadTestDataException e) {
                logError(EVENT_CODE, e);
            }
        } else {
            addFacesMessageBy(msgForInvalidDocument(EVENT_CODE));
        }
        return actionResult;
    }

    public boolean deleteEventCode() {
        boolean result = false;
        if (selectedEventCode != null) {
            result = wsService.deleteEventCode(selectedEventCode);
            refreshDocument();
            selectedEventCode = null;
        } else {
            addFacesMessageBy(msgForSelectDelete(EVENT_CODE));
        }
        return result;
    }

    public void newEventCode() {
        withEventCode = new EventCodeType();
    }

    public void editEventCode() {
        if (selectedEventCode != null) {
            withEventCode = wsService.getEventCodeBy(selectedEventCode.getEventCodeId());
            if(null == withEventCode){
                refreshDocument();
                addFacesMessageBy(msgForLoadFor(EVENT_CODE, EDIT));
            }
        } else {
            newEventCode();
            addFacesMessageBy(msgForSelectEdit(EVENT_CODE));
        }
    }

    // bean-property
    public String getDialogTitle() {
        return dialogTitle;
    }

    public boolean getDisableAccordion() {
        return !isValidDocumentId();
    }

    public boolean getDisableDocumentButtons() {
        return selectedDocument == null;
    }

    public boolean getDisableEventCodeButtons() {
        return selectedEventCode == null;
    }

    public Document getDocumentForm() {
        if (null == withDocument) {
            withDocument = new Document();
        }
        return withDocument;
    }

    public EventCodeType getEventCodeForm() {
        if (null == withEventCode) {
            withEventCode = new EventCodeType();
        }
        return withEventCode;
    }

    public Map<String, String> getListPatientId() {
        List<Patient> temp = HelperUtil.convertPatients(wsService.getAllPatients());
        return HelperUtil.populateListPatientId(temp);
    }

    public Map<String, String> getListStatusType() {
        if (null == listStatusType) {
            listStatusType = HelperUtil.populateListStatusType();
        }
        return listStatusType;
    }

    public void setDocumentFile(UploadedFile file) {
        if (file != null && withDocument != null) {
            DocumentType doc = new DocumentType();
            doc.setRawData(file.getContents());

            withDocument.setDocument(doc);
            withDocument.setSize(BigInteger.valueOf(file.getContents().length));
            withDocument.setMimeType(file.getContentType());
            try {
                withDocument.setHash(DigestUtils.md5Hex(file.getInputstream()));
            } catch (IOException e) {
                LOG.error("error while trying to get file-hash: {} {}", e.getMessage(), e);
            }
            addFacesMessageBy("inputRawData",
                HelperUtil.getMsgInfo("Click Save to complete uploading " + file.getFileName()));
            LOG.info(file.getFileName());
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        setDocumentFile(event.getFile());
    }

    public String getPatientId() {
        return getDocumentForm().getPatientId();
    }

    public void setPatientId(String patientIdentifierIso) {

        if (StringUtils.isNotBlank(patientIdentifierIso)) {
            String[] identifierValue = patientIdentifierIso.split("&");
            CoreHelpUtils.updateDocumentBy(withDocument,
                wsService.getPatientBy(identifierValue[0].replace("^^^", ""), identifierValue[1]));
        } else {
            throw new ValidationException("PatientIdentifier should not be empty.");
        }
    }

    // IDs and isValidId
    private boolean isValidDocumentId() {
        return withDocument != null && CoreHelpUtils.isId(withDocument.getDocumentId());
    }

    private static Map<Long, Patient> mapPatientById(List<Patient> patients) {
        Map<Long, Patient> lookup = new TreeMap<>();
        for (Patient rec : patients) {
            lookup.put(rec.getPatientId(), rec);
        }
        return lookup;
    }

    public String getDateNow() {
        return HelperUtil.getDateNow();
    }

    // msgs
    private static FacesMessage msgForSaveSuccess(String ofType, Long ofId) {
        return HelperUtil.getMsgInfo(MessageFormat.format("Save {0} successful.", ofType.toLowerCase()));
    }

    private static FacesMessage msgForSelectDelete(String ofType) {
        return HelperUtil.getMsgWarn(MessageFormat.format("Select {0} for delete.", ofType.toLowerCase()));
    }

    private static FacesMessage msgForSelectEdit(String ofType) {
        return HelperUtil.getMsgWarn(MessageFormat.format("Select {0} for edit.", ofType.toLowerCase()));
    }

    private static FacesMessage msgForLoadFor(String ofType, String action) {
        return HelperUtil.getMsgError(
            MessageFormat.format("Loading {0} for {1}.", ofType.toLowerCase(), action.toLowerCase()));
    }

    private static FacesMessage msgForInvalidDocument(String ofType) {
        return HelperUtil.getMsgError(MessageFormat.format("{0} cannot be saved: document does not exist.", ofType));
    }

    private static void logError(String logOf, LoadTestDataException e) {
        FacesContext.getCurrentInstance().validationFailed();
        addFacesMessageBy(HelperUtil.getMsgError(
            MessageFormat.format("Cannot save document {0}: {1}", logOf.toLowerCase(), e.getLocalizedMessage())));
        LOG.error("Error save-document-{0}: {}", logOf.toLowerCase(), e.getLocalizedMessage(), e);
    }

    private void refreshDocumentList() {
        documentList = null;
        getDocuments();
    }

    private void refreshDocument(){
        if(null == selectedDocument){
            selectedDocument = withDocument;
        }
        editDocument();
    }
}
