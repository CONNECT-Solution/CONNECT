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

import gov.hhs.fha.nhinc.nhinclib.NullChecker;

/**
 *
 * @author rayj
 */
public class Identifier implements java.io.Serializable {
    static final long serialVersionUID = -4713959967816233116L;

    private String id = "";
    private String organizationId = "";

    /**
     * Default constructor for Identifier.
     */
    public Identifier() {
    }

    /**
     * Constructor for Identifier.
     *
     * @param id the ID for this Identifier
     * @param organizationId the organizationID for this Identifier
     */
    public Identifier(String id, String organizationId) {
        this.id = id;
        this.organizationId = organizationId;
    }

    /**
     * @return the OrganizationId for this Identifier
     */
    public String getOrganizationId() {
        return organizationId;
    }

    /**
     * @param identifier the organizationId to set for this Identifier
     */
    public void setOrganizationId(String identifier) {
        organizationId = identifier;
    }

    /**
     * @return the Id for this Identifier
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set for this Identifier
     */
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Identifier)) {
            return false;
        }

        Identifier rhs = (Identifier) obj;

        if ((NullChecker.isNullish(this.getId())) || (this.getOrganizationId() == null)
            || (NullChecker.isNullish(rhs.getId())) || (rhs.getOrganizationId() == null)) {
            return false;
        }

        return this.getId().contentEquals(rhs.getId())
            && this.getOrganizationId().contentEquals(rhs.getOrganizationId());
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        if (NullChecker.isNotNullish(id)) {
            hashCode = id.hashCode();
            if (NullChecker.isNotNullish(organizationId)) {
                hashCode += organizationId.hashCode();
            }
        }
        return hashCode;
    }

}
