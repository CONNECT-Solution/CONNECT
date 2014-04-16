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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * The JPA Domain class
 */
@Entity
public class Domain {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String domainName;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar updateTime;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "domainId")
    private Collection<Address> addresses;

    @Column(nullable = false)
    @Enumerated
    private EntityStatus status = EntityStatus.NEW;

    /**
     * Construct a Domain.
     */
    public Domain() {
    }

    /**
     * Construct a Domain.
     *
     * @param aName
     *            The domain name.
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
    public Long getId() {
        return id;
    }

    /**
     * Set the value of id.
     *
     * @param anId
     *            The value of id.
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
    public EntityStatus getStatus() {
        return status;
    }

    /**
     * Set the value of domainName.
     *
     * @param aName
     *            The value of domainName.
     */
    public void setDomainName(String aName) {
        domainName = aName;
    }

    /**
     * Set the value of createTime.
     *
     * @param timestamp
     *            The value of createTime.
     */
    public void setCreateTime(Calendar timestamp) {
        createTime = timestamp;
    }

    /**
     * Set the value of updateTime.
     *
     * @param timestamp
     *            The value of updateTime.
     */
    public void setUpdateTime(Calendar timestamp) {
        updateTime = timestamp;
    }

    /**
     * Set the value of status.
     *
     * @param aStatus
     *            The value of status.
     */
    public void setStatus(EntityStatus aStatus) {
        status = aStatus;
    }

    /**
     * If we have an email address id, then search through the collection of
     * addresses to find an id match and return it.
     *
     * @return the postmaster email address.
     */
    @Transient
    public String getPostMasterEmail() {
        return "postmaster@" + this.getDomainName();
    }

    /**
     * Get a colection of addresses.
     *
     * @return a collection of addresses.
     */
    public Collection<Address> getAddresses() {
        if (addresses == null) {
            addresses = new ArrayList<Address>();
        }

        return addresses;
    }

    /**
     * Set the value of addresses.
     *
     * @param addresses
     *            the value of addresses
     */
    public void setAddresses(Collection<Address> addresses) {
        this.addresses = addresses;
    }

    /**
     * Verify the Domain is valid.
     *
     * @return true if the Domain is valid, false otherwise.
     */
    public boolean isValid() {
        boolean result = false;

        if ((getDomainName() != null)
                && (getDomainName().length() > 0)
                && ((getStatus().equals(EntityStatus.ENABLED)) || (getStatus().equals(EntityStatus.DISABLED)) || ((getStatus()
                        .equals(EntityStatus.NEW)) && (getId() == 0L)))) {
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
