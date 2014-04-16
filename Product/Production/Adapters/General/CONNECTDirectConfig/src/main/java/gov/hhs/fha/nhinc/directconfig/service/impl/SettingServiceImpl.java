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

import java.util.Arrays;
import java.util.Collection;

import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.directconfig.service.ConfigurationServiceException;
import gov.hhs.fha.nhinc.directconfig.service.SettingService;
import gov.hhs.fha.nhinc.directconfig.entity.Setting;
import gov.hhs.fha.nhinc.directconfig.dao.SettingDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Service class for methods related to a Service object.
 */
@WebService(endpointInterface = "gov.hhs.fha.nhinc.directconfig.service.SettingService")
public class SettingServiceImpl implements SettingService
{

    private static final Log log = LogFactory.getLog(SettingServiceImpl.class);

    @Autowired
    private SettingDao dao;
    
    /**
     * Initialization method.
     */
    public void init() {
        log.info("SettingService initialized");
    }

    
    /* 
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.SettingService#addSetting(java.lang.String, java.lang.String)
     */
    public void addSetting(String name, String value)
            throws ConfigurationServiceException {
        
        dao.add(name, value);
    }


    /* 
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.SettingService#deleteSetting(java.util.Collection)
     */
    public void deleteSetting(Collection<String> names) throws ConfigurationServiceException {
        
        dao.delete(names);        
    }


    /* 
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.SettingService#getAllSettings()
     */
    public Collection<Setting> getAllSettings()
            throws ConfigurationServiceException {
        
        return dao.getAll();
    }


    /* 
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.SettingService#getSettingByName(java.lang.String)
     */
    public Setting getSettingByName(String name)
            throws ConfigurationServiceException {
    
        Collection<Setting> settings = dao.getByNames(Arrays.asList(name));
        
        if (settings == null || settings.size() == 0)
            return null;
        
        return settings.iterator().next();
    }


    /* 
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.directconfig.service.SettingService#getSettingsByNames(java.util.Collection)
     */
    public Collection<Setting> getSettingsByNames(Collection<String> names)
            throws ConfigurationServiceException {

        return dao.getByNames(names);
    }


    public void updateSetting(String name, String value)
            throws ConfigurationServiceException {
        
        dao.update(name, value);    
    }

    /**
     * Set the value of the AnchorDao object.
     * 
     * @param dao
     *            the value of the AnchorDao object.
     */
    @Autowired
    public void setDao(SettingDao dao) {
        this.dao = dao;
    }
}
