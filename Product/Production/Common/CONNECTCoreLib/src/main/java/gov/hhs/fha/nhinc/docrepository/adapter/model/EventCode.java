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
package gov.hhs.fha.nhinc.docrepository.adapter.model;

import gov.hhs.fha.nhinc.common.loadtestdatamanagement.EventCodeType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.util.CoreHelpUtils;

/**
 * Data class for a document event code.
 *
 * @author Neil Webb
 */
public class EventCode {

    private Long eventCodeId;
    private String eventCode;
    private String eventCodeScheme;
    private String eventCodeDisplayName;
    private DocumentMetadata document;

    public EventCode() {
    }

    public EventCode(EventCodeType evCode, DocumentMetadata docMetadata) {
        eventCodeId = CoreHelpUtils.isId(evCode.getEventCodeId()) ? evCode.getEventCodeId() : null;
        eventCode = evCode.getEventCode();
        eventCodeScheme = evCode.getEventCodeScheme();
        eventCodeDisplayName = evCode.getEventCodeDisplayName();
        document = docMetadata;
    }

    public EventCodeType getEventCodeType() {
        EventCodeType build = new EventCodeType();
        build.setEventCodeId(eventCodeId);
        build.setEventCode(eventCode);
        build.setEventCodeScheme(eventCodeScheme);
        build.setEventCodeDisplayName(eventCodeDisplayName);
        build.setDocumentid(document.getDocumentid());
        return build;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getEventCodeDisplayName() {
        return eventCodeDisplayName;
    }

    public void setEventCodeDisplayName(String eventCodeDisplayName) {
        this.eventCodeDisplayName = eventCodeDisplayName;
    }

    public Long getEventCodeId() {
        return eventCodeId;
    }

    public void setEventCodeId(Long eventCodeId) {
        this.eventCodeId = eventCodeId;
    }

    public String getEventCodeScheme() {
        return eventCodeScheme;
    }

    public void setEventCodeScheme(String eventCodeScheme) {
        this.eventCodeScheme = eventCodeScheme;
    }

    public DocumentMetadata getDocument() {
        return document;
    }

    public void setDocument(DocumentMetadata document) {
        this.document = document;
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        if (eventCodeId != null) {
            hashCode = eventCodeId.hashCode();
        } else if (NullChecker.isNotNullish(eventCode)) {
            hashCode = eventCode.hashCode();
            if (NullChecker.isNotNullish(eventCodeScheme)) {
                hashCode += eventCodeScheme.hashCode();
            }
        }
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!this.getClass().equals(obj.getClass())) {
            return false;
        }
        EventCode toCheck = (EventCode) obj;

        if (getEventCodeId() == null && toCheck.getEventCodeId() != null) {
            return false;
        } else if (getEventCodeId() != null && !getEventCodeId().equals(toCheck.getEventCodeId())) {
            return false;
        }

        if (getEventCode() == null && toCheck.getEventCode() != null) {
            return false;
        } else if (getEventCode() != null && !getEventCode().equals(toCheck.getEventCode())) {
            return false;
        }

        if (getEventCodeScheme() == null && toCheck.getEventCodeScheme() != null) {
            return false;
        } else if (getEventCodeScheme() != null && !getEventCodeScheme().equals(toCheck.getEventCodeScheme())) {
            return false;
        }

        if (getEventCodeDisplayName() == null && toCheck.getEventCodeDisplayName() != null) {
            return false;
        } else if (getEventCodeDisplayName() != null
            && !getEventCodeDisplayName().equals(toCheck.getEventCodeDisplayName())) {
            return false;
        }
        return true;
    }

    public EventCode cloneEventCode() {
        EventCode clone = new EventCode();

        clone.setEventCodeId(null);
        clone.setEventCode(eventCode);
        clone.setEventCodeScheme(eventCodeScheme);
        clone.setEventCodeDisplayName(eventCodeDisplayName);
        clone.setDocument(null);

        return clone;
    }
}
