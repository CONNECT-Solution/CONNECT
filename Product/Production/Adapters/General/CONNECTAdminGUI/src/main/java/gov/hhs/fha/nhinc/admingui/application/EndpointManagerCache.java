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
package gov.hhs.fha.nhinc.admingui.application;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jassmit
 */
public class EndpointManagerCache implements EndpointManager {

    private final Map<String, Map<Integer, EndpointCacheInfo>> endpointCache = new HashMap<>();

    public EndpointManagerCache() {
    }

    @Override
    public void addOrUpdateEndpoint(String exchangeName, int id, String url, Date timestamp, boolean pingResult,
        int code) {
        getCache(exchangeName).put(id, new EndpointCacheInfo(id, url, timestamp, pingResult, code));
    }

    private Map<Integer, EndpointCacheInfo> getCache(String exchangeName) {
        if (null == endpointCache.get(exchangeName)) {
            endpointCache.put(exchangeName, new HashMap<Integer, EndpointCacheInfo>());
        }
        return endpointCache.get(exchangeName);
    }

    @Override
    public EndpointCacheInfo getEndpointInfo(String exchangeName, int id) {
        return getCache(exchangeName).get(id);
    }

    @Override
    public void loadCache(String exchangeName, Collection<EndpointCacheInfo> endpoints) {
        for (EndpointCacheInfo endpoint : endpoints) {
            getCache(exchangeName).put(endpoint.getId(), endpoint);
        }
    }

    @Override
    public Collection getAllCache() {
        return endpointCache.values();
    }

    @Override
    public void deleteCache(String exchangeName) {
        endpointCache.put(exchangeName, null);
    }

    public class EndpointCacheInfo {

        private String exchangeName;
        private String url;
        private Date timestamp;
        private boolean successfulPing;
        private int httpCode;
        private Integer id;

        public EndpointCacheInfo(int id, String url, Date timestamp, boolean successfulPing, int httpCode)
        {
            this.id = id;
            this.url = url;
            this.timestamp = timestamp;
            this.successfulPing = successfulPing;
            this.httpCode = httpCode;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
        }

        public boolean isSuccessfulPing() {
            return successfulPing;
        }

        public void setSuccessfulPing(boolean successfulPing) {
            this.successfulPing = successfulPing;
        }

        public int getHttpCode() {
            return httpCode;
        }

        public void setHttpCode(int httpCode) {
            this.httpCode = httpCode;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getExchangeName() {
            return exchangeName;
        }

        public void setExchangeName(String exchangeName) {
            this.exchangeName = exchangeName;
        }

    }

}
