/*
 * Copyright (c) 2009-2014, United States Government, as represented by the Secretary of Health and Human Services.
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

import gov.hhs.fha.nhinc.directconfig.dao.AnchorDao;
import gov.hhs.fha.nhinc.directconfig.entity.Anchor;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.EntityStatus;
import gov.hhs.fha.nhinc.directconfig.service.AnchorService;
import gov.hhs.fha.nhinc.directconfig.service.ConfigurationServiceException;
import gov.hhs.fha.nhinc.directconfig.service.helpers.CertificateGetOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * Service class for methods related to an Anchor object.
 */
@Service
@WebService(endpointInterface = "gov.hhs.fha.nhinc.directconfig.service.AnchorService")
public class AnchorServiceImpl extends SpringBeanAutowiringSupport implements AnchorService {

    private static final Log log = LogFactory.getLog(AnchorServiceImpl.class);

    @Autowired
    private AnchorDao dao;

    /**
     * Initialization method.
     */
    @PostConstruct
    public void init() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        log.info("AnchorService initialized");
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.AnchorService#addAnchors(java.util.Collection)
     */
    @Override
    public void addAnchors(Collection<Anchor> anchors) throws ConfigurationServiceException {
        if (anchors != null && anchors.size() > 0) {
            for (Anchor anchor : anchors) {
                dao.add(anchor);
            }
        } else {
            log.debug("No anchors were provided.");
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.AnchorService#getAnchor(java.lang.String, java.lang.String,
     * gov.hhs.fha.nhinc.directconfig.service.impl.CertificateGetOptions)
     */
    @Override
    public Anchor getAnchor(String owner, String thumbprint, CertificateGetOptions options)
            throws ConfigurationServiceException {

        List<String> owners = new ArrayList<String>();
        owners.add(owner);

        List<Anchor> anchors = dao.list(owners);

        if (anchors == null || anchors.size() == 0) {
            log.debug("No anchors found for owner: " + owner);
            return null;
        }

        for (Anchor anchor : anchors) {
            if (anchor.getThumbprint().equalsIgnoreCase(thumbprint)) {
                log.debug("Single anchor found, returning: " + owner + ", " + thumbprint);
                return anchor;
            }
        }

        log.debug("Found " + anchors.size() + " anchors, but none with thumbprint: " + thumbprint);

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.AnchorService#getAnchors(java.util.Collection,
     * gov.hhs.fha.nhinc.directconfig.service.impl.CertificateGetOptions)
     */
    @Override
    public Collection<Anchor> getAnchors(Collection<Long> anchorIds, CertificateGetOptions options)
            throws ConfigurationServiceException {

        if (anchorIds == null || anchorIds.size() == 0) {
            log.debug("No anchor ids were provided.");
            return Collections.emptyList();
        }

        return dao.listByIds(new ArrayList<Long>(anchorIds));
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.AnchorService#getAnchorsForOwner(java.lang.String,
     * gov.hhs.fha.nhinc.directconfig.service.impl.CertificateGetOptions)
     */
    @Override
    public Collection<Anchor> getAnchorsForOwner(String owner, CertificateGetOptions options)
            throws ConfigurationServiceException {

        List<String> owners = new ArrayList<String>();
        owners.add(owner);

        return dao.list(owners);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.AnchorService#getIncomingAnchors(java.lang.String,
     * gov.hhs.fha.nhinc.directconfig.service.impl.CertificateGetOptions)
     */
    @Override
    public Collection<Anchor> getIncomingAnchors(String owner, CertificateGetOptions options)
            throws ConfigurationServiceException {

        Collection<Anchor> anchors = getAnchorsForOwner(owner, options);

        if (anchors == null || anchors.size() == 0) {
            log.debug("No anchors found for owner: " + owner);
            return Collections.emptyList();
        }

        Collection<Anchor> retList = new ArrayList<Anchor>();

        for (Anchor anchor : anchors) {
            if (anchor.isIncoming()) {
                retList.add(anchor);
            }
        }

        log.debug("Found " + retList.size() + " incoming anchors");

        return retList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.AnchorService#getOutgoingAnchors(java.lang.String,
     * gov.hhs.fha.nhinc.directconfig.service.impl.CertificateGetOptions)
     */
    @Override
    public Collection<Anchor> getOutgoingAnchors(String owner, CertificateGetOptions options)
            throws ConfigurationServiceException {

        Collection<Anchor> anchors = getAnchorsForOwner(owner, options);

        if (anchors == null || anchors.size() == 0) {
            log.debug("No anchors found for owner: " + owner);
            return Collections.emptyList();
        }

        Collection<Anchor> retList = new ArrayList<Anchor>();

        for (Anchor anchor : anchors) {
            if (anchor.isOutgoing()) {
                retList.add(anchor);
            }
        }

        log.debug("Found " + retList.size() + " incoming anchors");

        return retList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.AnchorService#setAnchorStatusForOwner(java.lang.String,
     * gov.hhs.fha.nhinc.directconfig.entity.EntityStatus)
     */
    @Override
    public void setAnchorStatusForOwner(String owner, EntityStatus status) throws ConfigurationServiceException {
        dao.setStatus(owner, status);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.AnchorService#listAnchors(java.lang.Long, int,
     * gov.hhs.fha.nhinc.directconfig.service.impl.CertificateGetOptions)
     */
    @Override
    public Collection<Anchor> listAnchors(Long lastAnchorID, int maxResults, CertificateGetOptions options)
            throws ConfigurationServiceException {

        // Direct RI comment: just get all for now
        return dao.listAll();
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.AnchorService#removeAnchors(java.util.Collection)
     */
    @Override
    public void removeAnchors(Collection<Long> anchorIds) throws ConfigurationServiceException {

        if (anchorIds == null || anchorIds.size() == 0) {
            log.debug("No anchor ids specified, returning....");
            return;
        }

        List<Long> ids = new ArrayList<Long>(anchorIds);

        dao.delete(ids);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.AnchorService#removeAnchorsForOwner(java.lang.String)
     */
    @Override
    public void removeAnchorsForOwner(String owner) throws ConfigurationServiceException {
        dao.delete(owner);
    }
}
