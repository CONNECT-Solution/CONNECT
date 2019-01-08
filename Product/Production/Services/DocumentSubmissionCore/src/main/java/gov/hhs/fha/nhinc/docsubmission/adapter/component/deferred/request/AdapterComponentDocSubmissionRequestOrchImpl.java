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
package gov.hhs.fha.nhinc.docsubmission.adapter.component.deferred.request;

import gov.hhs.fha.nhinc.largefile.LargeFileUtils;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;
import java.net.URI;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the Java implementation for the AdapterComponentXDRRequest service. This is intended to be overridden by the
 * adapter. It really does nothing but returns the ACK message.
 *
 * @author Les Westberg
 */
public class AdapterComponentDocSubmissionRequestOrchImpl {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterComponentDocSubmissionRequestOrchImpl.class);

    /**
     * This method receives the document information
     *
     * @param body The XDR request message
     * @return The ACK
     */
    // This is a dummy adapter - ignoring the URL parameter in interest of not updating the interface
    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(
        ProvideAndRegisterDocumentSetRequestType body) {
        LOG.debug("Entering AdapterComponentXDRRequestOrchImpl.provideAndRegisterDocumentSetBRequest");

        processRequest(body);

        XDRAcknowledgementType response = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        response.setMessage(regResp);

        return response;
    }

    private static void processRequest(ProvideAndRegisterDocumentSetRequestType body) {
        LargeFileUtils fileUtils = LargeFileUtils.getInstance();
        List<Document> docList = body.getDocument();
        for (Document doc : docList) {
            try {
                if (fileUtils.isParsePayloadAsFileLocationEnabled()) {
                    URI payloadURI = fileUtils.parseBase64DataAsUri(doc.getValue());
                    if (NullChecker.isNotNullish(payloadURI.toString())) {
                        LOG.debug("Successfully parsed payload URI from Base64");
                    }
                } else {
                    LOG.debug("Closing request input streams.");
                    LargeFileUtils.getInstance().closeStreamWithoutException(
                        doc.getValue().getDataSource().getInputStream());
                }
            } catch (Exception ioe) {
                LOG.error("Failed to close input stream", ioe);
            }
        }
    }
}
