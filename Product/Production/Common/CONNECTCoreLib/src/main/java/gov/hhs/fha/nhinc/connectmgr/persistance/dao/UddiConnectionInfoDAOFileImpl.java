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
package gov.hhs.fha.nhinc.connectmgr.persistance.dao;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uddi.api_v3.BusinessDetail;

/**
 *
 * @author kshtabnoy
 *
 * Implementation of ConnectionManagerDAO that stores connection information in local file
 *
 */
public class UddiConnectionInfoDAOFileImpl extends ConnectionManagerDAOBase {

    private static UddiConnectionInfoDAOFileImpl instance = null;
    private File file = null;
    private static final Logger LOG = LoggerFactory.getLogger(UddiConnectionInfoDAOFileImpl.class);
    private static final String UDDI_XML_FILE_NAME = "uddiConnectionInfo.xml";

    public static UddiConnectionInfoDAOFileImpl getInstance() {
        if (instance == null) {
            instance = new UddiConnectionInfoDAOFileImpl();
        }
        return instance;
    }

    UddiConnectionInfoDAOFileImpl() {
        String fileName = getUddiConnectionFileLocation();
        LOG.debug("Reading UddiConnectionInfo from file: " + fileName);
        if (fileName != null) {
            file = new File(fileName);
        }
    }

    public String getUddiConnectionFileLocation() {
        if (file == null) {
            String sValue = PropertyAccessor.getInstance().getPropertyFileLocation();
            if (sValue != null && sValue.length() > 0) {
                if (sValue.endsWith(File.separator)) {
                    setFileName(sValue + UDDI_XML_FILE_NAME);
                } else {
                    setFileName(sValue + File.separator + UDDI_XML_FILE_NAME);
                }
            }
        }

        return isFile() ? file.getAbsolutePath() : null;
    }

    private boolean isFile() {
        return file != null && file.exists();
    }

    public BusinessDetail loadBusinessDetail() throws ConnectionManagerException {
        if (!isFile()) {
            throw new ConnectionManagerException("Unable to access system variable: nhinc.properties.dir.");
        }

        BusinessDetail resp;
        resp = super.loadExchangeInfo(BusinessDetail.class, file);
        return resp;
    }

    public void saveBusinessDetail(BusinessDetail BusinessDetail) {
        super.saveExchangeInfo(BusinessDetail, file);
    }

    public long getLastModified() {
        if (file.exists()) {
            return file.lastModified();
        } else {
            return 0;
        }
    }

    public void setFileName(String fileName) {
        file = new File(fileName);
    }
}
