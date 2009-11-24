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
    private List<EventCodeParam> eventCodeParams;

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

    public List<EventCodeParam> getEventCodeParams()
    {
        return eventCodeParams;
    }

    public void setEventCodeParams(List<EventCodeParam> eventCodeParams)
    {
        this.eventCodeParams = eventCodeParams;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) return false;
        if (!this.getClass().equals(obj.getClass()))
        {
            return false;
        }
        DocumentQueryParams toCheck = (DocumentQueryParams)obj;

        if((this.getPatientId() == null) && (toCheck.getPatientId() != null))
        {
            return false;
        }
        else if((this.getPatientId() != null) && (!this.getPatientId().equals(toCheck.getPatientId())))
        {
            return false;
        }

        if((this.getClassCodes() == null) && (toCheck.getClassCodes() != null))
        {
            return false;
        }
        else if((this.getClassCodes() != null) && (!this.getClassCodes().equals(toCheck.getClassCodes())))
        {
            return false;
        }

        if((this.getClassCodeScheme() == null) && (toCheck.getClassCodeScheme() != null))
        {
            return false;
        }
        else if((this.getClassCodeScheme() != null) && (!this.getClassCodeScheme().equals(toCheck.getClassCodeScheme())))
        {
            return false;
        }

        if((this.getCreationTimeFrom() == null) && (toCheck.getCreationTimeFrom() != null))
        {
            return false;
        }
        else if((this.getCreationTimeFrom() != null) && (!this.getCreationTimeFrom().equals(toCheck.getCreationTimeFrom())))
        {
            return false;
        }

        if((this.getCreationTimeTo() == null) && (toCheck.getCreationTimeTo() != null))
        {
            return false;
        }
        else if((this.getCreationTimeTo() != null) && (!this.getCreationTimeTo().equals(toCheck.getCreationTimeTo())))
        {
            return false;
        }

        if((this.getServiceStartTimeFrom() == null) && (toCheck.getServiceStartTimeFrom() != null))
        {
            return false;
        }
        else if((this.getServiceStartTimeFrom() != null) && (!this.getServiceStartTimeFrom().equals(toCheck.getServiceStartTimeFrom())))
        {
            return false;
        }

        if((this.getServiceStartTimeTo() == null) && (toCheck.getServiceStartTimeTo() != null))
        {
            return false;
        }
        else if((this.getServiceStartTimeTo() != null) && (!this.getServiceStartTimeTo().equals(toCheck.getServiceStartTimeTo())))
        {
            return false;
        }

        if((this.getServiceStopTimeFrom() == null) && (toCheck.getServiceStopTimeFrom() != null))
        {
            return false;
        }
        else if((this.getServiceStopTimeFrom() != null) && (!this.getServiceStopTimeFrom().equals(toCheck.getServiceStopTimeFrom())))
        {
            return false;
        }

        if((this.getServiceStopTimeTo() == null) && (toCheck.getServiceStopTimeTo() != null))
        {
            return false;
        }
        else if((this.getServiceStopTimeTo() != null) && (!this.getServiceStopTimeTo().equals(toCheck.getServiceStopTimeTo())))
        {
            return false;
        }

        if((this.getStatuses() == null) && (toCheck.getStatuses() != null))
        {
            return false;
        }
        else if((this.getStatuses() != null) && (!this.getStatuses().equals(toCheck.getStatuses())))
        {
            return false;
        }

        if((this.getDocumentUniqueIds() == null) && (toCheck.getDocumentUniqueIds() != null))
        {
            return false;
        }
        else if((this.getDocumentUniqueIds() != null) && (!this.getDocumentUniqueIds().equals(toCheck.getDocumentUniqueIds())))
        {
            return false;
        }

        if((this.getEventCodeParams() == null) && (toCheck.getEventCodeParams() != null))
        {
            return false;
        }
        else if((this.getEventCodeParams() != null) && (!this.getEventCodeParams().equals(toCheck.getEventCodeParams())))
        {
            return false;
        }

        return true;
    }



    
}
