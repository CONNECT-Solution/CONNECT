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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author jasonasmith
 */
@Entity
@Table(name = "PagePreference")
public class RolePreference implements Serializable {

    @Id
    @Column(name = "PREFID")
    @GeneratedValue
    private long id;

    @Column(name = "PAGENAME")
    private String pageName;

    @Column(name = "PAGEDESC")
    private String pageDesc;

    @Column(name = "ACCESSPAGE")
    private int access;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prefRoleId")
    private UserRole userRole;

    /**
     *
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getPageName() {
        return pageName;
    }

    /**
     *
     * @param pageName
     */
    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    /**
     *
     * @return
     */
    public String getPageDesc() {
        return pageDesc;
    }

    /**
     *
     * @param pageDesc
     */
    public void setPageDesc(String pageDesc) {
        this.pageDesc = pageDesc;
    }

    /**
     *
     * @return
     */
    public int getAccess() {
        return access;
    }

    /**
     *
     * @param access
     */
    public void setAccess(int access) {
        this.access = access;
    }

    /**
     *
     * @return
     */
    public UserRole getUserRole() {
        return userRole;
    }

    /**
     *
     * @param userRole
     */
    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

}
