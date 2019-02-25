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
package gov.hhs.fha.nhinc.admingui.managed;

import gov.hhs.fha.nhinc.admingui.constant.NavigationConstant;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.component.tabview.Tab;
import org.primefaces.event.TabChangeEvent;

/**
 *
 * @author jasonasmith / sadusumilli
 */
@ManagedBean(name = "tabBean")
@SessionScoped
public class TabBean {

    private int logsTabIndex = 0;
    private int adminTabIndex = 0;
    private int directTabIndex = 0;
    private int propIndex = 0;
    private int certTabIndex = 0;
    private int loadTestDataTabIndex = 0;
    private int loggingTabIndex = 0;


    private static final String GATEWAYPROPTAB = "gatewayTab";
    private static final String ADAPTERPROPTAB = "adapterTab";
    private static final String DIRECTDOMAINTAB = "directDomainTab";
    private static final String DIRECTAGENTTAB = "directAgentTab";
    private static final String DIRECTCERTTAB = "directCertTab";
    private static final String DIRECTTBTAB = "directTbTab";
    private static final String ACCTUSERSTAB = "acctUsersTab";
    private static final String ACCTROLESTAB = "acctRolesTab";
    private static final String AUDITPROPTAB = "auditTab";
    private static final String KEYSTORETAB = "keyStoreTab";
    private static final String TRUSTSTORETAB = "trustStoreTab";
    private static final String IMPORTCERTSTORETAB = "importCertStoreTab";
    private static final String LTDPATIENTTAB = "ltdPatientTab";
    private static final String LTDDOCUMENTTAB = "ltdDocumentTab";
    private static final String AUDITLOGTAB = "auditLogTab";
    private static final String ERRORLOGTAB = "errorLogTab";
    private static final String INTERNALENDPOINTSPROPTAB = "internalEndpointsTab";

    public int getDirectTabIndex() {
        return directTabIndex;
    }

    public void setDirectTabIndex(int directTabIndex) {
        this.directTabIndex = directTabIndex;
    }

    public int getLogsTabIndex() {
        return logsTabIndex;
    }

    public void setLogsTabIndex(int logsTabIndex) {
        this.logsTabIndex = logsTabIndex;
    }

    public int getAdminTabIndex() {
        return adminTabIndex;
    }

    public void setAdminTabIndex(int adminTabIndex) {
        this.adminTabIndex = adminTabIndex;
    }

    public String navigateToStatusDashBoard() {
        return NavigationConstant.STATUS_PAGE;
    }

    public String setLogsTabIndexNavigate(int logsTabIndex) {
        this.logsTabIndex = logsTabIndex;
        return "logs";
    }

    public String setAdminTabIndexNavigate(int adminTabIndex) {
        this.adminTabIndex = adminTabIndex;
        return NavigationConstant.ACCT_MGMT_PAGE;
    }

    public String setDirectTabIndexNavigate(int directTabIndex) {
        this.directTabIndex = directTabIndex;
        return NavigationConstant.DIRECT_PAGE;
    }

    /**
     * Event listener for tab change to set current active index of the direct tab view. Needed since active index is set
     * by menu links as well.
     *
     * @param tEvent
     */
    public void onDirectTabChange(TabChangeEvent tEvent) {
        Tab selectedTab = tEvent.getTab();
        if (DIRECTDOMAINTAB.equalsIgnoreCase(selectedTab.getId())) {
            directTabIndex = 0;
        } else if (DIRECTAGENTTAB.equalsIgnoreCase(selectedTab.getId())) {
            directTabIndex = 1;
        } else if (DIRECTCERTTAB.equalsIgnoreCase(selectedTab.getId())) {
            directTabIndex = 2;
        } else {
            directTabIndex = 3;
        }
    }

    public void onPropertyTabChange(TabChangeEvent tEvent) {
        Tab selectedTab = tEvent.getTab();
        if (GATEWAYPROPTAB.equalsIgnoreCase(selectedTab.getId())) {
            propIndex = 0;
        } else if (ADAPTERPROPTAB.equalsIgnoreCase(selectedTab.getId())) {
            propIndex = 1;
        } else if (AUDITPROPTAB.equalsIgnoreCase(selectedTab.getId())) {
            propIndex = 2;
        } else {
            propIndex = 3;
        }
    }

    public void onAcctTabChange(TabChangeEvent tEvent) {
        Tab selectedTab = tEvent.getTab();
        if (ACCTUSERSTAB.equalsIgnoreCase(selectedTab.getId())) {
            adminTabIndex = 0;
        } else {
            adminTabIndex = 1;
        }
    }

    // All "navigateTo" functions below were added as a workaround to an Expression Language bug found in WAS 8.5.0.1
    // For more information, see http://www-01.ibm.com/support/docview.wss?uid=swg1PM72533 (PM72533)
    public String navigateToDirectDomainTab() {
        return setDirectTabIndexNavigate(NavigationConstant.DIRECT_DOMAIN_TAB);
    }

    public String navigateToDirectSettingTab() {
        return setDirectTabIndexNavigate(NavigationConstant.DIRECT_SETTING_TAB);
    }

    public String navigateToDirectCertificateTab() {
        return setDirectTabIndexNavigate(NavigationConstant.DIRECT_CERTIFICATE_TAB);
    }

    public String navigateToDirectTrustbundleTab() {
        return setDirectTabIndexNavigate(NavigationConstant.DIRECT_TRUSTBUNDLE_TAB);
    }

    public String navigateToAccountMgmtUserAccountTab() {
        return setAdminTabIndexNavigate(NavigationConstant.ACCOUNT_MGMT_USERACC_TAB);
    }

    public String navigateToAccountMgmtManageRoleTab() {
        return setAdminTabIndexNavigate(NavigationConstant.ACCOUNT_MGMT_MANAGEROLE_TAB);
    }

    public String navigateToGatewayPropTab() {
        return setGatewayPropertyTabAndNavigate(0);
    }

    public String navigateToAdapterPropTab() {
        return setGatewayPropertyTabAndNavigate(1);
    }

    public String navigateToAuditPropTab() {
        return setGatewayPropertyTabAndNavigate(2);
    }

    public String navigateToInternalEndpointsPropTab() {
        return setGatewayPropertyTabAndNavigate(3);
    }

    public String navigateToFhir() {
        return NavigationConstant.FHIR_PAGE;
    }

    public String navigateToConnectionManagement() {
        return NavigationConstant.CM_PAGE;
    }

    public String navigateToPatientDiscoveryTab() {
        return setPatientSearchTabAndNavigate(0);
    }

    public String setGatewayPropertyTabAndNavigate(int i) {
        propIndex = i;
        return NavigationConstant.PROPERTIES_PAGE;
    }

    public String setPatientSearchTabAndNavigate(int i) {
        propIndex = i;
        return NavigationConstant.PATIENT_SEARCH_PAGE;
    }

    public String setAuditSearchTabAndNavigate(int i) {
        propIndex = i;
        return NavigationConstant.LOGGING_PAGE;
    }

    public int getPropIndex() {
        return propIndex;
    }

    public void setPropIndex(int propIndex) {
        this.propIndex = propIndex;
    }

    public String getGATEWAYPROPTAB() {
        return GATEWAYPROPTAB;
    }

    public String getADAPTERPROPTAB() {
        return ADAPTERPROPTAB;
    }

    public String getAUDITPROPTAB() {
        return AUDITPROPTAB;
    }

    public String getINTERNALENDPOINTSPROPTAB() {
        return INTERNALENDPOINTSPROPTAB;
    }
    public String getDIRECTDOMAINTAB() {
        return DIRECTDOMAINTAB;
    }

    public String getDIRECTAGENTTAB() {
        return DIRECTAGENTTAB;
    }

    public String getDIRECTCERTTAB() {
        return DIRECTCERTTAB;
    }

    public String getDIRECTTBTAB() {
        return DIRECTTBTAB;
    }

    public String getACCTUSERSTAB() {
        return ACCTUSERSTAB;
    }

    public String getACCTROLESTAB() {
        return ACCTROLESTAB;
    }

    // region LOAD-TEST-DATA
    public String getLTDPATIENTTAB() {
        return LTDPATIENTTAB;
    }

    public String getLTDDOCUMENTTAB() {
        return LTDDOCUMENTTAB;
    }

    // TAB-INDEX
    public int getLoadTestDataTabIndex() {
        return loadTestDataTabIndex;
    }

    public void setLoadTestDataTabIndex(int ltdIndexValue) {
        loadTestDataTabIndex = ltdIndexValue;
    }

    // TAB-CHANGE
    public void onLoadTestDataTabChange(TabChangeEvent tEvent) {
        Tab selectedTab = tEvent.getTab();
        if (LTDPATIENTTAB.equalsIgnoreCase(selectedTab.getId())) {
            loadTestDataTabIndex = 0;
        } else {
            loadTestDataTabIndex = 1;
        }
    }

    // TAB-NAVIGATE
    public String setLoadTestDataTabIndexNavigate(int loadTestDataTabIndex) {
        this.loadTestDataTabIndex = loadTestDataTabIndex;
        return NavigationConstant.LOAD_TEST_DATA_PAGE;
    }

    public String navigateToLTDPatientTab() {
        return setLoadTestDataTabIndexNavigate(NavigationConstant.LOAD_TEST_DATA_PATIENT_TAB);
    }

    public String navigateToLTDDocumentTab() {
        return setLoadTestDataTabIndexNavigate(NavigationConstant.LOAD_TEST_DATA_DOCUMENT_TAB);
    }

    // endRegion LOAD-TEST-DATA
    public String navigateToKeyStoreCertManagement() {
        return setCertTabIndexNavigate(NavigationConstant.KEYSTORE_MGMT_TAB);
    }

    public String navigateToTrustStoreCertManagement() {
        return setCertTabIndexNavigate(NavigationConstant.TRUSTSTORE_MGMT_TAB);
    }

    public String navigateToImportCertManagement() {
        return setCertTabIndexNavigate(NavigationConstant.IMPORT_CERT_MGMT_TAB);
    }

    public String getKEYSTORETAB() {
        return KEYSTORETAB;
    }

    public String getTRUSTSTORETAB() {
        return TRUSTSTORETAB;
    }

    public String getIMPORTCERTSTORETAB() {
        return IMPORTCERTSTORETAB;
    }

    public void onCertTabChange(TabChangeEvent tEvent) {
        Tab selectedTab = tEvent.getTab();
        if (KEYSTORETAB.equalsIgnoreCase(selectedTab.getId())) {
            certTabIndex = 0;
        } else if (TRUSTSTORETAB.equalsIgnoreCase(selectedTab.getId())) {
            certTabIndex = 1;
        } else {
            certTabIndex = 2;
        }
    }

    public int getCertTabIndex() {
        return certTabIndex;
    }

    public void setCertTabIndex(int certTabIndex) {
        this.certTabIndex = certTabIndex;
    }

    public String setCertTabIndexNavigate(int certTabIndex) {
        this.certTabIndex = certTabIndex;
        return NavigationConstant.CERTIFICATE_MGMT_PAGE;
    }

    public String navigateToExchangeManagement() {
        return NavigationConstant.EM_PAGE;
    }

    // logging
    public int getLoggingTabIndex() {
        return loggingTabIndex;
    }

    public void setLoggingTabIndex(int loggingTabIndex) {
        this.loggingTabIndex = loggingTabIndex;
    }

    public String setLoggingTabIndexNavigate(int loggingTabIndex) {
        this.loggingTabIndex = loggingTabIndex;
        return NavigationConstant.LOGGING_PAGE;
    }

    public String navigateToAuditLogTab() {
        return setLoggingTabIndexNavigate(NavigationConstant.AUDIT_LOG_TAB);
    }

    public String navigateToErrorLogTab() {
        return setLoggingTabIndexNavigate(NavigationConstant.ERROR_LOG_TAB);
    }

    public void onLoggingTabChange(TabChangeEvent tEvent) {
        Tab selectedTab = tEvent.getTab();
        if (AUDITLOGTAB.equalsIgnoreCase(selectedTab.getId())) {
            loggingTabIndex = 0;
        } else {
            loggingTabIndex = 1;
        }
    }

    public String getAUDITLOGTAB() {
        return AUDITLOGTAB;
    }

    public String getERRORLOGTAB() {
        return ERRORLOGTAB;
    }

    // to setup new tab you will need tab-index, tab-nagivation, tab-change-event
}
