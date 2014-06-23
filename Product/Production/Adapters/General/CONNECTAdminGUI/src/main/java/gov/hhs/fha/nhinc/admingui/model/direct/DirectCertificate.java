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

/**
 *
 * @author jasonasmith
 */
public class DirectCertificate {
    
    private int position;
    private String certStatus;
    private String certOwner;
    private String certPrivate;
    private String certThumb;
    private String certCreated;
    private String certStart;
    private String certEnd;

    public DirectCertificate(){
        
    }

    public DirectCertificate(int position, String certStatus, String certOwner, String certPrivate, String certThumb, String certCreated, String certStart, String certEnd) {
        this.position = position;
        this.certStatus = certStatus;
        this.certOwner = certOwner;
        this.certPrivate = certPrivate;
        this.certThumb = certThumb;
        this.certCreated = certCreated;
        this.certStart = certStart;
        this.certEnd = certEnd;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    
    public String getCertStatus() {
        return certStatus;
    }

    public void setCertStatus(String certStatus) {
        this.certStatus = certStatus;
    }

    public String getCertOwner() {
        return certOwner;
    }

    public void setCertOwner(String certOwner) {
        this.certOwner = certOwner;
    }

    public String getCertPrivate() {
        return certPrivate;
    }

    public void setCertPrivate(String certPrivate) {
        this.certPrivate = certPrivate;
    }

    public String getCertThumb() {
        return certThumb;
    }

    public void setCertThumb(String certThumb) {
        this.certThumb = certThumb;
    }

    public String getCertCreated() {
        return certCreated;
    }

    public void setCertCreated(String certCreated) {
        this.certCreated = certCreated;
    }

    public String getCertStart() {
        return certStart;
    }

    public void setCertStart(String certStart) {
        this.certStart = certStart;
    }

    public String getCertEnd() {
        return certEnd;
    }

    public void setCertEnd(String certEnd) {
        this.certEnd = certEnd;
    }
    
    
}
