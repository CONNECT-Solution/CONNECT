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
package gov.hhs.fha.nhinc.cxf.extraction;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import java.util.List;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.headers.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * This class is used to extract the AssertionType from Saml2Assertion in the ws security header. This AssertionType is
 * provided to the responding HIO for using the information for authorization.
 *
 * @author mweaver
 *
 */
public class SAML2AssertionExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(SAML2AssertionExtractor.class);

    private static SAML2AssertionExtractor INSTANCE = null;

    private SAMLExtractorDOM extractor;

    SAML2AssertionExtractor() {
        SAMLExtractorDOMFactory factory = new SAMLExtractorDOMFactory();
        extractor = factory.getExtractor();
    }

    public static SAML2AssertionExtractor getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SAML2AssertionExtractor();

        }
        return INSTANCE;
    }

    /**
     * This method is used to extract the saml assertion information.
     *
     * @param context context
     * @return AssertionType
     */
    public final AssertionType extractSamlAssertion(final WebServiceContext context) {
        LOG.debug("Executing Saml2AssertionExtractor.extractSamlAssertion()...");
        AssertionType target = null;

        if (context == null) {
            return null;
        }

        MessageContext mContext = context.getMessageContext();
        SoapHeader header = getSecuritySoapHeader(mContext);

        if (header != null) {
            Object obj = header.getObject();
            Element element = (Element) obj;

            target = extractor.extractSAMLAssertion(element);
        }

        return target;
    }

    private final SoapHeader getSecuritySoapHeader(MessageContext mContext) {

        if (mContext != null) {
            @SuppressWarnings("unchecked")
            List<Header> headers = (List<Header>) mContext.get(org.apache.cxf.headers.Header.HEADER_LIST);

            if (headers != null) {
                for (Header header : headers) {
                    if (header.getName().getLocalPart().equalsIgnoreCase("Security")) {
                        return (SoapHeader) header;
                    }
                }
            }
        }
        return null;
    }
}
