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
package gov.hhs.fha.nhinc.admindistribution.adapter;

import java.net.URI;
import java.util.List;

import javax.activation.DataHandler;

import oasis.names.tc.emergency.edxl.de._1.ContentObjectType;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.largefile.LargeFileUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author dunnek
 */
public class AdapterAdminDistributionOrchImpl {
	private Log log = null;

	public AdapterAdminDistributionOrchImpl() {
		log = createLogger();
	}

	protected Log createLogger() {
		return LogFactory.getLog(getClass());
	}

	public void sendAlertMessage(EDXLDistribution body, AssertionType assertion) {
		log.info("Received Alert Message");
		log.info(body.getCombinedConfidentiality());
		log.info("Time Sent: " + body.getDateTimeSent());
		log.info("Sender Id: " + body.getSenderID());
		log.info("Keyword: " + body.getKeyword().toString());

		try {
			if (LargeFileUtils.getInstance().isSavePayloadToFileEnabled()) {
				log.info("Configured to save payload to file. Will try to parse content as File URI.");

				List<ContentObjectType> contentObjectList = body
						.getContentObject();

				for (ContentObjectType co : contentObjectList) {
					if (co.getNonXMLContent() != null) {
						DataHandler data = co.getNonXMLContent()
								.getContentData();
						URI paylaodURI = LargeFileUtils.getInstance()
								.parseBase64DataAsUri(data);
						log.info("Payload Content: " + paylaodURI);

					}
				}

			}
		} catch (Exception e) {
			log.error("Failed to parse payload as URI.", e);
		}

	}
}
