/*
 * Copyright (c) 2009-2016, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.connectmgr.uddi;

import gov.hhs.fha.nhinc.common.connectionmanagerinfo.SuccessOrFailType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.UDDIUpdateManagerForceRefreshRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.UDDIUpdateManagerForceRefreshResponseType;
import gov.hhs.fha.nhinc.connectmgr.persistance.dao.UddiConnectionInfoDAOFileImpl;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uddi.api_v3.BusinessDetail;

/**
 * Helper class for the web service.
 *
 * @author Les Westberg
 */
public class UDDIUpdateManagerHelper {

    private static final Logger LOG = LoggerFactory.getLogger(UDDIUpdateManagerHelper.class);
    private static final String GATEWAY_PROPERTY_FILE = "gateway";
    private static final String UDDI_REFRESH_KEEP_BACKUPS_PROPERTY = "UDDIRefreshKeepBackups";
    private static final String UDDI_MAX_NUM_BACKUPS_PROPERTY = "UDDIMaxNumBackups";
    private static final String UDDI_UPDATE_TIME = "UDDILastUpdate";
    private static final int MAX_NUM_BACKUP = 10;
    private static final ArrayList<String> backupFileList = new ArrayList<>();

    /**
     * This method retrieves the BusinessDetail data from the UDDI server. The BusinessDetail contains the list of
     * BusinessEntity that contains all the necessary information for each services.
     *
     * @return The BusinessDetail data retrieved from UDDI.
     */
    private static BusinessDetail retrieveDataFromUDDI() throws UDDIAccessorException {
        UDDIAccessor oUDDIAccessor = new UDDIAccessor();
        return oUDDIAccessor.retrieveFromUDDIServer();
    }

    /**
     * This method is called to force a refresh of the uddiConnectionInfo.xml file. It will retrieve the data from the
     * UDDI server and update the local XML file.
     *
     * @throws UDDIAccessorException
     */
    public void forceRefreshUDDIFile() throws UDDIAccessorException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start: UDDIUpdateManagerHelper.forceRefreshUDDIFile method - loading from UDDI server.");
        }

        try {
            BusinessDetail businessDetail = retrieveDataFromUDDI();
            if (businessDetail != null) {
                createUddiFileBackupByRenaming();
                saveUddiResultsToFile(businessDetail);
            }

        } catch (Exception e) {
            String sErrorMessage = "Failed to retrieve data from UDDI.  Error: " + e.getLocalizedMessage();
            LOG.error("Error refreshing UDDI: {}", sErrorMessage, e);
            throw new UDDIAccessorException(sErrorMessage, e);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Done: UDDIUpdateManagerHelper.forceRefreshUDDIFile method - loading from UDDI server.");
        }
    }

    /**
     * This method creates the uddi backup file if enabled by the configuration. The backup is created by renaming the
     * current Uddi file to a newly generated filename.
     */
    private void createUddiFileBackupByRenaming() {
        boolean createBackups = true;
        try {
            createBackups = PropertyAccessor.getInstance().getPropertyBoolean(GATEWAY_PROPERTY_FILE,
                    UDDI_REFRESH_KEEP_BACKUPS_PROPERTY);
        } catch (Exception e) {
            LOG.warn("Failed to retrieve property {} from {}.properties. Defaulting to creating backups: {}",
                    UDDI_REFRESH_KEEP_BACKUPS_PROPERTY, GATEWAY_PROPERTY_FILE, e.getLocalizedMessage(), e);
        }

        if (createBackups) {
            String uddiFileLocation = UddiConnectionInfoDAOFileImpl.getInstance().getUddiConnectionFileLocation();
            String backupUddiFileLocation = generateUniqueFilename(uddiFileLocation);

            try {
                File currentFile = new File(uddiFileLocation);
                File newBackupFile = new File(backupUddiFileLocation);

                if (currentFile.exists()) {
                    currentFile.renameTo(newBackupFile);

                    addToBackupList(backupUddiFileLocation);
                }
            } catch (Exception e) {
                LOG.error("Failed to rename the current file {} to {}: {}", uddiFileLocation, backupUddiFileLocation,
                        e.getLocalizedMessage(), e);
            }
        }
    }

    private String generateUniqueFilename(String fileLocation) {
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss");
        return fileLocation + "." + dateFormat.format(currentTime.getTime());
    }

    private void saveUddiResultsToFile(BusinessDetail businessDetail) {
        UddiConnectionInfoDAOFileImpl uddiDao = UddiConnectionInfoDAOFileImpl.getInstance();
        addLastUpdateTime(businessDetail);
        uddiDao.saveBusinessDetail(businessDetail);
    }

    private void addToBackupList(String latestFilename) {
        int maxNumBackup = MAX_NUM_BACKUP;
        try {
            maxNumBackup = (int) PropertyAccessor.getInstance().getPropertyLong(GATEWAY_PROPERTY_FILE,
                    UDDI_MAX_NUM_BACKUPS_PROPERTY);
        } catch (Exception e) {
            LOG.warn("Failed to retrieve property {} from {}.properties. Defaulting to {}: {}",
                    UDDI_MAX_NUM_BACKUPS_PROPERTY, GATEWAY_PROPERTY_FILE, MAX_NUM_BACKUP, e.getLocalizedMessage(), e);
        }

        String filenameToDelete = null;
        if (backupFileList.size() >= maxNumBackup) {
            filenameToDelete = backupFileList.remove(0);
        }
        backupFileList.add(latestFilename);

        if (filenameToDelete != null) {
            try {
                File fileToDelete = new File(filenameToDelete);
                if (fileToDelete.exists()) {
                    fileToDelete.delete();
                }
            } catch (Exception e) {
                LOG.warn("Failed to delete backup file {}: {}", filenameToDelete, e.getLocalizedMessage());
                LOG.trace("Deletion exception: {}", e.getLocalizedMessage(), e);
            }
        }
    }

    /**
     * This method forces a refresh of the uddiConnectionInfo.xml file by retrieving the information from the UDDI NHIN
     * server.
     *
     * @param part1 No real data - just a way to keep the document unique.
     * @return True if the file was loaded false if it was not.
     */
    public UDDIUpdateManagerForceRefreshResponseType forceRefreshFileFromUDDIServer(
            UDDIUpdateManagerForceRefreshRequestType part1) {
        UDDIUpdateManagerForceRefreshResponseType oResponse = new UDDIUpdateManagerForceRefreshResponseType();
        oResponse.setSuccessOrFail(new SuccessOrFailType());
        oResponse.getSuccessOrFail().setSuccess(false);

        try {
            forceRefreshUDDIFile();
            // If we got here - we succeeded.
            oResponse.getSuccessOrFail().setSuccess(true);
        } catch (Exception e) {
            LOG.error("Failed to refresh the file from the UDDI server: {}", e.getLocalizedMessage(), e);
        }

        return oResponse;
    }
    
    private void addLastUpdateTime(BusinessDetail businessDetail) {
        try {
            if(PropertyAccessor.getInstance().getPropertyBoolean(GATEWAY_PROPERTY_FILE, UDDI_UPDATE_TIME)) {
                businessDetail.setUddiUpdatetime(getTimestamp());
            }
        } catch (PropertyAccessException ex) {
            LOG.error("Unable to access property: " + UDDI_UPDATE_TIME, ex);
        } catch (DatatypeConfigurationException ex) {
            LOG.error("Unable to create timestamp for: " + UDDI_UPDATE_TIME, ex);
        }
    }
    
    private XMLGregorianCalendar getTimestamp() throws DatatypeConfigurationException {
        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(System.currentTimeMillis());
        return datatypeFactory.newXMLGregorianCalendar(gc);
    }

    /**
     * Main method to test this class. Since this is relys on an external UDDI server, we do not want it part of our
     * unit tests to stop the build if the server is down or not accessible. This is a main method used for
     * debugging....
     *
     * @param args
     */
    public static void main(String args[]) {
        LOG.info("Starting test.");

        try {
            UDDIUpdateManagerForceRefreshRequestType part1 = new UDDIUpdateManagerForceRefreshRequestType();
            UDDIUpdateManagerHelper helper = new UDDIUpdateManagerHelper();
            UDDIUpdateManagerForceRefreshResponseType oResponse = helper.forceRefreshFileFromUDDIServer(part1);
            LOG.info("Response = " + oResponse.getSuccessOrFail().isSuccess());
        } catch (Exception e) {
            LOG.error("An unexpected error occurred: {}", e.getLocalizedMessage(), e);
            throw new RuntimeException(e);
        }

        LOG.info("Ending test.");
    }
}
