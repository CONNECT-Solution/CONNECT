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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import gov.hhs.fha.nhinc.document.DocumentConstants;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class DocRetrieveStatusUtilTest {

    @Test
    public void setResponseStatusSuccessCase() {
        DocRetrieveStatusUtil util = new DocRetrieveStatusUtil();
        String status = "Success";
        String returnedStatus;
        returnedStatus = util.setResponseStatus(createFromRequestSuccessCase(), createToRequestSucessCase());
        assertEquals(returnedStatus, status);
    }

    @Test
    public void setResponseStatusFailureCase() {
        DocRetrieveStatusUtil util = new DocRetrieveStatusUtil();
        String status = "Failure";
        String returnedStatus;
        returnedStatus = util.setResponseStatus(createFromRequestFailureCase(), createToRequestFailureCase());
        assertEquals(returnedStatus, status);
    }

    @Test
    public void setResponseStatusPartialSuccessCase() {
        DocRetrieveStatusUtil util = new DocRetrieveStatusUtil();
        String status = DocumentConstants.XDS_RETRIEVE_RESPONSE_STATUS_PARTIALSUCCESS;
        String returnedStatus;
        returnedStatus = util.setResponseStatus(createFromRequestSuccessCase(), createToRequestFailureCase());
        assertEquals(returnedStatus, status);
    }

    @Test
    public void testIsStatusSuccess() {
        String status = DocumentConstants.XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS;
        DocRetrieveStatusUtil util = new DocRetrieveStatusUtil();
        assertTrue(util.isStatusSuccess(status));
    }

    @Test
    public void testIsStatusFailureOrPartialFailure() {
        String status = DocumentConstants.XDS_RETRIEVE_RESPONSE_STATUS_FAILURE;
        DocRetrieveStatusUtil util = new DocRetrieveStatusUtil();
        assertFalse(util.isStatusSuccess(status));
    }

    private RetrieveDocumentSetResponseType createFromRequestSuccessCase() {
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        RegistryResponseType res = new RegistryResponseType();
        res.setStatus("Success");
        response.setRegistryResponse(res);
        return response;
    }

    private RetrieveDocumentSetResponseType createFromRequestFailureCase() {
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        RegistryResponseType res = new RegistryResponseType();
        res.setStatus("Failure");
        response.setRegistryResponse(res);
        return response;
    }

    private RetrieveDocumentSetResponseType createToRequestFailureCase() {
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        RegistryResponseType res = new RegistryResponseType();
        res.setStatus("Failure");
        response.setRegistryResponse(res);
        return response;
    }

    private RetrieveDocumentSetResponseType createToRequestSucessCase() {
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        RegistryResponseType res = new RegistryResponseType();
        res.setStatus("Success");
        response.setRegistryResponse(res);
        return response;
    }

}
