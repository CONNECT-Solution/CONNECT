/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.admingui.dashboard;

import gov.hhs.fha.nhinc.admingui.model.User;
import java.util.List;

/**
 *
 * @author jasonasmith
 */


public interface DashboardObserver {

    /**
     * Gets all open Admin GUI dashboard panels.
     * @return
     */
    public List<DashboardPanel> getOpenDashboardPanels();

    /**
     * Closes Admin GUI dashboard panel of given class type.
     * @param c
     */
    public void closePanel(Class c);

    /**
     * Opens the given dashboard panel.
     * @param panel
     */
    public void openPanel(DashboardPanel panel);

    /**
     * Saves the given dashboard panels according to the given user.
     * @param user
     */
    public void save(User user);

    /**
     * Determines which panels should be open on application start.
     */
    public void setDefaultPanels();

    /**
     * Sets the dashboard panels according to the user settings.
     * @param user
     */
    public void setUserPanels(User user);

    /**
     * Helps determine if this is the first time the panels have been loaded.
     * @return
     */
    public boolean isStarted();

    /**
     * Refreshes the data in the panels.
     */
    public void refreshData();

    /**
     * Gets all closed dashboard panels.
     * @return
     */
    public List<DashboardPanel> getClosedDashboardPanels();

    /**
     * Opens a dashboard panel of the given class type.
     * @param c
     */
    public void openPanel(Class c);
}
