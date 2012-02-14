/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.connectmgr.data;

import java.util.List;
import java.util.ArrayList;

/**
 * This class is used to contain the binding information for a service.
 * 
 * @author Les Westberg
 */
public class CMBindingTemplates {

    private List<CMBindingTemplate> bindingTemplateList = new ArrayList<CMBindingTemplate>();

    /**
     * Default constructor.
     */
    public CMBindingTemplates() {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear() {
        bindingTemplateList = new ArrayList<CMBindingTemplate>();
    }

    /**
     * Returns true of the contents of the object are the same as the one passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMBindingTemplates oCompare) {
        if (oCompare.bindingTemplateList.size() != this.bindingTemplateList.size()) {
            return false;
        }

        int iCnt = this.bindingTemplateList.size();
        for (int i = 0; i < iCnt; i++) {
            if (!this.bindingTemplateList.get(i).equals(oCompare.bindingTemplateList.get(i))) {
                return false;
            }
        }

        // If we got here then everything is the same...
        // ----------------------------------------------
        return true;
    }

    /**
     * Returns the list of binding template information.
     * 
     * @return The list of binding temnplate information.
     */
    public List<CMBindingTemplate> getBindingTemplate() {
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