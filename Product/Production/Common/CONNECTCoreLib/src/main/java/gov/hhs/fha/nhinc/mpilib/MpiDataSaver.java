/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.mpilib;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MpiDataSaver {
    private Log log = null;
    private String defaultMpiFilename = null;

    public MpiDataSaver() {
        log = createLogger();
        defaultMpiFilename = getDefaultMpiFilename();
    }

    public void saveMpi(Patients patientList) {
        saveMpi(patientList, defaultMpiFilename);
    }

    public void saveMpi(Patients patientList, String file) {
        if ((patientList == null)) {
            patientList = new Patients();
        }

        log.info("Saving " + patientList.size() + " patient(s)");
        log.info("Filename=" + file);

        FileOutputStream fos = null;
        try {
            fos = createFileOutputStream(file);
            writePatientList(fos, patientList);
        }
        catch (Exception e) {
            throw new UnableToInitializeMpi("Failed to save MPI.", e);
        } finally {
            closeFileOutputStream(fos);
        }
        
        log.info("Save complete");        
    }

    public Patients loadMpi() {
        return loadMpi(defaultMpiFilename);
    }

    public Patients loadMpi(String file) {
        log.info("Loading patients from " + file);
        
        openOrCreateMpiFile(file);

        Patients patientList = new Patients();
        FileInputStream fis = null;
        try {
            fis = createFileInputStream(file);
            patientList = readPatientList(fis);
        } catch (Exception e) {
            throw new UnableToInitializeMpi("Failed to load MPI.", e);
        } finally {
            closeFileInputStream(fis);
        }

        log.info("Loaded " + patientList.size() + " patient(s)");
        return patientList;        
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected void logException(Exception e) {
        Logger.getLogger(MpiDataSaver.class.getName()).log(Level.SEVERE, null, e);
    }

    protected String getDefaultMpiFilename() {
        return ((defaultMpiFilename != null) ? defaultMpiFilename : 
            PropertyAccessor.getPropertyFileLocation() + File.separator + "mpi.xml");
    }

    protected FileOutputStream createFileOutputStream(String file) {
        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            logException(e);
            throw new UnableToInitializeMpi("Error accessing mpi storage.", e);
        }
    }

    protected FileInputStream createFileInputStream(String file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            logException(e);
            throw new UnableToInitializeMpi("Error accessing mpi storage", e);
        }
    }

    protected void closeFileOutputStream(FileOutputStream fos) {
        try {
            fos.close();
        } catch (Exception ex) {
            logException(ex);
        }
    }

    protected void closeFileInputStream(FileInputStream fis) {
        try {
            fis.close();
        } catch (Exception ex) {
            logException(ex);
        }
    }

    protected void closeXMLEncoder(XMLEncoder xenc) {
        try {
            xenc.close();
        } catch (Exception ex) {
            logException(ex);
        }
    }

    protected void writePatientList(FileOutputStream fos, Patients patientList) {
        XMLEncoder xenc = new XMLEncoder(fos);
        try {
            xenc.writeObject(patientList);
            xenc.flush();
        } catch(Exception e) {
            logException(e);
            throw new UnableToInitializeMpi("Error writing patient list to xml.", e);
        } finally {
            closeXMLEncoder(xenc);
        }
    }

    protected Patients readPatientList(FileInputStream fis) {
        Patients patientList = new Patients();
        XMLDecoder xdec = new XMLDecoder(fis);
        try {
            Object o = xdec.readObject();
            patientList = (Patients) o;
        } catch(Exception e) {
            logException(e);
            throw new UnableToInitializeMpi("Error reading patient list.", e);
        } finally {
            xdec.close();
        }

        return patientList;
    }

    protected File openOrCreateMpiFile(String file) {
        File f = new File(file);
        if (!f.exists()) {
            try {
                f.createNewFile();
                saveMpi(new Patients(), file);
            } catch (Exception ex) {
                throw new UnableToInitializeMpi("Error accessing mpi storage", ex);
            }
        }

        return f;
    }

}
