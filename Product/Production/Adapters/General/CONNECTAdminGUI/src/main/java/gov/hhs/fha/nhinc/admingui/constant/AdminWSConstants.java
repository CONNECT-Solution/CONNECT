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
 * @author ttang
 *
 */
public class AdminWSConstants {

    public static final String ADMIN_CERT_IMPORT = "importCertificate";
    public static final String ADMIN_CERT_LIST_TRUSTSTORE = "listTrustStores";
    public static final String ADMIN_CERT_LIST_KEYSTORE = "listKeyStores";
    public static final String ADMIN_CERT_DELETE = "deleteCertificate";
    public static final String ADMIN_CERT_EDIT = "editCertificate";
    public static final String ADMIN_CERT_LIST_CHAINOFTRUST = "listChainOfTrust";
    public static final String ADMIN_CERT_DELETE_TEMPKEYSTORE = "deleteTemporaryKeystore";
    public static final String ADMIN_CERT_CREATE_CERTIFICATE = "createCertificate";
    public static final String ADMIN_CERT_CREATE_CSR = "createCSR";
    public static final String ADMIN_CERT_IMPORT_TOKEYSTORE = "importToKeystore";
    public static final String ADMIN_CERT_COMPLETE_IMPORTWIZARD = "completeImportWizard";
    public static final String ADMIN_CERT_LIST_TEMPORARYALIAS = "listTemporaryAlias";
    public static final String ADMIN_CERT_UNDO_IMPORTKEYSTORE = "undoImportKeystore";

    // Config Admin
    public static final String ENTITY_CONFIG_ADMIN_SERVICE_NAME = "entityconfigadmin";
    public static final String ENTITY_EXCHANGE_MANAGEMENT_SERVICE_NAME = "entityexchangemanagement";
    public static final String ENTITY_INTERNAL_EXCHANGE_MANAGEMENT_SERVICE_NAME = "entityinternalexchangemanagement";

    // exchange-management
    public static final String ADMIN_EXCHANGE_SAVE_EXCHANGE = "saveExchange";
    public static final String ADMIN_EXCHANGE_DELETE = "deleteExchange";
    public static final String ADMIN_EXCHANGE_REFRESH = "refreshExchangeManager";
    public static final String ADMIN_EXCHANGE_INFOVIEW = "getExchangeInfoView";
    public static final String ADMIN_EXCHANGE_LIST_ENDPOINTS = "listEndpoints";
    public static final String ADMIN_EXCHANGE_LIST_EXCHANGES = "listExchanges";
    public static final String ADMIN_EXCHANGE_LIST_ORGANIZATIONS = "listOrganizations";
    public static final String ADMIN_EXCHANGE_SAVE_CONFIG = "saveExchangeConfig";

    // internal-exchange-management
    public static final String ADMIN_EXCHANGE_UPDATE_ENDPOINT = "updateEndpoint";

    // Properties
    public static final String PROPERTIES_SERVICE_NAME = "adapterpropertyaccessor";
    public static final String PROPERTIES_LIST_PROP = "listProperties";
    public static final String PROPERTIES_SAVE_PROP = "saveProperty";

    public static final String ADMIN_GUI_MANAGEMENT_SERVICE_NAME = "admindashboard";
    public static final String ADMIN_DASHBOARD_ERRORLOG_LIST = "listErrorLog";
    public static final String ADMIN_DASHBOARD_ERRORLOG_GETFILTERS = "getSearchFilter";
    public static final String ADMIN_DASHBOARD_ERRORLOG_VIEW = "viewErrorLog";

    // load-test-data: ADMIN_LTD_SAVE/LIST/DELETE/GET
    public static final String ENTITY_LOAD_TEST_DATA_SERVICE_NAME = "entityloadtestdata";
    public static final String ADMIN_LTD_DELETEADDRESS = "deleteAddress";
    public static final String ADMIN_LTD_DELETEIDENTIFIER = "deleteIdentifier";
    public static final String ADMIN_LTD_DELETEPATIENT = "deletePatient";
    public static final String ADMIN_LTD_DELETEPERSONNAME = "deletePersonName";
    public static final String ADMIN_LTD_DELETEPHONENUMBER = "deletePhoneNumber";
    public static final String ADMIN_LTD_DELETEDOCUMENT = "deleteDocument";
    public static final String ADMIN_LTD_DELETEEVENTCODE = "deleteEventCode";
    public static final String ADMIN_LTD_DUPLICATEDOCUMENT = "duplicateDocument";
    public static final String ADMIN_LTD_DUPLICATEPATIENT = "duplicatePatient";
    public static final String ADMIN_LTD_GETADDRESS = "getAddress";
    public static final String ADMIN_LTD_GETDOCUMENT = "getDocument";
    public static final String ADMIN_LTD_GETEVENTCODE = "getEventCode";
    public static final String ADMIN_LTD_GETIDENTIFIER = "getIdentifier";
    public static final String ADMIN_LTD_GETPATIENT = "getPatient";
    public static final String ADMIN_LTD_GETPATIENT_BYIDENTIFIER = "getPatientByIdentifier";
    public static final String ADMIN_LTD_GETPERSONNAME = "getPersonName";
    public static final String ADMIN_LTD_GETPHONENUMBER = "getPhoneNumber";
    public static final String ADMIN_LTD_LISTALLADDRESS = "listAllAddress";
    public static final String ADMIN_LTD_LISTALLDOCUMENT = "listAllDocument";
    public static final String ADMIN_LTD_LISTALLEVENTCODE = "listAllEventCode";
    public static final String ADMIN_LTD_LISTALLIDENTIER = "listAllIdentier";
    public static final String ADMIN_LTD_LISTALLPATIENT = "listAllPatient";
    public static final String ADMIN_LTD_LISTALLPERSONNAME = "listAllPersonName";
    public static final String ADMIN_LTD_LISTALLPHONENUMBER = "listAllPhoneNumber";
    public static final String ADMIN_LTD_SAVEADDRESS = "saveAddress";
    public static final String ADMIN_LTD_SAVEDOCUMENT = "saveDocument";
    public static final String ADMIN_LTD_SAVEEVENTCODE = "saveEventCode";
    public static final String ADMIN_LTD_SAVEIDENTIFIER = "saveIdentifier";
    public static final String ADMIN_LTD_SAVEPATIENT = "savePatient";
    public static final String ADMIN_LTD_SAVEPERSONNAME = "savePersonName";
    public static final String ADMIN_LTD_SAVEPHONENUMBER = "savePhoneNumber";

    private AdminWSConstants() {
    }
}
