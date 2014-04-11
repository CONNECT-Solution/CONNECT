/*******************************************************************************
 * Copyright 2013 The California Health and Human Services Agency (CHHS). All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"), you may not use this file except in compliance with the License. You may obtain a copy of the License at: http://www.apache.org/licenses/LICENSE-2.0.
 * Unless required by applicable law or agreed to in writing, content (including but not limited to software, documentation, information, and all other works distributed under the License) is distributed on an "AS IS" BASIS, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE CONTENT OR THE USE OR OTHER DEALINGS IN THE CONTENT. IN NO EVENT SHALL CHHS HAVE ANY OBLIGATION TO PROVIDE SUPPORT, UPDATES, MODIFICATIONS, AND/OR UPGRADES FOR CONTENT. See the License for the specific language governing permissions and limitations under the License.
 * This publication/product was made possible by Award Number 90HT0029 from Office of the National Coordinator for Health Information Technology (ONC), U.S. Department of Health and Human Services. Its contents are solely the responsibility of the authors and do not necessarily represent the official views of ONC or the State of California.
 ******************************************************************************/
/**
 * 
 */
package gov.hhs.fha.nhinc.admingui.jee.jsf;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving userAuthorization events. The class that is interested in processing a
 * userAuthorization event implements this interface, and the object created with that class is registered with a
 * component using the component's <code>addUserAuthorizationListener<code> method. When
 * the userAuthorization event occurs, that object's appropriate
 * method is invoked.
 * 
 * @author msw
 */
public class UserAuthorizationListener implements PhaseListener {

    private static final Logger LOG = Logger.getLogger(UserAuthorizationListener.class);

    /** The Constant LOGIN_REQUIRED_DIR. */
    public static List<String> noLoginRequiredPages = null;

    /** The Constant USER_INFO_SESSION_ATTRIBUTE. */
    public static final String USER_INFO_SESSION_ATTRIBUTE = "userInfo";

    /** The Constant LOGIN_PAGE_NAV_OUTCOME. */
    public static final String LOGIN_PAGE_NAV_OUTCOME = "Login";

    /**
     * Serial version required for Serializable interface.
     * 
     */
    private static final long serialVersionUID = 4891265644965340362L;

    public UserAuthorizationListener() {
        noLoginRequiredPages = new ArrayList<String>();
        noLoginRequiredPages.add("/Login.xhtml");
        noLoginRequiredPages.add("/Index.xhtml");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.event.PhaseListener#afterPhase(javax.faces.event.PhaseEvent)
     */
    @Override
    public void afterPhase(PhaseEvent event) {
        FacesContext facesContext = event.getFacesContext();
        String currentPage = facesContext.getViewRoot().getViewId();
        LOG.debug("current page: ".concat(currentPage));

        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        Object currentUser = null;
        if (session != null) {
            currentUser = session.getAttribute(USER_INFO_SESSION_ATTRIBUTE);
        }

        if (!noLoginRequiredPages.contains(currentPage) && currentUser == null) {
            LOG.debug("login required and current user is null, redirecting to login page.");
            NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
            nh.handleNavigation(facesContext, null, LOGIN_PAGE_NAV_OUTCOME);
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

}
