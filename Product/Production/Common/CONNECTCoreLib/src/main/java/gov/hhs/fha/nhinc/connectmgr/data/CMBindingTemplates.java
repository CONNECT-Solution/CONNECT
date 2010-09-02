/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.connectmgr.data;

import java.util.List;
import java.util.ArrayList;

/**
 * This class is used to contain the binding information for a service.
 * 
 * @author Les Westberg
 */
public class CMBindingTemplates
{

    private List<CMBindingTemplate> bindingTemplateList = new ArrayList<CMBindingTemplate>();

    /**
     * Default constructor.
     */
    public CMBindingTemplates()
    {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
        bindingTemplateList = new ArrayList<CMBindingTemplate>();
    }

    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMBindingTemplates oCompare)
    {
        if (oCompare.bindingTemplateList.size() != this.bindingTemplateList.size())
        {
            return false;
        }
        
        int iCnt = this.bindingTemplateList.size();
        for (int i = 0; i < iCnt; i++)
        {
            if (! this.bindingTemplateList.get(i).equals(oCompare.bindingTemplateList.get(i)))
            {
                return false;
            }
        }
        
        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }
    
    /**
     * Returns the list of binding template information.
     * 
     * @return The list of binding temnplate information.
     */
    public List<CMBindingTemplate> getBindingTemplate()
    {
        return bindingTemplateList;
    }
    
    /**
     * Creates a deep copy of this object.
     *
     * @return A copy of this CMBindingTemplates object
     */
    public CMBindingTemplates createCopy() {
        CMBindingTemplates templatesCopy = new CMBindingTemplates();
        for (CMBindingTemplate origTemplate : getBindingTemplate()) {
            templatesCopy.getBindingTemplate().add(origTemplate.createCopy());
        }
        return templatesCopy;
    }
}