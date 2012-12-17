/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.direct.xdr;

import gov.hhs.fha.nhinc.direct.xdr.audit.Auditable;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * The Class SoapEdgeHeadersPropertiesImpl implements {@link SoapEdgeHeaders} and {@link Auditable} to provide an class
 * to carry all of the Soap Headers, as well as those used for auditing.
 */
public class SoapEdgeHeadersPropertiesImpl implements SoapEdgeHeaders, Auditable {

    /** The properties. */
    Map<String, String> properties = null;

    /**
     * Instantiates a new soap edge headers properties impl.
     */
    public SoapEdgeHeadersPropertiesImpl() {
        properties = new HashMap<String, String>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.Auditable#getAuditableValues()
     */
    @Override
    public Map<String, String> getAuditableValues() {
        Map<String, String> auditValues = new HashMap<String, String>();

        for (String s : Auditable.AuditKeys) {
            String value = properties.get(s);
            if (StringUtils.isNotBlank(value)) {
                auditValues.put(s, value);
            }
        }
        return auditValues;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.Auditable#getMessageId()
     */
    @Override
    public String getMessageId() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.Auditable#setMessageId(java.lang.String)
     */
    @Override
    public void setMessageId(String messageId) {
        properties.put(Auditable.MESSAGE_ID, messageId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.Auditable#getRemoteHost()
     */
    @Override
    public String getRemoteHost() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.Auditable#setRemoteHost(java.lang.String)
     */
    @Override
    public void setRemoteHost(String remoteHost) {
        properties.put(Auditable.REMOTE_HOST, remoteHost);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.Auditable#getEndpoint()
     */
    @Override
    public String getEndpoint() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.Auditable#setEndpoint(java.lang.String)
     */
    @Override
    public void setEndpoint(String endpoint) {
        properties.put(Auditable.ENDPOINT, endpoint);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.Auditable#getTo()
     */
    @Override
    public String getTo() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.Auditable#setTo(java.lang.String)
     */
    @Override
    public void setTo(String to) {
        properties.put(Auditable.TO, to);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.Auditable#getThisHost()
     */
    @Override
    public String getThisHost() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.Auditable#setThisHost(java.lang.String)
     */
    @Override
    public void setThisHost(String thisHost) {
        properties.put(Auditable.THIS_HOST, thisHost);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.Auditable#getPatId()
     */
    @Override
    public String getPatId() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.Auditable#setPatId(java.lang.String)
     */
    @Override
    public void setPatId(String patId) {
        properties.put(Auditable.PAT_ID, patId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.Auditable#getSubsetId()
     */
    @Override
    public String getSubsetId() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.Auditable#SubsetId(java.lang.String)
     */
    @Override
    public void SubsetId(String subsetId) {
        properties.put(Auditable.SUBSET_ID, subsetId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.Auditable#getPid()
     */
    @Override
    public String getPid() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.Auditable#setPid(java.lang.String)
     */
    @Override
    public void setPid(String pid) {
        properties.put(Auditable.P_ID, pid);
    }

}
