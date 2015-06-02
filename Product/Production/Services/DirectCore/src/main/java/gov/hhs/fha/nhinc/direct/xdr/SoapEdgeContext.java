/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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

/**
 * Interface to define the data elements which are extracted from the web services headers for an XD* message.
 *
 * @author mweaver
 *
 */
public interface SoapEdgeContext {

    public static final String MESSAGE_ID = "MESSAGE_ID";
    public static final String REMOTE_HOST = "REMOTE_HOST";
    public static final String ENDPOINT = "ENDPOINT";
    public static final String TO = "TO";
    public static final String THIS_HOST = "THIS_HOST";
    public static final String PAT_ID = "PAT_ID";
    public static final String SUBSET_ID = "SUBSET_ID";
    public static final String P_ID = "P_ID";
    public static final String ACTION = "ACTION";
    public static final String RELATES_TO = "RELATES_TO";
    public static final String REPLY_TO = "REPLY_TO";
    public static final String FROM = "FROM";
    public static final String DIRECT_FROM = "DIRECT_FROM";
    public static final String DIRECT_TO = "DIRECT_TO";
    public static final String DIRECT_METADATA_LEVEL = "DIRECT_METADATA_LEVEL";

    public static final String[] PropertyKeys = { MESSAGE_ID, REMOTE_HOST, ENDPOINT, TO, THIS_HOST, PAT_ID, SUBSET_ID,
            P_ID, ACTION, RELATES_TO, REPLY_TO, FROM, DIRECT_FROM, DIRECT_TO, DIRECT_METADATA_LEVEL };


    public ImmutableMap<String, String> getAuditableValues();

    public String getMessageId();

    public void setMessageId(String messageId);

    public String getRemoteHost();

    public void setRemoteHost(String remoteHost);

    public String getEndpoint();

    public void setEndpoint(String endpoint);

    public String getTo();

    public void setTo(String to);

    public String getThisHost();

    public void setThisHost(String thisHost);

    public String getPatId();

    public void setPatId(String patId);

    public String getSubsetId();

    public void SubsetId(String subsetId);

    public String getPid();

    public void setPid(String pid);

    /**
     * @param textContent
     */
    public void setAction(String textContent);

    /**
     * @param textContent
     */
    public void setRelatesTo(String textContent);

    /**
     * @param textContentFromChildNode
     */
    public void setReplyTo(String textContentFromChildNode);

    /**
     * @param textContentFromChildNode
     */
    public void setFrom(String textContentFromChildNode);

    /**
     * @param textContentFromChildNode
     */
    public void setDirectFrom(String textContentFromChildNode);

    /**
     * @param textContentFromChildNode
     */
    public void setDirectTo(String textContentFromChildNode);

    /**
     * @param textContentFromChildNode
     */
    public void setDirectMetaDataLevel(String textContentFromChildNode);

    /**
     * @return
     */
    public String getDirectTo();

    /**
     * @return
     */
    public String getDirectFrom();
}
