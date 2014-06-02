/*
 *  Copyright (c) 2009-2014, United States Government, as represented by the Secretary of Health and Human Services.
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *      * Redistributions of source code must retain the above
 *        copyright notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the documentation
 *        and/or other materials provided with the distribution.
 *      * Neither the name of the United States Government nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * The Class UserLogin.
 * 
 * @author msw
 */
// @NamedQueries({ @NamedQuery(name = "findByUserName", query =
// "SELECT u FROM UserLogin u WHERE u.userName = :userName") })
@Entity
@Table(name = "UserLogin")
public class UserLogin {

    /** The id. */
    @Id
    @Column(name = "ID")
    @GeneratedValue
    private long id;

    /** The user name. */
    @Column(name = "USERNAME")
    private String userName;

    /** The salt. */
    @Column(name = "SALT")
    private String salt;

    /** The sha1. */
    @Column(name = "SHA1")
    private String sha1;
    
    /** The role */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userRole")
    private UserRole userRole;

    /**
     * Instantiates a new user login.
     */
    public UserLogin() {

    }

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the id.
     * 
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the user name.
     * 
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user name.
     * 
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the salt.
     * 
     * @return the salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * Sets the salt.
     * 
     * @param salt the salt to set
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * Gets the sha1.
     * 
     * @return the sha1
     */
    public String getSha1() {
        return sha1;
    }

    /**
     * Sets the sha1.
     * 
     * @param sha1 the sha1 to set
     */
    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }
    
    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

}
