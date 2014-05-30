/*
 *  Copyright (c) 2009-2014, United States Government, as represented by the Secretary of Health and Human Services.
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *      * Redistributions of source code must retain the above
 *        copyright notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the documentation
 *        and/or other materials provided with the distribution.
 *      * Neither the name of the United States Government nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.admingui.managed;

import gov.hhs.fha.nhinc.admingui.model.Role;
import gov.hhs.fha.nhinc.admingui.services.ManageRoleService;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The Class ManageRoleBean.
 */
@ManagedBean
@ViewScoped
@Component
public class ManageRoleBean {

    @Autowired
    private ManageRoleService manageRoleService;

    private Set<String> roles;

    private Role selectedRole;

    public ManageRoleBean() {

    }

    public void roleChanged(final AjaxBehaviorEvent event) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Role Changed."));
    }

    /**
     * @return the selectedRole
     */
    public String getSelectedRoleName() {
        return selectedRole != null ? selectedRole.getName() : "";
    }

    /**
     * @param selectedRole the selectedRole to set
     */
    public void setSelectedRoleName(String selectedRole) {
        if (roles.contains(selectedRole)) {
            for (Role r : manageRoleService.getRoles()) {
                if (r.getName().equals(selectedRole)) {
                    this.selectedRole = r;
                }
            }
        } else {
            this.selectedRole = null;
        }

    }

    public Role getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(Role role) {
        selectedRole = role;
    }

    /**
     * Access level changed.
     * 
     * @param event the event
     */
    public void accessLevelChanged(final AjaxBehaviorEvent event) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Access Level Changed."));
    }

    /**
     * Inits the.
     */
    @PostConstruct
    public void init() {
        roles = new HashSet<String>();
        for (Role r : manageRoleService.getRoles()) {
            roles.add(r.getName());
        }
    }

    /**
     * @return the roles
     */
    public Set<String> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

}