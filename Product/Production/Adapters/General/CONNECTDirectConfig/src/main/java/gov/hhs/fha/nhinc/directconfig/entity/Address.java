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
import java.util.Calendar;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

public class Address {
    private String emailAddress;
    private Long id;
    private Domain domain;
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
     * @param aDomain The Domain.
     * @param anAddress The address.
     */
    public Address(Domain aDomain, String anAddress) {
        setDomain(aDomain);
        setEmailAddress(anAddress);
        setDisplayName("");
        setCreateTime(Calendar.getInstance());
        setUpdateTime(Calendar.getInstance());
        setStatus(EntityStatus.NEW);
    }

    /**
     * Construct an Address.
     *
     * @param aDomain The Domain.
     * @param anAddress The address.
     * @param aName The display name.
     */
    public Address(Domain aDomain, String anAddress, String aName) {
        setDomain(aDomain);
        setEmailAddress(anAddress);
        setDisplayName(aName);
        setCreateTime(Calendar.getInstance());
        setUpdateTime(Calendar.getInstance());
        setStatus(EntityStatus.NEW);
    }

    /**
     * Construct an Address.
     *
     * @param anAddress The address.
     */
    public Address(Address anAddress) {
        if (anAddress != null) {
            setDomain(anAddress.getDomain());
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
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Set the value of emailAddress.
     *
     * @param anEmail The value of emailAddress.
     */
    public void setEmailAddress(String anEmail) {
        emailAddress = anEmail;
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
     * @param id The value of id.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the value of domain.
     *
     * @return the value of domain.
     */
    @XmlTransient
    public Domain getDomain() {
        return domain;
    }

    /**
     * Set the value of domain.
     *
     * @param anId The value of domain.
     */
    public void setDomain(Domain anId) {
        domain = anId;

    }

    /**
     * Get the value of displayName.
     *
     * @return the value of displayName.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Set the value of displayName.
     *
     * @param aName The value of displayName.
     */
    public void setDisplayName(String aName) {
        displayName = aName;
    }

    /**
     * Get the value of endpoint.
     *
     * @return the value of entpoint.
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * Set the value of endpoint.
     *
     * @param anEndpoint The value of endpoint.
     */
    public void setEndpoint(String anEndpoint) {
        endpoint = anEndpoint;
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
     * Set the value of createTime.
     *
     * @param timestamp The value of createTime.
     */
    public void setCreateTime(Calendar timestamp) {
        createTime = timestamp;
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
     * Set the value of updateTime.
     *
     * @param timestamp The value of updateTime.
     */
    public void setUpdateTime(Calendar timestamp) {
        updateTime = timestamp;
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
     * Set the value of status.
     *
     * @param aStatus The value of status.
     */
    public void setStatus(EntityStatus aStatus) {
        status = aStatus;
    }

    /**
     * Get the value of type.
     *
     * @return the value of type.
     */
    public String getType() {
        return type;
    }

    /**
     * Set the value of type.
     *
     * @param aType The value of type.
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
        return "[ID: " + getId() + " | Address: " + getEmailAddress() + " | For: " + getDisplayName() + " | Domain: "
                + getDomain().getDomainName() + " | Endpoint: " + getEndpoint() + " | Status: " + getStatus()
                + " | Type: " + getType() + "]";
    }

    /**
     * Actions to run after an unmarshal.
     *
     * @param u The Unmarshaller.
     * @param parent The paret.
     */
    public void afterUnmarshal(Unmarshaller u, Object parent) {
        setDomain((Domain) parent);
    }
}
