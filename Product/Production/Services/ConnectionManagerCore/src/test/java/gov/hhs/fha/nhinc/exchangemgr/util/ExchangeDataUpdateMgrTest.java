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
package gov.hhs.fha.nhinc.exchangemgr.util;

import gov.hhs.fha.nhinc.common.exchangemanagement.ExchangeDownloadStatusType;
import gov.hhs.fha.nhinc.connectmgr.persistance.dao.ExchangeInfoDAOFileImpl;
import gov.hhs.fha.nhinc.connectmgr.uddi.UDDIAccessor;
import gov.hhs.fha.nhinc.connectmgr.uddi.UDDIAccessorException;
import gov.hhs.fha.nhinc.exchange.ExchangeType;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.exchangemgr.fhir.FhirClient;
import gov.hhs.fha.nhinc.exchangemgr.fhir.FhirClientException;
import gov.hhs.fha.nhinc.exchangemgr.fhir.MimeType;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import org.apache.http.client.methods.HttpGet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.uddi.api_v3.BusinessDetail;

/**
 *
 * @author tjafri
 */
public class ExchangeDataUpdateMgrTest {

    private static final String FILE_NAME = "/config/ExchangeDataUpdateMgrTest/exchangeInfoTest.xml";
    private static final String FHIR_XML
        = "<Bundle xmlns=\"http://hl7.org/fhir\"><id value=\"d8f98af1-b053-0135-867c-1fa06c71efa4\"/><meta><lastUpdated "
        + "value=\"2017-11-20T19:06:33+00:00\"/></meta><type value=\"searchset\"/><total value=\"1\"/><link><relation "
        + "value=\"self\"/><url value=\"/Organization?_format=xml\"/></link><entry><fullUrl value=\"/Organization/0\"/>"
        + "<resource><Organization xmlns=\"http://hl7.org/fhir\"><id value=\"0\"/><meta><versionId value=\"1\"/>"
        + "<lastUpdated value=\"2017-10-25T13:50:42-05:00\"/></meta><identifier><use value=\"official\"/>"
        + "<system value=\"http://www.hl7.org/oid/\"/><value value=\"urn:oid:2.16.840.1.113883.3.596\"/>"
        + "</identifier><active value=\"true\"/><name value=\"TestOrg1\"/></Organization></resource>"
        + "</entry></Bundle>";
    private static final String BAD_XML
        = "<badBundle xmlns=\"http://hl7.org/fhir\"><id value=\"d8f98af1-b053-0135-867c-1fa06c71efa4\"/><meta><lastUpdated "
        + "value=\"2017-11-20T19:06:33+00:00\"/></meta><type value=\"searchset\"/><total value=\"1\"/><link><relation "
        + "value=\"self\"/><url value=\"/Organization?_format=xml\"/></link></badBundle>";
    private final ExchangeInfoDAOFileImpl exDao = createExchangeInfoDAO(FILE_NAME);
    private final UDDIAccessor uddiAccessor = mock(UDDIAccessor.class);
    private final FhirClient fhirClient = mock(FhirClient.class);
    private final HttpGet mockRequest = mock(HttpGet.class);

    @Test
    public void testExchangeDownloadSuccess() throws ExchangeManagerException, UDDIAccessorException,
        DatatypeConfigurationException, FhirClientException {
        ExchangeDataUpdateMgr workerThread = createExchangeScheduledTask();
        when(uddiAccessor.retrieveFromUDDIServer(Mockito.any(ExchangeType.class))).thenReturn(
            new BusinessDetail());
        when(fhirClient.doGet(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(MimeType.class))).
            thenReturn(FHIR_XML);
        List<ExchangeDownloadStatusType> status = workerThread.task();
        assertNotNull(exDao.loadExchangeInfo().getExchanges().getExchange().get(0).getLastUpdated());
        assertEquals("No of exchanges do not match", 2, status.size());
        assertEquals("No of statuses for exchanges 1 do not match", 3, status.get(0).getStepStatus().size());
        assertEquals("Expecting Download successful status for Exchange 1", "Download successful.", status.get(
            0).getStepStatus().get(0).getMessage());
        assertEquals("Expecting Schema Validation skipped status for Exchange 1", "Schema Validation skipped.",
            status.get(0).getStepStatus().get(1).getMessage());
        assertEquals("Expecting Exchange refresh successful status for Exchange 1", "Exchange refresh successful.",
            status.get(0).getStepStatus().get(2).getMessage());
        assertEquals("Expecting Download successful status for Exchange 2", "Download successful.", status.get(
            1).getStepStatus().get(0).getMessage());
        assertEquals("Expecting Schema Validation successful status for Exchange 2", "Schema Validation successful.",
            status.get(1).getStepStatus().get(1).getMessage());
        assertEquals("Expecting Exchange refresh successful status for Exchange 2", "Exchange refresh successful.",
            status.get(1).getStepStatus().get(2).getMessage());
    }

    @Test
    public void testFhirExchangeDownloadFailure() throws ExchangeManagerException, UDDIAccessorException,
        DatatypeConfigurationException, FhirClientException {
        ExchangeDataUpdateMgr workerThread = createExchangeScheduledTask();
        when(uddiAccessor.retrieveFromUDDIServer(Mockito.any(ExchangeType.class))).thenReturn(
            new BusinessDetail());
        when(fhirClient.doGet(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(MimeType.class))).
            thenThrow(new FhirClientException());
        List<ExchangeDownloadStatusType> status = workerThread.task();
        assertNotNull(exDao.loadExchangeInfo().getExchanges().getExchange().get(0).getLastUpdated());
        assertEquals("Expecting Download successful status for Exchange 1", "Download successful.", status.get(
            0).getStepStatus().get(0).getMessage());
        assertEquals("Expecting Schema Validation skipped status for Exchange 1", "Schema Validation skipped.",
            status.get(0).getStepStatus().get(1).getMessage());
        assertEquals("Expecting Exchange refresh successful status for Exchange 1", "Exchange refresh successful.",
            status.get(0).getStepStatus().get(2).getMessage());
        assertEquals("Expecting Download failed status for Exchange 2", "Download failed.", status.get(1).
            getStepStatus().
            get(0).getMessage());
    }

    @Test
    public void testFhirExchangeSchemaValidationFailed() throws ExchangeManagerException, UDDIAccessorException,
        DatatypeConfigurationException, FhirClientException {
        ExchangeDataUpdateMgr workerThread = createExchangeScheduledTask();
        when(uddiAccessor.retrieveFromUDDIServer(Mockito.any(ExchangeType.class))).thenReturn(
            new BusinessDetail());
        when(fhirClient.doGet(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(MimeType.class))).
            thenReturn(BAD_XML);
        List<ExchangeDownloadStatusType> status = workerThread.task();
        assertNotNull(exDao.loadExchangeInfo().getExchanges().getExchange().get(0).getLastUpdated());
        assertEquals("Expecting Download successful status for Exchange 1", "Download successful.", status.get(
            0).getStepStatus().get(0).getMessage());
        assertEquals("Expecting Schema Validation skipped status for Exchange 1", "Schema Validation skipped.",
            status.get(0).getStepStatus().get(1).getMessage());
        assertEquals("Expecting Exchange refresh successful status for Exchange 1", "Exchange refresh successful.",
            status.get(0).getStepStatus().get(2).getMessage());
        assertEquals("Expecting Download successful status for Exchange 2", "Download successful.", status.get(1).
            getStepStatus().get(0).getMessage());
        assertEquals("Expecting Schema Validation failed status for Exchange 2", "Schema Validation failed.",
            status.get(1).getStepStatus().get(1).getMessage());
        assertEquals("Expecting Exchange refresh failed status for Exchange 2", "Exchange refresh failed.",
            status.get(1).getStepStatus().get(2).getMessage());
    }

    @Test
    public void testRefreshLockReleaseForFetchSuccess() throws ExchangeManagerException, UDDIAccessorException,
        DatatypeConfigurationException, FhirClientException {
        ExchangeDataUpdateMgr exUpdateMgr = createExchangeScheduledTask();
        when(uddiAccessor.retrieveFromUDDIServer(Mockito.any(ExchangeType.class))).thenReturn(
            new BusinessDetail());
        when(fhirClient.doGet(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(MimeType.class))).
            thenReturn(FHIR_XML);
        exUpdateMgr.task();
        assertTrue(!exUpdateMgr.getExchangeDAO().isRefreshLocked());
    }

    @Test
    public void testRefreshLockReleaseForFetchFailure() throws ExchangeManagerException, UDDIAccessorException,
        DatatypeConfigurationException, FhirClientException {
        ExchangeDataUpdateMgr exUpdateMgr = createExchangeScheduledTask();
        when(uddiAccessor.retrieveFromUDDIServer(Mockito.any(ExchangeType.class))).thenReturn(
            new BusinessDetail());
        when(fhirClient.doGet(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(MimeType.class))).
            thenThrow(new RuntimeException());
        exUpdateMgr.task();
        assertTrue(!exUpdateMgr.getExchangeDAO().isRefreshLocked());
    }

    private ExchangeDataUpdateMgr createExchangeScheduledTask() {
        return new ExchangeDataUpdateMgr() {
            @Override
            protected ExchangeInfoDAOFileImpl getExchangeDAO() {
                return exDao;
            }

            @Override
            protected UDDIAccessor getUDDIAccessor() {
                return uddiAccessor;
            }

            @Override
            protected FhirClient getFhirClient() {
                return fhirClient;
            }
        };
    }

    private ExchangeInfoDAOFileImpl createExchangeInfoDAO(String filename) {
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
