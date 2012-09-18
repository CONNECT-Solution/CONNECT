/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.properties;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author akong
 */
public class HibernateAccessor {
    private static Log log = LogFactory.getLog(HibernateAccessor.class);

    private static HibernateAccessor instance;
    
    private String propertyFileDir = "";
    private boolean failedToLoadEnvVar = false;
    
    protected HibernateAccessor() {
        loadPropertyFileDir();
    }
    
    public static HibernateAccessor getInstance() {
        if (instance == null) {
            instance = new HibernateAccessor();
        }
        return instance;
    }
    
    public synchronized File getHibernateFile(String hibernateFileName) throws PropertyAccessException {
        checkEnvVarSet();

        File result = new File(propertyFileDir + File.separator + "hibernate" + File.separator + hibernateFileName);
        if (!result.exists()) {
            throw new PropertyAccessException("Unable to locate " + result);
        }
        
        return result;
    }

    protected PropertyAccessor getPropertyAccessor() {
        return PropertyAccessor.getInstance();
    }
    
    private synchronized void loadPropertyFileDir() {
        propertyFileDir = getPropertyAccessor().getPropertyFileLocation();
        if (NullChecker.isNullish(propertyFileDir)) {
            log.error("Failed to load Hibernate Directory");
            failedToLoadEnvVar = true;
            return;
        }
        propertyFileDir = URLDecoder.decode(propertyFileDir);
        File dir = new File(propertyFileDir);
        propertyFileDir = dir.getAbsolutePath();
        
        if (!dir.exists()) {
            log.error("Failed to load Hibernate Directory");
            failedToLoadEnvVar = true;
        }
    }
    
    private synchronized boolean checkEnvVarSet() throws PropertyAccessException {
        if (failedToLoadEnvVar) {
            throw new PropertyAccessException("Failed to load Hibernate Directory");
        }

        return true; 
    }
    
    
}
