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

import gov.hhs.fha.nhinc.patientdb.model.Address;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * AddressDAO Class provides methods to query and update Address Data to/from MySQL Database using Hibernate.
 *
 * @author richard.ettema
 */
public class AddressDAO extends GenericDAOImpl<Address> {

    private static final Logger LOG = LoggerFactory.getLogger(AddressDAO.class);
    private static AddressDAO addressDAO = new AddressDAO();

    /**
     * Constructor.
     */
    private AddressDAO() {
        super(Address.class);
        LOG.trace("AddressDAO - Initialized");
    }

    /**
     * Singleton instance returned...
     *
     * @return AddressDAO
     */
    public static AddressDAO getAddressDAOInstance() {
        LOG.trace("getAddressDAOInstance()..");
        return addressDAO;
    }

    // =========================
    // Standard CRUD DML Methods
    // =========================
    /**
     * Create a single <code>Address</code> record. The generated id will be available in the addressRecord.
     *
     * @param addressRecord
     * @return boolean
     */
    @Override
    public boolean create(Address addressRecord) {
        LOG.trace("AddressDAO.create()");
        return addressRecord != null ? super.create(addressRecord) : true;
    }

    /**
     *
     * Read (Query) the database to get a <code>Address</code> record based
     *
     * on a known id.
     *
     * @param id
     *
     * @return Address
     */
    public Address read(Long id) {
        LOG.trace("AddressDAO.read() - Begin");
        if (id == null) {
            LOG.trace("-- id Parameter is required for Address Query --");
            LOG.trace("AddressDAO.read() - End");
            return null;
        }
        Address foundRecord = super.read(id);
        LOG.trace("AddressDAO.read() - End");
        return foundRecord;
    }

    /**
     *
     * Update a single <code>Address</code> record.
     *
     * @param addressRecord
     *
     * @return boolean
     */
    @Override
    public boolean update(Address addressRecord) {
        LOG.trace("AddressDAO.update()");
        return addressRecord != null ? super.update(addressRecord) : true;
    }

    /**
     *
     * Delete a <code>Address</code> record from the database.
     *
     * @param addressRecord
     */
    @Override
    public boolean delete(Address addressRecord) {
        LOG.trace("AddressDAO.delete() - Begin");
        if (addressRecord != null) {
            return super.delete(addressRecord);
        }
        LOG.trace("AddressDAO.delete() - End");
        return false;
    }

    // =========================
    // Find DML Methods
    // =========================
    /**
     *
     * Read (Query) the database to get all <code>Address</code> records based
     *
     * on a known patientId.
     *
     * @param patientId
     *
     * @return List<Address>
     */
    public List<Address> findPatientAddresses(Long patientId) {
        return super.findRecords(patientId);
    }

}
