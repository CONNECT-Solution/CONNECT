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

package gov.hhs.fha.nhinc.directconfig.dao;

import gov.hhs.fha.nhinc.directconfig.entity.TrustBundle;
import gov.hhs.fha.nhinc.directconfig.entity.TrustBundleAnchor;
import gov.hhs.fha.nhinc.directconfig.entity.TrustBundleDomainReltn;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.BundleRefreshError;
import gov.hhs.fha.nhinc.directconfig.exception.ConfigurationStoreException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Collection;

/**
 * DAO interface for trust bundle
 * @author Greg Meyer
 * @since 1.2
 */
public interface TrustBundleDao
{
    /**
     * Gets a collection of all trust bundles in the system
     * @return Collection of all trust bundles in the system.  If no trust bundles have been configured, then an empty collection is returned.
     * @throws ConfigurationStoreException
     */
    public Collection<TrustBundle> getTrustBundles() throws ConfigurationStoreException;

    /**
     * Gets a specific trust bundle by name.  Each trust bundle name is unique and case insensitive.
     * @param bundleName The bundle name.
     * @return The trust bundle specified by the name.  If a bundle with the given name is not found, then null is returned.
     * @throws ConfigurationStoreException
     */
    public TrustBundle getTrustBundleByName(String bundleName) throws ConfigurationStoreException;

    /**
     * Gets a specific trust bundle by its internal id.
     * @param id The internal trust bundle id.
     * @return The trust bundle specified by the id.  If a bundle with the given id is not found, then null is returned.
     * @throws ConfigurationStoreException
     */
    public TrustBundle getTrustBundleById(long id) throws ConfigurationStoreException;

    /**
     * Adds a trust bundle to the system.  The anchors should not be provided in the object as they will be downloaded.
     * from the provided trust bundle URL.
     * @param bundle The bundle to add.
     * @throws ConfigurationStoreException Throw if a bundle with the same name already exists of if required properties
     * are empty or null.
     */
    public void addTrustBundle(TrustBundle bundle) throws ConfigurationStoreException;

    /**
     * Updates the trust anchors within a bundle.  The last refresh time and refresh error will be updated as well (the error will
     * set to {@link BundleRefreshError#SUCCESS}
     * @param trustBundleId The id of the bundle to update.
     * @param attemptTime The time the update was performed.
     * @param newAnchorSet Collection of new anchors.  This is a complete list of all anchors that are in the bundle.
     * @param bundleCheckSum Checksum of the bundle.  This is generated by generating a thumbprint of the bundle.
     * @throws ConfigurationStoreException
     */
    public void updateTrustBundleAnchors(long trustBundleId, Calendar attemptTime, Collection<TrustBundleAnchor> newAnchorSet,
            String bundleCheckSum) throws ConfigurationStoreException;

    /**
     * Updates the last error that occurred when trying to download or refresh the bundle from its URL.
     * @param trustBundleId The id of the bundle.
     * @param attemptTime The time the bundle update was attempted
     * @param error The reason for the error.
     * @throws ConfigurationStoreException
     */
    public void updateLastUpdateError(long trustBundleId, Calendar attemptTime, BundleRefreshError error) throws ConfigurationStoreException;

    /**
     * Deletes a set of trust bundle from the system.  All associations to domains are deleted as well.
     * @param trustBundleIds The ids of the bundles to delete.
     * @throws ConfigurationStoreException
     */
    public void deleteTrustBundles(long[] trustBundleIds) throws ConfigurationStoreException;

    /**
     * Updates the signing certificate used to validate the authenticity of a bundle.
     * @param trustBundleId The trust bundle id.
     * @param signingCert The certificate used to sign the bundle
     * @throws ConfigurationStoreException
     */
    public void updateTrustBundleSigningCertificate(long trustBundleId, X509Certificate signingCert) throws ConfigurationStoreException;

    /**
     * Updates multiple attributes of a trust bundle.
     * @param trustBundleId  The id of the bundle to update.
     * @param bundleName The new bundle name.  If empty or null, then the name is not updated.
     * @param bundleUrl The new bundle url.  If empty or null, then the url is not updated.
     * @param signingCert The new certificate used to sign the bundle.  The attributes will be applied even if the signingCert is null.
     * @param refreshInterval The new refreshInterval
     * @throws ConfigurationStoreException
     */
    public void updateTrustBundleAttributes(long trustBundleId, String bundleName, String bundleUrl, X509Certificate signingCert,
            int refreshInterval) throws ConfigurationStoreException;

    /**
     * Associates a domain to a trust bundle.  Bundle associates are directional and may be set as incoming only, outgoing only,
     * or incoming and outgoing.
     * @param domainId The id of the domain.
     * @param trustBundleId The id of the trust bundle.
     * @param incoming Indicates if the trust should allow incoming messages.
     * @param outgoing Indicates if the trust should allow outgoing messages.
     * @throws ConfigurationStoreException
     */
    public void associateTrustBundleToDomain(long domainId, long trustBundleId, boolean incoming,
            boolean outgoing) throws ConfigurationStoreException;


    /**
     * Dissociates a domain from a trust bundle.
     * @param domainId The id of the domain
     * @param trustBundleId The id of the trust bundle
     * @throws ConfigurationStoreException
     */
    public void disassociateTrustBundleFromDomain(long domainId, long trustBundleId) throws ConfigurationStoreException;

    /**
     * Dissociates a domain from all trust bundles.
     * @param domainId The id of the domain
     * @throws ConfigurationStoreException
     */
    public void disassociateTrustBundlesFromDomain(long domainId) throws ConfigurationStoreException;

    /**
     * Dissociates a trust bundle from all domains.
     * @param trustBundleId The trust bundle id.
     * @throws ConfigurationStoreException
     */
    public void disassociateTrustBundleFromDomains(long trustBundleId) throws ConfigurationStoreException;

    /**
     * Gets all trust bundles associated with a domain
     * @param domainId The domain id
     * @return Collection of all trust bundles associated with a specific domain.
     * @throws ConfigurationStoreException
     */
    public Collection<TrustBundleDomainReltn> getTrustBundlesByDomain(long domainId) throws ConfigurationStoreException;
}
