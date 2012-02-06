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

/**
 * This class represents a single business service for a business entity.
 * 
 * @author Les Westberg
 */
public class CMBusinessService {

    private String serviceKey = "";
    private CMBindingNames names = null;
    private CMBindingDescriptions descriptions = null;
    private String uniformServiceName = "";
    private boolean internalWebService = false;
    private CMBindingTemplates bindingTemplates = null;

    /**
     * Default constructor.
     */
    public CMBusinessService() {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear() {
        uniformServiceName = "";
        internalWebService = false;
        serviceKey = "";
        names = null;
        descriptions = null;
        bindingTemplates = null;
    }

    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMBusinessService oCompare) {
        if ((!this.uniformServiceName.equals(oCompare.uniformServiceName)) ||
                (this.internalWebService != oCompare.internalWebService) ||
                (!this.serviceKey.equals(oCompare.serviceKey)) ||
                (!this.names.equals(oCompare.names)) ||
                (!this.descriptions.equals(oCompare.descriptions)) ||
                (!this.bindingTemplates.equals(oCompare.bindingTemplates)))
        {
            return false;
        }

        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }

    /**
     * Returns the binding information for this service.
     * 
     * @return The binding information for this service.
     */
    public CMBindingTemplates getBindingTemplates() {
        return bindingTemplates;
    }

    /**
     * Sets the binding information for this service.
     * 
     * @param bindingTemplates The binding information for this service.
     */
    public void setBindingTemplates(CMBindingTemplates bindingTemplates) {
        this.bindingTemplates = bindingTemplates;
    }

    /**
     * Returns the binding description information for this service.
     * 
     * @return The binding description information for this service.
     */
    public CMBindingDescriptions getDescriptions() {
        return descriptions;
    }

    /**
     * Sets the binding description information for this service.
     * 
     * @param descriptions The binding description information for this service.
     */
    public void setDescriptions(CMBindingDescriptions descriptions) {
        this.descriptions = descriptions;
    }

    /**
     * Returns true if this web service is internal to this gateway false if it is
     * exposed to the NHIN.
     * 
     * @return True if this web service is internal to this gateway and false if it
     *         is exposed to the NHIN.
     */
    public boolean isInternalWebService() {
        return internalWebService;
    }

    /**
     * Set to true if this web service is internal to this gateway false if it is
     * exposed to the NHIN.
     * 
     * @param internalWebService True if this web service is internal to this gateway and false if it
     *         is exposed to the NHIN.
     */
    public void setInternalWebService(boolean internalWebService) {
        this.internalWebService = internalWebService;
    }

    /**
     * Return the binding names for this service.
     * 
     * @return The binding names for this service.
     */
    public CMBindingNames getNames() {
        return names;
    }

    /**
     * Sets the binding names for this service.
     * 
     * @return The binding names for this service.
     */
    public void setNames(CMBindingNames names) {
        this.names = names;
    }

    /**
     * Return the service key for this service.
     * 
     * @return The service key for this service.
     */
    public String getServiceKey() {
        return serviceKey;
    }

    /**
     * Sets the service key for this service.
     * 
     * @param serviceKey The service key for this service.
     */
    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    /**
     * Returns the uniform service name for this service.
     * 
     * @return The uniform service name for this service.
     */
    public String getUniformServiceName() {
        return uniformServiceName;
    }

    /**
     * Sets the uniform service name for this service.
     * 
     * @param uniformServiceName The uniform service name for this service.
     */
    public void setUniformServiceName(String uniformServiceName) {
        this.uniformServiceName = uniformServiceName;
    }

    /**
     * Creates a deep copy of this business service.
     *
     * @return A copy of this service that includes copies of data structures
     */
    public CMBusinessService createCopy() {
        CMBusinessService serviceCopy = new CMBusinessService();

        if (bindingTemplates != null) {
            serviceCopy.setBindingTemplates(bindingTemplates.createCopy());
        } else {
            serviceCopy.setBindingTemplates(null);
        }

        if (descriptions != null) {
            serviceCopy.setDescriptions(descriptions.createCopy());
        } else {
            serviceCopy.setDescriptions(null);
        }

        serviceCopy.setInternalWebService(internalWebService);

        if (names != null) {
            serviceCopy.setNames(names.createCopy());
        } else {
            serviceCopy.setNames(null);
        }

        serviceCopy.setServiceKey(serviceKey);

        serviceCopy.setUniformServiceName(uniformServiceName);

        return serviceCopy;
    }
}
