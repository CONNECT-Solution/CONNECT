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
package gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author jasonasmith
 */

@Entity
@Table(name = "UserRole")
public class UserRole implements Serializable {

    @Id
    @Column(name = "ROLEID")
    @GeneratedValue
    private long roleId;

    @Column(name = "ROLENAME")
    private String roleName;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userRole")
    private Set<RolePreference> preferences = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userRole")
    private Set<UserLogin> userLogins = new HashSet<>();

    /**
     *
     * @return
     */
    public long getRoleId() {
        return roleId;
    }

    /**
     *
     * @param roleId
     */
    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    /**
     *
     * @return
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     *
     * @param roleName
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     *
     * @return
     */
    public Set<RolePreference> getPreferences() {
        return preferences;
    }

    /**
     *
     * @param preferences
     */
    public void setPreferences(Set<RolePreference> preferences) {
        this.preferences = preferences;
    }

    /**
     *
     * @param userLogins
     */
    public void setUserLogin(Set<UserLogin> userLogins) {
        this.userLogins = userLogins;
    }

    /**
     *
     * @param userLogin
     */
    public void addLogin(UserLogin userLogin) {
        userLogins.add(userLogin);
    }

}
