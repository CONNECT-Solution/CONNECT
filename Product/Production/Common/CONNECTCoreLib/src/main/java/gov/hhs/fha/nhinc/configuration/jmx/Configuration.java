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
package gov.hhs.fha.nhinc.configuration.jmx;

import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class Configuration.
 *
 * @author msw
 */
public class Configuration implements ConfigurationMXBean {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);

    /** The Constant ERROR_ACCESSING_PROPERTY_FILE. */
    private static final String ERROR_ACCESSING_PROPERTY_FILE = "Error accessing property file: ";

    /**
     * Instantiates a new configuration.
     */
    public Configuration() {

    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.IConfiguration#getProperty(java.lang.String, java.lang.String)
     */
    @Override
    public String getProperty(String propertyFileName, String key) {
        String value = null;
        try {
            value = PropertyAccessor.getInstance().getProperty(propertyFileName, key);
        } catch (PropertyAccessException e) {
            LOG.error(generatePropertyAccessErrorMsg(propertyFileName), e);
        }
        return value;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.IConfiguration#setProperty(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public void setProperty(String propertyFileName, String key, String value) {
        try {
            PropertyAccessor.getInstance().setProperty(propertyFileName, key, value);
        } catch (PropertyAccessException e) {
            LOG.error(generatePropertyAccessErrorMsg(propertyFileName), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.Configuration#persistConfiguration()
     */
    @Override
    public void persistConfiguration() {
        throw new RuntimeException("Method not implemented");
    }

    /**
     * Generate property access error msg.
     *
     * @param file the name of the file
     * @return the error message string
     */
    private String generatePropertyAccessErrorMsg(String file) {
        StringBuilder sb = new StringBuilder();
        sb.append(ERROR_ACCESSING_PROPERTY_FILE);
        sb.append(file);
        return sb.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.IConfiguration#setPassthruMode()
     */
    @Override
    public void setPassthruMode() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        PassthruMXBeanRegistry registry = PassthruMXBeanRegistry.getInstance();
        registry.setPassthruMode();
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.IConfiguration#setStandardMode()
     */
    @Override
    public void setStandardMode() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        PassthruMXBeanRegistry registry = PassthruMXBeanRegistry.getInstance();
        registry.setStandardMode();
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.IConfiguration#setStandardMode(java.lang.String, java.lang.String)
     */
    @Override
    public void setPassthruMode(serviceEnum serviceName, directionEnum direction)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        PassthruMXBeanRegistry registry = PassthruMXBeanRegistry.getInstance();
        registry.setPassthruMode(serviceName, direction);

    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.IConfiguration#setPassthruMode(java.lang.String, java.lang.String)
     */
    @Override
    public void setStandardMode(serviceEnum serviceName, directionEnum direction)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        PassthruMXBeanRegistry registry = PassthruMXBeanRegistry.getInstance();
        registry.setStandardMode(serviceName, direction);
    }

    @Override
    public boolean isPassthru(serviceEnum serviceName, directionEnum direction) {
        boolean passthruMode;
        PassthruMXBeanRegistry registry = PassthruMXBeanRegistry.getInstance();
        passthruMode = registry.isPassthru(serviceName, direction);
        return passthruMode;
    }

    @Override
    public boolean isStandard(serviceEnum serviceName, directionEnum direction) {
        boolean standardMode;
        PassthruMXBeanRegistry registry = PassthruMXBeanRegistry.getInstance();
        standardMode = registry.isStandard(serviceName, direction);
        return standardMode;
    }
}
