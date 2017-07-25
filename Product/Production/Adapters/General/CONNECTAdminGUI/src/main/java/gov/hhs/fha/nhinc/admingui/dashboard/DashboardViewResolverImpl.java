/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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

import java.util.List;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.application.Application;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import org.primefaces.component.behavior.ajax.AjaxBehavior;
import org.primefaces.component.behavior.ajax.AjaxBehaviorListenerImpl;
import org.primefaces.component.dashboard.Dashboard;
import org.primefaces.component.panel.Panel;
import org.primefaces.event.CloseEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.springframework.stereotype.Service;

/**
 *
 * @author jasonasmith
 */

@Service
public class DashboardViewResolverImpl implements DashboardViewResolver {

    public static final int DEFAULT_COLUMN_COUNT = 4;
    private int columnCount = DEFAULT_COLUMN_COUNT;

    private Dashboard dashboard;

    private static final String DASHBOARD_ID = "admingui_dashboard";
    private static final String DASHBOARD_CLASS = "org.primefaces.component.Dashboard";
    private static final String DASHBOARD_RENDERER_CLASS = "org.primefaces.component.DashboardRenderer";
    private static final String PANEL_CLASS = "org.primefaces.component.Panel";
    private static final String PANEL_RENDERER_CLASS = "org.primefaces.component.PanelRenderer";

    private MethodExpression closeExpp;
    private static final String CLOSE_EXPRESSION_VALUE = "#{dashboardBean.handleClose}";

    /**
     *
     */
    public DashboardViewResolverImpl() {

    }

    /**
     *
     * @param panelDataList
     */
    @Override
    public void setView(List<DashboardPanel> panelDataList) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application application = fc.getApplication();

        dashboard = (Dashboard) application.createComponent(fc, DASHBOARD_CLASS, DASHBOARD_RENDERER_CLASS);
        dashboard.setId(DASHBOARD_ID);

        DashboardModel model = getModel();
        dashboard.setModel(model);

        int i = 1;
        for (DashboardPanel panelData : panelDataList) {
            Panel panel = getPanel(panelData, application, fc);
            int mod = i % getColumnCount();
            DashboardColumn column = model.getColumn(mod);
            column.addWidget(panel.getId());

            getDashboard().getChildren().add(panel);
            i++;
        }
    }

    /**
     *
     * @return
     */
    @Override
    public Dashboard getDashboard() {
        return dashboard;
    }

    /**
     *
     * @param dashboard
     */
    @Override
    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

    /**
     *
     * @return
     */
    public int getColumnCount() {
        return columnCount;
    }

    /**
     *
     * @param columnCount
     */
    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    /**
     *
     * @param event
     * @param panels
     */
    @Override
    public void handleClose(CloseEvent event, List<DashboardPanel> panels) {
        String id = event.getComponent().getId();

        for (DashboardPanel panel : panels) {
            if (id.equalsIgnoreCase(panel.getType().replace(" ", "_"))) {
                panel.close();
                break;
            }
        }
    }

    private DashboardModel getModel() {
        DashboardModel model = new DefaultDashboardModel();

        for (int i = 0, n = getColumnCount(); i < n; i++) {
            DashboardColumn column = new DefaultDashboardColumn();
            model.addColumn(column);
        }
        return model;
    }

    private Panel getPanel(DashboardPanel panelData, Application application, FacesContext fc) {
        Panel panel = (Panel) application.createComponent(fc, PANEL_CLASS, PANEL_RENDERER_CLASS);

        panel.setId(panelData.getType().replace(" ", "_").toLowerCase());
        panel.setHeader(panelData.getType().toUpperCase());
        panel.setClosable(true);

        addTitle(panel, panelData);
        addDescription(panel, panelData);

        panel.addClientBehavior("close", getAjaxBehavior());

        return panel;
    }

    private void addTitle(Panel panel, DashboardPanel panelData) {
        if (panelData.getTitle() != null && !panelData.getTitle().isEmpty()) {
            HtmlOutputText title = new HtmlOutputText();
            title.setValue(panelData.getTitle());
            panel.getChildren().add(title);
        }
    }

    private void addDescription(Panel panel, DashboardPanel panelData) {
        if (panelData.getDescription() != null && !panelData.getDescription().isEmpty()) {
            HtmlOutputText description = new HtmlOutputText();
            description.setValue("<br>" + panelData.getDescription());
            description.setEscape(false);

            panel.getChildren().add(description);
        }
    }

    private AjaxBehavior getAjaxBehavior() {
        AjaxBehavior ajaxBehavior = new AjaxBehavior();
        ajaxBehavior.addAjaxBehaviorListener(new AjaxBehaviorListenerImpl(getCloseExpression(), getCloseExpression()));
        return ajaxBehavior;
    }

    private MethodExpression getCloseExpression() {
        if (closeExpp == null) {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExpressionFactory ef = fc.getApplication().getExpressionFactory();
            closeExpp = ef.createMethodExpression(fc.getELContext(), CLOSE_EXPRESSION_VALUE, null,
                new Class<?>[] { CloseEvent.class });
        }
        return closeExpp;
    }

    /**
     *
     * @param panelData
     */
    @Override
    public void addPanel(DashboardPanel panelData) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application application = fc.getApplication();

        Panel panel = getPanel(panelData, application, fc);

        getDashboard().getChildren().add(panel);
        DashboardColumn column = getDashboard().getModel().getColumn(0);
        column.addWidget(panel.getId());
    }
}
