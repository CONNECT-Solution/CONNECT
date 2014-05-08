/**
 * Copyright (c) 2009-2014, United States Government, as represented by the Secretary of Health and Human Services. All
 * rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met: Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution. Neither the name of the United States Government nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*
 */
package gov.hhs.fha.nhinc.admingui.dashboard;

import gov.hhs.fha.nhinc.admingui.managed.DashboardBean;
import java.util.List;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import org.primefaces.component.dashboard.Dashboard;
import org.primefaces.component.panel.Panel;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

/**
 *
 * @author jasonasmith
 */

@ManagedBean(name = "dashboardBacker")
@RequestScoped
public class DashboardBacker {
    
    public static final int DEFAULT_COLUMN_COUNT = 4;
	private int columnCount = DEFAULT_COLUMN_COUNT;
	
	private Dashboard dashboard;

	public DashboardBacker() {
		FacesContext fc = FacesContext.getCurrentInstance();
		Application application = fc.getApplication();

		dashboard = (Dashboard) application.createComponent(fc, "org.primefaces.component.Dashboard", "org.primefaces.component.DashboardRenderer");
		dashboard.setId("admingui_dashboard");

		DashboardModel model = new DefaultDashboardModel();
        
		for( int i = 0, n = getColumnCount(); i < n; i++ ) {
			DashboardColumn column = new DefaultDashboardColumn();
			model.addColumn(column);
		}
		dashboard.setModel(model);

		List<DashboardPanel> dbPanels = getPanels();
        
        int i = 1;
        
		for( DashboardPanel dbPanel : dbPanels ) {
			Panel panel = (Panel) application.createComponent(fc, "org.primefaces.component.Panel", "org.primefaces.component.PanelRenderer");
			panel.setId("measure_" + i);
			panel.setHeader(dbPanel.getType());
			panel.setClosable(true);
			panel.setToggleable(true);
            panel.setStyle("width: 200px; height: 200px");

			getDashboard().getChildren().add(panel);
			DashboardColumn column = model.getColumn(i%getColumnCount());
			column.addWidget(panel.getId());
			
            HtmlOutputText title = new HtmlOutputText();
			title.setValue(dbPanel.getTitle() + "\n");
            title.setEscape(false);
 
            HtmlOutputText description = new HtmlOutputText();
			description.setValue(dbPanel.getDescription());

			panel.getChildren().add(title);
            panel.getChildren().add(description);
            i++;
		}
	}

	public Dashboard getDashboard() {
		return dashboard;
	}

	public void setDashboard(Dashboard dashboard) {
		this.dashboard = dashboard;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}
    
    public List<DashboardPanel> getPanels(){
        
        FacesContext context = FacesContext.getCurrentInstance();
        DashboardBean dashboardBean = (DashboardBean) context.
            getELContext().getELResolver().getValue(context.getELContext(), null,"dashboardBean");
        return dashboardBean.getPanels();
    }
}

