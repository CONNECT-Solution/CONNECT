/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.lift.file.manager;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author JHOPPESC
 */
public class LiFTFileManager {

    private static Log log = LogFactory.getLog(LiFTFileManager.class);
    private final static int FILECHUNK = 65536;

    public boolean copyFile(String url, String guid) {
        boolean result = false;

        if (NullChecker.isNotNullish(url) &&
                NullChecker.isNotNullish(guid)) {
            // Open the destination directory on the file server
            File destDirectory = new File(createFileServerPath(guid));

            // Test if directory exists on Apache Server
            if (createDestDirectory(destDirectory) == false) {
                log.error("Could not create destination directory: " + destDirectory.getAbsolutePath());
                return false;
            }

            // Convert the url to a uri
            URI uri = null;

            try {
                uri = new URI(url);
            } catch (URISyntaxException e) {
                log.error("Unexpected form of url: " + url + ". " + e.getMessage());
                return false;
            }

            File sourceFile = new File(uri);

            String destPath = destDirectory.getAbsolutePath() + "/" + extractFile(url);

            result = transferFile(destPath, sourceFile);
        }

        return result;
    }

    protected String createFileServerPath(String guid) {
        String destDirectoryPath = null;

        if (NullChecker.isNotNullish(guid)) {
            // Get Properties to find apache/File server root location
            String fileServerRoot = getBaseDirectoryProperty();

            if (NullChecker.isNotNullish(fileServerRoot)) {
                log.debug("File Server root is: " + fileServerRoot);

                destDirectoryPath = fileServerRoot.concat("/" + guid);
                log.debug("File Server destination directory is: " + destDirectoryPath);
            }
        }

        return destDirectoryPath;
    }

    protected boolean createDestDirectory(File destDirectory) {
        if (!destDirectory.exists()) {
            if (!destDirectory.mkdirs()) {
                log.error("Failed to create directory to transfer file to: " + destDirectory);
                return false;
            }
        }

        // Make sure the directory is writable
        if (!(destDirectory.setWritable(true, false))) {
            log.error("Unabled to set " + destDirectory + " to writable");
            return false;
        }

        return true;
    }

    protected boolean transferFile(String filePath, File fileToMove) {
        boolean transferSucceeded = false;
        File inputFile = fileToMove;
        File outputFile = new File(filePath);
        byte[] c = new byte[FILECHUNK];
        int numBytes = 0;
        FileInputStream in = null;
        FileOutputStream out = null;

        try {
            in = new FileInputStream(inputFile);
            out = new FileOutputStream(outputFile);
            log.debug("Opened the input and output files successfully");

            while ((numBytes = in.read(c)) != -1) {
                log.debug("Attepting to write " + numBytes + " bytes of the file to the destination directory");
                out.write(c, 0, numBytes);
            }

            transferSucceeded = true;

        } catch (IOException e) {
            log.error("File mover failed to move file.", e);
            transferSucceeded = false;
        } finally {

            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    log.warn("Unable to close input stream: " + in);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    log.warn("Unable to close output stream: " + in);
                }
            }
        }


        return transferSucceeded;
    }

    private String getBaseDirectoryProperty() {
        String propVal = null;

        // Check the property file to retreive the property
        try {
            propVal = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_BASE_FILE_SERVER_DIR_PROP_NAME);
            log.debug("Returning base directory value of " + propVal);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.LIFT_BASE_FILE_SERVER_DIR_PROP_NAME + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }

        return propVal;
    }

    public String extractFile(String fileUrl) {
        String fileName = null;

        log.debug("Spliting file: " + fileUrl);

        String[] splitStrings = fileUrl.split("/");
        int len = splitStrings.length;

        fileName = splitStrings[len - 1];

        return fileName;
    }
}
