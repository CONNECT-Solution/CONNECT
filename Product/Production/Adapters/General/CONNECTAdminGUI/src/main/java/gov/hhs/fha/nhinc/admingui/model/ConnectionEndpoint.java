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
package gov.hhs.fha.nhinc.admingui.model;

import static java.net.HttpURLConnection.HTTP_OK;

import gov.hhs.fha.nhinc.admingui.util.HelperUtil;

/**
 *
 * @author jassmit
 */
public class ConnectionEndpoint {

    private String name;
    private String serviceSpec;
    private String serviceUrl;
    private String pingTimestamp;
    private int responseCode;
    private int id;

    public ConnectionEndpoint(String name, String serviceUrl, String serviceSpec, String hcid, int responseCode,
        String pingTimestamp) {
        this.name = name;
        id = HelperUtil.getHashCodeBy(name, serviceUrl, serviceSpec, hcid);

        this.serviceSpec = serviceSpec;
        this.serviceUrl = serviceUrl;
        this.responseCode = responseCode;
        this.pingTimestamp = pingTimestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceSpec() {
        return serviceSpec;
    }

    public void setServiceSpec(String serviceSpec) {
        this.serviceSpec = serviceSpec;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getPingStatus() {
        switch (responseCode) {
            case 0: return "None";
            case HTTP_OK: return "Pass";
            default:
                return "Fail";
        }
    }

    public boolean isPingSuccessful() {
        return responseCode == HTTP_OK;
    }

    public String getPingTimestamp() {
        return pingTimestamp;
    }

    public void setPingTimestamp(String pingTimestamp) {
        this.pingTimestamp = pingTimestamp;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
