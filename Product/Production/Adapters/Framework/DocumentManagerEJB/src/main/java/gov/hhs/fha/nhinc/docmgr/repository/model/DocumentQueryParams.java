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
 * Parameter object for document queries.
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

    /**
     *
     * @return classCodes as List<String>
     */
    public List<String> getClassCodes() {
        return classCodes;
    }

    /**
     *
     * @param classCodes as List<String>
     */
    public void setClassCodes(List<String> classCodes) {
        this.classCodes = classCodes;
    }

    /**
     *
     * @return classCodeScheme as String
     */
    public String getClassCodeScheme() {
        return classCodeScheme;
    }

    /**
     *
     * @param classCodeScheme as String
     */
    public void setClassCodeScheme(String classCodeScheme) {
        this.classCodeScheme = classCodeScheme;
    }

    /**
     *
     * @return creationTimeFrom as Date
     */
    public Date getCreationTimeFrom() {
        return creationTimeFrom;
    }

    /**
     *
     * @param creationTimeFrom as Date
     */
    public void setCreationTimeFrom(Date creationTimeFrom) {
        this.creationTimeFrom = creationTimeFrom;
    }

    /**
     *
     * @return creationTimeTo as Date
     */
    public Date getCreationTimeTo() {
        return creationTimeTo;
    }

    /**
     *
     * @param creationTimeTo as Date
     */
    public void setCreationTimeTo(Date creationTimeTo) {
        this.creationTimeTo = creationTimeTo;
    }

    /**
     *
     * @return documentUniqueIds as List<String>
     */
    public List<String> getDocumentUniqueIds() {
        return documentUniqueIds;
    }

    /**
     *
     * @param pDocumentUniqueIds as List<String>
     */
    public void setDocumentUniqueId(List<String> pDocumentUniqueIds) {
        this.documentUniqueIds = pDocumentUniqueIds;
    }

    /**
     *
     * @return repositoryIds as List<String>
     */
    public List<String> getRepositoryIds() {
        return repositoryIds;
    }

    /**
     *
     * @param repositoryIds as List<String>
     */
    public void setRepositoryIds(List<String> repositoryIds) {
        this.repositoryIds = repositoryIds;
    }

    /**
     *
     * @return patientId as String
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     *
     * @param patientId as String
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     *
     * @return serviceStartTimeFrom as Date
     */
    public Date getServiceStartTimeFrom() {
        return serviceStartTimeFrom;
    }

    /**
     *
     * @param serviceStartTimeFrom as Date
     */
    public void setServiceStartTimeFrom(Date serviceStartTimeFrom) {
        this.serviceStartTimeFrom = serviceStartTimeFrom;
    }

    /**
     *
     * @return serviceStartTimeTo as Date
     */
    public Date getServiceStartTimeTo() {
        return serviceStartTimeTo;
    }

    /**
     *
     * @param serviceStartTimeTo as Date
     */
    public void setServiceStartTimeTo(Date serviceStartTimeTo) {
        this.serviceStartTimeTo = serviceStartTimeTo;
    }

    /**
     *
     * @return serviceStopTimeFrom as Date
     */
    public Date getServiceStopTimeFrom() {
        return serviceStopTimeFrom;
    }

    /**
     *
     * @param serviceStopTimeFrom as Date
     */
    public void setServiceStopTimeFrom(Date serviceStopTimeFrom) {
        this.serviceStopTimeFrom = serviceStopTimeFrom;
    }

    /**
     *
     * @return serviceStopTimeTo as Date
     */
    public Date getServiceStopTimeTo() {
        return serviceStopTimeTo;
    }

    /**
     *
     * @param serviceStopTimeTo as Date
     */
    public void setServiceStopTimeTo(Date serviceStopTimeTo) {
        this.serviceStopTimeTo = serviceStopTimeTo;
    }

    /**
     *
     * @return statuses as List<String>
     */
    public List<String> getStatuses() {
        return statuses;
    }

    /**
     *
     * @param statuses as List<String>
     */
    public void setStatuses(List<String> statuses) {
        this.statuses = statuses;
    }
}
