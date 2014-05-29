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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 * The Class ManageRoleBean.
 */
@ManagedBean(name = "ManageRoleBean")
@ViewScoped
public class ManageRoleBean {

    /** The pages model. */
    private DataModel<PageAccessMapping> pagesModel;

    /**
     * Access level changed.
     *
     * @param event the event
     */
    public void accessLevelChanged(final AjaxBehaviorEvent event) {

        System.out.println("here we save the changed access level for " + pagesModel.getRowData().page + " to "
                + pagesModel.getRowData().selectedAccessLevel + ".");
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO", "Access Level Changed."));

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
        this.pagesModel = pages;
    }

    /**
     * Inits the.
     */
    @PostConstruct
    public void init() {
        List<PageAccessMapping> pages = new ArrayList<PageAccessMapping>();
        pages.add(new PageAccessMapping("acctmanagePrime.xhtml"));
        pages.add(new PageAccessMapping("DashboardPrime.xhtml"));
        pages.add(new PageAccessMapping("ManageRole.xhtml"));
        pages.add(new PageAccessMapping("StatusPrime.xhtml"));
        pages.add(new PageAccessMapping("direct.xhtml"));
        pages.add(new PageAccessMapping("trust-bundle-anchor1.xhtml"));
        pages.add(new PageAccessMapping("trust-bundle-anchor2.xhtml"));

        pagesModel = new ListDataModel<PageAccessMapping>(pages);
    }

    /**
     * The Class PageAccessMapping.
     */
    public class PageAccessMapping {
        
        /** The page. */
        private String page;
        
        /** The available access levels. */
        private Collection<String> availableAccessLevels;
        
        /** The selected access level. */
        private String selectedAccessLevel;

        /**
         * Instantiates a new page access mapping.
         *
         * @param page the page
         */
        public PageAccessMapping(String page) {
            this.page = page;
            availableAccessLevels = new ArrayList<String>();
            availableAccessLevels.add("No Access");
            availableAccessLevels.add("Read Only");
            availableAccessLevels.add("Read Write");
            selectedAccessLevel = "NoAccess";
        }

        /**
         * Gets the page.
         *
         * @return the page
         */
        public String getPage() {
            return page;
        }

        /**
         * Sets the page.
         *
         * @param page the page to set
         */
        public void setPage(String page) {
            this.page = page;
        }

        /**
         * Gets the available access levels.
         *
         * @return the availableAccessLevels
         */
        public Collection<String> getAvailableAccessLevels() {
            return availableAccessLevels;
        }

        /**
         * Sets the available access levels.
         *
         * @param availableAccessLevels the availableAccessLevels to set
         */
        public void setAvailableAccessLevels(Collection<String> availableAccessLevels) {
            this.availableAccessLevels = availableAccessLevels;
        }

        /**
         * Gets the selected access level.
         *
         * @return the selectedAccessLevel
         */
        public String getSelectedAccessLevel() {
            return selectedAccessLevel;
        }

        /**
         * Sets the selected access level.
         *
         * @param selectedAccessLevel the selectedAccessLevel to set
         */
        public void setSelectedAccessLevel(String selectedAccessLevel) {
            this.selectedAccessLevel = selectedAccessLevel;
        }

    }

}