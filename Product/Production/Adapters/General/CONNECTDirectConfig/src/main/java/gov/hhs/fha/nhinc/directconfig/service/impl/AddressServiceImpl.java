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

package gov.hhs.fha.nhinc.directconfig.service.impl;

import gov.hhs.fha.nhinc.directconfig.dao.AddressDao;
import gov.hhs.fha.nhinc.directconfig.entity.Address;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.EntityStatus;
import gov.hhs.fha.nhinc.directconfig.service.AddressService;
import gov.hhs.fha.nhinc.directconfig.service.ConfigurationServiceException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import javax.annotation.PostConstruct;
import javax.jws.WebService;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * Service class for methods related to an Address object.
 */
@Service("addressSvc")
@WebService(endpointInterface = "gov.hhs.fha.nhinc.directconfig.service.AddressService", portName = "ConfigurationServiceImplPort", targetNamespace = "http://nhind.org/config")
public class AddressServiceImpl extends SpringBeanAutowiringSupport implements AddressService {

    private static final Log log = LogFactory.getLog(AddressServiceImpl.class);

    @Autowired
    private AddressDao dao;

    /**
     * Initialization method.
     */
    @PostConstruct
    public void init() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        log.info("AddressService initialized");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addAddress(Collection<Address> address) throws ConfigurationServiceException {
        throw new ConfigurationServiceException(new NotImplementedException());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAddress(Address address) throws ConfigurationServiceException {
        throw new ConfigurationServiceException(new NotImplementedException());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAddressCount() throws ConfigurationServiceException {
        throw new ConfigurationServiceException(new NotImplementedException());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Address> getAddress(Collection<String> addressNames, EntityStatus status)
            throws ConfigurationServiceException {

        if (addressNames == null || addressNames.isEmpty()) {
            log.debug("No address names were provided.");
            return Collections.emptyList();
        }

        return dao.listAddresses(new ArrayList<>(addressNames), status);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAddress(String addressName) throws ConfigurationServiceException {
        if (addressName == null) {
            log.debug("No address name was provided.");
            return;
        }

        dao.delete(addressName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Address> listAddresss(String lastAddressName, int maxResults)
            throws ConfigurationServiceException {

        throw new ConfigurationServiceException(new NotImplementedException());
    }
}
