/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.repository.model;

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
}
