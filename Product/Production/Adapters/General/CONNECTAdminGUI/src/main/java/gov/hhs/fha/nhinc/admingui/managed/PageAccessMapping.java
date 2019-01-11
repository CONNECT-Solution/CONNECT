/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.admingui.managed;

import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.RolePreference;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The Class PageAccessMapping.
 */
public class PageAccessMapping {

    private RolePreference preference;
    /**
     * The available access levels.
     */
    private Collection<String> availableAccessLevels;
    /**
     * The selected access level.
     */
    private String selectedAccessLevel;

    public static final String NO_ACCESS = "No Access";
    public static final String READ_ONLY = "Read Only";
    public static final String READ_WRITE = "Read Write";

    /**
     *
     * @param preference
     * @param pageAccessMapping
     */
    public PageAccessMapping(RolePreference preference, final ManageRoleBean pageAccessMapping) {
        this.preference = preference;
        availableAccessLevels = new ArrayList<>();
        availableAccessLevels.add(NO_ACCESS);
        availableAccessLevels.add(READ_ONLY);
        availableAccessLevels.add(READ_WRITE);
        setDefaultSelectedAccessLevel();
    }

    private void setDefaultSelectedAccessLevel() {
        switch (preference.getAccess()) {
        case -1:
            selectedAccessLevel = "NoAccess";
            break;
        case 0:
            selectedAccessLevel = "Read Only";
            break;
        case 1:
            selectedAccessLevel = "Read Write";
            break;
        default:
            selectedAccessLevel = "No Access";
        }
    }

    /**
     *
     * @return
     */
    public RolePreference getPreference() {
        return preference;
    }

    /**
     *
     * @param preference
     */
    public void setPreference(RolePreference preference) {
        this.preference = preference;
    }

    /**
     * Gets the available access levels.
     *
     * @return the availableAccessLevels
     */
    public Collection<String> getAvailableAccessLevels() {
        return availableAccessLevels;
    }

    /**
     * Sets the available access levels.
     *
     * @param availableAccessLevels the availableAccessLevels to set
     */
    public void setAvailableAccessLevels(Collection<String> availableAccessLevels) {
        this.availableAccessLevels = availableAccessLevels;
    }

    /**
     * Gets the selected access level.
     *
     * @return the selectedAccessLevel
     */
    public String getSelectedAccessLevel() {
        return selectedAccessLevel;
    }

    /**
     * Sets the selected access level.
     *
     * @param selectedAccessLevel the selectedAccessLevel to set
     */
    public void setSelectedAccessLevel(String selectedAccessLevel) {
        this.selectedAccessLevel = selectedAccessLevel;
    }

}
