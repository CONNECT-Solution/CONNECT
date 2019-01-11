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

import gov.hhs.fha.nhinc.directconfig.dao.AnchorDao;
import gov.hhs.fha.nhinc.directconfig.entity.Anchor;
import gov.hhs.fha.nhinc.directconfig.service.AnchorService;
import gov.hhs.fha.nhinc.directconfig.service.ConfigurationServiceException;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.AddAnchors;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.AddAnchorsResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetAnchor;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetAnchorResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetAnchors;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetAnchorsForOwner;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetAnchorsForOwnerResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetAnchorsResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetIncomingAnchors;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetIncomingAnchorsResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetOutgoingAnchors;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.GetOutgoingAnchorsResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.ListAnchors;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.ListAnchorsResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.RemoveAnchors;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.RemoveAnchorsForOwner;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.RemoveAnchorsForOwnerResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.RemoveAnchorsResponse;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.SetAnchorStatusForOwner;
import gov.hhs.fha.nhinc.directconfig.service.jaxws.SetAnchorStatusForOwnerResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.jws.WebService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * Service class for methods related to an Anchor object.
 */
@Service("anchorSvc")
@WebService(endpointInterface = "gov.hhs.fha.nhinc.directconfig.service.AnchorService", portName = "ConfigurationServiceImplPort", targetNamespace = "http://nhind.org/config")
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

    /**
     * {@inheritDoc}
     */
    @Override
    public AddAnchorsResponse addAnchors(AddAnchors addAnchors) throws ConfigurationServiceException {
        if (CollectionUtils.isNotEmpty(addAnchors.getAnchor())) {
            for (Anchor anchor : addAnchors.getAnchor()) {
                dao.add(anchor);
            }
        } else {
            log.debug("No anchors were provided.");
        }

        return new AddAnchorsResponse();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetAnchorResponse getAnchor(GetAnchor getAnchor) throws ConfigurationServiceException {
        GetAnchorResponse getAnchorResponse = new GetAnchorResponse();
        getAnchorResponse.setReturn(null);

        List<String> owners = new ArrayList<>();
        owners.add(getAnchor.getOwner());

        List<Anchor> anchors = dao.list(owners);

        if (anchors != null) {
            for (Anchor anchor : anchors) {
                if (anchor.getThumbprint().equalsIgnoreCase(getAnchor.getThumbprint())) {
                    log.debug("Anchor found matching supplied owner and thumbprint");
                    getAnchorResponse.setReturn(anchor);
                    break;
                }
            }
        }

        if (getAnchorResponse.getReturn() == null) {
            log.debug("No anchors found for owner");
        }

        return getAnchorResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetAnchorsResponse getAnchors(GetAnchors getAnchors) throws ConfigurationServiceException {
        GetAnchorsResponse getAnchorsResponse = new GetAnchorsResponse();
        List<Anchor> retList;

        if (CollectionUtils.isNotEmpty(getAnchors.getAnchorId())) {
            retList = dao.listByIds(new ArrayList<>(getAnchors.getAnchorId()));
        } else {
            log.debug("No anchor ids were provided.");
            retList = Collections.emptyList();
        }

        getAnchorsResponse.setReturn(retList);

        return getAnchorsResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetAnchorsForOwnerResponse getAnchorsForOwner(GetAnchorsForOwner getAnchorsForOwner)
        throws ConfigurationServiceException {

        List<String> owners = new ArrayList<>();
        owners.add(getAnchorsForOwner.getOwner());

        GetAnchorsForOwnerResponse anchors = new GetAnchorsForOwnerResponse();
        anchors.setReturn(dao.list(owners));

        return anchors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetIncomingAnchorsResponse getIncomingAnchors(GetIncomingAnchors getIncomingAnchors)
        throws ConfigurationServiceException {

        GetIncomingAnchorsResponse getIncomingAnchorsResponse = new GetIncomingAnchorsResponse();

        GetAnchorsForOwner getAnchorsForOwner = new GetAnchorsForOwner();
        getAnchorsForOwner.setOwner(getIncomingAnchors.getOwner());
        getAnchorsForOwner.setOptions(getIncomingAnchors.getOptions());

        Collection<Anchor> anchors = getAnchorsForOwner(getAnchorsForOwner).getReturn();
        Collection<Anchor> retList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(anchors)) {
            for (Anchor anchor : anchors) {
                if (anchor.isIncoming()) {
                    retList.add(anchor);
                }
            }

            log.debug("Found " + retList.size() + " incoming anchors");
        } else {
            log.debug("No anchors found for owner");
            retList = Collections.emptyList();
        }

        getIncomingAnchorsResponse.setReturn(retList);

        return getIncomingAnchorsResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GetOutgoingAnchorsResponse getOutgoingAnchors(GetOutgoingAnchors getOutgoingAnchors)
        throws ConfigurationServiceException {

        GetAnchorsForOwner getAnchorsForOwner = new GetAnchorsForOwner();
        getAnchorsForOwner.setOwner(getOutgoingAnchors.getOwner());
        getAnchorsForOwner.setOptions(getOutgoingAnchors.getOptions());

        Collection<Anchor> allAnchors = getAnchorsForOwner(getAnchorsForOwner).getReturn();
        Collection<Anchor> outgoingAnchors = new ArrayList<>();

        if (allAnchors != null) {
            for (Anchor anchor : allAnchors) {
                if (anchor.isOutgoing()) {
                    outgoingAnchors.add(anchor);
                }
            }

            log.debug("Found " + outgoingAnchors.size() + " incoming anchors for owner");
        } else {
            log.debug("No anchors found for owner");
            outgoingAnchors = Collections.emptyList();
        }

        GetOutgoingAnchorsResponse getOutgoingAnchorsResponse = new GetOutgoingAnchorsResponse();
        getOutgoingAnchorsResponse.setReturn(outgoingAnchors);

        return getOutgoingAnchorsResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SetAnchorStatusForOwnerResponse setAnchorStatusForOwner(SetAnchorStatusForOwner setAnchorStatusForOwner)
        throws ConfigurationServiceException {

        dao.setStatus(setAnchorStatusForOwner.getOwner(), setAnchorStatusForOwner.getStatus());

        return new SetAnchorStatusForOwnerResponse();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListAnchorsResponse listAnchors(ListAnchors listAnchors) throws ConfigurationServiceException {
        ListAnchorsResponse listAnchorsResponse = new ListAnchorsResponse();

        // Direct RI comment: just get all for now
        listAnchorsResponse.setReturn(dao.listAll());

        return listAnchorsResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RemoveAnchorsResponse removeAnchors(RemoveAnchors removeAnchors) throws ConfigurationServiceException {
        Collection<Long> ids = removeAnchors.getAnchorId();

        if (CollectionUtils.isNotEmpty(ids)) {
            dao.delete(new ArrayList<>(ids));
        } else {
            log.debug("No Anchor IDs specified for deletion.");
        }

        return new RemoveAnchorsResponse();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RemoveAnchorsForOwnerResponse removeAnchorsForOwner(RemoveAnchorsForOwner removeAnchorsForOwner)
        throws ConfigurationServiceException {

        dao.delete(removeAnchorsForOwner.getOwner());

        return new RemoveAnchorsForOwnerResponse();
    }
}
