/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.mpilib;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MpiDataSaver {
    private static Log log = LogFactory.getLog(MpiDataSaver.class);
    private static String filename = "mpi.xml";


    public static void SaveMpi(Patients patientList, String file)
    {
        log.info("Saving " + patientList.size() + " patient(s)");

        // Create output stream.
        log.info("Filename=" + file);

        FileOutputStream fos;
        
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MpiDataSaver.class.getName()).log(Level.SEVERE, null, ex);
            throw new UnableToInitializeMpi("Error accessing mpi storage", ex);
        }

        try {
            // Create XML encoder.
            XMLEncoder xenc = new XMLEncoder(fos);
            try {
                // Write object.
                xenc.writeObject(patientList);
                xenc.flush();
            } finally {
                xenc.close();
            }
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(MpiDataSaver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        log.info("Save complete");        
    }
    public static void SaveMpi(Patients patientList) {
        if ((patientList == null)) {
            log.info("Patiet List is null");
            patientList = new Patients();
        }
        SaveMpi(patientList, filename);
        

    }

    public static Patients LoadMpi(String file)
    {
        log.info("Loading patients");

        Patients patientList;
        File f;
        
        // Create input stream.
        log.info("Filename=" + file);
        log.info("user.dir: " + System.getProperty("user.dir"));

        f = new File(file);
            

        if (!f.exists()) {
            //file does not exist, so create it
            //i would like to replace this with ability to create a default mpi
            //for testing purposes
            try
            {
                f.createNewFile();
                SaveMpi(new Patients(), file);
            }
            catch (Exception ex)
            {
                throw new UnableToInitializeMpi("Error accessing mpi storage", ex);
            }
            
        }

        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MpiDataSaver.class.getName()).log(Level.SEVERE, null, ex);
            throw new UnableToInitializeMpi("Error accessing mpi storage", ex);
        }
        try {
            // Create XML decode.
            XMLDecoder xdec = new XMLDecoder(fis);
            try {
                // Write object.
                log.info("Loading object");
                Object o = xdec.readObject();
                log.info("casting object to 'patients'");
                patientList = (Patients) o;
            } finally {
                xdec.close();
            }
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(MpiDataSaver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        log.info("Loaded " + patientList.size() + " patient(s)");
        return patientList;        
    }
    public static Patients LoadMpi() {
        return LoadMpi(filename);
    }
}
