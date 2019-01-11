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

package gov.hhs.fha.nhinc.directconfig.service.jaxws;

import gov.hhs.fha.nhinc.directconfig.entity.Certificate;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "updateTrustBundleAttributes", namespace = "http://nhind.org/config")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateTrustBundleAttributes", namespace = "http://nhind.org/config", propOrder = {
    "trustBundleId",
    "trustBundleName",
    "trustBundleURL",
    "signingCert",
    "trustBundleRefreshInterval"
})
public class UpdateTrustBundleAttributes {

    @XmlElement(name = "trustBundleId", namespace = "")
    private long trustBundleId;
    @XmlElement(name = "trustBundleName", namespace = "")
    private String trustBundleName;
    @XmlElement(name = "trustBundleURL", namespace = "")
    private String trustBundleURL;
    @XmlElement(name = "signingCert", namespace = "")
    private Certificate signingCert;
    @XmlElement(name = "trustBundleRefreshInterval", namespace = "")
    private int trustBundleRefreshInterval;

    /**
     *
     * @return
     *     returns long
     */
    public long getTrustBundleId() {
        return this.trustBundleId;
    }

    /**
     *
     * @param trustBundleId
     *     the value for the trustBundleId property
     */
    public void setTrustBundleId(long trustBundleId) {
        this.trustBundleId = trustBundleId;
    }

    /**
     *
     * @return
     *     returns String
     */
    public String getTrustBundleName() {
        return this.trustBundleName;
    }

    /**
     *
     * @param trustBundleName
     *     the value for the trustBundleName property
     */
    public void setTrustBundleName(String trustBundleName) {
        this.trustBundleName = trustBundleName;
    }

    /**
     *
     * @return
     *     returns String
     */
    public String getTrustBundleURL() {
        return this.trustBundleURL;
    }

    /**
     *
     * @param trustBundleURL
     *     the value for the trustBundleURL property
     */
    public void setTrustBundleURL(String trustBundleURL) {
        this.trustBundleURL = trustBundleURL;
    }

    /**
     *
     * @return
     *     returns Certificate
     */
    public Certificate getSigningCert() {
        return this.signingCert;
    }

    /**
     *
     * @param signingCert
     *     the value for the signingCert property
     */
    public void setSigningCert(Certificate signingCert) {
        this.signingCert = signingCert;
    }

    /**
     *
     * @return
     *     returns int
     */
    public int getTrustBundleRefreshInterval() {
        return this.trustBundleRefreshInterval;
    }

    /**
     *
     * @param trustBundleRefreshInterval
     *     the value for the trustBundleRefreshInterval property
     */
    public void setTrustBundleRefreshInterval(int trustBundleRefreshInterval) {
        this.trustBundleRefreshInterval = trustBundleRefreshInterval;
    }

}
