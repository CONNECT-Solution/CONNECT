/**
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
package gov.hhs.fha.nhinc.admindistribution.aspect;

import java.util.ArrayList;
import java.util.List;

import oasis.names.tc.emergency.edxl.de._1.ContentObjectType;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author zmelnick
 *
 */
public class EDXLDistributionDescriptionExtractor {

    private static final Log LOG = LogFactory.getLog(EDXLDistributionDescriptionExtractor.class);

    /**
     * Determines the payload sizes for each alert message.
     * @param alertMessage the EDXLDistribution.
     * @return list of sizes
     */
    public List<String> getPayloadSizes(EDXLDistribution alertMessage) {
        List<String> payloadSize = new ArrayList<String>();
        if (alertMessage != null) {
            List<ContentObjectType> contents = alertMessage.getContentObject();
            for (ContentObjectType message : contents) {
                if (isPayloadSizeEmpty(message)) {
                    LOG.info("Paylod size not provided");
                    payloadSize.clear();
                    break;
                } else {
                    payloadSize.add(message.getNonXMLContent().getSize().toString());
                }
            }
        }
        return payloadSize;
    }

   
    private boolean isPayloadSizeEmpty(ContentObjectType message) {
        return message.getXmlContent() != null
                || (message.getNonXMLContent() != null && message.getNonXMLContent().getSize() == null);
    }

}
