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

import gov.hhs.fha.nhinc.directconfig.service.CertificateService;
import gov.hhs.fha.nhinc.directconfig.service.ConfigurationServiceException;

import java.security.Security;
import java.util.ArrayList;
import java.util.Collection;


import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nhindirect.config.model.utils.CertUtils;
import org.nhindirect.config.store.Certificate;
import org.nhindirect.config.store.EntityStatus;
import org.nhindirect.config.store.dao.CertificateDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Service class for methods related to a Certificate object.
 */
@WebService(endpointInterface = "org.nhindirect.config.service.CertificateService")
public class CertificateServiceImpl implements CertificateService {
	
    private static final Log log = LogFactory.getLog(CertificateServiceImpl.class);

    private CertificateDao dao;

    static
    {

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }
    
    /**
     * Set the value of the CertificateDao object.
     * 
     * @param aDao
     *            The value of the CertificateDao object.
     */
    @Autowired
    public void setDao(CertificateDao aDao) {
        dao = aDao;
    }

    /**
     * Initialization method.
     */
    public void init() {
        log.info("CertificateService initialized");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.CertificateService#addCertificates(java.util.Collection)
     */
    public void addCertificates(Collection<Certificate> certs) throws ConfigurationServiceException {
        
    	if (certs != null && certs.size() > 0)
    		for (Certificate cert : certs)
    		{
    			if ((cert.getOwner() == null || cert.getOwner().isEmpty()) && cert.getData() != null)
    			{
    				// get the owner from the certificate information
    				// first transform into a certificate
    				CertUtils.CertContainer cont = CertUtils.toCertContainer(cert.getData());
    				if (cont != null && cont.getCert() != null)
    				{
   					
    					// now get the owner info from the cert
    					String theOwner = CertUtils.getOwner(cont.getCert());

    					if (theOwner != null && !theOwner.isEmpty())
    						cert.setOwner(theOwner);
    				}
    				
    			}
    			dao.save(cert);
    		}
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.CertificateService#getCertificate(java.lang.String, java.lang.String, org.nhindirect.config.service.impl.CertificateGetOptions)
     */
    public Certificate getCertificate(String owner, String thumbprint, CertificateGetOptions options)
            throws ConfigurationServiceException {

        return dao.load(owner, thumbprint);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.CertificateService#getCertificates(java.util.Collection, org.nhindirect.config.service.impl.CertificateGetOptions)
     */
    public Collection<Certificate> getCertificates(Collection<Long> certIds, CertificateGetOptions options)
            throws ConfigurationServiceException {

    	return dao.list(new ArrayList<Long>(certIds));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.CertificateService#getCertificatesForOwner(java.lang.String, org.nhindirect.config.service.impl.CertificateGetOptions)
     */
    public Collection<Certificate> getCertificatesForOwner(String owner, CertificateGetOptions options)
            throws ConfigurationServiceException {

    	return dao.list(owner);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.CertificateService#setCertificateStatus(java.util.Collection, org.nhindirect.config.store.EntityStatus)
     */
    public void setCertificateStatus(Collection<Long> certificateIDs, EntityStatus status)
            throws ConfigurationServiceException {
       
    	dao.setStatus(new ArrayList<Long>(certificateIDs), status);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.CertificateService#setCertificateStatusForOwner(java.lang.String, org.nhindirect.config.store.EntityStatus)
     */
    public void setCertificateStatusForOwner(String owner, EntityStatus status) throws ConfigurationServiceException {
    	
        dao.setStatus(owner, status);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.CertificateService#removeCertificates(java.util.Collection)
     */
    public void removeCertificates(Collection<Long> certificateIds) throws ConfigurationServiceException {
        
    	dao.delete(new ArrayList<Long>(certificateIds));

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.CertificateService#removeCertificatesForOwner(java.lang.String)
     */
    public void removeCertificatesForOwner(String owner) throws ConfigurationServiceException {
        
    	dao.delete(owner);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.CertificateService#listCertificates(long, int, org.nhindirect.config.service.impl.CertificateGetOptions)
     */
    public Collection<Certificate> listCertificates(long lastCertificateID, int maxResults,
            CertificateGetOptions options) throws ConfigurationServiceException {
      
    	// just return all for now
    	return dao.list((String)null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nhindirect.config.service.CertificateService#contains(org.nhindirect.config.store.Certificate)
     */
    public boolean contains(Certificate cert) 
    {
        return dao.load(cert.getOwner(), cert.getThumbprint()) != null;
    } 
    
}
