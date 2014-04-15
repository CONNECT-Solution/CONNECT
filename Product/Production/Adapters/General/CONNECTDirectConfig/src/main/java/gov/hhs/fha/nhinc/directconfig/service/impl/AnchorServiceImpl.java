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

import gov.hhs.fha.nhinc.directconfig.service.AnchorService;
import gov.hhs.fha.nhinc.directconfig.service.ConfigurationServiceException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nhindirect.config.store.Anchor;
import org.nhindirect.config.store.EntityStatus;
import org.nhindirect.config.store.dao.AnchorDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Service class for methods related to an Anchor object.
 */
@WebService(endpointInterface = "org.nhindirect.config.service.AnchorService")
public class AnchorServiceImpl implements AnchorService {

    @SuppressWarnings("unused")
    private static final Log log = LogFactory.getLog(AnchorServiceImpl.class);

    private AnchorDao dao;

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.AnchorService#addAnchors(java.util.Collection
     * )
     */
    public void addAnchors(Collection<Anchor> anchors) throws ConfigurationServiceException
    {
    	if (anchors != null && anchors.size() > 0)
    		for (Anchor anchor : anchors)
    			dao.add(anchor);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.AnchorService#getAnchor(java.lang.String, java.lang.String, org.nhindirect.config.service.impl.CertificateGetOptions)
     */
    public Anchor getAnchor(String owner, String thumbprint, CertificateGetOptions options)
            throws ConfigurationServiceException 
    {    	
    	List<String> owners = new ArrayList<String>();
    	owners.add(owner);
    	
    	List<Anchor> anchors = dao.list(owners);
    	
    	if (anchors == null || anchors.size() == 0)
    		return null;
    	
    	for (Anchor anchor : anchors)
    		if (anchor.getThumbprint().equalsIgnoreCase(thumbprint))
    			return anchor;
    	
    	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.AnchorService#getAnchors(java.util.Collection, org.nhindirect.config.service.impl.CertificateGetOptions)
     */
    public Collection<Anchor> getAnchors(Collection<Long> anchorIds, CertificateGetOptions options)
            throws ConfigurationServiceException 
    {    	
    	if (anchorIds == null || anchorIds.size() == 0)
    		return Collections.emptyList();
    	
    	List<Long> ids = new ArrayList<Long>(anchorIds);   	    	
    	return dao.listByIds(ids);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.AnchorService#getAnchorsForOwner(java.lang.String, org.nhindirect.config.service.impl.CertificateGetOptions)
     */
    public Collection<Anchor> getAnchorsForOwner(String owner, CertificateGetOptions options)
            throws ConfigurationServiceException 
    {
    	List<String> owners = new ArrayList<String>();
    	owners.add(owner);
    	
    	return dao.list(owners);    	    	
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.AnchorService#getIncomingAnchors(java.lang.String, org.nhindirect.config.service.impl.CertificateGetOptions)
     */
    public Collection<Anchor> getIncomingAnchors(String owner, CertificateGetOptions options)
            throws ConfigurationServiceException 
    {    	
        Collection<Anchor> anchors = getAnchorsForOwner(owner, options);
        
        if (anchors == null || anchors.size() == 0)
        	return Collections.emptyList();

        Collection<Anchor> retList = new ArrayList<Anchor>();
        for (Anchor anchor : anchors)
        	if (anchor.isIncoming())
        		retList.add(anchor);
        
        return retList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.AnchorService#getOutgoingAnchors(java.lang.String, org.nhindirect.config.service.impl.CertificateGetOptions)
     */
    public Collection<Anchor> getOutgoingAnchors(String owner, CertificateGetOptions options)
            throws ConfigurationServiceException 
    {
        Collection<Anchor> anchors = getAnchorsForOwner(owner, options);
        
        if (anchors == null || anchors.size() == 0)
        	return Collections.emptyList();

        Collection<Anchor> retList = new ArrayList<Anchor>();
        for (Anchor anchor : anchors)
        	if (anchor.isOutgoing())
        		retList.add(anchor);
        
        return retList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.AnchorService#setAnchorStatusForOwner(java.lang.String, org.nhindirect.config.store.EntityStatus)
     */
    public void setAnchorStatusForOwner(String owner, EntityStatus status) throws ConfigurationServiceException 
    {
        dao.setStatus(owner, status);    	
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.AnchorService#listAnchors(java.lang.Long, int, org.nhindirect.config.service.impl.CertificateGetOptions)
     */
    public Collection<Anchor> listAnchors(Long lastAnchorID, int maxResults, CertificateGetOptions options)
            throws ConfigurationServiceException 
    {
    	// just get all for now
    	return dao.listAll();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.AnchorService#removeAnchors(java.util.Collection)
     */
    public void removeAnchors(Collection<Long> anchorIds) throws ConfigurationServiceException 
    {
    	if (anchorIds == null || anchorIds.size() == 0)
    		return;
    	
    	List<Long> ids = new ArrayList<Long>(anchorIds);
    	
   		dao.delete(ids);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.AnchorService#removeAnchorsForOwner(java.lang.String)
     */
    public void removeAnchorsForOwner(String owner) throws ConfigurationServiceException 
    {
    	dao.delete(owner);
    }

    /**
     * Set the value of the AnchorDao object.
     * 
     * @param dao
     *            the value of the AnchorDao object.
     */
    @Autowired
    public void setDao(AnchorDao dao) {
        this.dao = dao;
    }

    /**
     * Return the value of the AnchorDao object.
     * 
     * @return the value of the AnchorDao object.
     */
    public AnchorDao getDao() {
        return dao;
    }

}
