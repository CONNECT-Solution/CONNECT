/**
 * Copyright (c) 2009-2014, United States Government, as represented by the
 * Secretary of Health and Human Services. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. Neither the name of the United States Government nor
 * the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package gov.hhs.fha.nhinc.admingui.managed;

import gov.hhs.fha.nhinc.admingui.constant.NavigationConstant;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;

/**
 *
 * @author jasonasmith / sadusumilli
 */
@ManagedBean(name = "tabBean")
@SessionScoped
public class TabBean {

    private int dashboardTabIndex = 0;
    private int logsTabIndex = 0;
    private int adminTabIndex = 0;
    private int directTabIndex = 0;

    /**
     *
     * @return
     */
    public int getDirectTabIndex() {
        return directTabIndex;
    }

    /**
     *
     * @param directTabIndex
     */
    public void setDirectTabIndex(int directTabIndex) {
        this.directTabIndex = directTabIndex;
    }
    
    /**
     *
     * @return
     */
    public int getDashboardTabIndex() {
        return dashboardTabIndex;
    }

    /**
     *
     * @param dashboardTabIndex
     */
    public void setDashboardTabIndex(int dashboardTabIndex) {
        this.dashboardTabIndex = dashboardTabIndex;
    }

    /**
     *
     * @return
     */
    public int getLogsTabIndex() {
        return logsTabIndex;
    }

    /**
     *
     * @param logsTabIndex
     */
    public void setLogsTabIndex(int logsTabIndex) {
        this.logsTabIndex = logsTabIndex;
    }

    /**
     *
     * @return
     */
    public int getAdminTabIndex() {
        return adminTabIndex;
    }   

    /**
     *
     * @param adminTabIndex
     */
    public void setAdminTabIndex(int adminTabIndex) {
        this.adminTabIndex = adminTabIndex;
    }

    /**
     *
     * @param dashboardTabIndex
     * @return
     */
    public String setDashboardTabIndexNavigate(int dashboardTabIndex) {
        this.dashboardTabIndex = dashboardTabIndex;
        return NavigationConstant.STATUS_PAGE;
    }

    /**
     *
     * @param logsTabIndex
     * @return
     */
    public String setLogsTabIndexNavigate(int logsTabIndex) {
        this.logsTabIndex = logsTabIndex;
        return "logs";
    }

    /**
     *
     * @param adminTabIndex
     * @return
     */
    public String setAdminTabIndexNavigate(int adminTabIndex) {
        this.adminTabIndex = adminTabIndex;
        return NavigationConstant.ACCT_MGMT_PAGE;
    }
    
    /**
     *
     * @param directTabIndex
     * @return
     */
    public String setDirectTabIndexNavigate(int directTabIndex){
        this.directTabIndex = directTabIndex;
        return NavigationConstant.DIRECT_PAGE;
    }
    
    /**
     * Event listener for tab change to set current active index of the 
     * direct tab view.  Needed since active index is set by menu links as well.
     * @param tEvent 
     */
    public void onDirectTabChange(TabChangeEvent tEvent){
        TabView tabView = (TabView) tEvent.getComponent();
        this.directTabIndex = tabView.getChildren().indexOf(tEvent.getTab());
    }

}
