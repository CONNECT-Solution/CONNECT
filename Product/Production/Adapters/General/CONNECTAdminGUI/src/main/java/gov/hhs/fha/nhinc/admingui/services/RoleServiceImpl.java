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
package gov.hhs.fha.nhinc.admingui.services;

import gov.hhs.fha.nhinc.admingui.constant.NavigationConstant;
import gov.hhs.fha.nhinc.admingui.hibernate.dao.UserLoginDAO;
import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.RolePreference;
import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.UserLogin;
import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.UserRole;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jasonasmith
 */
@Service
public class RoleServiceImpl implements RoleService {

    private static final long serialVersionUID = -7778943978909089913L;
    @Autowired
    private transient UserLoginDAO userDAO;

    /**
     * Default constructor.
     */
    public RoleServiceImpl() {

    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.services.RoleService#checkRole(String, gov.hhs.fha
     * .nhinc.admingui.services.persistence.jpa.entity.UserLogin)
     */
    @Override
    public boolean checkRole(String pageName, UserLogin user) {
        if (user != null && user.getUserRole() != null && user.getUserRole().getPreferences() != null) {
            if (pageName.equalsIgnoreCase(NavigationConstant.STATUS_PAGE.concat(".xhtml"))) {
                return true;
            }

            for (RolePreference preference : user.getUserRole().getPreferences()) {
                if (preference.getPageName().equalsIgnoreCase(pageName)) {
                    return preference.getAccess() >= 0;
                }
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.services.RoleService#getAllRoles()
     */
    @Override
    public List<UserRole> getAllRoles() {
        return userDAO.getAllRoles();
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.services.RoleService#updatePreference(gov.hhs.fha
     * .nhinc.admingui.services.persistence.jpa.entity.RolePreference)
     */
    @Override
    public boolean updatePreference(RolePreference preference) {
        return userDAO.updatePreference(preference);
    }

}
