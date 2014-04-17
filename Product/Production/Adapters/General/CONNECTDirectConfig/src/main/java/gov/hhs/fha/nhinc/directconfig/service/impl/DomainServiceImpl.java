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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.directconfig.service.ConfigurationServiceException;
import gov.hhs.fha.nhinc.directconfig.service.DomainService;
import gov.hhs.fha.nhinc.directconfig.entity.Domain;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.EntityStatus;
import gov.hhs.fha.nhinc.directconfig.dao.DomainDao;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Service class for methods related to a Domain object.
 */
@org.springframework.stereotype.Service
@WebService(endpointInterface = "gov.hhs.fha.nhinc.directconfig.service.DomainService")
public class DomainServiceImpl extends org.springframework.web.context.support.SpringBeanAutowiringSupport implements DomainService {

    private static final Log log = LogFactory.getLog(DomainServiceImpl.class);

    @Autowired
    private DomainDao dao;

    /**
     * Initialization method.
     */
    public void init() {
        log.info("DomainService initialized");
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.DomainService#addDomain(gov.hhs.fha.nhinc.directconfig.entity.Domain)
     */
    public void addDomain(Domain domain) throws ConfigurationServiceException {
        if (log.isDebugEnabled())
            log.debug("Enter");

        dao.add(domain);
        log.info("Added Domain: " + domain.getDomainName());
        if (log.isDebugEnabled())
            log.debug("Exit");
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.DomainService#updateDomain(gov.hhs.fha.nhinc.directconfig.entity.Domain)
     */
    public void updateDomain(Domain domain) throws ConfigurationServiceException {
        if (log.isDebugEnabled())
            log.debug("Enter");

        if (domain != null) {
            dao.update(domain);
            log.info("Modified Domain: " + domain.getDomainName());
        }

        if (log.isDebugEnabled())
            log.debug("Exit");
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.DomainService#getDomainCount()
     */
    public int getDomainCount() throws ConfigurationServiceException {
        return dao.count();
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.DomainService#getDomains(java.util.Collection, gov.hhs.fha.nhinc.directconfig.entity.EntityStatus)
     */
    public Collection<Domain> getDomains(Collection<String> domainNames, EntityStatus status)
            throws ConfigurationServiceException {
        ArrayList<String> domains = new ArrayList<String>(domainNames);
        return dao.getDomains(domains, status);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.DomainService#removeDomain(java.lang.String)
     */
    public void removeDomain(String domainName) throws ConfigurationServiceException {
        if (log.isDebugEnabled())
            log.debug("Enter");

        dao.delete(domainName);
        log.info("Modified Domain: " + domainName);

        if (log.isDebugEnabled())
            log.debug("Exit");
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.DomainService#removeDomainById(java.lang.Long)
     */
    public void removeDomainById(Long domainId) throws ConfigurationServiceException {
        if (log.isDebugEnabled())
            log.debug("Enter");

        dao.delete(domainId);
        log.info("Modified Domain: " + domainId);

        if (log.isDebugEnabled())
            log.debug("Exit");
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.DomainService#listDomains(java.lang.String, int)
     */
    public Collection<Domain> listDomains(String lastDomainName, int maxResults) throws ConfigurationServiceException {
        if (log.isDebugEnabled())
            log.debug("Enter");

        List<Domain> result = dao.listDomains(lastDomainName, maxResults);

        if (log.isDebugEnabled()) {
            if (result == null) {
                log.debug("Exit: NULL");
            } else {
                log.debug("Exit: " + result.toString());
            }
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.DomainService#searchDomain(java.lang.String, gov.hhs.fha.nhinc.directconfig.entity.EntityStatus)
     */
    public Collection<Domain> searchDomain(String domain, EntityStatus status) {
        if (log.isDebugEnabled())
            log.debug("Enter");

        List<Domain> result = dao.searchDomain(domain, status);

        if (log.isDebugEnabled()) {
            if (result == null) {
                log.debug("Exit: NULL");
            } else {
                log.debug("Exit: " + result.toString());
            }
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.DomainService#getDomain(java.lang.Long)
     */
    public Domain getDomain(Long id) {
        if (log.isDebugEnabled())
            log.debug("Enter");

        Domain result = dao.getDomain(id);

        if (log.isDebugEnabled()) {
            if (result == null) {
                log.debug("Exit: NULL");
            } else {
                log.debug("Exit: " + result.toString());
            }
        }

        return result;
    }

}
