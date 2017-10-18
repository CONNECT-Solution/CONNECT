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

import gov.hhs.fha.nhinc.admingui.util.HelperUtil;
import gov.hhs.fha.nhinc.docrepository.adapter.model.Document;
import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCode;
import gov.hhs.fha.nhinc.patientdb.model.Patient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.commons.codec.digest.DigestUtils;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private String dialogTitle = "Set the Title for Edit/New";
    private String eventCode;
    private String eventCodeScheme;
    private String eventCodeDisplayName;

    private UploadedFile uploadedFile;

    private Document withDocument;

    private Document selectedDocument;
    private EventCode selectedEventCode;

    private Map<String, String> listPatientId;

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
        return new ArrayList<>();
    }

    public boolean saveDocument() {
        return false;
    }

    public boolean deleteDocument() {
        return false;
    }

    public void newDocument() {
        withDocument = new Document();
        listPatientId = getListPatientId();
        // return false;
    }

    public void editDocument() {
        withDocument = new Document();
        withDocument.setDocumentid(1L);
        withDocument.setDocumentUniqueId("this is a database value");
        // return false;
    }

    public List<EventCode> getEventCodes() {
        return new ArrayList<>();
    }

    public boolean saveEventCode() {
        return false;
    }

    public boolean deleteEventCode() {
        return false;
    }

    public void newEventCode() {
        // return false;
    }

    public void editEventCode() {
        // return false;
    }

    // bean-property
    public String getDialogTitle() {
        return dialogTitle;
    }

    public boolean getDisableAccordion() {
        return !isValidDocumentId();
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getEventCodeScheme() {
        return eventCodeScheme;
    }

    public void setEventCodeScheme(String eventCodeScheme) {
        this.eventCodeScheme = eventCodeScheme;
    }

    public String getEventCodeDisplayName() {
        return eventCodeDisplayName;
    }

    public void setEventCodeDisplayName(String eventCodeDisplayName) {
        this.eventCodeDisplayName = eventCodeDisplayName;
    }

    public Document getDocumentForm() {
        if (null == withDocument) {
            withDocument = new Document();
        }
        return withDocument;
    }

    public Map<String, String> getListPatientId() {
        if (null == listPatientId) {
            listPatientId = HelperUtil.populateListPatientId(new ArrayList<Patient>());
        }
        return listPatientId;
    }

    public UploadedFile getDocumentFile() {
        return uploadedFile;
    }

    @SuppressWarnings("boxing")
    public void setDocumentFile(UploadedFile file) {
        uploadedFile = file;
        if (file != null && withDocument != null) {
            withDocument.setRawData(file.getContents());
            withDocument.setSize((int) file.getSize());
            withDocument.setMimeType(file.getContentType());
            try{
                withDocument.setHash(DigestUtils.md5Hex(file.getInputstream()));
            } catch (IOException e) {
                LOG.error("error while trying to get file-hash: {} {}", e.getMessage(), e);
            }
        }
    }

    // IDs and isValidId
    private boolean isValidDocumentId() {
        return withDocument != null && HelperUtil.isId(withDocument.getDocumentid());
    }

    public Long getDocumentid() {
        Long localId = 0L;
        if (isValidDocumentId()) {
            localId = withDocument.getDocumentid();
        }
        return localId;
    }



    // read-record


    // bean-entity


    // clear input forms

}
