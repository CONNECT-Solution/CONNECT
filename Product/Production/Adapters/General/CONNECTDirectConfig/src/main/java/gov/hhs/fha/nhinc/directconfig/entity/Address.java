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

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "address")
/**
 * The JPA Address class
 */
public class Address {

    private String emailAddress;

    private Long id;

    private String displayName;

    private String endpoint;

    private Calendar createTime;

    private Calendar updateTime;

    private EntityStatus status;

    private String type;

    /**
     * Construct an Address.
     */
    public Address() {
    }

    /**
     * Construct an Address.
     *
     * @param anAddress
     *            The address.
     */
    public Address(Address anAddress) {
        if (anAddress != null) {
            setEmailAddress(anAddress.getEmailAddress());
            setDisplayName(anAddress.getDisplayName());
            setEndpoint(anAddress.getEndpoint());
            setCreateTime(anAddress.getCreateTime());
            setUpdateTime(anAddress.getUpdateTime());
            setStatus(anAddress.getStatus());
            setType(anAddress.getType());
        }
    }

    /**
     * Get the value of emailAddress.
     *
     * @return the value of emailAddress.
     */
    @Column(name = "emailaddress", length = 400)
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Set the value of emailAddress.
     *
     * @param anEmail
     *            The value of emailAddress.
     */
    public void setEmailAddress(String anEmail) {
        emailAddress = anEmail;
    }

    /**
     * Get the value of id.
     *
     * @return the value of id.
     */
    @Column(name = "id", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlAttribute
    public Long getId() {
        if (id == null) {
            setId(new Long(0L));
        }
        return id;
    }

    /**
     * Set the value of id.
     *
     * @param id
     *            The value of id.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the value of displayName.
     *
     * @return the value of displayName.
     */
    @Column(name = "displayname", length = 100)
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Set the value of displayName.
     *
     * @param aName
     *            The value of displayName.
     */
    public void setDisplayName(String aName) {
        displayName = aName;
    }

    /**
     * Get the value of endpoint.
     *
     * @return the value of entpoint.
     */
    @Column(name = "endpoint", length = 255)
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * Set the value of endpoint.
     *
     * @param anEndpoint
     *            The value of endpoint.
     */
    public void setEndpoint(String anEndpoint) {
        endpoint = anEndpoint;
    }

    /**
     * Get the value of createTime.
     *
     * @return the value of createTime.
     */
    @Temporal(TemporalType.TIMESTAMP)
    public Calendar getCreateTime() {
        return createTime;
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
     * Get the value of updateTime.
     *
     * @return the value of updateTime.
     */
    @Temporal(TemporalType.TIMESTAMP)
    public Calendar getUpdateTime() {
        return updateTime;
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
     * Get the value of status.
     *
     * @return the value of status.
     */
    @Column(name = "status")
    @Enumerated
    public EntityStatus getStatus() {
        return status;
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
     * Get the value of type.
     *
     * @return the value of type.
     */
    @Column(name = "type", length = 64)
    public String getType() {
        return type;
    }

    /**
     * Set the value of type.
     *
     * @param aType
     *            The value of type.
     */
    public void setType(String aType) {
        type = aType;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[ID: " + getId() +
               " | Address: " + getEmailAddress() +
               " | For: " + getDisplayName() +
               " | Endpoint: " + getEndpoint() +
               " | Status: " + getStatus() +
               " | Type: " + getType() +
               "]";
    }
}
