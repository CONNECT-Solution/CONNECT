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
package gov.hhs.fha.nhinc.patientdb.dao;

import gov.hhs.fha.nhinc.patientdb.model.Phonenumber;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * PhonenumberDAO Class provides methods to query and update Phonenumber Data to/from MySQL Database using Hibernate
 *
 * @author richard.ettema
 */
public class PhonenumberDAO extends GenericDAOImpl<Phonenumber> {

    private static final Logger LOG = LoggerFactory.getLogger(PhonenumberDAO.class);
    private static PhonenumberDAO phonenumberDAO = new PhonenumberDAO();

    /**
     *
     * Constructor
     */
    private PhonenumberDAO() {
        super(Phonenumber.class);
        LOG.trace("PhonenumberDAO - Initialized");
    }

    /**
     *
     * Singleton instance returned...
     *
     * @return PhonenumberDAO
     */
    public static PhonenumberDAO getPhonenumberDAOInstance() {
        LOG.trace("getPhonenumberDAOInstance()..");
        return phonenumberDAO;
    }

    // =========================
    // Standard CRUD DML Methods
    // =========================
    /**
     *
     * Create a single <code>Phonenumber</code> record. The generated id
     *
     * will be available in the phonenumberRecord.
     *
     * @param phonenumberRecord
     *
     * @return boolean
     */
    @Override
    public boolean create(Phonenumber phonenumberRecord) {
        LOG.trace("PhonenumberDAO.create() - Begin");
        return phonenumberRecord != null ? super.create(phonenumberRecord) : true;
    }

    /**
     *
     * Read (Query) the database to get a <code>Phonenumber</code> record based
     *
     * on a known id.
     *
     * @param id
     *
     * @return Phonenumber
     */
    public Phonenumber read(Long id) {
        LOG.trace("PhonenumberDAO.read() - Begin");
        if (id == null) {
            LOG.trace("-- id Parameter is required for Phonenumber Query --");
            LOG.trace("PhonenumberDAO.read() - End");
            return null;
        }
        Phonenumber foundRecord = super.read(id);
        LOG.trace("PhonenumberDAO.read() - End");
        return foundRecord;
    }

    /**
     *
     * Update a single <code>Phonenumber</code> record.
     *
     * @param phonenumberRecord
     *
     * @return boolean
     */
    @Override
    public boolean update(Phonenumber phonenumberRecord) {
        LOG.trace("PhonenumberDAO.update()");
        return phonenumberRecord != null ? super.update(phonenumberRecord) : true;
    }

    /**
     *
     * Delete a <code>Phonenumber</code> record from the database
     *
     * @param phonenumberRecord
     */
    @Override
    public boolean delete(Phonenumber phonenumberRecord) {
        LOG.trace("PhonenumberDAO.delete() - Begin");
        if (phonenumberRecord != null) {
            return super.delete(phonenumberRecord);
        }
        LOG.trace("PhonenumberDAO.delete() - End");
        return false;
    }

    // =========================
    // Find DML Methods
    // =========================
    /**
     *
     * Read (Query) the database to get a <code>Phonenumber</code> record based
     *
     * on a known patientId.
     *
     * @param patientId
     *
     * @return List<Phonenumber>
     */
    public List<Phonenumber> findPatientPhonenumbers(Long patientId) {
        return super.findRecords(patientId);
    }


}
