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
package gov.hhs.fha.nhinc.messaging.server;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author jassmit
 */
public class HttpHeaderHelper {
    
    private static final Set<String> STANDARD_HEADERS = new HashSet<>();
    
    static {
        STANDARD_HEADERS.add("Accept-Encoding".toUpperCase());
        STANDARD_HEADERS.add("Accept-Language".toUpperCase());
        STANDARD_HEADERS.add("Cookie".toUpperCase());
        STANDARD_HEADERS.add("Host".toUpperCase());
        STANDARD_HEADERS.add("User-Agent".toUpperCase());
        STANDARD_HEADERS.add("Referer".toUpperCase());
        STANDARD_HEADERS.add("Cache-Control".toUpperCase());
        STANDARD_HEADERS.add("Content-Encoding".toUpperCase());
        STANDARD_HEADERS.add("Content-Type".toUpperCase());
        STANDARD_HEADERS.add("Content-Location".toUpperCase());
        STANDARD_HEADERS.add("Transfer-Encoding".toUpperCase()); 
        STANDARD_HEADERS.add("Connection".toUpperCase());
        STANDARD_HEADERS.add("accept".toUpperCase());
        STANDARD_HEADERS.add("server".toUpperCase());
        STANDARD_HEADERS.add("keep-alive".toUpperCase());
        STANDARD_HEADERS.add("pragma".toUpperCase());
    }
    
    public boolean isStandardHeader(String value) {
        return NullChecker.isNotNullish(value) ? STANDARD_HEADERS.contains(value.toUpperCase()) : false;
    }
}
