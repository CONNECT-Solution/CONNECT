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
package gov.hhs.fha.nhinc.admingui.proxy;

/**
 *
 * @author jasonasmith
 */
public class DirectConfigConstants {

    public static final String DIRECT_CONFIG_SERVICE_NAME = "directconfig";

    public static final String DIRECT_CONFIG_GET_DOMAIN = "getDomain";
    public static final String DIRECT_CONFIG_ADD_DOMAIN = "addDomain";
    public static final String DIRECT_CONFIG_LIST_DOMAINS = "listDomains";
    public static final String DIRECT_CONFIG_UPDATE_DOMAIN = "updateDomain";
    public static final String DIRECT_CONFIG_DELETE_DOMAIN = "removeDomain";

    public static final String DIRECT_CONFIG_ADD_ANCHOR = "addAnchor";
    public static final String DIRECT_CONFIG_DELETE_ANCHOR = "removeAnchors";
    public static final String DIRECT_CONFIG_GET_ANCHORS_FOR_OWNER = "getAnchorsForOwner";

    public static final String DIRECT_CONFIG_DELETE_ADDRESS = "removeAddress";

    public static final String DIRECT_CONFIG_ADD_SETTING = "addSetting";
    public static final String DIRECT_CONFIG_DELETE_SETTING = "deleteSetting";
    public static final String DIRECT_CONFIG_LIST_SETTINGS = "getAllSettings";

    public static final String DIRECT_CONFIG_ADD_CERT = "addCertificates";
    public static final String DIRECT_CONFIG_DELETE_CERT = "removeCertificates";
    public static final String DIRECT_CONFIG_GET_CERTS_FOR_OWNER = "getCertificatesForOwner";
    public static final String DIRECT_CONFIG_LIST_CERTS = "listCertificates";

    public static final String DIRECT_CONFIG_ADD_TRUST_BUNDLE = "addTrustBundle";
    public static final String DIRECT_CONFIG_DELETE_TRUST_BUNDLE = "deleteTrustBundles";
    public static final String DIRECT_CONFIG_GET_TRUST_BUNDLES = "getTrustBundles";
    public static final String DIRECT_CONFIG_GET_TRUST_BUNDLE_BY_NAME = "getTrustBundleByName";
    public static final String DIRECT_CONFIG_GET_TRUST_BUNDLES_BY_DOMAIN = "getTrustBundlesByDomain";
    public static final String DIRECT_CONFIG_ASSOCIATE_TRUST_BUNDLE_TO_DOMAIN = "associateTrustBundleToDomain";
    public static final String DIRECT_CONFIG_DISASSOCIATE_TRUST_BUNDLE_FROM_DOMAIN = "disassociateTrustBundleFromDomain";
    public static final String DIRECT_CONFIG_DISASSOCIATE_TRUST_BUNDLE_FROM_DOMAINS = "disassociateTrustBundleFromDomains";
    public static final String DIRECT_CONFIG_DISASSOCIATE_TRUST_BUNDLES_FROM_DOMAIN = "disassociateTrustBundlesFromDomain";
    public static final String DIRECT_CONFIG_UPDATE_TRUST_BUNDLE_ATTRIBUTES = "updateTrustBundleAttributes";
    public static final String DIRECT_CONFIG_REFRESH_TRUST_BUNDLE = "refreshTrustBundle";
    public static final String DIRECT_CONFIG_REMOVE_ADDRESS = "removeAddress";
}
