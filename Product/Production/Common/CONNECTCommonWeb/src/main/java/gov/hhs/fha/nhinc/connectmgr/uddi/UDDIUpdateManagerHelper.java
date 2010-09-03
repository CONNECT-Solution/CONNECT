/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.connectmgr.uddi;

import gov.hhs.fha.nhinc.common.connectionmanagerinfo.SuccessOrFailType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.UDDIUpdateManagerForceRefreshRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.UDDIUpdateManagerForceRefreshResponseType;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessEntities;
import gov.hhs.fha.nhinc.connectmgr.data.CMUDDIConnectionInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMUDDIConnectionInfoXML;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Helper class for the web service.
 * 
 * @author Les Westberg
 */
public class UDDIUpdateManagerHelper {

    private static Log log = LogFactory.getLog(UDDIUpdateManagerHelper.class);
    private static final String GATEWAY_PROPERTY_FILE = "gateway";
    private static final String UDDI_REFRESH_KEEP_BACKUPS_PROPERTY = "UDDIRefreshKeepBackups";
    private static final String UDDI_CONNECTION_INFO_FILENAME = "uddiConnectionInfo.xml";

    /**
     * This method retrieves the data from the UDDI server and transforms it into
     * and XML string and returns it.
     * 
     * @return The XML representation of the data retrieved from UDDI.
     */
    private static String retrieveDataFromUDDI()
            throws UDDIAccessorException {
        String sXML = "";

        UDDIAccessor oUDDIAccessor = new UDDIAccessor();
        CMBusinessEntities oEntities = oUDDIAccessor.retrieveFromUDDIServer();

        if ((oEntities != null) &&
                (oEntities.getBusinessEntity() != null) &&
                (oEntities.getBusinessEntity().size() > 0)) {
            CMUDDIConnectionInfo oUDDIConnectionInfo = new CMUDDIConnectionInfo();
            oUDDIConnectionInfo.setBusinessEntities(oEntities);
            try {
                sXML = CMUDDIConnectionInfoXML.serialize(oUDDIConnectionInfo);
            } catch (Exception e) {
                String sErrorMessage = "Failed to serialize information from UDDI into valid XML message.  Error: " +
                        e.getMessage();
                log.error(sErrorMessage, e);
                throw new UDDIAccessorException(sErrorMessage, e);
            }
        }

        return sXML;
    }

    /**
     * This method is called to force a refresh of the uddiConnectionInfo.xml file.  
     * It will retrieve the data from the UDDI server and update the local XML file.
     * 
     * @throws UDDIAccessorException 
     */
    public void forceRefreshUDDIFile()
            throws UDDIAccessorException {
        if (log.isDebugEnabled()) {
            log.debug("Start: UDDIUpdateManagerHelper.forceRefreshUDDIFile method - loading from UDDI server.");
        }

        String sXML = "";
        Boolean bCreateBackups = true;
        String sXMLFileLocation = "";

        try {
            sXML = retrieveDataFromUDDI();
        } catch (Exception e) {
            String sErrorMessage = "Failed to retrieve data from UDDI.  Error: " + e.getMessage();
            log.error(sErrorMessage, e);
            throw new UDDIAccessorException(sErrorMessage, e);
        }

        if ((sXML != null) && (sXML.length() > 0)) {
            try {
                bCreateBackups = PropertyAccessor.getPropertyBoolean(GATEWAY_PROPERTY_FILE, UDDI_REFRESH_KEEP_BACKUPS_PROPERTY);
            } catch (Exception e) {
                bCreateBackups = true;
                String sErrorMessage = "Failed to retrieve property: " + UDDI_REFRESH_KEEP_BACKUPS_PROPERTY +
                        " from " + GATEWAY_PROPERTY_FILE + ".properties. Defaulting to creating backups. " +
                        "Error: " + e.getMessage();
                log.warn(sErrorMessage, e);
            }

            sXMLFileLocation = PropertyAccessor.getPropertyFileLocation() + UDDI_CONNECTION_INFO_FILENAME;
            File fCurrentFile = new File(sXMLFileLocation);

            Calendar oNow = Calendar.getInstance();
            SimpleDateFormat oFormat = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss");
            String sXMLFileLocationNewName = sXMLFileLocation + "." + oFormat.format(oNow.getTime());
            File fNewFileName = new File(sXMLFileLocationNewName);

            if (fCurrentFile.exists()) {

                try {
                    fCurrentFile.renameTo(fNewFileName);
                } catch (Exception e) {
                    String sErrorMessage = "Failed to rename the current file: " + sXMLFileLocation +
                            " to: " + sXMLFileLocationNewName + ".  Aborting changes to " +
                            "uddi connection information.  Error: " + e.getMessage();
                    log.error(sErrorMessage, e);
                    throw new UDDIAccessorException(sErrorMessage, e);
                }
            }   // if (fCurrentFile.exists())

            // Create the file with the new information.
            //-------------------------------------------
            FileWriter fwCurrentFile = null;
            try {
                fwCurrentFile = new FileWriter(fCurrentFile);
                fwCurrentFile.write(sXML);
            } catch (Exception e) {
                String sErrorMessage = "Failed to write file: " + sXMLFileLocation +
                        ". Error: " + e.getMessage();
                log.error(sErrorMessage, e);
                throw new UDDIAccessorException(sErrorMessage, e);
            } finally {
                if (fwCurrentFile != null) {
                    try {
                        fwCurrentFile.close();
                    } catch (Exception e) {
                        String sErrorMessage = "Failed to close file: " + sXMLFileLocation +
                                ". Error: " + e.getMessage();
                        log.error(sErrorMessage, e);
                        throw new UDDIAccessorException(sErrorMessage, e);
                    }
                }
            }

            // if we are not creating backups...  Then we need to delete our temproary one.  
            //------------------------------------------------------------------------------
            if (!bCreateBackups) {
                try {
                    fNewFileName.delete();
                } catch (Exception e) {
                    String sErrorMessage = "Failed to delete the temporary backup file: " + sXMLFileLocationNewName +
                            "Error: " + e.getMessage();
                    log.error(sErrorMessage, e);
                    throw new UDDIAccessorException(sErrorMessage, e);
                }
            }
        }   // if ((sXML != null) && (sXML.length() > 0))

        if (log.isDebugEnabled()) {
            log.debug("Done: UDDIUpdateManagerHelper.forceRefreshUDDIFile method - loading from UDDI server.");
        }
    }

    /**
     * This method forces a refresh of the uddiConnectionInfo.xml file by retrieving the
     * information from the UDDI NHIN server.
     *
     * @param part1 No real data - just a way to keep the document unique.
     * @return True if the file was loaded false if it was not.
     */
    public UDDIUpdateManagerForceRefreshResponseType forceRefreshFileFromUDDIServer(UDDIUpdateManagerForceRefreshRequestType part1) {
        UDDIUpdateManagerForceRefreshResponseType oResponse = new UDDIUpdateManagerForceRefreshResponseType();
        oResponse.setSuccessOrFail(new SuccessOrFailType());
        oResponse.getSuccessOrFail().setSuccess(false);

        try {
            forceRefreshUDDIFile();
            oResponse.getSuccessOrFail().setSuccess(true);      // If we got here - we succeeded.
        } catch (Exception e) {
            String sErrorMessage = "Failed to refresh the file from the UDDI server.  Error: " + e.getMessage();
            log.error(sErrorMessage, e);
        }

        return oResponse;
    }

    /**
     * Main method to test this class.   Since this is relys on an external UDDI server,
     * we do not want it part of our unit tests to stop the build if the server is
     * down or not accessible.  This is a main method used for debugging....
     * @param args
     */
    public static void main(String args[]) {
        System.out.println("Starting test.");

        try {
            UDDIUpdateManagerForceRefreshRequestType part1 = new UDDIUpdateManagerForceRefreshRequestType();
            UDDIUpdateManagerHelper helper = new UDDIUpdateManagerHelper();
            UDDIUpdateManagerForceRefreshResponseType oResponse = helper.forceRefreshFileFromUDDIServer(part1);
            System.out.println("Response = " + oResponse.getSuccessOrFail().isSuccess());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.println("Ending test.");

    }
}
