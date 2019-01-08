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
package gov.hhs.fha.nhinc.direct.xdr;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 * The Class SoapEdgeHeadersPropertiesImpl implements {@link SoapEdgeContext} and {@link SoapEdgeContext} to provide an
 * class to carry all of the Soap Headers, as well as those used for auditing.
 */
public class SoapEdgeContextMapImpl implements SoapEdgeContext {

    /** The properties. */
    Map<String, String> properties = null;

    private static final String[] PropertyKeys = { MESSAGE_ID, REMOTE_HOST, ENDPOINT, TO, THIS_HOST, PAT_ID, SUBSET_ID,
            P_ID, ACTION, RELATES_TO, REPLY_TO, FROM, DIRECT_FROM, DIRECT_TO, DIRECT_METADATA_LEVEL };

    /**
     * Instantiates a new soap edge headers properties impl.
     */
    public SoapEdgeContextMapImpl() {
        properties = new HashMap<>();
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.SoapEdgeHeaders#getSoapEdgeHeadersValues()
     */
    @Override
    public ImmutableMap<String, String> getAuditableValues() {
        ImmutableMap.Builder<String, String> builder = new ImmutableMap.Builder<>();

        for (String s : PropertyKeys) {
            String value = properties.get(s);
            if (StringUtils.isNotBlank(value)) {
                builder.put(s, value);
            }
        }
        return builder.build();
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.SoapEdgeHeaders#getMessageId()
     */
    @Override
    public String getMessageId() {
        return properties.get(SoapEdgeContext.MESSAGE_ID);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.SoapEdgeHeaders#setMessageId(java.lang.String)
     */
    @Override
    public void setMessageId(String messageId) {
        properties.put(SoapEdgeContext.MESSAGE_ID, messageId);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.SoapEdgeHeaders#getRemoteHost()
     */
    @Override
    public String getRemoteHost() {
        return properties.get(SoapEdgeContext.REMOTE_HOST);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.SoapEdgeHeaders#setRemoteHost(java.lang.String)
     */
    @Override
    public void setRemoteHost(String remoteHost) {
        properties.put(SoapEdgeContext.REMOTE_HOST, remoteHost);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.SoapEdgeHeaders#getEndpoint()
     */
    @Override
    public String getEndpoint() {
        return properties.get(SoapEdgeContext.ENDPOINT);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.SoapEdgeHeaders#setEndpoint(java.lang.String)
     */
    @Override
    public void setEndpoint(String endpoint) {
        properties.put(SoapEdgeContext.ENDPOINT, endpoint);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.SoapEdgeHeaders#getTo()
     */
    @Override
    public String getTo() {
        return properties.get(SoapEdgeContext.TO);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.SoapEdgeHeaders#setTo(java.lang.String)
     */
    @Override
    public void setTo(String to) {
        properties.put(SoapEdgeContext.TO, to);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.SoapEdgeHeaders#getThisHost()
     */
    @Override
    public String getThisHost() {
        return properties.get(SoapEdgeContext.THIS_HOST);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.SoapEdgeHeaders#setThisHost(java.lang.String)
     */
    @Override
    public void setThisHost(String thisHost) {
        properties.put(SoapEdgeContext.THIS_HOST, thisHost);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.SoapEdgeHeaders#getPatId()
     */
    @Override
    public String getPatId() {
        return properties.get(SoapEdgeContext.PAT_ID);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.SoapEdgeHeaders#setPatId(java.lang.String)
     */
    @Override
    public void setPatId(String patId) {
        properties.put(SoapEdgeContext.PAT_ID, patId);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.SoapEdgeHeaders#getSubsetId()
     */
    @Override
    public String getSubsetId() {
        return properties.get(SoapEdgeContext.SUBSET_ID);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.SoapEdgeHeaders#SubsetId(java.lang.String)
     */
    @Override
    public void subsetId(String subsetId) {
        properties.put(SoapEdgeContext.SUBSET_ID, subsetId);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.SoapEdgeHeaders#getPid()
     */
    @Override
    public String getPid() {
        return properties.get(SoapEdgeContext.P_ID);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.SoapEdgeHeaders#setPid(java.lang.String)
     */
    @Override
    public void setPid(String pid) {
        properties.put(SoapEdgeContext.P_ID, pid);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.direct.xdr.SoapEdgeHeaders#setAction(java.lang.String)
     */
    @Override
    public void setAction(String action) {
        properties.put(SoapEdgeContext.ACTION, action);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.direct.xdr.SoapEdgeHeaders#setRelatesTo(java.lang.String)
     */
    @Override
    public void setRelatesTo(String relatesTo) {
        properties.put(SoapEdgeContext.RELATES_TO, relatesTo);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.direct.xdr.SoapEdgeHeaders#setReplyTo(java.lang.String)
     */
    @Override
    public void setReplyTo(String replyTo) {
        properties.put(SoapEdgeContext.REPLY_TO, replyTo);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.direct.xdr.SoapEdgeHeaders#setFrom(java.lang.String)
     */
    @Override
    public void setFrom(String from) {
        properties.put(SoapEdgeContext.FROM, from);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.direct.xdr.SoapEdgeHeaders#setDirectFrom(java.lang.String)
     */
    @Override
    public void setDirectFrom(String directFrom) {
        properties.put(SoapEdgeContext.DIRECT_FROM, directFrom);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.direct.xdr.SoapEdgeHeaders#setDirectTo(java.lang.String)
     */
    @Override
    public void setDirectTo(String directTo) {
        properties.put(SoapEdgeContext.DIRECT_TO, directTo);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.direct.xdr.SoapEdgeHeaders#setDirectMetaDataLevel(java.lang.String)
     */
    @Override
    public void setDirectMetaDataLevel(String directMetadataLevel) {
        properties.put(SoapEdgeContext.DIRECT_METADATA_LEVEL, directMetadataLevel);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.direct.xdr.SoapEdgeContext#getDirectTo()
     */
    @Override
    public String getDirectTo() {
        return properties.get(SoapEdgeContext.DIRECT_TO);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.direct.xdr.SoapEdgeContext#getDirectFrom()
     */
    @Override
    public String getDirectFrom() {
        return properties.get(SoapEdgeContext.DIRECT_FROM);
    }

}
