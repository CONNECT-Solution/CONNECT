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
package gov.hhs.fha.nhinc.perfrepo.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 
 * @author richard.ettema
 */
public class Perfrepository implements Serializable {

    private Long id;
    private Timestamp time;
    private String servicetype;
    private String messagetype;
    private String direction;
    private String communityid;
    private Integer status;
    
    private String version;
    private String payloadType;
    private String size;
    private String correlationId;
    private String otherCommunityId;
    private String errorCode;
    
    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id new value for id
     */
    public void setId(Long id) {
        this.id = id;
    }

   
   
    /**
     * @return servicetype
     */
    public String getServicetype() {
        return servicetype;
    }

    /**
     * @param servicetype new value for servicetype
     */
    public void setServicetype(String servicetype) {
        this.servicetype = servicetype;
    }

    /**
     * @return messagetype
     */
    public String getMessagetype() {
        return messagetype;
    }

    /**
     * @param messagetype new value for messgtetype
     */
    public void setMessagetype(String messagetype) {
        this.messagetype = messagetype;
    }

    /**
     * @return direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * @param direction new value for direction
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * @return communityid
     */
    public String getCommunityid() {
        return communityid;
    }

    /**
     * @param communityid new value for communityid
     */
    public void setCommunityid(String communityid) {
        this.communityid = communityid;
    }

    /**
     * @return status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status new value for status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessageVersion() {
        return version;
    }

    public void setMessageVersion(String messageVersion) {
        this.version = messageVersion;
    }

    public String getPayLoadType() {
        return payloadType;
    }

    public void setPayLoadType(String payLoadType) {
        this.payloadType = payLoadType;
    }

    public String getPayLoadSize() {
        return size;
    }

    public void setPayLoadSize(String payLoadSize) {
        this.size = payLoadSize;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getOtherCommunityId() {
        return otherCommunityId;
    }

    public void setOtherCommunityId(String otherCommunityId) {
        this.otherCommunityId = otherCommunityId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * @return the time
     */
    public Timestamp getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(Timestamp time) {
        this.time = time;
    }

}
