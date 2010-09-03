/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docrepository.adapter.model;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;

/**
 * Query parameter for a single event code.
 *
 * @author Neil Webb
 */
public class EventCodeParam
{

    private String eventCode;
    private String eventCodeScheme;

    public String getEventCode()
    {
        return eventCode;
    }

    public void setEventCode(String eventCode)
    {
        this.eventCode = eventCode;
    }

    public String getEventCodeScheme()
    {
        return eventCodeScheme;
    }

    public void setEventCodeScheme(String eventCodeScheme)
    {
        this.eventCodeScheme = eventCodeScheme;
    }

    @Override
    public int hashCode()
    {
        int hashCode = 0;
        if(NullChecker.isNotNullish(eventCode))
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
        EventCodeParam toCheck = (EventCodeParam)obj;

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
        return true;
    }


}
