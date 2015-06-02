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
package gov.hhs.fha.nhinc.admingui.managed;

/**
 *
 * @author sadusumilli / jasonasmith
 */
import gov.hhs.fha.nhinc.admingui.constant.NavigationConstant;
import gov.hhs.fha.nhinc.admingui.dashboard.DashboardObserver;
import gov.hhs.fha.nhinc.admingui.dashboard.DashboardPanel;
import gov.hhs.fha.nhinc.admingui.dashboard.DashboardViewResolver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.primefaces.component.dashboard.Dashboard;
import org.primefaces.event.CloseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author jsmith2
 */
@ManagedBean(name = "dashboardBean")
@RequestScoped
@Component
public class DashboardBean {

    @Autowired
    private DashboardObserver dashboardObserver;

    @Autowired
    private DashboardViewResolver dashboardView;

    private List<String> selectedClosedPanels;

    /**
     *
     * @return
     */
    public List<String> getSelectedClosedPanels() {
        return selectedClosedPanels;
    }

    /**
     *
     * @param selectedClosedPanels
     */
    public void setSelectedClosedPanels(List<String> selectedClosedPanels) {
        this.selectedClosedPanels = selectedClosedPanels;
    }

    /**
     *
     * @return
     */
    public String addPanels() {
        List<DashboardPanel> openPanels = new ArrayList<DashboardPanel>();

        try {
            for (DashboardPanel panel : dashboardObserver.getClosedDashboardPanels()) {
                for (String type : getSelectedClosedPanels()) {
                    if (type.equals(panel.getType())) {
                        openPanels.add(panel);
                    }
                }
            }

            for (DashboardPanel panel : openPanels) {
                panel.open();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        TabBean tabs = (TabBean) FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().get("tabBean");

        if (tabs != null) {
            tabs.setDashboardTabIndex(0);
        }

        return NavigationConstant.STATUS_PAGE;
    }

    /**
     *
     * @return
     */
    public Dashboard getDashboard() {
        if (!dashboardObserver.isStarted()) {
            dashboardObserver.setDefaultPanels();
        } else {
            dashboardObserver.refreshData();
        }

        dashboardView.setView(getPanels());

        return dashboardView.getDashboard();
    }

    /**
     *
     * @param dashboard
     */
    public void setDashboard(Dashboard dashboard) {
        dashboardView.setDashboard(dashboard);
    }

    /**
     *
     * @return
     */
    public List<DashboardPanel> getPanels() {
        return dashboardObserver.getOpenDashboardPanels();
    }

    /**
     *
     * @param event
     */
    public void handleClose(CloseEvent event) {
        dashboardView.handleClose(event, getPanels());
    }

    /**
     *
     * @return
     */
    public String getAllProperties() {
        StringBuilder builder = new StringBuilder();
        Set keys = System.getProperties().keySet();

        for (Object key : keys) {
            builder.append((String) key).append(" : ")
                    .append((String) System.getProperty((String) key)).append("\n");
        }

        return builder.toString();
    }

    /**
     *
     * @return
     */
    public Map<String, String> getClosedPanels() {
        Map<String, String> closedPanels = new HashMap<String, String>();

        for (DashboardPanel panel : dashboardObserver.getClosedDashboardPanels()) {
            closedPanels.put(panel.getType(), panel.getType());
        }

        return closedPanels;
    }

}
