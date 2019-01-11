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
package gov.hhs.fha.nhinc.admingui.jee.jsf;

import gov.hhs.fha.nhinc.admingui.constant.NavigationConstant;
import gov.hhs.fha.nhinc.admingui.display.DisplayHolder;
import gov.hhs.fha.nhinc.admingui.services.RoleService;
import gov.hhs.fha.nhinc.admingui.services.RoleServiceImpl;
import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.UserLogin;
import java.util.HashSet;
import java.util.Set;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * The listener interface for receiving userAuthorization events. The class that is interested in processing a
 * userAuthorization event implements this interface, and the object created with that class is registered with a
 * component using the component's <code>addUserAuthorizationListener</code> method. When the userAuthorization event
 * occurs, that object's appropriate method is invoked.
 *
 * @author msw
 */
@Component
public class UserAuthorizationListener implements PhaseListener {

    private static final Logger LOG = LoggerFactory.getLogger(UserAuthorizationListener.class);

    /**
     * The Constant NO_LOGIN_REQUIRED_PAGES
     */
    protected static final Set<String> NO_LOGIN_REQUIRED_PAGES = new HashSet<>();

    /**
     * The Constant USER_INFO_SESSION_ATTRIBUTE.
     */
    public static final String USER_INFO_SESSION_ATTRIBUTE = "userInfo";

    private final RoleService roleService = new RoleServiceImpl();

    /**
     * Serial version required for Serializable interface.
     */
    private static final long serialVersionUID = 4891265644965340362L;

    /**
     *
     */
    public UserAuthorizationListener() {
        NO_LOGIN_REQUIRED_PAGES.add(NavigationConstant.LOGIN_XHTML);
        NO_LOGIN_REQUIRED_PAGES.add(NavigationConstant.CUSTOM_ERROR_XHTML);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.event.PhaseListener#afterPhase(javax.faces.event.PhaseEvent)
     */
    @Override
    public void afterPhase(PhaseEvent event) {

        FacesContext facesContext = event.getFacesContext();
        if (facesContext != null && facesContext.getViewRoot() != null) {
            String currentPage = facesContext.getViewRoot().getViewId();
            LOG.debug("current page: ".concat(currentPage));
            HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
            UserLogin currentUser = null;
            if (session != null) {
                currentUser = (UserLogin) session.getAttribute(USER_INFO_SESSION_ATTRIBUTE);
            }
            validateCsrfToken(event);
            if (!NO_LOGIN_REQUIRED_PAGES.contains(currentPage) && currentUser == null) {
                LOG.debug("login required and current user is null, redirecting to login page.");
                NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
                nh.handleNavigation(facesContext, null, NavigationConstant.LOGIN_PAGE);
            } else {
                boolean hasRolePermission = roleService.checkRole(formatPageName(currentPage), currentUser);
                boolean isConfigured = checkConfiguredDisplay(formatPageName(currentPage));
                // route to status page if user doesn't have permission to view or missing configuration
                if (currentUser != null && (!hasRolePermission || !isConfigured)) {
                    LOG.debug("User, " + currentUser.getUserName() + " can not access given page: " + currentPage);
                    NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
                    nh.handleNavigation(facesContext, null, NavigationConstant.STATUS_PAGE);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.event.PhaseListener#beforePhase(javax.faces.event.PhaseEvent)
     */
    @Override
    public void beforePhase(PhaseEvent arg0) {
        // Do nothing.
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.event.PhaseListener#getPhaseId()
     */
    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    private String formatPageName(String pageName) {
        if (pageName.startsWith("/")) {
            return pageName.substring(1, pageName.length()).toLowerCase();
        } else {
            return pageName.toLowerCase();
        }
    }

    private boolean checkConfiguredDisplay(String currentPage) {
        if (currentPage.equalsIgnoreCase(NavigationConstant.DIRECT_XHTML)) {
            return DisplayHolder.getInstance().isDirectEnabled();
        } else if (currentPage.equalsIgnoreCase(NavigationConstant.FHIR_XHTML)) {
            return DisplayHolder.getInstance().isFhirEnabled();
        }
        return true;
    }

    /**
     * @param event
     *
     *            when a transition happens from status.xhtml to properties.xhtml the csrf token will be validated for
     *            status.xhtml and afterPhase method will be again executed and it will try to verify the token for
     *            properties.xhtml.The validation is now performed by checking if token is null and ignore the
     *            transitioned page. Below "if" method can be re-written in a way so that if token is null and the phase
     *            of transition can be checked it will be safer to ignore that validation.
     */
    public void validateCsrfToken(PhaseEvent event) {
        String csrfToken = event.getFacesContext().getExternalContext().getRequestParameterMap().get("csrfToken");
        if (csrfToken != null) {
            LOG.debug("csrfToken from http Request parameter is not null");
            if (csrfToken.equals(event.getFacesContext().getExternalContext().getSessionMap().get("salt"))) {
                LOG.debug("CSRF Token successfully validated");
            } else {
                LOG.error("The Session is invalidated. User will be re-directed to Login Page");
                event.getFacesContext().getExternalContext().invalidateSession();
                NavigationHandler nh = event.getFacesContext().getApplication().getNavigationHandler();
                nh.handleNavigation(event.getFacesContext(), null, NavigationConstant.LOGIN_PAGE);
            }
        }
    }
}
