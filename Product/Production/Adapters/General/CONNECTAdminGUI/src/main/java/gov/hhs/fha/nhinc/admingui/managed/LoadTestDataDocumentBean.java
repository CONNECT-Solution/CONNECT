/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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

import gov.hhs.fha.nhinc.admingui.services.LoadTestDataService;
import gov.hhs.fha.nhinc.admingui.services.exception.LoadTestDataException;
import gov.hhs.fha.nhinc.admingui.util.HelperUtil;
import gov.hhs.fha.nhinc.docrepository.adapter.model.Document;
import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCode;
import gov.hhs.fha.nhinc.patientdb.model.Patient;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

/**
 * @author Tran Tang
 *
 */
@ManagedBean(name = "LoadTestDataDocumentBean")
@ImportResource("file:${nhinc.properties.dir}/LoadTestDataConfig.xml")
@SessionScoped
@Component
public class LoadTestDataDocumentBean {
    private static final Logger LOG = LoggerFactory.getLogger(LoadTestDataDocumentBean.class);
    private static final String DOCUMENT = "Document Info";
    private static final String EVENT_CODE = " an Eventcode";
    private static final String GROWL_MESSAGE = "msgForGrowl";

    private String dialogTitle;
    private Document withDocument;
    private EventCode withEventCode;

    private Document selectedDocument;
    private EventCode selectedEventCode;

    @Autowired
    private LoadTestDataService loadTestDataService;
    private Map<String, String> listStatusType;

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

    public EventCode getSelectedEventCode() {
        return selectedEventCode;
    }

    public void setSelectedEventCode(EventCode selectedEventCode) {
        this.selectedEventCode = selectedEventCode;
    }

    // database-methods
    public List<Document> getDocuments() {
        List<Document> documents = loadTestDataService.getAllDocuments();
        Map<Long, Patient> lookupPatient = mapPatientById(loadTestDataService.getCachePatients());
        for (Document doc : documents) {
            if (doc.getPatientRecordId() != null) {
                doc.setPatientIdentifier(lookupPatient.get(doc.getPatientRecordId()).getPatientIdentifier());
            }
        }
        return documents;
    }

    public boolean saveDocument() {
        boolean actionResult = false;
        try {
            if (withDocument != null) {
                actionResult = loadTestDataService.saveDocument(withDocument);

                if (actionResult) {
                    addFacesMessageBy(GROWL_MESSAGE, msgForSaveSuccess(DOCUMENT, withDocument.getDocumentid()));
                }
            } else {
                addFacesMessageBy(GROWL_MESSAGE, HelperUtil.getMsgInfo("Document is null."));
            }
        } catch (LoadTestDataException e) {
            logError(DOCUMENT, e);
        }
        return actionResult;
    }

    public boolean deleteDocument() {
        boolean result = false;
        if (selectedDocument != null) {
            result = loadTestDataService.deleteDocument(selectedDocument);
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
            withDocument = loadTestDataService.getDocumentBy(selectedDocument.getDocumentid());
            newEventCode();
        } else {
            newDocument();
            addFacesMessageBy(GROWL_MESSAGE, msgForSelectEdit(DOCUMENT));
        }
    }

    public List<EventCode> getEventCodes() {
        return loadTestDataService.getAllEventCodesBy(getDocumentid());
    }

    public boolean saveEventCode() {
        boolean actionResult = false;

        if (isValidDocumentId()) {
            try {
                withEventCode.setDocument(withDocument);
                actionResult = loadTestDataService.saveEventCode(withEventCode);

                if (actionResult) {
                    addFacesMessageBy(msgForSaveSuccess(EVENT_CODE, withEventCode.getEventCodeId()));
                    withEventCode = new EventCode();
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
            result = loadTestDataService.deleteEventCode(selectedEventCode);
            selectedEventCode = null;
        } else {
            addFacesMessageBy(msgForSelectDelete(EVENT_CODE));
        }
        return result;
    }

    public void newEventCode() {
        withEventCode = new EventCode();
    }

    public void editEventCode() {
        if (selectedEventCode != null) {
            withEventCode = loadTestDataService.getEventCodeBy(selectedEventCode.getEventCodeId());
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

    public EventCode getEventCodeForm() {
        if (null == withEventCode) {
            withEventCode = new EventCode();
        }
        return withEventCode;
    }

    public Map<String, String> getListPatientId() {
        return HelperUtil.populateListPatientId(loadTestDataService.getCachePatients());
    }

    public Map<String, String> getListStatusType() {
        if (null == listStatusType) {
            listStatusType = HelperUtil.populateListStatusType();
        }
        return listStatusType;
    }

    public void setDocumentFile(UploadedFile file) {
        if (file != null && withDocument != null) {
            withDocument.setRawData(file.getContents());
            withDocument.setSize(file.getContents().length);
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
            HelperUtil.updateDocumentBy(withDocument,
                loadTestDataService.getPatientBy(identifierValue[0].replace("^^^", ""), identifierValue[1]));
        } else {
            HelperUtil.updateDocumentBy(withDocument, new Patient());
        }
    }

    // IDs and isValidId
    private boolean isValidDocumentId() {
        return withDocument != null && HelperUtil.isId(withDocument.getDocumentid());
    }

    private Long getDocumentid() {
        Long localId = 0L;
        if (isValidDocumentId()) {
            localId = withDocument.getDocumentid();
        }
        return localId;
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

    private static FacesMessage msgForInvalidDocument(String ofType) {
        return HelperUtil.getMsgError(MessageFormat.format("{0} cannot be saved: document does not exist.", ofType));
    }

    private static void logError(String logOf, LoadTestDataException e) {
        FacesContext.getCurrentInstance().validationFailed();
        addFacesMessageBy(HelperUtil.getMsgError(
            MessageFormat.format("Cannot save document {0}: {1}", logOf.toLowerCase(), e.getLocalizedMessage())));
        LOG.error("Error save-document-{0}: {}", logOf.toLowerCase(), e.getLocalizedMessage(), e);
    }

}
