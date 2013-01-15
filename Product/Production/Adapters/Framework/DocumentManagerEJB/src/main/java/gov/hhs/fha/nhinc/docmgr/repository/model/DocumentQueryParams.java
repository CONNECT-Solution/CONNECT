/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.

 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information. *
 * All other rights reserved.
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
package gov.hhs.fha.nhinc.docmgr.repository.model;

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
    private List<String> repositoryIds;

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
        return creationTimeFrom;
    }

    public void setCreationTimeFrom(Date creationTimeFrom) {
        this.creationTimeFrom = creationTimeFrom;
    }

    public Date getCreationTimeTo() {
        return creationTimeTo;
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

    public List<String> getRepositoryIds() {
        return repositoryIds;
    }

    public void setRepositoryIds(List<String> repositoryIds) {
        this.repositoryIds = repositoryIds;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Date getServiceStartTimeFrom() {
        return serviceStartTimeFrom;
    }

    public void setServiceStartTimeFrom(Date serviceStartTimeFrom) {
        this.serviceStartTimeFrom = serviceStartTimeFrom;
    }

    public Date getServiceStartTimeTo() {
        return serviceStartTimeTo;
    }

    public void setServiceStartTimeTo(Date serviceStartTimeTo) {
        this.serviceStartTimeTo = serviceStartTimeTo;
    }

    public Date getServiceStopTimeFrom() {
        return serviceStopTimeFrom;
    }

    public void setServiceStopTimeFrom(Date serviceStopTimeFrom) {
        this.serviceStopTimeFrom = serviceStopTimeFrom;
    }

    public Date getServiceStopTimeTo() {
        return serviceStopTimeTo;
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
}
