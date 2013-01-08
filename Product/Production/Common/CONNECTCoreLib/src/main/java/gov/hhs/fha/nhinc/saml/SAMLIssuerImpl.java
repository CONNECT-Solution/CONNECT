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
package gov.hhs.fha.nhinc.saml;

import java.util.Properties;

import org.apache.ws.security.WSSecurityException;

/**
 * This class extends the default SAMLIssuerImpl to replace the passed in properties with the ones from the nhinc
 * configuration directory. This is to allow the saml properties file to be configurable outside the ear file and is a
 * workaround to a WSS4J bug that expects that file to always be in the classpath. More info can be found here:
 * 
 * https://issues.connectopensource.org/browse/GATEWAY-3306, https://issues.apache.org/jira/browse/WSS-418
 */
public class SAMLIssuerImpl extends org.apache.ws.security.saml.SAMLIssuerImpl {

    /**
     * This constructor replaces the passed in properties with the one read from the nhinc configuration directory.
     * 
     * @param prop - this parameter is ignored
     * @throws WSSecurityException
     */
    public SAMLIssuerImpl(Properties prop) throws WSSecurityException {
        super(SAMLConfigFactory.getInstance().getConfiguration());
    }
}
