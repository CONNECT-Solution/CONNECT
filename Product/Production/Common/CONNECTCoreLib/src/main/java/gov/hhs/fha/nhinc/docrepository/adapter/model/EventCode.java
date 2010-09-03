/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docrepository.adapter.model;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;

/**
 * Data class for a document event code.
 * 
 * @author Neil Webb
 */
public class EventCode
{

    private Long eventCodeId;
    private String eventCode;
    private String eventCodeScheme;
    private String eventCodeDisplayName;
    private Document document;

    public String getEventCode()
    {
        return eventCode;
    }

    public void setEventCode(String eventCode)
    {
        this.eventCode = eventCode;
    }

    public String getEventCodeDisplayName()
    {
        return eventCodeDisplayName;
    }

    public void setEventCodeDisplayName(String eventCodeDisplayName)
    {
        this.eventCodeDisplayName = eventCodeDisplayName;
    }

    public Long getEventCodeId()
    {
        return eventCodeId;
    }

    public void setEventCodeId(Long eventCodeId)
    {
        this.eventCodeId = eventCodeId;
    }

    public String getEventCodeScheme()
    {
        return eventCodeScheme;
    }

    public void setEventCodeScheme(String eventCodeScheme)
    {
        this.eventCodeScheme = eventCodeScheme;
    }

    public Document getDocument()
    {
        return document;
    }

    public void setDocument(Document document)
    {
        this.document = document;
    }

    @Override
    public int hashCode()
    {
        int hashCode = 0;
        if(eventCodeId != null)
        {
            hashCode = eventCodeId.hashCode();
        }
        else if(NullChecker.isNotNullish(eventCode))
        {
            hashCode = eventCode.hashCode();
            if(NullChecker.isNotNullish(eventCodeScheme))
            {
                hashCode += eventCodeScheme.hashCode();
            }
        }
        return hashCode;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) return false;
        if (!this.getClass().equals(obj.getClass()))
        {
            return false;
        }
        EventCode toCheck = (EventCode)obj;

        if((this.getEventCodeId() == null) && (toCheck.getEventCodeId() != null))
        {
            return false;
        }
        else if((this.getEventCodeId() != null) && (!this.getEventCodeId().equals(toCheck.getEventCodeId())))
        {
            return false;
        }

        if((this.getEventCode() == null) && (toCheck.getEventCode() != null))
        {
            return false;
        }
        else if((this.getEventCode() != null) && (!this.getEventCode().equals(toCheck.getEventCode())))
        {
            return false;
        }

        if((this.getEventCodeScheme() == null) && (toCheck.getEventCodeScheme() != null))
        {
            return false;
        }
        else if((this.getEventCodeScheme() != null) && (!this.getEventCodeScheme().equals(toCheck.getEventCodeScheme())))
        {
            return false;
        }

        if((this.getEventCodeDisplayName() == null) && (toCheck.getEventCodeDisplayName() != null))
        {
            return false;
        }
        else if((this.getEventCodeDisplayName() != null) && (!this.getEventCodeDisplayName().equals(toCheck.getEventCodeDisplayName())))
        {
            return false;
        }
        return true;
    }


}
