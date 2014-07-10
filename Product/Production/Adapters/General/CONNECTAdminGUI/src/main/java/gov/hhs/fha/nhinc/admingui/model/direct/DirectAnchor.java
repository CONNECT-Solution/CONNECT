/**
 * Copyright (c) 2009-2014, United States Government, as represented by the Secretary of Health and Human Services. All
 * rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met: Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution. Neither the name of the United States Government nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package gov.hhs.fha.nhinc.admingui.model.direct;

import java.text.SimpleDateFormat;

import org.nhind.config.common.Anchor;
import org.nhind.config.common.EntityStatus;

/**
 *
 * @author jasonasmith
 */
public class DirectAnchor {

    private String trustedDomainOrUser;

    private long id;
    private long certificateId;

    private String owner;
    private String thumbprint;

    private EntityStatus status;

    private String createTime;
    private String validEndDate;
    private String validStartDate;

    private boolean isOutgoing;
    private boolean isIncoming;

    public DirectAnchor() {
    }

    public DirectAnchor(Anchor anchor, String trustedDomainOrUser) {
        this.trustedDomainOrUser = trustedDomainOrUser;

        id = anchor.getId();
        certificateId = anchor.getCertificateId();

        owner = anchor.getOwner();
        thumbprint = anchor.getThumbprint();

        status = anchor.getStatus();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        createTime = sdf.format(anchor.getCreateTime().toGregorianCalendar().getTime());
        validEndDate = sdf.format(anchor.getValidEndDate().toGregorianCalendar().getTime());
        validStartDate = sdf.format(anchor.getValidStartDate().toGregorianCalendar().getTime());

        isIncoming = anchor.isIncoming();
        isOutgoing = anchor.isOutgoing();
    }

    public String getTrustedDomainOrUser() {
        return trustedDomainOrUser;
    }

    public long getId() {
        return id;
    }

    public long getCertificateId() {
        return certificateId;
    }

    public String getOwner() {
        return owner;
    }

    public String getThumbprint() {
        return thumbprint;
    }

    public EntityStatus getStatus() {
        return status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getValidEndDate() {
        return validEndDate;
    }

    public boolean isOutgoing() {
        return isOutgoing;
    }

    public boolean isIncoming() {
        return isIncoming;
    }

    public String getValidStartDate() {
        return validStartDate;
    }

    public void setTrustedDomainOrUser(String trustedDomainOrUser) {
        this.trustedDomainOrUser = trustedDomainOrUser;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCertificateId(long certificateId) {
        this.certificateId = certificateId;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setThumbprint(String thumbprint) {
        this.thumbprint = thumbprint;
    }

    public void setStatus(EntityStatus status) {
        this.status = status;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setValidEndDate(String validEndDate) {
        this.validEndDate = validEndDate;
    }

    public void setValidStartDate(String validStartDate) {
        this.validStartDate = validStartDate;
    }

    public void setOutgoing(boolean isOutgoing) {
        this.isOutgoing = isOutgoing;
    }

    public void setIncoming(boolean isIncoming) {
        this.isIncoming = isIncoming;
    }
}
