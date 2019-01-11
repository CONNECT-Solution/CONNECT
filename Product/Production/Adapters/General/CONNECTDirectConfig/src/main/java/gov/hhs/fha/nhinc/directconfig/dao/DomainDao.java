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

import gov.hhs.fha.nhinc.directconfig.entity.Domain;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.EntityStatus;
import java.util.List;

/**
 * Domain data access methods.
 */
public interface DomainDao {

    /**
     * Get a count of Domains.
     *
     * @return a count of Domains.
     */
    public int count();

    /**
     * Add a Domain.
     *
     * @param item
     *            The Domain.
     */
    public void add(Domain item);

    /**
     * Update a Domain.
     *
     * @param item
     *            The Domain.
     */
    public void update(Domain item);

    /**
     * Save a Domain.
     *
     * @param item
     *            The Domain.
     */
    public void save(Domain item);

    /**
     * Delete a Domain.
     *
     * @param name
     *            The Domain name.
     */
    public void delete(String name);

    /**
     * Delete a Domain.
     *
     * @param name
     *            The Domain name.
     */
    public void delete(Long anId);

    /**
     * Get a Domain.
     *
     * @param domain
     *            The Domain name.
     * @return a Domain.
     */
    public Domain getDomainByName(String domain);

    /**
     * Get a Domain.
     *
     * @param id
     *            The Domain id.
     * @return a Domain.
     */
    public Domain getDomain(Long id);

    /**
     * Get a collection of Domains.
     *
     * @param name
     *            The Domain name.
     * @param status
     *            The Domain status.
     * @return a collection of Domains.
     */
    public List<Domain> searchDomain(String name, EntityStatus status);

    /**
     * Get a collection of Domains.
     *
     * @param name
     *            The collection of Domain names.
     * @param status
     *            The Domain status.
     * @return a collection of Domains.
     */
    public List<Domain> getDomains(List<String> name, EntityStatus status);

    /**
     * Get a collection of Domains.
     *
     * @param name
     *            The Domain name.
     * @param count
     *            The count of Domains.
     * @return a collection of Domains.
     */
    public List<Domain> listDomains(String name, int count);

}
