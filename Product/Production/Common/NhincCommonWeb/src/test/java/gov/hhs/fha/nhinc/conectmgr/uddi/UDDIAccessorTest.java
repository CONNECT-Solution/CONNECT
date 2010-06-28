/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.conectmgr.uddi;

import com.sun.xml.registry.uddi.bindings_v2_2.URLType;
import org.uddi.api_v3.AccessPoint;
import org.uddi.api_v3.BindingTemplate;
import org.uddi.api_v3.BindingTemplates;
import org.uddi.api_v3.BusinessService;
import org.uddi.api_v3.CategoryBag;
import org.uddi.api_v3.HostingRedirector;
import org.uddi.api_v3.InstanceDetails;
import org.uddi.api_v3.KeyedReference;
import org.uddi.api_v3.OverviewDoc;
import org.uddi.api_v3.TModelInstanceDetails;
import org.uddi.api_v3.TModelInstanceInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMAddress;
import gov.hhs.fha.nhinc.connectmgr.data.CMAddresses;
import gov.hhs.fha.nhinc.connectmgr.data.CMBindingDescriptions;
import gov.hhs.fha.nhinc.connectmgr.data.CMBindingNames;
import gov.hhs.fha.nhinc.connectmgr.data.CMBindingTemplate;
import gov.hhs.fha.nhinc.connectmgr.data.CMBindingTemplates;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessDescriptions;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessEntities;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessEntity;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessNames;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessService;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessServices;
import gov.hhs.fha.nhinc.connectmgr.data.CMContact;
import gov.hhs.fha.nhinc.connectmgr.data.CMContactDescriptions;
import gov.hhs.fha.nhinc.connectmgr.data.CMContacts;
import gov.hhs.fha.nhinc.connectmgr.data.CMDiscoveryURLs;
import gov.hhs.fha.nhinc.connectmgr.data.CMStates;
import gov.hhs.fha.nhinc.connectmgr.uddi.UDDIAccessor;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 *
 * @author vvickers
 */
@RunWith(JMock.class)
public class UDDIAccessorTest {

    Mockery context = new JUnit4Mockery() {

        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    private static String UNIFORM_SERVICE_NAME_KEY = "uddi:nhin:standard-servicenames";

    public UDDIAccessorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of populateUniformServiceName of UDDIAccessor class for a single service name entry
     */
    @Test
    public void testPopulateSingleUniformServiceName() {

        UDDIAccessor accessor = new UDDIAccessor() {

            @Override
            protected CMBusinessEntity findSpecificBusiness(
                    List<CMBusinessEntity> oaEntities, String sBusinessKey) {
                // For the test return the first one provided in the list
                return oaEntities.get(0);
            }

            @Override
            protected List<String> findAndGetValueFromKeyedReference(
                    List<KeyedReference> oaKeys, String sDesiredKey) {
                List<String> retServiceNames = new ArrayList<String>();
                retServiceNames.add("service1");
                return retServiceNames;
            }

            @Override
            protected Log createLogger() {
                return context.mock(Log.class);
            }
        };

        // create a BusinessService and populate it with test data
        BusinessService testUDDIService = generateTestBusinessService();

        // create a CMBusinessEntity and populate it with test data
        CMBusinessEntities testBusinessEntities = new CMBusinessEntities();
        CMBusinessEntity testEntity = generateTestCMBusinessEntity();
        testBusinessEntities.getBusinessEntity().add(testEntity);

        // CMBusinessEntity has been created with one CMBusinessService
        // and populated with test data
        CMBusinessService testBusinessService =
                testBusinessEntities.getBusinessEntity().get(0).
                getBusinessServices().getBusinessService().get(0);

        // Test that the accessor populates the business entity
        // with a single service with the name service1
        accessor.populateUniformServiceNameAndReplicateService(testUDDIService,
                testBusinessEntities, testBusinessService);
        assertNotNull("Returned Business Entities was null",
                testBusinessEntities);
        if (testBusinessEntities != null) {
            assertNotNull("Returned Business Entity list was null",
                    testBusinessEntities.getBusinessEntity());
            if (testBusinessEntities.getBusinessEntity() != null) {
                assertTrue("A single business entity was not returned",
                        testBusinessEntities.getBusinessEntity().size() == 1);
                if (testBusinessEntities.getBusinessEntity().size() == 1) {
                    CMBusinessEntity businessEntity =
                            testBusinessEntities.getBusinessEntity().get(0);
                    if (businessEntity != null) {
                        assertNotNull("Returned Business Services was null",
                                businessEntity.getBusinessServices());
                        if (businessEntity.getBusinessServices() != null) {
                            assertNotNull("Returned Business Service list was null",
                                    businessEntity.getBusinessServices().getBusinessService());
                            if (businessEntity.getBusinessServices().getBusinessService() != null) {
                                assertTrue("A single business service was not returned",
                                        businessEntity.getBusinessServices().getBusinessService().size() == 1);
                                if (businessEntity.getBusinessServices().getBusinessService().size() == 1) {
                                    CMBusinessService businessService =
                                            businessEntity.getBusinessServices().getBusinessService().get(0);
                                    assertEquals("Service name mismatch -", "service1",
                                            businessService.getUniformServiceName());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Test of populateUniformServiceName of UDDIAccessor class for a
     * multiple service name entry
     */
    @Test
    public void testPopulateMultipleUniformServiceName() {

        UDDIAccessor accessor = new UDDIAccessor() {

            @Override
            protected CMBusinessEntity findSpecificBusiness(List<CMBusinessEntity> oaEntities, String sBusinessKey) {
                // For the test return the first one provided in the list
                return oaEntities.get(0);
            }

            @Override
            protected List<String> findAndGetValueFromKeyedReference(List<KeyedReference> oaKeys, String sDesiredKey) {
                List<String> retServiceNames = new ArrayList<String>();
                retServiceNames.add("service1");
                retServiceNames.add("service2");
                retServiceNames.add("service3");
                return retServiceNames;
            }

            @Override
            protected Log createLogger() {
                return context.mock(Log.class);
            }
        };

        // create a BusinessService and populate it with test data
        BusinessService testUDDIService = generateTestBusinessService();

        // create a CMBusinessEntity and populate it with test data
        CMBusinessEntities testBusinessEntities = new CMBusinessEntities();
        CMBusinessEntity testEntity = generateTestCMBusinessEntity();
        testBusinessEntities.getBusinessEntity().add(testEntity);

        // CMBusinessEntity has been created with one CMBusinessService
        // and populated with test data
        CMBusinessService testBusinessService =
                testBusinessEntities.getBusinessEntity().get(0).
                getBusinessServices().getBusinessService().get(0);

        // Test that the accessor populates the business entity with 
        // three services with the names service1, service2, and service3
        // as set above in the override
        accessor.populateUniformServiceNameAndReplicateService(testUDDIService,
                testBusinessEntities, testBusinessService);
        assertNotNull("Returned Business Entities was null", testBusinessEntities);
        if (testBusinessEntities != null) {
            assertNotNull("Returned Business Entity list was null",
                    testBusinessEntities.getBusinessEntity());
            if (testBusinessEntities.getBusinessEntity() != null) {
                assertTrue("A single business entity was not returned",
                        testBusinessEntities.getBusinessEntity().size() == 1);
                if (testBusinessEntities.getBusinessEntity().size() == 1) {
                    CMBusinessEntity businessEntity =
                            testBusinessEntities.getBusinessEntity().get(0);
                    if (businessEntity != null) {
                        assertNotNull("Returned Business Services was null",
                                businessEntity.getBusinessServices());
                        if (businessEntity.getBusinessServices() != null) {
                            assertNotNull("Returned Business Service list was null",
                                    businessEntity.getBusinessServices().getBusinessService());
                            if (businessEntity.getBusinessServices().getBusinessService() != null) {
                                assertTrue("Three business services were not returned",
                                        businessEntity.getBusinessServices().getBusinessService().size() == 3);
                                if (businessEntity.getBusinessServices().getBusinessService().size() == 3) {
                                    for (int i = 0; i < 3; i++) {
                                        CMBusinessService businessService =
                                                businessEntity.getBusinessServices().getBusinessService().get(i);
                                        assertEquals("Service name mismatch -", "service" + (i + 1),
                                                businessService.getUniformServiceName());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Create a generic Business Service filled with test data
     *
     * @return The test Business Service
     */
    private BusinessService generateTestBusinessService() {
        BusinessService oUDDIService = new BusinessService();
        BindingTemplates testBindingTemplates = new BindingTemplates();
        BindingTemplate testBindingTemplate = new BindingTemplate();

        AccessPoint testAccessPoint = new AccessPoint();
        testAccessPoint.setValue("testAccessPointValue");
        testBindingTemplate.setAccessPoint(testAccessPoint);

        testBindingTemplate.setBindingKey("testBindingKey");

        HostingRedirector testHostingRedirector = new HostingRedirector();
        testHostingRedirector.setBindingKey("testBindingKey");
        testBindingTemplate.setHostingRedirector(testHostingRedirector);

        testBindingTemplate.setServiceKey("testServiceKey");

        TModelInstanceDetails testTModelInstanceDetails = new TModelInstanceDetails();
        TModelInstanceInfo testTModelInstanceInfo = new TModelInstanceInfo();
        InstanceDetails testInstanceDetails = new InstanceDetails();
        testInstanceDetails.setInstanceParms("testInstanceParams");
        testTModelInstanceInfo.setInstanceDetails(testInstanceDetails);
        testTModelInstanceDetails.getTModelInstanceInfo().add(testTModelInstanceInfo);
        testBindingTemplate.setTModelInstanceDetails(testTModelInstanceDetails);

        testBindingTemplates.getBindingTemplate().add(testBindingTemplate);
        oUDDIService.setBindingTemplates(testBindingTemplates);

        oUDDIService.setBusinessKey("testBusinessKey");

        CategoryBag testCategoryBag = new CategoryBag();
        KeyedReference testKeyedReference = new KeyedReference();
        testKeyedReference.setKeyName("testKeyName");
        testKeyedReference.setKeyValue("testKeyValue");
        testKeyedReference.setTModelKey("testTModelKey");
        testCategoryBag.getKeyedReference().add(testKeyedReference);
        oUDDIService.setCategoryBag(testCategoryBag);

        oUDDIService.setServiceKey("testServiceKey");

        return oUDDIService;
    }

    /**
     * Create a generic Business Entity filled with test data
     *
     * @return The test Business Entity
     */
    private CMBusinessEntity generateTestCMBusinessEntity() {

        CMBusinessEntity testEntity = new CMBusinessEntity();

        // create BusinessKey
        testEntity.setBusinessKey("testBusinessKey");

        // create a CMBusinessService and put it in the CMBusinessServices list
        CMBusinessServices testBusinessServices = new CMBusinessServices();
        testBusinessServices.getBusinessService().add(generateTestCMBusinessService());

        testEntity.setBusinessServices(testBusinessServices);

        // create CMContacts
        CMContacts testContacts = new CMContacts();
        CMContact testContact = new CMContact();

        CMAddresses testAddresses = new CMAddresses();
        CMAddress testAddress = new CMAddress();
        testAddress.getAddressLine().add("testAddressLine");
        testAddresses.getAddress().add(testAddress);
        testContact.setAddresses(testAddresses);

        CMContactDescriptions testContactDescriptions = new CMContactDescriptions();
        testContactDescriptions.getDescription().add("testContractDescription");
        testContact.setDescriptions(testContactDescriptions);
        testContacts.getContact().add(testContact);
        testEntity.setContacts(testContacts);

        // create CMBusinessDescriptions
        CMBusinessDescriptions testBusinessDescriptions = new CMBusinessDescriptions();
        testBusinessDescriptions.getBusinessDescription().add("testBusinessDescription");
        testEntity.setDescriptions(testBusinessDescriptions);

        // create CMDiscoveryURLs
        CMDiscoveryURLs testDiscoveryURLs = new CMDiscoveryURLs();
        testDiscoveryURLs.getDiscoveryURL().add("testDiscoveryURL");
        testEntity.setDiscoveryURLs(testDiscoveryURLs);

        // create FederalHIE
        testEntity.setFederalHIE(false);

        // create HomeCommunityId
        testEntity.setHomeCommunityId("testHomeCommunity");

        // create CMBusinessNames
        CMBusinessNames testBusinessNames = new CMBusinessNames();
        testBusinessNames.getBusinessName().add("testBusinessName");
        testEntity.setNames(testBusinessNames);

        // create PublicKey
        testEntity.setPublicKey("testPublicKey");

        // create PublicKeyURI
        testEntity.setPublicKeyURI("testPublicKeyURI");

        // create CMStates
        CMStates testStates = new CMStates();
        testStates.getState().add("testState");
        testEntity.setStates(testStates);

        return testEntity;
    }

    /**
     * Create a generic CMBusiness Service filled with test data
     *
     * @return The test CMBusiness Service
     */
    private CMBusinessService generateTestCMBusinessService() {

        CMBusinessService testBusinessService = new CMBusinessService();

        CMBindingTemplates testBindingTemplates = new CMBindingTemplates();
        CMBindingTemplate testBindingTemplate = new CMBindingTemplate();
        testBindingTemplate.setBindingKey("testBindingKey");
        testBindingTemplate.setEndpointURL("testEndpointURL");
        testBindingTemplate.setServiceVersion("testServiceVersion");
        testBindingTemplate.setWsdlURL("testWsdlURL");
        testBindingTemplates.getBindingTemplate().add(testBindingTemplate);
        testBusinessService.setBindingTemplates(testBindingTemplates);

        CMBindingDescriptions testBindingDescriptions = new CMBindingDescriptions();
        testBindingDescriptions.getDescription().add("testBindingDescription");
        testBusinessService.setDescriptions(testBindingDescriptions);

        testBusinessService.setInternalWebService(false);

        CMBindingNames testBindingNames = new CMBindingNames();
        testBindingNames.getName().add("testBindingName");
        testBusinessService.setNames(testBindingNames);

        testBusinessService.setServiceKey("testServiceKey");

        testBusinessService.setUniformServiceName("testUniformServiceName");

        return testBusinessService;
    }
}
