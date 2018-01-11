/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.connectmgr.uddi;

import gov.hhs.fha.nhinc.common.connectionmanagerinfo.SuccessOrFailType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.UDDIUpdateManagerForceRefreshRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.UDDIUpdateManagerForceRefreshResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uddi.api_v3.BusinessDetail;

/**
 * Helper class for the web service.
 *
 * @author Les Westberg
 */
public class UDDIUpdateManagerHelper {

    private static final Logger LOG = LoggerFactory.getLogger(UDDIUpdateManagerHelper.class);

    /**
     * This method retrieves the BusinessDetail data from the UDDI server. The BusinessDetail contains the list of
     * BusinessEntity that contains all the necessary information for each services.
     *
     * @return The BusinessDetail data retrieved from UDDI.
     */
    private static BusinessDetail retrieveDataFromUDDI(String exchangeURL) throws UDDIAccessorException {
        UDDIAccessor oUDDIAccessor = new UDDIAccessor();
        return oUDDIAccessor.retrieveFromUDDIServer(exchangeURL);
    }

    public BusinessDetail forceRefreshUDDIFile(String exchangeURL) throws UDDIAccessorException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Start: UDDIUpdateManagerHelper.forceRefreshUDDIFile method - loading from UDDI server.");
        }

        try {
            return retrieveDataFromUDDI(exchangeURL);

        } catch (UDDIAccessorException e) {
            String sErrorMessage = "Failed to retrieve data from UDDI.  Error: " + e.getLocalizedMessage();
            LOG.error("Error refreshing UDDI: {}", sErrorMessage, e);
            throw new UDDIAccessorException(sErrorMessage, e);
        }
    }

    /**
     * This method forces a refresh of the uddiConnectionInfo.xml file by retrieving the information from the UDDI NHIN
     * server.
     *
     * @param part1 No real data - just a way to keep the document unique.
     * @return True if the file was loaded false if it was not.
     */
    public UDDIUpdateManagerForceRefreshResponseType forceRefreshFileFromUDDIServer(
        UDDIUpdateManagerForceRefreshRequestType part1) {
        UDDIUpdateManagerForceRefreshResponseType oResponse = new UDDIUpdateManagerForceRefreshResponseType();
        oResponse.setSuccessOrFail(new SuccessOrFailType());
        oResponse.getSuccessOrFail().setSuccess(false);

        try {
            forceRefreshUDDIFile("");
            // If we got here - we succeeded.
            oResponse.getSuccessOrFail().setSuccess(true);
        } catch (Exception e) {
            LOG.error("Failed to refresh the file from the UDDI server: {}", e.getLocalizedMessage(), e);
        }

        return oResponse;
    }
}
