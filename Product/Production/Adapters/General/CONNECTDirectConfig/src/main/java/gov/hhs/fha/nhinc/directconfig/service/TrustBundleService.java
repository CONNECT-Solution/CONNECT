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
/*
Copyright (c) 2010, NHIN Direct Project
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer
in the documentation and/or other materials provided with the distribution.
3. Neither the name of the The NHIN Direct Project (nhindirect.org) nor the names of its contributors may be used to endorse or promote
products derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
THE POSSIBILITY OF SUCH DAMAGE.
 */

package gov.hhs.fha.nhinc.directconfig.service;

import gov.hhs.fha.nhinc.directconfig.entity.Certificate;
import gov.hhs.fha.nhinc.directconfig.entity.TrustBundle;
import gov.hhs.fha.nhinc.directconfig.entity.TrustBundleDomainReltn;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.BundleRefreshError;
import java.util.Calendar;
import java.util.Collection;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Web service interface for trust bundle operations.
 * @author Greg Meyer
 * @since 1.3
 */
@WebService
public interface TrustBundleService
{
    /**
     * Gets a collection of all trust bundles in the system
     * @param fetchAnchors Indicates if the anchors should be fetched.  When only the trust bundle names are needed, it is desireable
     * to suppress returning the list of anchors for efficiency.
     * @return Collection of all trust bundles in the system.  If no trust bundles have been configured, then an empty collection is returned.
     * @throws ConfigurationServiceException
     */
    @WebMethod(operationName = "getTrustBundles", action = "urn:GetTrustBundles")
    Collection<TrustBundle> getTrustBundles(@WebParam(name = "fetchAnchors") boolean fetchAnchors) throws ConfigurationServiceException;

    /**
     * Gets a specific trust bundle by name.  Each trust bundle name is unique and case insensitive.
     * @param bundleName The bundle name.
     * @return The trust bundle specified by the name.  If a bundle with the given name is not found, then null is returned.
     * @throws ConfigurationServiceException
     */
    @WebMethod(operationName = "getTrustBundleByName", action = "urn:GetTrustBundleByName")
    public TrustBundle getTrustBundleByName(@WebParam(name = "bundleName")  String bundleName) throws ConfigurationServiceException;

    /**
     * Gets a specific trust bundle by its internal id.
     * @param id The internal trust bundle id.
     * @return The trust bundle specified by the id.  If a bundle with the given id is not found, then null is returned.
     * @throws ConfigurationServiceException
     */
    @WebMethod(operationName = "getTrustBundleById", action = "urn:GetTrustBundleById")
    public TrustBundle getTrustBundleById(@WebParam(name = "id")  long id) throws ConfigurationServiceException;

    /**
     * Adds a trust bundle to the system.  The anchors should not be provided in the object as they will be downloaded.
     * from the provided trust bundle URL.
     * @param bundle The bundle to add.
     * @throws ConfigurationServiceException Throw if a bundle with the same name already exists of if required properties
     * are empty or null.
     */
    @WebMethod(operationName = "addTrustBundle", action = "urn:AddTrustBundle")
    public void addTrustBundle(@WebParam(name = "bundle") TrustBundle bundle) throws ConfigurationServiceException;

    /**
     * Forces an on demand refresh of a trust bundle regardless of its refresh internal.
     * @param id The id of the trust bundle to refresh.
     * @throws ConfigurationServiceException
     */
    @WebMethod(operationName = "refreshTrustBundle", action = "urn:RefreshTrustBundle")
    public void refreshTrustBundle(@WebParam(name = "id") long id) throws ConfigurationServiceException;

    /**
     * Updates the last error that occurred when trying to download or refresh the bundle from its URL.
     * @param trustBundleId The id of the bundle.
     * @param attemptTime The time the bundle update was attempted
     * @param error The reason for the error.
     * @throws ConfigurationServiceException
     */
    @WebMethod(operationName = "updateLastUpdateError", action = "urn:UpdateLastUpdateError")
    public void updateLastUpdateError(@WebParam(name = "trustBundleId") long trustBundleId,
            @WebParam(name = "attemptTime") Calendar attemptTime, @WebParam(name = "error") BundleRefreshError error)  throws ConfigurationServiceException;

    /**
     * Deletes a set of trust bundle from the system.  All associations to domains are deleted as well.
     * @param trustBundleIds The ids of the bundles to delete.
     * @throws ConfigurationServiceException
     */
    @WebMethod(operationName = "deleteTrustBundles", action = "urn:DeleteTrustBundles")
    public void deleteTrustBundles(@WebParam(name = "trustBundleIds") long[] trustBundleIds) throws ConfigurationServiceException;

    /**
     * Updates the signing certificate used to validate the authenticity of a bundle.
     * @param trustBundleId The trust bundle id.
     * @param signingCert The certificate used to sign the bundle
     * @throws ConfigurationServiceException
     */
    @WebMethod(operationName = "updateTrustBundleSigningCertificate", action = "urn:UpdateTrustBundleSigningCertificate")
    public void updateTrustBundleSigningCertificate(@WebParam(name = "trustBundleIds") long trustBundleId,
            @WebParam(name = "signingCert") Certificate signingCert) throws ConfigurationServiceException;

    /**
     * Updates multiple attributes of a trust bundle.
     * @param trustBundleId  The id of the bundle to update.
     * @param bundleName The new bundle name.  If empty or null, then the name is not updated.  If the URL changes, a bundle refresh is forced.
     * @param bundleUrl The new bundle url.  If empty or null, then the url is not updated.
     * @param signingCert The new certificate used to sign the bundle.  The attributes will be applied even if the signingCert is null.
     * @param refreshInterval The new refresh interval.
     * @throws ConfigurationServiceException
     */
    @WebMethod(operationName = "updateTrustBundleAttributes", action = "urn:UpdateTrustBundleAttributes")
    public void updateTrustBundleAttributes(@WebParam(name = "trustBundleId") long trustBundleId, @WebParam(name = "trustBundleName") String bundleName,
            @WebParam(name = "trustBundleURL") String bundleUrl, @WebParam(name = "signingCert") Certificate signingCert,
             @WebParam(name = "trustBundleRefreshInterval") int refreshInterval) throws ConfigurationServiceException;

    /**
     * Associates a domain to a trust bundle.  Bundle associates are directional and may be set as incoming only, outgoing only,
     * or incoming and outgoing.
     * @param domainId The id of the domain.
     * @param trustBundleId The id of the trust bundle.
     * @param incoming Indicates if the trust should allow incoming messages.
     * @param outgoing Indicates if the trust should allow outgoing messages.
     * @throws ConfigurationServiceException
     */
    @WebMethod(operationName = "associateTrustBundleToDomain", action = "urn:AssociateTrustBundleToDomain")
    public void associateTrustBundleToDomain(@WebParam(name = "domainId") long domainId,
            @WebParam(name = "trustBundleId") long trustBundleId, @WebParam(name = "incoming") boolean incoming,
            @WebParam(name = "outgoing") boolean outgoing) throws ConfigurationServiceException;

    /**
     * Dissociates a domain from a trust bundle.
     * @param domainId The id of the domain
     * @param trustBundleId The id of the trust bundle
     * @throws ConfigurationServiceException
     */
    @WebMethod(operationName = "disassociateTrustBundleFromDomain", action = "urn:DisassociateTrustBundleFromDomain")
    public void disassociateTrustBundleFromDomain(@WebParam(name = "domainId") long domainId,
            @WebParam(name = "trustBundleId") long trustBundleId) throws ConfigurationServiceException;

    /**
     * Dissociates a domain from all trust bundles.
     * @param domainId The id of the domain
     * @throws ConfigurationServiceException
     */
    @WebMethod(operationName = "disassociateTrustBundlesFromDomain", action = "urn:DisassociateTrustBundlesFromDomain")
    public void disassociateTrustBundlesFromDomain(@WebParam(name = "domainId") long domainId) throws ConfigurationServiceException;

    /**
     * Dissociates a trust bundle from all domains.
     * @param trustBundleId The trust bundle id.
     * @throws ConfigurationServiceException
     */
    @WebMethod(operationName = "disassociateTrustBundleFromDomains", action = "urn:DisassociateTrustBundleFromDomains")
    public void disassociateTrustBundleFromDomains(@WebParam(name = "trustBundleId") long trustBundleId) throws ConfigurationServiceException;

    /**
     * Gets all trust bundles associated with a domain
     * @param domainId The domain id
     * @param fetchAnchors Indicates if the anchors should be returned with each bundle.  When only the trust bundle names are needed, it is desirable
     * to suppress returning the list of anchors for efficiency.
     * @return Collection of all trust bundles associated with a specific domain.
     * @throws ConfigurationServiceException
     */
    @WebMethod(operationName = "getTrustBundlesByDomain", action = "urn:GetTrustBundlesByDomain")
    public Collection<TrustBundleDomainReltn>  getTrustBundlesByDomain(@WebParam(name = "domainId") long domainId,
            @WebParam(name = "fetchAnchors") boolean fetchAnchors) throws ConfigurationServiceException;
}
