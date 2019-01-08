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
package gov.hhs.fha.nhinc.exchange.transform.uddi;

import gov.hhs.fha.nhinc.connectmgr.BaseConnctionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.persistance.dao.InternalConnectionInfoDAOFileImpl;
import gov.hhs.fha.nhinc.connectmgr.persistance.dao.InternalExchangeInfoDAOFileImpl;
import gov.hhs.fha.nhinc.exchange.ExchangeInfoType;
import gov.hhs.fha.nhinc.exchange.ExchangeListType;
import gov.hhs.fha.nhinc.exchange.ExchangeType;
import gov.hhs.fha.nhinc.exchange.OrganizationListType;
import gov.hhs.fha.nhinc.exchange.directory.OrganizationType;
import gov.hhs.fha.nhinc.exchange.transform.ExchangeTransformException;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uddi.api_v3.BusinessDetail;

/**
 *
 * @author tjafri
 */
public class UDDITransformTest extends BaseConnctionManagerCache {

    private static final String UDDI_FILE = "/config/ConnectionManagerCacheTest/uddiToExchangeSchemaTransform.xml";
    private static final String ICI_FILE = "/config/ConnectionManagerCacheTest/internalConnectionInfoAdapterTest.xml";
    private static final String INTERNAL_EXCHANE_FILE
        = "/config/ConnectionManagerCacheTest/internalExchangeInfoTest.xml";
    private static final Logger LOG = LoggerFactory.getLogger(UDDITransformTest.class);
    private static final String ENTITY_1_NAME = "Health Solutions";
    private static final String ENTITY_1_HCID = "urn:oid:2";
    private static final int ENTITY_1_CONTACTS = 3;
    private static final String[] ENTITY_1_REGION = {"US-MD", "US-VA"};
    private static final int ENTITY_1_NUM_OF_SERVICES = 2;
    private static final String PATIENT_DISCOVERY = "PatientDiscovery";
    private static final String QUERY_FOR_DOCUMENT = "QueryForDocument";
    private static final String QUERY_FOR_DOCUMENTS = "QueryForDocuments";
    private static final int ENTITY_1_NUM_OF_PD_URL = 2;
    private static final int ENTITY_1_NUM_OF_QD_URL = 2;
    private static final String ENTITY_1_PD_URL
        = "https://conformance1.HealthSolutions.com/CONNECTGateway/1_0/NhinService/NhinPatientDiscovery";
    private static final String ENTITY_1_QD_URL2
        = "https://conformance1.HealthSolutions.com/CONNECTGateway/2_0/NhinService/DocQuery";
    private static final String ENTITY_1_QD_URL3
        = "https://conformance1.HealthSolutions.com/CONNECTGateway/3_0/NhinService/DocQuery";
    private static final String SPEC_VERSION1 = "1.0";
    private static final String SPEC_VERSION2 = "2.0";
    private static final String SPEC_VERSION3 = "3.0";
    private static final String CONTACT_NAME_1 = "Doe, Jhon";
    private static final String CONTACT_PHONE_1 = "804-571-0458";
    private static final String CONTACT_EMAIL_1 = "jdoe@hSolution.com";
    private static final String CONTACT_ADDRLINE_1 = "11710 Plaza America Drive Suite 2000";
    private static final String CONTACT_ADDRLINE_2 = "Reston, VA 20190";
    private UDDITransform transformer = null;
    private BusinessDetail bDetail = null;

    @Before
    public void setup() {
        transformer = new UDDITransform();
        try {
            bDetail = loadBusinessDetail(UDDI_FILE);
        } catch (Exception ex) {
            LOG.error("Unable to read {} {}", UDDI_FILE, ex.getLocalizedMessage(), ex);
        }
    }

    @Test
    public void testBusinessEntity() throws ExchangeTransformException {
        List<OrganizationType> org = transformer.transform(bDetail).getOrganization();
        verifyOrganizationsWithBusinessEntities(org);
    }

    @Test
    public void testNullBusinessEnity() throws ExchangeTransformException {
        List<OrganizationType> org = transformer.transform(null).getOrganization();
        assertNotNull(org);
        assertEquals("Expecting emoty organization list", 0, org.size());
    }

    @Test
    public void testEmptyBusinessEnity() throws ExchangeTransformException {
        List<OrganizationType> org = transformer.transform(new BusinessDetail()).getOrganization();
        assertNotNull(org);
        assertEquals("Expecting emoty organization list", 0, org.size());
    }

    @Test
    public void testEmptyBusinessEnityWithNoService() throws ExchangeTransformException {
        BusinessDetail uddiDetail = new BusinessDetail();
        uddiDetail.getBusinessEntity().add(bDetail.getBusinessEntity().get(1));
        List<OrganizationType> org = transformer.transform(uddiDetail).getOrganization();
        assertNotNull(org);
        assertNull(org.get(0).getEndpointList());
    }

    @Test
    public void testBusinessEnityWithNoEndpoints() throws ExchangeTransformException {
        BusinessDetail uddiDetail = new BusinessDetail();
        uddiDetail.getBusinessEntity().add(bDetail.getBusinessEntity().get(2));
        List<OrganizationType> org = transformer.transform(uddiDetail).getOrganization();
        assertNotNull(org);
        assertNotNull(org.get(0).getEndpointList());
        assertEquals("Expecting an empty EndpointList", 0, org.get(0).getEndpointList().getEndpoint().size());
    }

    @Test
    public void transformICI() throws ExchangeManagerException, ExchangeTransformException {
        try {
            InternalConnectionInfoDAOFileImpl internalDao = createInternalConnectionInfoDAO(ICI_FILE);
            OrganizationListType org = transformer.transform(internalDao.loadBusinessDetail());
            ExchangeInfoType exInfo = new ExchangeInfoType();
            exInfo.setDefaultExchange("adapter");
            ExchangeListType exList = new ExchangeListType();
            ExchangeType exType = new ExchangeType();
            exType.setName("adapter");
            exType.setType("local");
            exType.setOrganizationList(org);
            exList.getExchange().add(exType);
            exInfo.setExchanges(exList);
            InternalExchangeInfoDAOFileImpl exDao = createInternalExchangeInfoDAO(INTERNAL_EXCHANE_FILE);
            exDao.saveExchangeInfo(exInfo);
        } catch (ConnectionManagerException | ExchangeManagerException ex) {
            LOG.error("Unable to save {} {}", ICI_FILE, ex.getLocalizedMessage(), ex);
        }
    }

    private void verifyOrganizationsWithBusinessEntities(List<OrganizationType> orgList) {
        assertEquals("Transform Object should have 3 organizations", orgList.size(), 3);
        OrganizationType org = orgList.get(0);
        assertEquals("Organization Name does not match", ENTITY_1_NAME, org.getName());
        assertEquals("Organization HCID does not match", ENTITY_1_HCID, org.getHcid());
        assertArrayEquals("Target Region do not match", ENTITY_1_REGION, org.getTargetRegion().toArray());
        assertEquals("Number of Contacts do not match", ENTITY_1_CONTACTS, org.getContact().size());
        assertEquals("Business entity 1 --> Contact 1 fullName do not match", CONTACT_NAME_1, org.getContact().get(0).
            getFullName().get(0));
        assertEquals("Business entity 1 --> Contact 1 phone do not match", CONTACT_PHONE_1, org.getContact().get(0).
            getPhone().get(0));
        assertEquals("Business entity 1 --> Contact 1 email do not match", CONTACT_EMAIL_1, org.getContact().get(0).
            getEmail().get(0));
        assertEquals("Business entity 1 --> Address not match", CONTACT_ADDRLINE_1,
            org.getContact().get(0).getAddress().get(0).getAddressLine().get(0));
        assertEquals("Business entity 1 --> Address not match", CONTACT_ADDRLINE_2,
            org.getContact().get(0).getAddress().get(0).getAddressLine().get(1));

        assertEquals("Number of Endpoints(Services) do not match", ENTITY_1_NUM_OF_SERVICES, org.
            getEndpointList().getEndpoint().size());
        assertEquals("Service Name do not match", PATIENT_DISCOVERY, org.getEndpointList().getEndpoint().get(0).
            getName().get(0));
        assertEquals("PatientDiscovery endpoints do not match", ENTITY_1_NUM_OF_PD_URL, org.getEndpointList().
            getEndpoint().get(0).getEndpointConfigurationList().getEndpointConfiguration().size());
        assertEquals("PatientDiscovery endpoint urls do not match", ENTITY_1_PD_URL, org.getEndpointList().
            getEndpoint().get(0).getEndpointConfigurationList().getEndpointConfiguration().get(0).getUrl());
        assertEquals("PatientDiscovery endpoint version do not match", SPEC_VERSION1, org.getEndpointList().
            getEndpoint().get(0).getEndpointConfigurationList().getEndpointConfiguration().get(0).getVersion());

        assertEquals("PatientDiscovery endpoint urls do not match", ENTITY_1_PD_URL, org.getEndpointList().
            getEndpoint().get(0).getEndpointConfigurationList().getEndpointConfiguration().get(1).getUrl());
        assertEquals("PatientDiscovery endpoint urls do not match", SPEC_VERSION2, org.getEndpointList().
            getEndpoint().get(0).getEndpointConfigurationList().getEndpointConfiguration().get(1).getVersion());

        assertEquals("Service Name do not match", QUERY_FOR_DOCUMENT, org.getEndpointList().
            getEndpoint().get(1).getName().get(0));
        assertEquals("Service Name do not match", QUERY_FOR_DOCUMENTS, org.getEndpointList().
            getEndpoint().get(1).getName().get(1));
        assertEquals("QueryForDocuments endpoints do not match", ENTITY_1_NUM_OF_QD_URL, org.getEndpointList().
            getEndpoint().get(1).getEndpointConfigurationList().getEndpointConfiguration().size());
        assertEquals("QueryForDocuments endpoint urls do not match", ENTITY_1_QD_URL2, org.getEndpointList().
            getEndpoint().get(1).getEndpointConfigurationList().getEndpointConfiguration().get(0).getUrl());

        assertEquals("QueryForDocuments endpoint version do not match", SPEC_VERSION2, org.getEndpointList().
            getEndpoint().get(1).getEndpointConfigurationList().getEndpointConfiguration().get(0).getVersion());

        assertEquals("QueryForDocuments endpoint urls do not match", ENTITY_1_QD_URL3, org.getEndpointList().
            getEndpoint().get(1).getEndpointConfigurationList().getEndpointConfiguration().get(1).getUrl());
        assertEquals("QueryForDocuments endpoint urls do not match", SPEC_VERSION3, org.getEndpointList().
            getEndpoint().get(1).getEndpointConfigurationList().getEndpointConfiguration().get(1).getVersion());
    }

    private BusinessDetail loadBusinessDetail(String fileName) throws Exception {
        return createUddiConnectionInfoDAO(fileName).loadBusinessDetail();
    }

    protected InternalExchangeInfoDAOFileImpl createInternalExchangeInfoDAO(String filename) {
        try {
            URL url = this.getClass().getResource(filename);
            File exchangeInfoFile = new File(url.toURI());
            assertTrue("File does not exist: " + exchangeInfoFile, exchangeInfoFile.exists());
            InternalExchangeInfoDAOFileImpl exchangeDAO = InternalExchangeInfoDAOFileImpl.getInstance();
            exchangeDAO.setFileName(exchangeInfoFile.getAbsolutePath());

            return exchangeDAO;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
