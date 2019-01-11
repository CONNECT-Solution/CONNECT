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
package gov.hhs.fha.nhinc.docretrieve.util;

import gov.hhs.fha.nhinc.document.DocumentConstants;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

/**
 * @author achidamb
 *
 */
public class DocRetrieveStatusUtil {

    /**
     * Sets the response status (Success, Partial, Failure) of the to ResponseType based on the from ResponseType.
     *
     * @param from the ResponseType to based the status from
     * @param to the ResponseType whose status is to be modified
     * @return a String containing the response set on the from ResponseType
     */
    public static String setResponseStatus(RetrieveDocumentSetResponseType from, RetrieveDocumentSetResponseType to) {

        String status = null;

        if (to.getRegistryResponse().getStatus() == null) {
            status = from.getRegistryResponse().getStatus();
        } else if (from.getRegistryResponse().getStatus().equalsIgnoreCase(to.getRegistryResponse().getStatus())) {
            status = from.getRegistryResponse().getStatus();
        } else if (!from.getRegistryResponse().getStatus().equals(to.getRegistryResponse().getStatus())) {
            status = DocumentConstants.XDS_RETRIEVE_RESPONSE_STATUS_PARTIALSUCCESS;
        }
        return status;

    }

    /**
     * Returns true if the passed in status string has a success value.
     *
     * @param status String that contains the value to be checked
     * @return true if status string represents a success
     */
    public static boolean isStatusSuccess(String status) {
        return DocumentConstants.XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS.equalsIgnoreCase(status);
    }

    /**
     * Returns true if the passed in status string has a failure value.
     *
     * @param status String that contains the value to be checked
     * @return true if status string represents a failure or partial failure
     */
    public static boolean isStatusFailureOrPartialFailure(String status) {
        return !isStatusSuccess(status);
    }

}
