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

@ManagedBean(name = "ManageRoleBean")
@ViewScoped
public class ManageRoleBean {

    private DataModel<PageAccessMapping> pagesModel;

    public void accessLevelChanged(final AjaxBehaviorEvent event) {

        System.out.println("here we save the changed access level for " + pagesModel.getRowData().page + " to " + pagesModel.getRowData().selectedAccessLevel + ".");
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO", "Access Level Changed."));

    }

    /**
     * @return the pages
     */
    public DataModel<PageAccessMapping> getPages() {
        return pagesModel;
    }

    /**
     * @param pages the pages to set
     */
    public void setPages(DataModel<PageAccessMapping> pages) {
        this.pagesModel = pages;
    }

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

    public class PageAccessMapping {
        private String page;
        private Collection<String> availableAccessLevels;
        private String selectedAccessLevel;

        public PageAccessMapping(String page) {
            this.page = page;
            availableAccessLevels = new ArrayList<String>();
            availableAccessLevels.add("No Access");
            availableAccessLevels.add("Read Only");
            availableAccessLevels.add("Read Write");
            selectedAccessLevel = "NoAccess";
        }

        /**
         * @return the page
         */
        public String getPage() {
            return page;
        }

        /**
         * @param page the page to set
         */
        public void setPage(String page) {
            this.page = page;
        }

        /**
         * @return the availableAccessLevels
         */
        public Collection<String> getAvailableAccessLevels() {
            return availableAccessLevels;
        }

        /**
         * @param availableAccessLevels the availableAccessLevels to set
         */
        public void setAvailableAccessLevels(Collection<String> availableAccessLevels) {
            this.availableAccessLevels = availableAccessLevels;
        }

        /**
         * @return the selectedAccessLevel
         */
        public String getSelectedAccessLevel() {
            return selectedAccessLevel;
        }

        /**
         * @param selectedAccessLevel the selectedAccessLevel to set
         */
        public void setSelectedAccessLevel(String selectedAccessLevel) {
            this.selectedAccessLevel = selectedAccessLevel;
        }

    }

}