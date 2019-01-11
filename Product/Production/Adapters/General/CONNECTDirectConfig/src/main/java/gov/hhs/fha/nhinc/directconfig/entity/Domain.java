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
/*
 Copyright (c) 2010, NHIN Direct Project
 All rights reserved.

 Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer
 in the documentation and/or other materials provided with the distribution.
 3. Neither the name of the The NHIN Direct Project (nhindirect.org) nor the names of its contributors may be used to endorse or promote
 products derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS
 BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 THE POSSIBILITY OF SUCH DAMAGE.
 */

package gov.hhs.fha.nhinc.directconfig.entity;

import gov.hhs.fha.nhinc.directconfig.entity.helpers.EntityStatus;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.commons.collections.CollectionUtils;

/**
 * The JPA Domain class
 */
@XmlRootElement
public class Domain {
    private String domainName;
    private Calendar createTime;
    private Calendar updateTime;
    private Long postmasterAddressId;
    private Collection<Address> addresses;
    private Long id;
    private EntityStatus status = EntityStatus.NEW;
    private Collection<TrustBundleDomainReltn> relations;

    /**
     * Construct a Domain.
     */
    public Domain() {
    }

    /**
     * Construct a Domain.
     *
     * @param aName The domain name.
     */
    public Domain(String aName) {
        setDomainName(aName);
        setCreateTime(Calendar.getInstance());
        setUpdateTime(Calendar.getInstance());
        setStatus(EntityStatus.NEW);
    }

    /**
     * Get the value of id.
     *
     * @return the value of id.
     */
    @XmlAttribute
    public Long getId() {
        return id;
    }

    /**
     * Set the value of id.
     *
     * @param anId The value of id.
     */
    public void setId(Long anId) {
        id = anId;
    }

    /**
     * Get the value of domainName.
     *
     * @return the value of domainName.
     */
    public String getDomainName() {
        return domainName;
    }

    /**
     * Get the value of createTime.
     *
     * @return the value of createTime.
     */
    public Calendar getCreateTime() {
        return createTime;
    }

    /**
     * Get the value of postmasterAddressId.
     *
     * @return the value of postmasterAddressId.
     */
    public Long getPostmasterAddressId() {
        return postmasterAddressId;
    }

    /**
     * Set the value of postmasterAddressId.
     *
     * @param anId The value of postmasterAddressId.
     */
    public void setPostmasterAddressId(Long anId) {
        postmasterAddressId = anId;
    }

    /**
     * Get the value of updateTime.
     *
     * @return the value of updateTime.
     */
    public Calendar getUpdateTime() {
        return updateTime;
    }

    /**
     * Get the value of status.
     *
     * @return the value of status.
     */
    @XmlAttribute
    public EntityStatus getStatus() {
        return status;
    }

    /**
     * Set the value of domainName.
     *
     * @param aName The value of domainName.
     */
    public void setDomainName(String aName) {
        domainName = aName;
    }

    /**
     * Set the value of createTime.
     *
     * @param timestamp The value of createTime.
     */
    public void setCreateTime(Calendar timestamp) {
        createTime = timestamp;
    }

    /**
     * Set the value of updateTime.
     *
     * @param timestamp The value of updateTime.
     */
    public void setUpdateTime(Calendar timestamp) {
        updateTime = timestamp;
    }

    /**
     * Set the value of status.
     *
     * @param aStatus The value of status.
     */
    public void setStatus(EntityStatus aStatus) {
        status = aStatus;
    }

    /**
     * If we have an email address id, then search through the collection of addresses to find an id match and return
     * it.
     *
     * @return the postmaster email address.
     */
    public String getPostMasterEmail() {
        String result = null;
        // return the address that matched the ID
        if (CollectionUtils.isNotEmpty(getAddresses()) && getPostmasterAddressId() != null && getPostmasterAddressId() > 0) {
            for (Address address : getAddresses()) {
                if (address.getId().equals(getPostmasterAddressId())) {
                    result = address.getEmailAddress();
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Process according to the following table:
     * <p>
     * id name action<br>
     * 0/Null 0/Null None<br>
     * Not Null 0/Null Set Id to null. Don't remove the Address<br>
     * 0/Null Not Null Add to Address if not there, set Id<br>
     * Not Null Not Null if id.address = address then None, otherwise update id
     * </p>
     *
     * @param email The postmaster email address.
     */
    public void setPostMasterEmail(String email) {
        if (email == null) {
            if (getPostmasterAddressId() != null) {
                setPostmasterAddressId(null);
            }
        } else {
            Long addressId = null;
            boolean matched = false;

            // Check to see if we've already got the address
            for (Address address : getAddresses()) {
                if (address.getEmailAddress().equals(email)) {
                    addressId = address.getId();
                    matched = true;
                    break;
                }
            }

            if (!matched) { // It's a new address so add it
                Address postmaster = new Address(this, email);

                postmaster.setDisplayName("Postmaster");
                postmaster.setStatus(EntityStatus.NEW);
                getAddresses().add(postmaster);

                addressId = postmaster.getId();
            }

            setPostmasterAddressId(addressId);
        }
        return;
    }

    /**
     * Get a colection of addresses.
     *
     * @return a collection of addresses.
     */
    @XmlElement(name = "address")
    public Collection<Address> getAddresses() {
        if (addresses == null) {
            addresses = new ArrayList<>();
        }

        return addresses;
    }

    /**
     * Set the value of addresses.
     *
     * @param addresses the value of addresses
     */
    public void setAddresses(Collection<Address> addresses) {
        this.addresses = addresses;
    }

    /**
     * Set the collection of Trust Bundle - Domain relations
     *
     * @param relations The collection of Trust Bundle - Domain relations
     */
    public void setRelations(Collection<TrustBundleDomainReltn> relations) {
        this.relations = relations;
    }

    /**
     * Get the collection of Trust Bundle - Domain relations
     *
     * @return The collection of Trust Bundle - Domain relations
     */
    @XmlTransient
    public Collection<TrustBundleDomainReltn> getRelations() {
        if (relations == null) {
            relations = new ArrayList<>();
        }

        return relations;
    }

    /**
     * Verify the Domain is valid.
     *
     * @return true if the Domain is valid, false otherwise.
     */
    public boolean isValid() {
        boolean result = false;
        if (getDomainName() != null
            && getDomainName().length() > 0
            && (getStatus().equals(EntityStatus.ENABLED) || getStatus().equals(EntityStatus.DISABLED) || getStatus()
                .equals(EntityStatus.NEW) && getId() == 0L)) {

            result = true;
        }

        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[ID: " + getId() + " | Domain: " + getDomainName() + " | Status: " + getStatus().toString()
            + " | Addresses: " + getAddresses().size() + "]";
    }
}
