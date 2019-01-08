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

import gov.hhs.fha.nhinc.patientdb.model.Personname;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * PersonnameDAO Class provides methods to query and update Personname Data to/from MySQL Database using Hibernate
 *
 * @author richard.ettema
 */
public class PersonnameDAO extends GenericDAOImpl<Personname> {

    private static final Logger LOG = LoggerFactory.getLogger(PersonnameDAO.class);
    private static PersonnameDAO personnameDAO = new PersonnameDAO();
    /**
     *
     * Constructor
     */
    private PersonnameDAO() {
        super(Personname.class);
        LOG.trace("PersonnameDAO - Initialized");
    }

    /**
     *
     * Singleton instance returned...
     *
     * @return PersonnameDAO
     */
    public static PersonnameDAO getPersonnameDAOInstance() {
        LOG.trace("getPersonnameDAOInstance()..");
        return personnameDAO;
    }

    // =========================
    // Standard CRUD DML Methods
    // =========================
    /**
     *
     * Create a single <code>Personname</code> record. The generated id
     *
     * will be available in the personnameRecord.
     *
     * @param personnameRecord
     *
     * @return boolean
     */
    @Override
    public boolean create(Personname personnameRecord) {
        LOG.trace("PersonnameDAO.create() - Begin");
        return personnameRecord != null ? super.create(personnameRecord) : true;
    }

    /**
     *
     * Read (Query) the database to get a <code>Personname</code> record based
     *
     * on a known id.
     *
     * @param id
     *
     * @return Personname
     */
    public Personname read(Long id) {
        LOG.trace("PersonnameDAO.read() - Begin");
        if (id == null) {
            LOG.trace("-- id Parameter is required for Personname Query --");
            LOG.trace("PersonnameDAO.read() - End");
            return null;
        }
        Personname foundRecord = super.read(id);
        LOG.trace("PersonnameDAO.read() - End");
        return foundRecord;
    }

    /**
     *
     * Update a single <code>Personname</code> record.
     *
     * @param personnameRecord
     *
     * @return boolean
     */
    @Override
    public boolean update(Personname personnameRecord) {
        LOG.trace("PersonnameDAO.update()");
        return personnameRecord != null ? super.update(personnameRecord) : true;
    }

    /**
     *
     * Delete a <code>Personname</code> record from the database
     *
     * @param personnameRecord
     */
    @Override
    public boolean delete(Personname personnameRecord) {
        LOG.trace("PersonnameDAO.delete() - Begin");
        if (personnameRecord != null) {
            return super.delete(personnameRecord);
        }
        LOG.trace("PersonnameDAO.delete() - End");
        return false;
    }

    // =========================
    // Find DML Methods
    // =========================
    /**
     *
     * Read (Query) the database to get all <code>Personname</code> records based
     *
     * on a known patientId.
     *
     * @param patientId
     *
     * @return List<Personname>
     */
    public List<Personname> findPatientPersonnames(Long patientId) {
        return super.findRecords(patientId);
    }
}
