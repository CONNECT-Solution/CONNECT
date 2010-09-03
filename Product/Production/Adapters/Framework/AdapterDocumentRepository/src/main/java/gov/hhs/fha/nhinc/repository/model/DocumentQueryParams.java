/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.repository.model;

import java.util.Date;
import java.util.List;

/**
 * Parameter object for document queries
 * 
 * @author Neil Webb
 */
public class DocumentQueryParams
{
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

    public List<String> getClassCodes()
    {
        return classCodes;
    }

    public void setClassCodes(List<String> classCodes)
    {
        this.classCodes = classCodes;
    }

    public String getClassCodeScheme()
    {
        return classCodeScheme;
    }

    public void setClassCodeScheme(String classCodeScheme)
    {
        this.classCodeScheme = classCodeScheme;
    }

    public Date getCreationTimeFrom()
    {
        return creationTimeFrom;
    }

    public void setCreationTimeFrom(Date creationTimeFrom)
    {
        this.creationTimeFrom = creationTimeFrom;
    }

    public Date getCreationTimeTo()
    {
        return creationTimeTo;
    }

    public void setCreationTimeTo(Date creationTimeTo)
    {
        this.creationTimeTo = creationTimeTo;
    }

    public List<String> getDocumentUniqueIds()
    {
        return documentUniqueIds;
    }

    public void setDocumentUniqueId(List<String> documentUniqueIds)
    {
        this.documentUniqueIds = documentUniqueIds;
    }

    public List<String> getRepositoryIds() {
        return repositoryIds;
    }

    public void setRepositoryIds(List<String> repositoryIds) {
        this.repositoryIds = repositoryIds;
    }

    public String getPatientId()
    {
        return patientId;
    }

    public void setPatientId(String patientId)
    {
        this.patientId = patientId;
    }

    public Date getServiceStartTimeFrom()
    {
        return serviceStartTimeFrom;
    }

    public void setServiceStartTimeFrom(Date serviceStartTimeFrom)
    {
        this.serviceStartTimeFrom = serviceStartTimeFrom;
    }

    public Date getServiceStartTimeTo()
    {
        return serviceStartTimeTo;
    }

    public void setServiceStartTimeTo(Date serviceStartTimeTo)
    {
        this.serviceStartTimeTo = serviceStartTimeTo;
    }

    public Date getServiceStopTimeFrom()
    {
        return serviceStopTimeFrom;
    }

    public void setServiceStopTimeFrom(Date serviceStopTimeFrom)
    {
        this.serviceStopTimeFrom = serviceStopTimeFrom;
    }

    public Date getServiceStopTimeTo()
    {
        return serviceStopTimeTo;
    }

    public void setServiceStopTimeTo(Date serviceStopTimeTo)
    {
        this.serviceStopTimeTo = serviceStopTimeTo;
    }

    public List<String> getStatuses()
    {
        return statuses;
    }

    public void setStatuses(List<String> statuses)
    {
        this.statuses = statuses;
    }
}
