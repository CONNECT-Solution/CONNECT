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
package gov.hhs.fha.nhinc.properties;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.io.File;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConversionException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author akong, msw
 */
public class PropertyFileDAO {

    private static final Logger LOG = LoggerFactory.getLogger(PropertyFileDAO.class);

    private HashMap<String, PropertiesConfiguration> propertyFilesHashmap = new HashMap<>();

    PropertyFileDAO() {

    }

    public void loadPropertyFile(File propertyFile, String propertyFileName) throws PropertyAccessException {
        PropertiesConfiguration properties = new PropertiesConfiguration();

        try {
            properties.setReloadingStrategy(new FileChangedReloadingStrategy());
            properties.load(propertyFile);
            properties.setFile(propertyFile);
            properties.setAutoSave(false);
            properties.refresh();

            propertyFilesHashmap.put(propertyFileName, properties);
        } catch (ConfigurationException e) {
            String sMessage = "Failed to load property file.  Error: " + e.getMessage();
            throw new PropertyAccessException(sMessage, e);
        }
    }

    public String getProperty(String propertyFileName, String propertyName) throws PropertyAccessException {
        PropertiesConfiguration properties = propertyFilesHashmap.get(propertyFileName);
        if (properties != null && properties.containsKey(propertyName)) {
            String propertyValue = properties.getString(propertyName);
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
    public void setProperty(String propertyFileName, String key, Object value) throws PropertyAccessException {
        PropertiesConfiguration props = propertyFilesHashmap.get(propertyFileName);
        if (props != null) {
            props.setProperty(key, value);
            Date now = new Date();
            props.getLayout().setHeaderComment("Updated " + propertyFileName + ".properties at: " + now.toString());
            try {
                props.save();
            } catch (ConfigurationException ex) {
                throw new PropertyAccessException(ex.getMessage(), ex);
            }
        }
    }

    public String getPropertyComment(String propertyFileName, String key) {
        PropertiesConfiguration props = propertyFilesHashmap.get(propertyFileName);
        if (props != null && props.getLayout() != null) {
            String comment = props.getLayout().getComment(key);
            if (NullChecker.isNotNullish(comment) && !comment.startsWith("!")) {
                return comment.replaceAll("#", "");
            }
        }
        return "";
    }

    public boolean getPropertyBoolean(String propertyFileName, String propertyName) throws PropertyAccessException {
        PropertiesConfiguration properties = propertyFilesHashmap.get(propertyFileName);
        if (properties != null && properties.containsKey(propertyName)) {
            try {
                return properties.getBoolean(propertyName);
            } catch (ConversionException ex) {
                LOG.warn("Could not read property {}: {}", propertyName, ex.getLocalizedMessage(), ex);
                String sProp = properties.getString(propertyName);
                if (NullChecker.isNotNullish(sProp)) {
                    return sProp.equalsIgnoreCase("t");
                }
            }
        }
        throw new PropertyAccessException(getErrorMessage(propertyFileName, propertyName));
    }

    public long getPropertyLong(String propertyFileName, String propertyName) throws PropertyAccessException {
        PropertiesConfiguration properties = propertyFilesHashmap.get(propertyFileName);
        if (properties != null && properties.containsKey(propertyName)) {
            try {
                return properties.getLong(propertyName);
            } catch (ConversionException ex) {
                throw new PropertyAccessException("Could not convert property value to long for: " + propertyName, ex);
            }
        }
        throw new PropertyAccessException(getErrorMessage(propertyFileName, propertyName));
    }

    public Long getPropertyLongObject(String propertyFileName, String propertyName, Long defaultValue) {
        PropertiesConfiguration properties = propertyFilesHashmap.get(propertyFileName);
        if (properties != null && properties.containsKey(propertyName)) {
            try {
                return properties.getLong(propertyName, defaultValue);
            } catch (ConversionException ex) {
                LOG.error("Could not convert property value to long for: {} ", propertyName, ex);
            }
        }
        return defaultValue;
    }

    public Set<String> getPropertyNames(String propertyFileName) {
        Set<String> setPropNames = null;

        PropertiesConfiguration properties = propertyFilesHashmap.get(propertyFileName);
        if (properties != null && properties.getKeys() != null) {
            setPropNames = new HashSet<>();
            Iterator<String> keys = properties.getKeys();
            while (keys.hasNext()) {
                setPropNames.add(keys.next());
            }
        }
        return setPropNames;
    }

    public Properties getProperties(String propertyFileName) {
        PropertiesConfiguration properties = propertyFilesHashmap.get(propertyFileName);

        return deepCopyProperties(properties);
    }

    public void printToLog(String propertyFileName) {

        PropertiesConfiguration properties = propertyFilesHashmap.get(propertyFileName);
        LOG.info("Dumping information for property file: " + propertyFileName);
        if (properties != null) {
            Iterator<String> keys = properties.getKeys();
            if (keys != null) {
                while (keys.hasNext()) {
                    String sKey = keys.next();
                    String sValue = properties.getString(sKey);
                    if (sValue != null) {
                        sValue = sValue.trim();
                    }
                    LOG.info("Property:" + sKey + "=" + sValue);
                }
            } else {
                LOG.info("No properties were found in the property file.");
            }
        } else {
            LOG.info("No content.  Property file has never been loaded.");
        }
    }

    public boolean containsPropFile(String fileName) {
        return propertyFilesHashmap.containsKey(fileName);
    }

    /**
     * This creates a new properties class with a full copy of all of the properties.
     *
     * @param properties The property file that is to be copied.
     * @return The copy that is returned.
     */
    private Properties deepCopyProperties(PropertiesConfiguration properties) {
        Properties oRetProps = new Properties();

        if (properties != null) {
            Iterator<String> keys = properties.getKeys();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = properties.getString(key);
                if (value != null) {
                    value = value.trim();
                    oRetProps.put(key, value);
                }
            }
        }

        return oRetProps;
    }

    private static String getErrorMessage(String propertyFileName, String propertyName) {
        return MessageFormat.format("Could not find the property: {0} in the file: {1}", propertyName, propertyFileName);
    }
}
