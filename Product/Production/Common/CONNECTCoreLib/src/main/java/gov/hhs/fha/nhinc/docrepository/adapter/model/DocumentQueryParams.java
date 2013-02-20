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
package gov.hhs.fha.nhinc.docrepository.adapter.model;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.Date;
import java.util.List;

/**
 * Parameter object for document queries
 *
 * @author Neil Webb
 */
public class DocumentQueryParams {

    private String patientId;
    private List<String> classCodes;
    private String classCodeScheme;
    private Date creationTimeFrom;
    private Date creationTimeTo;
    private Date serviceStartTimeFrom;
    private Date serviceStartTimeTo;
    private Date serviceStopTimeFrom;
    private Date serviceStopTimeTo;
    private List<String> statuses;
    private List<String> documentUniqueIds;
    private List<EventCodeParam> eventCodeParams;
    private boolean onDemand = false;

    public List<String> getClassCodes() {
        return classCodes;
    }

    public void setClassCodes(List<String> classCodes) {
        this.classCodes = classCodes;
    }

    public String getClassCodeScheme() {
        return classCodeScheme;
    }

    public void setClassCodeScheme(String classCodeScheme) {
        this.classCodeScheme = classCodeScheme;
    }

    public Date getCreationTimeFrom() {
        if (creationTimeFrom == null) {
            return null;
        } else {
            return (Date) creationTimeFrom.clone();
        }

    }

    public void setCreationTimeFrom(Date creationTimeFrom) {
        this.creationTimeFrom = creationTimeFrom;
    }

    public Date getCreationTimeTo() {
        if (creationTimeTo == null) {
            return null;
        } else {
            return (Date) creationTimeTo.clone();
        }
    }

    public void setCreationTimeTo(Date creationTimeTo) {
        this.creationTimeTo = creationTimeTo;
    }

    public List<String> getDocumentUniqueIds() {
        return documentUniqueIds;
    }

    public void setDocumentUniqueId(List<String> documentUniqueIds) {
        this.documentUniqueIds = documentUniqueIds;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Date getServiceStartTimeFrom() {
        if (serviceStartTimeFrom == null) {
            return null;
        } else {
            return (Date) serviceStartTimeFrom.clone();
        }
    }

    public void setServiceStartTimeFrom(Date serviceStartTimeFrom) {
        this.serviceStartTimeFrom = serviceStartTimeFrom;
    }

    public Date getServiceStartTimeTo() {
        if (serviceStartTimeTo == null) {
            return null;
        } else {
            return (Date) serviceStartTimeTo.clone();
        }

    }

    public void setServiceStartTimeTo(Date serviceStartTimeTo) {
        this.serviceStartTimeTo = serviceStartTimeTo;
    }

    public Date getServiceStopTimeFrom() {
        if (serviceStopTimeFrom == null) {
            return null;
        } else {
            return (Date) serviceStopTimeFrom.clone();
        }
    }

    public void setServiceStopTimeFrom(Date serviceStopTimeFrom) {
        this.serviceStopTimeFrom = serviceStopTimeFrom;
    }

    public Date getServiceStopTimeTo() {
        if (serviceStopTimeTo == null) {
            return null;
        } else {
            return (Date) serviceStopTimeTo.clone();
        }
    }

    public void setServiceStopTimeTo(Date serviceStopTimeTo) {
        this.serviceStopTimeTo = serviceStopTimeTo;
    }

    public List<String> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<String> statuses) {
        this.statuses = statuses;
    }

    public List<EventCodeParam> getEventCodeParams() {
        return eventCodeParams;
    }

    public void setEventCodeParams(List<EventCodeParam> eventCodeParams) {
        this.eventCodeParams = eventCodeParams;
    }

    public boolean getOnDemand() {
        return onDemand;
    }

    public void setOnDemandParams(boolean onDemand) {
        this.onDemand = onDemand;
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        if (NullChecker.isNotNullish(patientId)) {
            hashCode = patientId.hashCode();
            if (NullChecker.isNotNullish(classCodes)) {
                hashCode += classCodes.hashCode();
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
        DocumentQueryParams toCheck = (DocumentQueryParams) obj;

        if ((this.getPatientId() == null) && (toCheck.getPatientId() != null)) {
            return false;
        } else if ((this.getPatientId() != null) && (!this.getPatientId().equals(toCheck.getPatientId()))) {
            return false;
        }

        if ((this.getClassCodes() == null) && (toCheck.getClassCodes() != null)) {
            return false;
        } else if ((this.getClassCodes() != null) && (!this.getClassCodes().equals(toCheck.getClassCodes()))) {
            return false;
        }

        if ((this.getClassCodeScheme() == null) && (toCheck.getClassCodeScheme() != null)) {
            return false;
        } else if ((this.getClassCodeScheme() != null)
            && (!this.getClassCodeScheme().equals(toCheck.getClassCodeScheme()))) {
            return false;
        }

        if ((this.getCreationTimeFrom() == null) && (toCheck.getCreationTimeFrom() != null)) {
            return false;
        } else if ((this.getCreationTimeFrom() != null)
            && (!this.getCreationTimeFrom().equals(toCheck.getCreationTimeFrom()))) {
            return false;
        }

        if ((this.getCreationTimeTo() == null) && (toCheck.getCreationTimeTo() != null)) {
            return false;
        } else if ((this.getCreationTimeTo() != null)
            && (!this.getCreationTimeTo().equals(toCheck.getCreationTimeTo()))) {
            return false;
        }

        if ((this.getServiceStartTimeFrom() == null) && (toCheck.getServiceStartTimeFrom() != null)) {
            return false;
        } else if ((this.getServiceStartTimeFrom() != null)
            && (!this.getServiceStartTimeFrom().equals(toCheck.getServiceStartTimeFrom()))) {
            return false;
        }

        if ((this.getServiceStartTimeTo() == null) && (toCheck.getServiceStartTimeTo() != null)) {
            return false;
        } else if ((this.getServiceStartTimeTo() != null)
            && (!this.getServiceStartTimeTo().equals(toCheck.getServiceStartTimeTo()))) {
            return false;
        }

        if ((this.getServiceStopTimeFrom() == null) && (toCheck.getServiceStopTimeFrom() != null)) {
            return false;
        } else if ((this.getServiceStopTimeFrom() != null)
            && (!this.getServiceStopTimeFrom().equals(toCheck.getServiceStopTimeFrom()))) {
            return false;
        }

        if ((this.getServiceStopTimeTo() == null) && (toCheck.getServiceStopTimeTo() != null)) {
            return false;
        } else if ((this.getServiceStopTimeTo() != null)
            && (!this.getServiceStopTimeTo().equals(toCheck.getServiceStopTimeTo()))) {
            return false;
        }

        if ((this.getStatuses() == null) && (toCheck.getStatuses() != null)) {
            return false;
        } else if ((this.getStatuses() != null) && (!this.getStatuses().equals(toCheck.getStatuses()))) {
            return false;
        }

        if ((this.getDocumentUniqueIds() == null) && (toCheck.getDocumentUniqueIds() != null)) {
            return false;
        } else if ((this.getDocumentUniqueIds() != null)
            && (!this.getDocumentUniqueIds().equals(toCheck.getDocumentUniqueIds()))) {
            return false;
        }

        if ((this.getEventCodeParams() == null) && (toCheck.getEventCodeParams() != null)) {
            return false;
        } else if ((this.getEventCodeParams() != null)
            && (!this.getEventCodeParams().equals(toCheck.getEventCodeParams()))) {
            return false;
        }

        if (this.getOnDemand() != toCheck.getOnDemand()) {
            return false;
        }

        return true;
    }
}
