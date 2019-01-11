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

import static gov.hhs.fha.nhinc.admingui.jee.jsf.UserAuthorizationListener.USER_INFO_SESSION_ATTRIBUTE;

import gov.hhs.fha.nhinc.admingui.display.DisplayHolder;
import gov.hhs.fha.nhinc.admingui.services.RoleService;
import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.RolePreference;
import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.UserLogin;
import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.UserRole;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The Class ManageRoleBean.
 */
@Component
@ManagedBean
@RequestScoped
public class ManageRoleBean {

    @Autowired
    private RoleService roleService;

    /**
     * The pages model.
     */
    private DataModel<PageAccessMapping> pagesModel;

    private UserRole currentRole;

    private String selectedRole;

    private final HashMap<String, UserRole> roles = new HashMap<>();

    /**
     * Access level changed.
     *
     * @param event the event
     */
    public void accessLevelChanged(final AjaxBehaviorEvent event) {
        RolePreference preference = pagesModel.getRowData().getPreference();

        int prefAccess = -1;
        String selectedAccess = pagesModel.getRowData().getSelectedAccessLevel();

        if (selectedAccess.equals(PageAccessMapping.NO_ACCESS)) {
            prefAccess = -1;
        } else if (selectedAccess.equals(PageAccessMapping.READ_ONLY)) {
            prefAccess = 0;
        } else if (selectedAccess.equals(PageAccessMapping.READ_WRITE)) {
            prefAccess = 1;
        }

        preference.setAccess(prefAccess);

        boolean updated = roleService.updatePreference(preference);

        if (updated) {
            UserRole userRole = getCurrentUser().getUserRole();
            if (preference.getUserRole().getRoleName().equals(userRole.getRoleName())) {
                for (RolePreference currPref : userRole.getPreferences()) {
                    if (currPref.getPageName().equals(preference.getPageName())) {
                        currPref.setAccess(prefAccess);
                        break;
                    }
                }
            }
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO", "Access Level Changed."));
        }
    }

    /**
     *
     * @param event
     */
    public void roleChanged(final AjaxBehaviorEvent event) {
        currentRole = roles.get(selectedRole);
        setPreferences(currentRole);
    }

    /**
     * Gets the pages.
     *
     * @return the pages
     */
    public DataModel<PageAccessMapping> getPages() {
        return pagesModel;
    }

    /**
     * Sets the pages.
     *
     * @param pages the pages to set
     */
    public void setPages(DataModel<PageAccessMapping> pages) {
        pagesModel = pages;
    }

    /**
     *
     */
    public void initData() {
        if (selectedRole == null) {

            UserLogin user = getCurrentUser();

            List<UserRole> roleList = roleService.getAllRoles();
            for (UserRole role : roleList) {
                roles.put(role.getRoleName(), role);
            }

            if (user != null) {
                currentRole = user.getUserRole();
            } else {
                currentRole = roleList.get(0);
            }

            selectedRole = currentRole.getRoleName();

            setPreferences(currentRole);
        }
    }

    private void setPreferences(UserRole role) {
        List<PageAccessMapping> mappings = new ArrayList<>();
        for (RolePreference preference : role.getPreferences()) {
            if (!DisplayHolder.getInstance().isDirectEnabled()
                    && preference.getPageName().toLowerCase().contains("direct")) {
                continue;
            }

            if (!DisplayHolder.getInstance().isFhirEnabled()
                    && preference.getPageName().toLowerCase().contains("fhir")) {
                continue;
            }

            mappings.add(new PageAccessMapping(preference, this));
        }
        pagesModel = new ListDataModel<>(mappings);
    }

    private UserLogin getCurrentUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        return (UserLogin) session.getAttribute(USER_INFO_SESSION_ATTRIBUTE);
    }

    /**
     *
     * @return
     */
    public UserRole getCurrentRole() {
        return currentRole;
    }

    /**
     *
     * @param currentRole
     */
    public void setCurrentRole(UserRole currentRole) {
        this.currentRole = currentRole;
    }

    /**
     *
     * @return
     */
    public String getSelectedRole() {
        return selectedRole;
    }

    /**
     *
     * @param selectedRole
     */
    public void setSelectedRole(String selectedRole) {
        this.selectedRole = selectedRole;
    }

    /**
     *
     * @return
     */
    public Set<String> getRoleLabels() {
        return roles.keySet();
    }

}
