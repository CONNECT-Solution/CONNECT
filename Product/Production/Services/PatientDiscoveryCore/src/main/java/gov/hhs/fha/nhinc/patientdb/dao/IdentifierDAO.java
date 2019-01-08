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

import gov.hhs.fha.nhinc.patientdb.model.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * IdentifierDAO Class provides methods to query and update Identifier Data to/from MySQL Database using Hibernate
 *
 * @author richard.ettema
 */
public class IdentifierDAO extends GenericDAOImpl<Identifier> {

    private static final Logger LOG = LoggerFactory.getLogger(IdentifierDAO.class);
    private static IdentifierDAO identifierDAO = new IdentifierDAO();

    /**
     *
     * Constructor
     */
    private IdentifierDAO() {
        super(Identifier.class);
        LOG.trace("IdentifierDAO - Initialized");
    }

    /**
     *
     * Singleton instance returned...
     *
     * @return IdentifierDAO
     */
    public static IdentifierDAO getIdentifierDAOInstance() {

        LOG.trace("getIdentifierDAOInstance()..");
        return identifierDAO;

    }

    // =========================
    // Standard CRUD DML Methods
    // =========================
    /**
     *
     * Create a single <code>Identifier</code> record. The generated id
     *
     * will be available in the identifierRecord.
     *
     * @param identifierRecord
     *
     * @return boolean
     */
    @Override
    public boolean create(Identifier identifierRecord) {
        LOG.trace("IdentifierDAO.create()");
        return identifierRecord != null ? super.create(identifierRecord) : true;
    }

    /**
     *
     * Read (Query) the database to get a <code>Identifier</code> record based
     *
     * on a known id.
     *
     * @param id
     *
     * @return Identifier
     */
    public Identifier read(Long id) {
        LOG.trace("IdentifierDAO.read() - readBy() - Begin");
        if (id == null) {
            LOG.trace("-- id Parameter is required for Identifier Query --");
            LOG.trace("IdentifierDAO.read() - readBy() - End");
            return null;
        }
        Identifier foundRecord = super.readBy(id,"identifierId");
        LOG.trace("IdentifierDAO.read() - readBy() - End");
        return foundRecord;
    }

    /**
     *
     * Update a single <code>Identifier</code> record.
     *
     * @param identifierRecord
     *
     * @return boolean
     */
    @Override
    public boolean update(Identifier identifierRecord) {
        LOG.trace("IdentifierDAO.update()");
        return identifierRecord != null ? super.update(identifierRecord) : true;

    }

    /**
     *
     * Delete a <code>Identifier</code> record from the database
     *
     * @param identifierRecord
     */
    @Override
    public boolean delete(Identifier identifierRecord) {
        LOG.trace("IdentifierDAO.delete() - Begin");
        if (identifierRecord != null) {
            return super.delete(identifierRecord);
        }
        LOG.trace("IdentifierDAO.delete() - End");
        return false;
    }

}
