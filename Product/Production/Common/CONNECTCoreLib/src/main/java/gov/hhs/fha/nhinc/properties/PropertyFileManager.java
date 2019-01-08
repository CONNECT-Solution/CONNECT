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
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to manage a property file programmtically.
 *
 * @author Les Westberg
 */
public class PropertyFileManager {

    private static final Logger LOG = LoggerFactory.getLogger(PropertyFileManager.class);

    /**
     * This saves out the properties to the specified property file. If the file already exists, it is replaced by the
     * specified one. If it does not exist, it is created.
     *
     * @param sPropertyFile The name of the property file without the ".properties" extension.
     * @param oProps The contents to write out as the property file.
     * @throws PropertyAccessException This exception is thrown if there are any errors.
     */
    public static void writePropertyFile(String sPropertyFile, Properties oProps) throws PropertyAccessException {
        if (sPropertyFile == null || sPropertyFile.length() <= 0) {
            throw new PropertyAccessException("writePropertyFile called with no property file name.");
        }

        if (oProps == null) {
            throw new PropertyAccessException("writePropertyFile called with no property file.");
        }

        String sPropFile = PropertyAccessor.getInstance().getPropertyFileLocation(sPropertyFile);
        OutputStreamWriter fwPropFile = null;
        FileOutputStream propFOS = null;
        Exception eError = null;
        String sErrorMessage = "";

        try {
            propFOS = new FileOutputStream(sPropertyFile);
            fwPropFile = new OutputStreamWriter(propFOS, StringUtil.UTF8_CHARSET);
            oProps.store(fwPropFile, "");
        } catch (Exception e) {
            sErrorMessage = "Failed to store property file: " + sPropFile + ".  Error: " + e.getMessage();
            eError = e;
        } finally {
            StreamUtils.closeReaderSilently(propFOS);
            StreamUtils.closeWriterSilently(fwPropFile);
        }

        if (eError != null) {
            throw new PropertyAccessException(sErrorMessage, eError);
        }
    }

    /**
     * Delete the specified property file.
     *
     * @param sPropertyFile The file to be deleted. This is the name of the property file without the ".properties"
     *            extension. It must be located in the configured properties directory.
     * @throws gov.hhs.fha.nhinc.properties.PropertyAccessException This exception is thrown if there is an error.
     */
    public static void deletePropertyFile(String sPropertyFile) throws PropertyAccessException {
        String sPropFile = PropertyAccessor.getInstance().getPropertyFileLocation(sPropertyFile);
        File fPropFile = new File(sPropFile);

        try {
            if (fPropFile.exists()) {
                boolean fileDeleted = fPropFile.delete();
                LOG.info("File deleted. fPropFile.delete() returned:" + fileDeleted);
            }
        } catch (Exception e) {
            throw new PropertyAccessException("Failed to delete file: " + sPropFile + ".  Error: " + e.getMessage(), e);
        }
    }
}
