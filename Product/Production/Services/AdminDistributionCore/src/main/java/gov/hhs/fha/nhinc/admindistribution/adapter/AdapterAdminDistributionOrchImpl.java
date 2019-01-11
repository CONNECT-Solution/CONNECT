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
package gov.hhs.fha.nhinc.admindistribution.adapter;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.largefile.LargeFileUtils;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.net.URI;
import java.util.List;
import javax.activation.DataHandler;
import oasis.names.tc.emergency.edxl.de._1.ContentObjectType;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dunnek
 */
public class AdapterAdminDistributionOrchImpl {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterAdminDistributionOrchImpl.class);

    /**
     * @param body Emergency Message Distribution Element transaction received.
     * @param assertion Assertion received.
     */
    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion) {
        LOG.info("Received Alert Message");
        LOG.info(body.getCombinedConfidentiality());
        LOG.info("Time Sent: " + body.getDateTimeSent());
        LOG.info("Sender Id: " + body.getSenderID());
        LOG.info("Keyword: " + body.getKeyword().toString());

        try {
            if (LargeFileUtils.getInstance().isSavePayloadToFileEnabled()) {
                LOG.debug("Configured to save payload to file. Will try to parse content as File URI.");

                List<ContentObjectType> contentObjectList = body.getContentObject();

                for (ContentObjectType co : contentObjectList) {
                    if (co.getNonXMLContent() != null) {
                        DataHandler data = co.getNonXMLContent().getContentData();
                        URI payloadURI = LargeFileUtils.getInstance().parseBase64DataAsUri(data);
                        if (NullChecker.isNotNullish(payloadURI.toString())) {
                            LOG.debug("Successfully parsed payload URI from Base64");
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Failed to parse payload as URI.", e);
        }

    }
}
