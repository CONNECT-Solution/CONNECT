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

import gov.hhs.fha.nhinc.util.StreamUtils;
import gov.hhs.fha.nhinc.util.StringUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author akong
 *
 */
public class PropertyAccessorFileUtilities {

    private static final Logger LOG = LoggerFactory.getLogger(PropertyAccessorFileUtilities.class);
    private String propertyFileDirAbsolutePath = "";

    /**
     * Constructor.
     */
    public PropertyAccessorFileUtilities() {
        checkPropertyFileDir();
    }

    protected void checkPropertyFileDir() {
        propertyFileDirAbsolutePath = System.getProperty("nhinc.properties.dir");

        if (propertyFileDirAbsolutePath == null) {
            LOG.error("Unable to determine the path to the configuration files.  "
                + "Please make sure that the runtime nhinc.properties.dir system property is set to the absolute location "
                + "of your CONNECT configuration files.");
            propertyFileDirAbsolutePath = "";
        } else {
            propertyFileDirAbsolutePath = addFileSeparatorSuffix(propertyFileDirAbsolutePath);
        }
    }

    /**
     * This method will return the path to the property files for the currently running servlet.
     */
    public String getPropertyFileLocation() {
        return propertyFileDirAbsolutePath;
    }

    public String getPropertyFileLocation(String propertyFile) {
        return propertyFileDirAbsolutePath + propertyFile + ".properties";
    }

    public void setPropertyFileLocation(String propertyFileDirAbsolutePath) {
        this.propertyFileDirAbsolutePath = addFileSeparatorSuffix(propertyFileDirAbsolutePath);
    }

    /**
     * This method will return the path to the property files for the currently running servlet.
     */
    public String getPropertyFileURL() {
        return "file:///" + propertyFileDirAbsolutePath;
    }

    /**
     * Loads the property file name into an object. Note that the property file name is assumed to be a properties file
     * and such the extension (.properties) should not be passed in.
     *
     * @param propertyFilename - String containing the filename only (no extension)
     * @return A Properties object containing the contents of the file. If the file does not exists, an empty Properties
     *         is returned.
     */
    public Properties loadPropertyFile(String propertyFilename) {
        return loadPropertyFile(new File(getPropertyFileLocation(propertyFilename)));
    }

    /**
     * Loads the property file name into an object.
     *
     * @param propertyFile - The properties file to load
     * @return A Properties object containing the contents of the file. If the file does not exists, an empty Properties
     *         is returned.
     */
    public Properties loadPropertyFile(File propertyFile) {
        Properties properties = new Properties();
        InputStreamReader propFile = null;
        FileInputStream propFIS = null;
        try {
            propFIS = new FileInputStream(propertyFile);
            propFile = new InputStreamReader(propFIS, StringUtil.UTF8_CHARSET);

            properties.load(propFile);
        } catch (Exception e) {
            LOG.error("Failed to load property file {}: {}", propertyFile, e.getLocalizedMessage(), e);
        } finally {
            StreamUtils.closeFileSilently(propFile);
            StreamUtils.closeStreamSilently(propFIS);
        }

        return properties;
    }

    private String addFileSeparatorSuffix(String dirPath) {
        if (dirPath != null && !dirPath.endsWith(File.separator)) {
            dirPath = dirPath + File.separator;
        }

        return dirPath;
    }
}
