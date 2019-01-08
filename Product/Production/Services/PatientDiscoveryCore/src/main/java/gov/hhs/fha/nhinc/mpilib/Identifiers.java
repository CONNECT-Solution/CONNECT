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

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author rayj
 */
public class Identifiers extends ArrayList<Identifier> implements java.io.Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(Identifiers.class);
    static final long serialVersionUID = -917875998116976597L;

    /**
     * Default constructor for Identifiers.
     */
    public Identifiers() {
        LOG.info("Identifiers Initiated..");
    }

    /**
     * @param identifiers Identifiers to add to Identifiers
     * @return true. This method will always return true.
     */
    public boolean add(Identifiers identifiers) {
        for (Identifier identifier : identifiers) {
            add(identifier);
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.util.ArrayList#add(java.lang.Object)
     */
    @Override
    public boolean add(Identifier identifier) {
        // check to see if this id already exists
        Identifier myIdentifier;

        if (!doesIdentifierExist(identifier)) {
            myIdentifier = new Identifier(identifier.getId(), identifier.getOrganizationId());
            super.add(myIdentifier);
        }
        return true;
    }

    private boolean doesIdentifierExist(Identifier identifier) {
        boolean found = false;
        for (Identifier existingId : this) {
            if ((existingId.getOrganizationId().contentEquals(identifier.getOrganizationId()) && (existingId.getId()
                    .contentEquals(identifier.getId())))) {
                found = true;
            }
        }
        return found;
    }

    /**
     * Method will create a new Identifier from the id and organizationId, and add it to this Identifier.
     * @param id the id
     * @param organizationId the organizationId
     * @return true. This method will always return true.
     */
    public boolean add(String id, String organizationId) {
        return add(new Identifier(id, organizationId));
    }
}
