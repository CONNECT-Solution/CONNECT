/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.repository.model;

/**
 * Data class for a document extra slot.
 * 
 * @author Chrisjan Matser
 */
public class ExtraSlot
{

    private Long extraSlotId;
    private String extraSlotName;
    private String extraSlotValue;
    private Document document;

    public String getExtraSlotName()
    {
        return extraSlotName;
    }

    public void setExtraSlotName(String extraSlotName)
    {
        this.extraSlotName = extraSlotName;
    }

    public String getExtraSlotValue()
    {
        return extraSlotValue;
    }

    public void setExtraSlotValue(String extraSlotValue)
    {
        this.extraSlotValue = extraSlotValue;
    }

    public Long getExtraSlotId()
    {
        return extraSlotId;
    }

    public void setExtraSlotId(Long extraSlotId)
    {
        this.extraSlotId = extraSlotId;
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
