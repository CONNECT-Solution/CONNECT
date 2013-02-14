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

import gov.hhs.fha.nhinc.nhinclib.NullChecker;

/**
 * @author Les Westberg
 */
public class CMInternalConnectionInfo {
    private String homeCommunityId;
    private String name;
    private String description;
    private CMInternalConnInfoServices services;
    private CMInternalConnectionInfoStates states;

    /**
     * Default Constructor.
     */
    public CMInternalConnectionInfo() {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear() {
        homeCommunityId = "";
        name = "";
        description = "";
        services = null;
        states = null;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        if (homeCommunityId != null) {
            hashCode = homeCommunityId.hashCode();
        } else if (NullChecker.isNotNullish(name)) {
            hashCode = name.hashCode();
            if (NullChecker.isNotNullish(description)) {
                hashCode += description.hashCode();
            }
        }
        return hashCode;
    }

    /**
     * Returns true of the contents of the object are the same as the one passed in.
     * 
     * @param object The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(Object object) {
        
        if (!(object instanceof CMInternalConnectionInfo))
            return false;
        CMInternalConnectionInfo oCompare = (CMInternalConnectionInfo) object;   
        boolean descMatch = false;
        boolean nameMatch = false;
        boolean hcidMatch = false;
        boolean serviceMatch = false;
        boolean stateMatch = false;
        boolean result = false;

        // Compare the names
        if (NullChecker.isNullish(oCompare.name) && NullChecker.isNullish(this.name)) {
            nameMatch = true;
        } else if (NullChecker.isNullish(oCompare.name) || NullChecker.isNullish(this.name)) {
            nameMatch = false;
        } else {
            if (this.name.equalsIgnoreCase(oCompare.name)) {
                nameMatch = true;
            }
        }

        // Compare the description
        if (NullChecker.isNullish(oCompare.description) && NullChecker.isNullish(this.description)) {
            descMatch = true;
        } else if (NullChecker.isNullish(oCompare.description) || NullChecker.isNullish(this.description)) {
            descMatch = false;
        } else {
            if (this.description.equalsIgnoreCase(oCompare.description)) {
                descMatch = true;
            }
        }

        // Compare the home community id
        if (NullChecker.isNullish(oCompare.homeCommunityId) && NullChecker.isNullish(this.homeCommunityId)) {
            hcidMatch = true;
        } else if (NullChecker.isNullish(oCompare.homeCommunityId) || NullChecker.isNullish(this.homeCommunityId)) {
            hcidMatch = false;
        } else {
            if (this.homeCommunityId.equalsIgnoreCase(oCompare.homeCommunityId)) {
                hcidMatch = true;
            }
        }

        // Compare the services
        if (oCompare.services == null && this.services == null) {
            serviceMatch = true;
        } else if (oCompare.services == null || this.services == null) {
            serviceMatch = false;
        } else {
            if (this.services.equals(oCompare.services)) {
                serviceMatch = true;
            }
        }

        // Compare the states
        if (oCompare.states == null && this.states == null) {
            stateMatch = true;
        } else if (oCompare.states == null || this.states == null) {
            stateMatch = false;
        } else {
            if (this.states.equals(oCompare.states)) {
                stateMatch = true;
            }
        }

        if (descMatch == true && nameMatch == true && hcidMatch == true && serviceMatch == true && stateMatch == true) {
            result = true;
        } else {
            result = false;
        }

        return result;
    }

    /**
     * Return the description of the connection.
     * 
     * @return The description of the connection.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the connection.
     * 
     * @param description The description of the connection.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Return the home community ID associated with this connection.
     * 
     * @return The home community ID associated with this connection.
     */
    public String getHomeCommunityId() {
        return homeCommunityId;
    }

    /**
     * Set the home community ID associated with this connection.
     * 
     * @param homeCommunityId The home community ID associated with this connection.
     */
    public void setHomeCommunityId(String homeCommunityId) {
        this.homeCommunityId = homeCommunityId;
    }

    /**
     * Return the name of this home community.
     * 
     * @return The name of this home community.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this home community.
     * 
     * @param name The name of this home community.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return the services associated with this home community.
     * 
     * @return The services associated with this home community.
     */
    public CMInternalConnInfoServices getServices() {
        return services;
    }

    /**
     * Sets the services associated with this home community.
     * 
     * @param services The services associated with this home community.
     */
    public void setServices(CMInternalConnInfoServices services) {
        this.services = services;
    }

    /**
     * Return the states associated with this home community.
     * 
     * @return The states associated with this home community.
     */
    public CMInternalConnectionInfoStates getStates() {
        return states;
    }

    /**
     * Sets the states associated with this home community.
     * 
     * @param states The states associated with this home community.
     */
    public void setStates(CMInternalConnectionInfoStates states) {
        this.states = states;
    }

}