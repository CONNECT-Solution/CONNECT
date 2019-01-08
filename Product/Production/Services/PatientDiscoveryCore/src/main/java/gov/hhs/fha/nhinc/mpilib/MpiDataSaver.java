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
package gov.hhs.fha.nhinc.mpilib;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class MpiDataSaver {
    private static final Logger LOG = LoggerFactory.getLogger(MpiDataSaver.class);
    private String defaultMpiFilename = null;

    /**
     * Default constructor for this class.
     */
    public MpiDataSaver() {
        defaultMpiFilename = getDefaultMpiFilename();
    }

    /**
     * Method saves the MPI with the patient list passed in, and uses the default MPI filename.
     *
     * @param patientList the PatientList used to create the MPI
     */
    public void saveMpi(Patients patientList) {
        saveMpi(patientList, defaultMpiFilename);
    }

    /**
     * Method saves the MPI with the patient list passed in to the file provided.
     *
     * @param patientList the PatientList used to create the MPI
     * @param file the file to save the MPI to
     */
    public void saveMpi(Patients patientList, String file) {
        if ((patientList == null)) {
            patientList = new Patients();
        }

        LOG.info("Saving " + patientList.size() + " patient(s)");
        LOG.info("Filename=" + file);

        FileOutputStream fos = null;
        try {
            fos = createFileOutputStream(file);
            writePatientList(fos, patientList);
        } catch (Exception e) {
            throw new MpiException("Failed to save MPI.", e);
        } finally {
            closeFileOutputStream(fos);
        }

        LOG.info("Save complete");
    }

    /**
     * @return the patient list from the default MPI file.
     */
    public Patients loadMpi() {
        return loadMpi(defaultMpiFilename);
    }

    /**
     * @param file the MPI file
     * @return the patient list from the given filename
     */
    public Patients loadMpi(String file) {
        LOG.info("Loading patients from " + file);

        openOrCreateMpiFile(file);

        Patients patientList = new Patients();
        FileInputStream fis = null;
        try {
            fis = createFileInputStream(file);
            patientList = readPatientList(fis);
        } catch (Exception e) {
            throw new MpiException("Failed to load MPI.", e);
        } finally {
            closeFileInputStream(fis);
        }

        LOG.info("Loaded " + patientList.size() + " patient(s)");
        return patientList;
    }

    /**
     * @return the default mpi filename
     */
    protected String getDefaultMpiFilename() {
        return ((defaultMpiFilename != null) ? defaultMpiFilename : PropertyAccessor.getInstance()
                .getPropertyFileLocation() + File.separator + "mpi.xml");
    }

    /**
     * @param file
     * @return a file output stream
     */
    protected FileOutputStream createFileOutputStream(String file) {
        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            LOG.error("File not found: " + file, e);
            throw new MpiException("Error accessing mpi storage.", e);
        }
    }

    protected FileInputStream createFileInputStream(String file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            LOG.error("File not found: " + file, e);
            throw new MpiException("Error accessing mpi storage", e);
        }
    }

    protected void closeFileOutputStream(FileOutputStream fos) {
        try {
            fos.close();
        } catch (Exception ex) {
            LOG.error("Error closing FileOutputStream.", ex);
        }
    }

    protected void closeFileInputStream(FileInputStream fis) {
        try {
            fis.close();
        } catch (Exception ex) {
            LOG.error("Error closing FileInputStream.", ex);
        }
    }

    protected void writePatientList(FileOutputStream fos, Patients patientList) {
        XMLEncoder xenc = new XMLEncoder(fos);
        try {
            xenc.writeObject(patientList);
            xenc.flush();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new MpiException("Error writing patient list to xml.", e);
        } finally {
            xenc.close();
        }
    }

    protected Patients readPatientList(FileInputStream fis) {
        Patients patientList = new Patients();
        XMLDecoder xdec = new XMLDecoder(fis);
        try {
            Object o = xdec.readObject();
            patientList = (Patients) o;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new MpiException("Error reading patient list.", e);
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
                throw new MpiException("Error accessing mpi storage", ex);
            }
        }

        return f;
    }

}
