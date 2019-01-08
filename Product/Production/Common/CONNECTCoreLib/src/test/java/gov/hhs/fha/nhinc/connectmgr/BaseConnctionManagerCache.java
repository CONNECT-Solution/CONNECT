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
package gov.hhs.fha.nhinc.connectmgr;

import gov.hhs.fha.nhinc.connectmgr.persistance.dao.ExchangeInfoDAOFileImpl;
import gov.hhs.fha.nhinc.connectmgr.persistance.dao.InternalConnectionInfoDAOFileImpl;
import gov.hhs.fha.nhinc.connectmgr.persistance.dao.UddiConnectionInfoDAOFileImpl;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import static org.junit.Assert.assertTrue;

/**
 * @author msw
 *
 */
public class BaseConnctionManagerCache {

    protected static String HCID_1 = "1.1";
    protected static String HCID_2 = "2.2";
    protected static String HCID_3 = "3.3";
    protected static String VERSION_OF_SERVICE_2_0 = "2.0";
    protected static String VERSION_OF_SERVICE_3_0 = "3.0";
    protected static String QUERY_FOR_DOCUMENTS_NAME = "QueryForDocuments";
    protected static String RETRIEVE_DOCUMENTS_NAME = "RetrieveDocuments";
    protected static String DOCUMENT_SUBMISSION_NAME = "DocumentSubmission";
    protected static String NHIN_TARGET_ENDPOINT_URL_VALUE = "http://localhost:8080/";
    protected static String QUERY_FOR_DOCUMENTS_URL = "https://localhost:8181/QueryForDocuments";
    protected static String QUERY_FOR_DOCUMENTS_NULL_URL = "";
    protected static String DOC_QUERY_DEFERRED_NAME = "QueryForDocumentsDeferredRequest";
    protected static String QUERY_FOR_DOCUMENTS_DEFERRED_URL = "https://localhost:8181/QueryForDocumentsDeferredRequest";
    protected static String QUERY_FOR_DOCUMENTS_URL_22 = "https://server2:8181/QueryForDocuments";
    protected static String QUERY_FOR_DOCUMENTS_DEFERRED_URL_22
        = "https://server2:8181/QueryForDocumentsDeferredRequest";
    protected static String QUERY_FOR_DOCUMENTS_URL_3 = "https://server2:8181/QueryForDocuments/3_0";
    protected static String QUERY_FOR_DOCUMENTS_URL_2 = "https://server2:8181/QueryForDocuments/2_0";

    protected static String FL_REGION_VALUE = "US-FL";

    protected UddiConnectionInfoDAOFileImpl createUddiConnectionInfoDAO(String filename) {
        try {
            URL url = this.getClass().getResource(filename);

            File uddiConnectionFile = new File(url.toURI());
            assertTrue("File does not exist: " + uddiConnectionFile, uddiConnectionFile.exists());
            UddiConnectionInfoDAOFileImpl uddiDAO = UddiConnectionInfoDAOFileImpl.getInstance();
            uddiDAO.setFileName(uddiConnectionFile.getAbsolutePath());

            return uddiDAO;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    protected InternalConnectionInfoDAOFileImpl createInternalConnectionInfoDAO(String filename) {
        try {
            URL url = this.getClass().getResource(filename);
            File internalConnectionFile = new File(url.toURI());
            assertTrue("File does not exist: " + internalConnectionFile, internalConnectionFile.exists());
            InternalConnectionInfoDAOFileImpl internalDAO = InternalConnectionInfoDAOFileImpl.getInstance();
            internalDAO.setFileName(internalConnectionFile.getAbsolutePath());

            return internalDAO;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    protected ExchangeInfoDAOFileImpl createExchangeInfoDAO(String filename) {
        try {
            URL url = this.getClass().getResource(filename);
            File exchangeInfoFile = new File(url.toURI());
            assertTrue("File does not exist: " + exchangeInfoFile, exchangeInfoFile.exists());
            ExchangeInfoDAOFileImpl exchangeDAO = ExchangeInfoDAOFileImpl.getInstance();
            exchangeDAO.setFileName(exchangeInfoFile.getAbsolutePath());

            return exchangeDAO;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
