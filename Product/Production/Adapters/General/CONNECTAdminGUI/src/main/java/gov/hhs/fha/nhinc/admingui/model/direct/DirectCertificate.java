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
package gov.hhs.fha.nhinc.admingui.model.direct;

import gov.hhs.fha.nhinc.direct.config.Certificate;
import gov.hhs.fha.nhinc.direct.config.EntityStatus;
import java.text.SimpleDateFormat;

/**
 *
 * @author jasonasmith
 */
public class DirectCertificate {

    private String thumbprint;

    private long id;

    private EntityStatus status;
    private boolean privateKey;

    private String owner;

    private String createTime;
    private String validEndDate;
    private String validStartDate;

    /**
     *
     */
    public DirectCertificate() {
    }

    /**
     *
     * @param cert
     * @param thumbprint
     */
    public DirectCertificate(Certificate cert, String thumbprint) {
        this.thumbprint = thumbprint;

        id = cert.getId();

        status = cert.getStatus();
        privateKey = cert.isPrivateKey();

        owner = cert.getOwner();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        createTime = sdf.format(cert.getCreateTime().toGregorianCalendar().getTime());
        validEndDate = sdf.format(cert.getValidEndDate().toGregorianCalendar().getTime());
        validStartDate = sdf.format(cert.getValidStartDate().toGregorianCalendar().getTime());
    }

    /**
     *
     * @return
     */
    public String getThumbprint() {
        return thumbprint;
    }

    /**
     *
     * @param thumbprint
     */
    public void setThumbprint(String thumbprint) {
        this.thumbprint = thumbprint;
    }

    /**
     *
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public EntityStatus getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(EntityStatus status) {
        this.status = status;
    }

    /**
     *
     * @return
     */
    public boolean isPrivateKey() {
        return privateKey;
    }

    /**
     *
     * @param privateKey
     */
    public void setPrivateKey(boolean privateKey) {
        this.privateKey = privateKey;
    }

    /**
     *
     * @return
     */
    public String getOwner() {
        return owner;
    }

    /**
     *
     * @param owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     *
     * @return
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     *
     * @param createTime
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     *
     * @return
     */
    public String getValidEndDate() {
        return validEndDate;
    }

    /**
     *
     * @param validEndDate
     */
    public void setValidEndDate(String validEndDate) {
        this.validEndDate = validEndDate;
    }

    /**
     *
     * @return
     */
    public String getValidStartDate() {
        return validStartDate;
    }

    /**
     *
     * @param validStartDate
     */
    public void setValidStartDate(String validStartDate) {
        this.validStartDate = validStartDate;
    }
}
