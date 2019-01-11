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

import gov.hhs.fha.nhinc.directconfig.entity.Domain;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.EntityStatus;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.AddDomain;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.AddDomainResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.UpdateDomain;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.UpdateDomainResponse;
import java.util.Collection;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Service class for methods related to a Domain object.
 */
@WebService
public interface DomainService {

    /**
     * Add a Domain
     *
     * @param addDomain
     * @return
     * @throws ConfigurationServiceException
     */
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    @WebResult(name = "addDomainResponse", targetNamespace = "http://nhind.org/config", partName = "parameters")
    @WebMethod(operationName = "addDomain", action = "urn:AddDomain")
    AddDomainResponse addDomain(@WebParam(partName = "parameters", name = "addDomain", targetNamespace = "http://nhind.org/config") AddDomain addDomain) throws ConfigurationServiceException;

    /**
     * Update a Domain.
     *
     * @param domain
     *            The Domain to update.
     * @return
     * @throws ConfigurationServiceException
     */
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    @WebResult(name = "updateDomainResponse", targetNamespace = "http://nhind.org/config", partName = "parameters")
    @WebMethod(action = "urn:UpdateDomain")
    UpdateDomainResponse updateDomain(@WebParam(name = "updateDomain") UpdateDomain domain) throws ConfigurationServiceException;

    /**
     * Get a count of Domains.
     *
     * @return a count of Domains.
     * @throws ConfigurationServiceException
     */
    @WebMethod(operationName = "getDomainCount", action = "urn:GetDomainCount")
    int getDomainCount() throws ConfigurationServiceException;

    /**
     * Get a collection of all Domains matching the parameters.
     *
     * @param domainNames
     *            A collection of domain names.
     * @param status
     *            An EntityStatus object.
     * @return a collection of all Domains matching the parameters.
     * @throws ConfigurationServiceException
     */
    @WebMethod(operationName = "getDomains", action = "urn:GetDomains")
    Collection<Domain> getDomains(@WebParam(name = "names") Collection<String> domainNames,
            @WebParam(name = "status") EntityStatus status) throws ConfigurationServiceException;

    /**
     * Remove a Domain.
     *
     * @param domainName
     *            The name of the Domain to remove.
     * @throws ConfigurationServiceException
     *
     * @deprecated Use of removeDomain(Long domainId) is preferred.
     */
    @Deprecated
    @WebMethod(operationName = "removeDomain", action = "urn:RemoveDomain")
    void removeDomain(@WebParam(name = "name") String domainName) throws ConfigurationServiceException;

    /**
     * Remove a Domain.
     *
     * @param domainId
     *            The id of the Domain to remove.
     * @throws ConfigurationServiceException
     */
    @WebMethod(operationName = "removeDomainById", action = "urn:RemoveDomainById")
    void removeDomainById(@WebParam(name = "id") Long domainId) throws ConfigurationServiceException;

    /**
     * Return a list of Domains matching the parameters.
     *
     * @param lastDomainName
     *            The last domain name.
     * @param maxResults
     *            The maximum number of results.
     * @return a List of Domains matching the parameters.
     * @throws ConfigurationServiceException
     */
    @WebMethod(operationName = "listDomains", action = "urn:listDomains")
    Collection<Domain> listDomains(@WebParam(name = "names") String lastDomainName,
            @WebParam(name = "maxResults") int maxResults) throws ConfigurationServiceException;

    /**
     * Return a collection of Domains matching the parameters.
     *
     * @param domain
     *            The Domain name.
     * @param status
     *            The Domain EntityStatus.
     * @return a collection of Domains matching the parameters.
     */
    @WebMethod(operationName = "searchDomain", action = "urn:SearchDomain")
    Collection<Domain> searchDomain(@WebParam(name = "name") String domain,
            @WebParam(name = "status") EntityStatus status);

    /**
     * Return a Domain matching the given ID.
     *
     * @param id
     *            The ID of the Domain.
     * @return a Domain matching the given ID.
     */
    @WebMethod(operationName = "getDomain", action = "urn:GetDomain")
    Domain getDomain(@WebParam(name = "id") Long id);
}
