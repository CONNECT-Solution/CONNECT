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
package gov.hhs.fha.nhinc.admingui.constant;

/**
 * @author sadusumilli
 *
 */
public class NavigationConstant {

    public static final String STATUS_PAGE = "status";
    public static final String LOGIN_PAGE = "login";
    public static final String LOGIN_XHTML = "/login.xhtml";
    public static final String ACCT_MGMT_PAGE = "acctmanage";
    public static final String DIRECT_PAGE = "direct";
    public static final String DIRECT_XHTML = "direct.xhtml";
    public static final String FHIR_PAGE = "fhir";
    public static final String FHIR_XHTML = "fhir.xhtml";
    public static final String CM_PAGE = "connectionManager";
    public static final String PROPERTIES_PAGE = "properties";
    public static final String PATIENT_SEARCH_PAGE = "patientDiscovery";
    public static final String CUSTOM_ERROR_PAGE = "customerror";
    public static final String CUSTOM_ERROR_XHTML = "/customerror.xhtml";
    public static final String CERTIFICATE_MGMT_PAGE = "certificateManager";
    public static final String CERTIFICATE_MGMT_XHTML = "/certificateManager.xhtml";
    public static final String LOAD_TEST_DATA_PAGE = "loadTestData";
    public static final String LOAD_TEST_DATA_XHTML = "/loadTestData.xhtml";
    public static final String LOGGING_PAGE = "auditLog";
    public static final String LOGGING_XHTML = "/auditLog.xhtml";
    public static final String EM_PAGE = "exchangeManager";


    public static final int DIRECT_DOMAIN_TAB = 0;
    public static final int DIRECT_SETTING_TAB = 1;
    public static final int DIRECT_CERTIFICATE_TAB = 2;
    public static final int DIRECT_TRUSTBUNDLE_TAB = 3;

    public static final int ACCOUNT_MGMT_USERACC_TAB = 0;
    public static final int ACCOUNT_MGMT_MANAGEROLE_TAB = 1;

    public static final int KEYSTORE_MGMT_TAB = 0;
    public static final int TRUSTSTORE_MGMT_TAB = 1;
    public static final int IMPORT_CERT_MGMT_TAB = 2;

    public static final int LOAD_TEST_DATA_PATIENT_TAB = 0;
    public static final int LOAD_TEST_DATA_DOCUMENT_TAB = 1;

    public static final int AUDIT_LOG_TAB = 0;
    public static final int ERROR_LOG_TAB = 1;

    /**
     * Use this class to hold constant variables
     */
    private NavigationConstant() {
    }

}
