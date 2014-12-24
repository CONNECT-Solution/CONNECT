/*
 * Copyright (c) 2009-2013, United States Government, as represented by the Secretary of Health and Human Services. 
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
import gov.hhs.fha.nhinc.util.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * @author akong, msw
 */
public class PropertyFileDAO {
    private static final Logger LOG = Logger.getLogger(PropertyFileDAO.class);
    
    private Hashtable<String, Properties> propertyFilesHashmap = new Hashtable<String, Properties>();

    PropertyFileDAO() {
        
    }
    
    public void loadPropertyFile(InputStream inStream, String propertyFileName) throws PropertyAccessException {
        if (inStream == null) {
            throw new PropertyAccessException("Failed to open property file:'" + propertyFileName + "'.  "
                    + "File does not exist.");
        }
        
        Properties properties = new Properties();
        try {
            properties.load(inStream);
        } catch (IOException e) {
            String sMessage = "Failed to load property file.  Error: " + e.getMessage();
            throw new PropertyAccessException(sMessage, e);
        }
        propertyFilesHashmap.put(propertyFileName, properties);
    }
    
    public void store(String file, String path) throws IOException {       
        FileOutputStream out = new FileOutputStream(path);        
        propertyFilesHashmap.get(file).store(out, null);
              
        StreamUtils.closeReaderSilently(out);
    }
    
    public void loadPropertyFile(File propertyFile, String propertyFileName) throws PropertyAccessException {                       
        Properties properties = new Properties();
        InputStreamReader propFile = null;

        try {           
            propFile = StreamUtils.openInputStream(propertyFile);

            properties.load(propFile);

            propertyFilesHashmap.put(propertyFileName, properties);
        } catch (Exception e) {
            String sMessage = "Failed to load property file.  Error: " + e.getMessage();
            throw new PropertyAccessException(sMessage, e);
        } finally {
            StreamUtils.closeFileSilently(propFile);
        }
    }
    
    public String getProperty(String propertyFileName, String propertyName) throws PropertyAccessException {
        Properties properties = propertyFilesHashmap.get(propertyFileName);
        if (properties != null) {
            String propertyValue = properties.getProperty(propertyName);
            if (NullChecker.isNotNullish(propertyValue)) {
                return propertyValue.trim();
            }
        }

        return null;
    }
    
    /**
     * Sets a property.
     *
     * @param propertyFileName the property file name
     * @param key the property key
     * @param value the property value
     * @throws PropertyAccessException the property access exception
     */
    public void setProperty(String propertyFileName, String key, String value) throws PropertyAccessException {
        Properties props = propertyFilesHashmap.get(propertyFileName);
        if (props != null) {
            props.setProperty(key, value);
        }
    }

    public boolean getPropertyBoolean(String propertyFileName, String propertyName) throws PropertyAccessException {
        Properties properties = propertyFilesHashmap.get(propertyFileName);
        if (properties != null) {
            String propertyValue = properties.getProperty(propertyName);
            if (NullChecker.isNotNullish(propertyValue) ) {
                if ((propertyValue.trim().equalsIgnoreCase("T")) || (propertyValue.trim().equalsIgnoreCase("TRUE"))) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        throw new PropertyAccessException("Could not find the property: " + propertyName + " in the file:" + propertyFileName);
    }
    
    public long getPropertyLong(String propertyFileName, String propertyName) throws PropertyAccessException {       
        Properties properties = propertyFilesHashmap.get(propertyFileName);
        if (properties != null) {
            String propertyValue = properties.getProperty(propertyName);
            if (NullChecker.isNotNullish(propertyValue)) {
                try {
                    return Long.parseLong(propertyValue.trim());
                } catch (Exception e) {                    
                    LOG.error("Unable to read the property..", e);
                    throw new PropertyAccessException("Unable to read the property..", e);
                }
            }
        } 
        throw new PropertyAccessException("Could not find the property: " + propertyName + " in the file:" + propertyFileName);
    }
    
    public Set<String> getPropertyNames(String propertyFileName) {
        Set<String> setPropNames = null;

        Properties properties = propertyFilesHashmap.get(propertyFileName);
        if (properties != null) {
            setPropNames = properties.stringPropertyNames();
        }
        return setPropNames;
    }
    
    public Properties getProperties(String propertyFileName) {
        Properties properties = propertyFilesHashmap.get(propertyFileName);
        
        return deepCopyProperties(properties);
    }
    
    public void printToLog(String propertyFileName) {
        
        Properties properties = propertyFilesHashmap.get(propertyFileName);        
        LOG.info("Dumping information for property file: " + propertyFileName);
        if (properties != null) {
            Set<String> setKeys = properties.stringPropertyNames();
            if (setKeys != null) {
                Iterator<String> iterKeys = setKeys.iterator();
                while (iterKeys.hasNext()) {
                    String sKey = iterKeys.next();
                    String sValue = properties.getProperty(sKey);
                    if (sValue != null) {
                        sValue = sValue.trim();
                    }
                    LOG.info("Property:" + sKey + "=" + sValue);
                }
            } 
            else {
                LOG.info("No properties were found in the property file.");
            }
        } else {
            LOG.info("No content.  Property file has never been loaded.");
        }
    }
    
    /**
     * This creates a new properties class with a full copy of all of the properties.
     * 
     * @param properties The property file that is to be copied.
     * @return The copy that is returned.
     */
    private Properties deepCopyProperties(Properties properties) {
        Properties oRetProps = new Properties();

        Set<String> setKeys = properties.stringPropertyNames();
        Iterator<String> iterKeys = setKeys.iterator();
        while (iterKeys.hasNext()) {
            String sKey = iterKeys.next();
            String sValue = properties.getProperty(sKey);
            if (sValue != null) {
                sValue = sValue.trim();
                oRetProps.put(sKey, sValue);
            }
        }

        return oRetProps;
    }
}
