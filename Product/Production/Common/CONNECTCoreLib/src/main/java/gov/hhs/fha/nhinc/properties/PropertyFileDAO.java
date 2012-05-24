/**
 *Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 *All rights reserved.
 *
 *Redistribution and use in source and binary forms, with or without
 *modification, are permitted provided that the following conditions are met:
 *    * Redistributions of source code must retain the above
 *      copyright notice, this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *    * Neither the name of the United States Government nor the
 *      names of its contributors may be used to endorse or promote products
 *      derived from this software without specific prior written permission.
 *
 *THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 *DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package gov.hhs.fha.nhinc.properties;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;

import java.io.File;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author akong
 * 
 */
public class PropertyFileDAO {
    private static Log log = LogFactory.getLog(PropertyFileDAO.class);
    
    private Hashtable<String, Properties> propertyFilesHashmap = new Hashtable<String, Properties>();

    PropertyFileDAO() {
        
    }
    
    public void loadPropertyFile(File propertyFile, String propertyFileName) throws PropertyAccessException {                       
        Properties properties = new Properties();
        FileReader propFile = null;
        try {           
            if (!propertyFile.exists()) {
                throw new PropertyAccessException("Failed to open property file:'" + propertyFile + "'.  "
                        + "File does not exist.");
            }
            propFile = new FileReader(propertyFile);
            properties.load(propFile);
            
            propertyFilesHashmap.put(propertyFileName, properties);
           
        } catch (Exception e) {
            String sMessage = "Failed to load property file.  Error: " + e.getMessage();
            throw new PropertyAccessException(sMessage, e);
        } finally {
            if (propFile != null) {
                try {
                    propFile.close();
                } catch (Exception e1) {
                    log.error("Failed to close property file: '" + propertyFile + "'", e1);
                }
            }
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
                    String errorMsg = "Failed to convert string value: '" + propertyValue + "' to a long.  Error: "
                            + e.getMessage();
                    getLogger().error(errorMsg, e);
                    throw new PropertyAccessException(errorMsg, e);
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
        Log log = getLogger();
        
        Properties properties = propertyFilesHashmap.get(propertyFileName);        
        log.info("Dumping information for property file: " + propertyFileName);
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
                    log.info("Property:" + sKey + "=" + sValue);
                }
            } 
            else {
                log.info("No properties were found in the property file.");
            }
        } else {
            log.info("No content.  Property file has never been loaded.");
        }
    }
        
    protected Log getLogger() {
        return log;
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
            }
            oRetProps.put(sKey, sValue);
        }

        return oRetProps;
    }
}
