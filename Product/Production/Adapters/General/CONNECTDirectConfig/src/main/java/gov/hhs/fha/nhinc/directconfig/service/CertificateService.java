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
import gov.hhs.fha.nhinc.directconfig.entity.helpers.EntityStatus;
import gov.hhs.fha.nhinc.directconfig.service.helpers.CertificateGetOptions;
import java.util.Collection;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Service class for methods related to a Certificate object.
 */
@WebService
public interface CertificateService {

    // TODO Should X509Certificate actually be X509CertificateEx?

    /**
     * Add a Certificate.
     *
     * @param certs
     *            The Certificate.
     * @throws ConfigurationServiceException
     */
    @WebMethod(operationName = "addCertificates", action = "urn:AddCertificates")
    void addCertificates(@WebParam(name = "certs") Collection<Certificate> certs) throws ConfigurationServiceException;

    /**
     * Get a Certificate.
     *
     * @param owner
     *            The Certificate owner.
     * @param thumbprint
     *            The Certificate thumbprint.
     * @param options
     *            The Certificate options.
     * @return a Certificate.
     * @throws ConfigurationServiceException
     */
    @WebMethod(operationName = "getCertificate", action = "urn:GetCertificate")
    Certificate getCertificate(@WebParam(name = "owner") String owner,
            @WebParam(name = "thumbprint") String thumbprint, @WebParam(name = "options") CertificateGetOptions options)
            throws ConfigurationServiceException;

    /**
     * Get a collection of Certificates.
     *
     * @param certificateIds
     *            A collection of Certificate IDs.
     * @param options
     *            The Certificate options.
     * @return a collection of Certificates.
     * @throws ConfigurationServiceException
     */
    @WebMethod(operationName = "getCertificates", action = "urn:GetCertificates")
    Collection<Certificate> getCertificates(@WebParam(name = "certificateIds") Collection<Long> certificateIds,
            @WebParam(name = "options") CertificateGetOptions options) throws ConfigurationServiceException;

    /**
     * Get a collection of Certificates for an owner.
     *
     * @param owner
     *            The Certificate owner.
     * @param options
     *            The Certificate options.
     * @return a collection of Certificates.
     * @throws ConfigurationServiceException
     */
    @WebMethod(operationName = "getCertificatesForOwner", action = "urn:GetCertificatesForOwner")
    Collection<Certificate> getCertificatesForOwner(@WebParam(name = "owner") String owner,
            @WebParam(name = "options") CertificateGetOptions options) throws ConfigurationServiceException;

    /**
     * Set a Certificate status.
     *
     * @param certificateIds
     *            A collection of Certificates.
     * @param status
     *            The Certificate status.
     * @throws ConfigurationServiceException
     */
    @WebMethod(operationName = "setCertificateStatus", action = "urn:SetCertificateStatus")
    void setCertificateStatus(@WebParam(name = "certificateIds") Collection<Long> certificateIds,
            @WebParam(name = "status") EntityStatus status) throws ConfigurationServiceException;

    /**
     * Set the Certificate status for an owner.
     *
     * @param owner
     *            The Certificate owner.
     * @param status
     *            The Certificate status.
     * @throws ConfigurationServiceException
     */
    @WebMethod(operationName = "setCertificateStatusForOwner", action = "urn:SetCertificateStatusForOwner")
    void setCertificateStatusForOwner(@WebParam(name = "owner") String owner,
            @WebParam(name = "status") EntityStatus status) throws ConfigurationServiceException;

    /**
     * Remove a Certificate.
     *
     * @param certificateIds
     *            A collection of Certificate IDs.
     * @throws ConfigurationServiceException
     */
    @WebMethod(operationName = "removeCertificates", action = "urn:RemoveCertificates")
    void removeCertificates(@WebParam(name = "certificateIds") Collection<Long> certificateIds)
            throws ConfigurationServiceException;

    /**
     * Remove the Certificates for an owner.
     *
     * @param owner
     *            The Certificate owner.
     * @throws ConfigurationServiceException
     */
    @WebMethod(operationName = "removeCertificatesForOwner", action = "urn:RemoveCertificatesForOwner")
    void removeCertificatesForOwner(@WebParam(name = "owner") String owner) throws ConfigurationServiceException;

    /**
     * Get a collection of Certificates.
     *
     * @param lastCertificateId
     *            The last Certificate ID.
     * @param maxResults
     *            The maximum number of results.
     * @param options
     *            The Certificate options.
     * @return a collection of Certificates.
     * @throws ConfigurationServiceException
     */
    @WebMethod(operationName = "listCertificates", action = "urn:ListCertificates")
    Collection<Certificate> listCertificates(@WebParam(name = "lastCertificateId") long lastCertificateId,
            @WebParam(name = "maxResutls") int maxResults, @WebParam(name = "options") CertificateGetOptions options)
            throws ConfigurationServiceException;

    /**
     * Determines if a certificate exists in the certificate store. Although not
     * specific in the interface definition, certificate thumbprinting is
     * recommended for certificate searching.
     *
     * @param cert
     *            The certificate to search for.
     * @return True if the certificate exist in the store. False otherwise.
     */
    @WebMethod(operationName = "contains", action = "urn:Contains")
    public boolean contains(@WebParam(name = "cert") Certificate cert);

}
