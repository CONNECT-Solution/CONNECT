/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.adapter.reidentification;

import gov.hhs.fha.nhinc.adapter.reidentification.data.PseudonymMap;
import gov.hhs.fha.nhinc.adapter.reidentification.data.PseudonymMaps;
import gov.hhs.fha.nhinc.adapter.reidentification.data.PseudonymMapsXML;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is used to manage the Pseudonym map.  It provides operations that
 * can be used to retrieve, save, and locate items within the map.
 */
public class PseudonymMapManager {

    private static Log log = LogFactory.getLog(PseudonymMapManager.class);
    private static final String RE_ID_FILENAME = "reidentification.xml";
    private static PseudonymMaps pseudonymMaps = new PseudonymMaps();
    private static File reIdFile;

    /**
     * Lazy initializer for the location of the reidentification properties file
     */
    private static File getReidentificationFile() {
        if (reIdFile == null) {
            String propertyDir = PropertyAccessor.getPropertyFileLocation();
            if ((propertyDir != null) && (propertyDir.length() > 0)) {
                reIdFile = new File(propertyDir, RE_ID_FILENAME);
            } else {
                reIdFile = new File(RE_ID_FILENAME);
            }
        }
        return reIdFile;
    }

    /**
     * This method reads the map in from the XML text file, de-serializes it,
     * and holds it in a private member variable.
     */
    public static void readMap() {
        log.debug("Entering PseudonymMapManager.readMap");

        StringBuilder contents = new StringBuilder();
        File file = getReidentificationFile();
        try {
            if (file.exists() && file.canRead()) {
                BufferedReader input = new BufferedReader(new FileReader(file));
                try {
                    String line = null;
                    while ((line = input.readLine()) != null) {
                        contents.append(line);
                        //readLine chops off the newline character, so add it back
                        contents.append(System.getProperty("line.separator"));
                    }
                    log.debug("Reading Pseudonym Maps from: " + file.getPath() + "\n" + contents.toString());
                    //de-serialize the contents of the file to get a list of items
                    PseudonymMaps fileItems = PseudonymMapsXML.deserialize(contents.toString());
                    if(fileItems != null && fileItems.getPseudonymMap() != null){
                        pseudonymMaps = fileItems;
                    } else {
                        pseudonymMaps.getPseudonymMap().clear();
                    }
                } finally {
                    input.close();
                }
            } else {
                log.error(file.getCanonicalPath() + " does not exist or can not be read.");
            }
        } catch (IOException ex) {
            log.error("Exception on accessing " + file.getPath() + ": " + ex.getMessage());
        }
        log.debug("Exiting PseudonymMapManager.readMap");
    }

    /**
     * This operation takes the map that is located in the private internal
     * memory location and writes it out to disk, replacing the old copy of the
     * same file.
     */
    public static void writeMap() {
        log.debug("Entering PseudonymMapManager.writeMap");

        File file = getReidentificationFile();
        try {
            // if file does not exist, create it
            file.createNewFile();
            Writer output = null;
            try {
                output = new BufferedWriter(new FileWriter(file));
                // serialize the contents of the object and write it out
                log.debug("Writing serialied Pseudonym Maps to: " + file.getPath());
                output.write(PseudonymMapsXML.serialize(pseudonymMaps));
            } finally {
                output.close();
            }
        } catch (IOException ex) {
            log.error("Exception on accessing " + file.getPath() + ": " + ex.getMessage());
        }

        log.debug("Exiting PseudonymMapManager.writeMap");
    }

    /**
     * This method takes the pseudonym patient ID assigning authority with it's
     * corresponding pseudonym patient ID and returns the mapping that is
     * found in the map.  If no mapping is found, then null is returned.
     */
    public static PseudonymMap findPseudonymMap(String pseudonymPatientIdAssigningAuthority, String pseudonymPatientId) {
        log.debug("Entering PseudonymMapManager.findPseudonymMap with authority: " + pseudonymPatientIdAssigningAuthority + " and patient:" + pseudonymPatientId);

        PseudonymMap foundMap = null;
        if (pseudonymPatientIdAssigningAuthority != null && pseudonymPatientId != null) {
            for (PseudonymMap testMap : pseudonymMaps.getPseudonymMap()) {
                if (pseudonymPatientIdAssigningAuthority.equals(testMap.getPseudonymPatientIdAssigningAuthority()) && pseudonymPatientId.equals(testMap.getPseudonymPatientId())) {
                    foundMap = testMap;
                    log.debug("Found match: " + foundMap.toString());
                    break;
                }
            }
        }

        log.debug("Exiting PseudonymMapManager.findPseudonymMap: " + foundMap);
        return foundMap;
    }

    /**
     * This adds the pseudonym map to the mappings.  If there was already a map
     * for this pseudonym, then it replaces it, and passes back the old one.
     * If this is new, then null is returned.  Note that by design, a pseudonym
     * can only map to one real ID.  There cannot be a mapping of one pseudonym
     * and pseudonym assigning authority to multiple real IDs.
     */
    public static PseudonymMap addPseudonymMap(PseudonymMap pseudonymMap) {
        log.debug("Entering PseudonymMapManager.addPseudonymMap");
        PseudonymMap retMap = null;
        if (pseudonymMap != null) {
            String mapKey = pseudonymMap.getRealPatientId();
            if (mapKey != null) {
                boolean isIdFound = false;
                for (PseudonymMap testMap : pseudonymMaps.getPseudonymMap()) {
                    if (mapKey.equals(testMap.getRealPatientId())) {
                        // if successfully removed then return the previous map
                        // and add the new one
                        if (removePseudonymMap(testMap)) {
                            retMap = testMap;
                            pseudonymMaps.getPseudonymMap().add(pseudonymMap);
                            log.debug("New map for ID: " + mapKey + " has been stored.");
                        }
                        isIdFound = true;
                        break;
                    }
                }
                // if it was not already in the list add it
                if (!isIdFound) {
                    pseudonymMaps.getPseudonymMap().add(pseudonymMap);
                    log.debug("Map for ID: " + mapKey + " has been stored.");
                }
            } else {
                log.error("Attempt to add an pseudonym map with a null real patient ID has been rejected.");
            }
        } else {
            log.error("Attempt to add a null pseudonym map has been rejected.");
        }
        log.debug("Exiting PseudonymMapManager.addPseudonymMap");
        return retMap;
    }

    /**
     * This looks for and removes the specified pseudonym mapping from the set
     * of maps.  If it existed and is actually removed, then true is returned.
     * If it was never in the map to begin with then false is returned.
     */
    public static boolean removePseudonymMap(PseudonymMap pseudonymMap) {
        log.debug("Entering PseudonymMapManager.removePseudonymMap");
        boolean isRemoved = false;
        if (pseudonymMap != null) {
            String mapKey = pseudonymMap.getRealPatientId();
            if (mapKey != null) {
                for (PseudonymMap testMap : pseudonymMaps.getPseudonymMap()) {
                    if (mapKey.equals(testMap.getRealPatientId())) {
                        isRemoved = pseudonymMaps.getPseudonymMap().remove(testMap);
                        if (isRemoved) {
                            log.debug("Previous map for ID: " + mapKey + " has been removed.");
                        } else {
                            log.error("Attempt to remove map for ID: " + mapKey + " has failed.");
                        }
                        break;
                    }
                }
            }else {
                log.error("Attempt to remove an pseudonym map with a null real patient ID has been rejected.");
            }
        } else {
            log.error("Attempt to remove a null pseudonym map has been rejected.");
        }
        log.debug("Exiting PseudonymMapManager.removePseudonymMap removal: " + isRemoved);
        return isRemoved;
    }
}