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

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.Date;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;

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
    private Boolean onDemand = null;
    private List<SlotType1> slots;

    public List<String> getClassCodes() {
        return classCodes;
    }

    /**
     * @return the slots
     */
    public List<SlotType1> getSlots() {
        return slots;
    }

    /**
     * @param slots the slots to set
     */
    public void setSlots(List<SlotType1> slots) {
        this.slots = slots;
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
        if (creationTimeFrom != null) {
            this.creationTimeFrom = (Date) creationTimeFrom.clone();
        } else {
            this.creationTimeFrom = null;
        }
    }

    public Date getCreationTimeTo() {
        if (creationTimeTo == null) {
            return null;
        } else {
            return (Date) creationTimeTo.clone();
        }
    }

    public void setCreationTimeTo(Date creationTimeTo) {
        if (creationTimeTo != null) {
            this.creationTimeTo = (Date) creationTimeTo.clone();
        } else {
            this.creationTimeTo = null;
        }
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
        if (serviceStartTimeFrom != null) {
            this.serviceStartTimeFrom = (Date) serviceStartTimeFrom.clone();
        } else {
            this.serviceStartTimeFrom = null;
        }
    }

    public Date getServiceStartTimeTo() {
        if (serviceStartTimeTo == null) {
            return null;
        } else {
            return (Date) serviceStartTimeTo.clone();
        }

    }

    public void setServiceStartTimeTo(Date serviceStartTimeTo) {
        if (serviceStartTimeTo != null) {
            this.serviceStartTimeTo = (Date) serviceStartTimeTo.clone();
        } else {
            this.serviceStartTimeTo = null;
        }
    }

    public Date getServiceStopTimeFrom() {
        if (serviceStopTimeFrom == null) {
            return null;
        } else {
            return (Date) serviceStopTimeFrom.clone();
        }
    }

    public void setServiceStopTimeFrom(Date serviceStopTimeFrom) {
        if (serviceStopTimeFrom != null) {
            this.serviceStopTimeFrom = (Date) serviceStopTimeFrom.clone();
        } else {
            this.serviceStopTimeFrom = null;
        }
    }

    public Date getServiceStopTimeTo() {
        if (serviceStopTimeTo == null) {
            return null;
        } else {
            return (Date) serviceStopTimeTo.clone();
        }
    }

    public void setServiceStopTimeTo(Date serviceStopTimeTo) {
        if (serviceStopTimeTo != null) {
            this.serviceStopTimeTo = (Date) serviceStopTimeTo.clone();
        } else {
            this.serviceStopTimeTo = null;
        }
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

    public Boolean getOnDemand() {
        return onDemand;
    }

    public void setOnDemandParams(Boolean onDemand) {
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

        if (getPatientId() == null && toCheck.getPatientId() != null) {
            return false;
        } else if (getPatientId() != null && !getPatientId().equals(toCheck.getPatientId())) {
            return false;
        }

        if (getClassCodes() == null && toCheck.getClassCodes() != null) {
            return false;
        } else if (getClassCodes() != null && !getClassCodes().equals(toCheck.getClassCodes())) {
            return false;
        }

        if (getClassCodeScheme() == null && toCheck.getClassCodeScheme() != null) {
            return false;
        } else if (getClassCodeScheme() != null && !getClassCodeScheme().equals(toCheck.getClassCodeScheme())) {
            return false;
        }

        if (getCreationTimeFrom() == null && toCheck.getCreationTimeFrom() != null) {
            return false;
        } else if (getCreationTimeFrom() != null && !getCreationTimeFrom().equals(toCheck.getCreationTimeFrom())) {
            return false;
        }

        if (getCreationTimeTo() == null && toCheck.getCreationTimeTo() != null) {
            return false;
        } else if (getCreationTimeTo() != null && !getCreationTimeTo().equals(toCheck.getCreationTimeTo())) {
            return false;
        }

        if (getServiceStartTimeFrom() == null && toCheck.getServiceStartTimeFrom() != null) {
            return false;
        } else if (getServiceStartTimeFrom() != null
                && !getServiceStartTimeFrom().equals(toCheck.getServiceStartTimeFrom())) {
            return false;
        }

        if (getServiceStartTimeTo() == null && toCheck.getServiceStartTimeTo() != null) {
            return false;
        } else if (getServiceStartTimeTo() != null
                && !getServiceStartTimeTo().equals(toCheck.getServiceStartTimeTo())) {
            return false;
        }

        if (getServiceStopTimeFrom() == null && toCheck.getServiceStopTimeFrom() != null) {
            return false;
        } else if (getServiceStopTimeFrom() != null
                && !getServiceStopTimeFrom().equals(toCheck.getServiceStopTimeFrom())) {
            return false;
        }

        if (getServiceStopTimeTo() == null && toCheck.getServiceStopTimeTo() != null) {
            return false;
        } else if (getServiceStopTimeTo() != null && !getServiceStopTimeTo().equals(toCheck.getServiceStopTimeTo())) {
            return false;
        }

        if (getStatuses() == null && toCheck.getStatuses() != null) {
            return false;
        } else if (getStatuses() != null && !getStatuses().equals(toCheck.getStatuses())) {
            return false;
        }

        if (getDocumentUniqueIds() == null && toCheck.getDocumentUniqueIds() != null) {
            return false;
        } else if (getDocumentUniqueIds() != null && !getDocumentUniqueIds().equals(toCheck.getDocumentUniqueIds())) {
            return false;
        }

        if (getEventCodeParams() == null && toCheck.getEventCodeParams() != null) {
            return false;
        } else if (getEventCodeParams() != null && !getEventCodeParams().equals(toCheck.getEventCodeParams())) {
            return false;
        }

        if (getOnDemand() != toCheck.getOnDemand()) {
            return false;
        }

        return true;
    }

}
