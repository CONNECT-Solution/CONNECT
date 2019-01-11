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

import gov.hhs.fha.nhinc.directconfig.entity.Certificate;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.EntityStatus;
import java.util.List;

/**
 * Certificate data access methods.
 */
public interface CertificateDao {

    /**
     * Load a Certificate.
     *
     * @param owner
     *            The Certificate owner.
     * @param thumbprint
     *            The Certificate thumbprint.
     * @return a Certificate.
     */
    public Certificate load(String owner, String thumbprint);

    /**
     * Get a collection of Certificates.
     *
     * @param idList
     *            The collection of Certificate IDs.
     * @return a collection of Certificates.
     */
    public List<Certificate> list(List<Long> idList);

    /**
     * Get a collection of Certificates.
     *
     * @param owner
     *            The Certificate owner.
     * @return a collection of Certificates.
     */
    public List<Certificate> list(String owner);

    /**
     * Save a Certificate.
     *
     * @param cert
     *            The Certificate.
     */
    public void save(Certificate cert);

    /**
     * Save a collection of Certificates.
     *
     * @param certList
     *            The Collection of Certificates.
     */
    public void save(List<Certificate> certList);

    /**
     * Set the status of a collection of Certificates.
     *
     * @param certificateIDs
     *            The collection of Certificate IDs.
     * @param status
     *            The Certificate status.
     */
    public void setStatus(List<Long> certificateIDs, EntityStatus status);

    /**
     * Set the status of a collection of Certificates.
     *
     * @param owner
     *            The Certificate owner.
     * @param status
     *            The Certificate status.
     */
    public void setStatus(String owner, EntityStatus status);

    /**
     * Delete a collection of Certificates.
     *
     * @param idList
     *            The collection of Certificate IDs.
     */
    public void delete(List<Long> idList);

    /**
     * Delete a collection of Certificates.
     *
     * @param owner
     *            The Certificate owner.
     */
    public void delete(String owner);

}
