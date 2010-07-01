/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.connectmgr.data;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author westbergl
 */
public class CMUDDIConnectionInfoXMLTest {

    public CMUDDIConnectionInfoXMLTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        System.setProperty("nhinc.properties.dir", System.getenv("NHINC_PROPERTIES_DIR"));
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Create a set of binding names.
     *
     * @param sTag The tag to append to the data.
     * @return The BindingNames object.
     */
    private CMBindingNames createBindingNames(String sTag)
    {
        CMBindingNames oBindingNames = new CMBindingNames();
        oBindingNames.getName().add("BindingName" + sTag + ".1");
        oBindingNames.getName().add("BindingName" + sTag + ".2");

        return oBindingNames;
    }

    /**
     * Create a set of binding descriptions.
     *
     * @param sTag The tag to append to the data.
     * @return The BindingDescriptions
     */
    private CMBindingDescriptions createBindingDescriptions(String sTag)
    {
        CMBindingDescriptions oDescriptions = new CMBindingDescriptions();
        oDescriptions.getDescription().add("BindingDescription" + sTag + ".1");
        oDescriptions.getDescription().add("BindingDescription" + sTag + ".2");

        return oDescriptions;
    }

    /**
     * Create a single binding template with the data appended with the tag.
     *
     * @param sTag The tag to append to the data.
     * @return The new CMBindingTemplate.
     */
    private CMBindingTemplate createBindingTemplate(String sTag)
    {
        CMBindingTemplate oTemplate = new CMBindingTemplate();

        oTemplate.setBindingKey("BindingKey" + sTag);
        oTemplate.setEndpointURL("EndpointURL" + sTag);
        oTemplate.setWsdlURL("WSDLURL" + sTag);
		oTemplate.setServiceVersion("1.0");

        return oTemplate;
    }

    /**
     * This method creates the binding templates.
     *
     * @param sTag The tag to append to the data.
     * @return The new binding templates.
     */
    private CMBindingTemplates createBindingTemplates(String sTag)
    {
        CMBindingTemplates oBindingTemplates = new CMBindingTemplates();

        oBindingTemplates.getBindingTemplate().add(createBindingTemplate(sTag + ".1"));
        oBindingTemplates.getBindingTemplate().add(createBindingTemplate(sTag + ".2"));

        return oBindingTemplates;
    }

    /**
     * Create a single business service with the data containing the given tag.
     *
     * @param sTag The tag to append to the data.
     * @return The new business service.
     */
    private CMBusinessService createBusinessService(String sTag)
    {
        CMBusinessService oService = new CMBusinessService();

        oService.setInternalWebService(true);
        oService.setServiceKey("ServiceKey" + sTag);
        oService.setUniformServiceName("ServiceName" + sTag);
        oService.setBindingTemplates(createBindingTemplates(sTag));
        oService.setNames(createBindingNames(sTag));
        oService.setDescriptions(createBindingDescriptions(sTag));
        oService.setLiftProtocols(createBusinessLiftProtocols(sTag));
        oService.setLiftSupported(true);

        return oService;
    }

    /**
     * This method loads up a business services.
     *
     * @param idx1 The first index to use in the data.
     * @return The new BusinessServices
     */
    private CMBusinessServices createBusinessServices(String sTag)
    {
        CMBusinessServices oServices = new CMBusinessServices();
        oServices.getBusinessService().add(createBusinessService(sTag + ".1"));
        oServices.getBusinessService().add(createBusinessService(sTag + ".2"));

        return oServices;
    }

    /**
     * Creates the descriptions for a contact.
     *
     * @param sTag The data to append to the data.
     * @return The contact descriptions that were created.
     */
    private CMContactDescriptions  createContactDescriptions(String sTag)
    {
        CMContactDescriptions oDescriptions = new CMContactDescriptions();
        oDescriptions.getDescription().add("ContactDescription" + sTag + ".1");
        oDescriptions.getDescription().add("ContactDescription" + sTag + ".2");
        return oDescriptions;
    }

    /**
     * Creates the names for a contact.
     *
     * @param sTag The data to append to the data.
     * @return The contact names that were created.
     */
    private CMPersonNames createContactPersonNames(String sTag)
    {
        CMPersonNames oNames = new CMPersonNames();
        oNames.getPersonName().add("ContactPersonName" + sTag + ".1");
        oNames.getPersonName().add("ContactPersonName" + sTag + ".2");
        return oNames;
    }

    /**
     * Creates the phone numbers for a contact.
     *
     * @param sTag The tag to append to the data.
     * @return The phone numbers.
     */
    private CMPhones createContactPhones(String sTag)
    {
        CMPhones oPhones = new CMPhones();

        oPhones.getPhone().add("ContactPhone" + sTag + ".1");
        oPhones.getPhone().add("ContactPhone" + sTag + ".2");

        return oPhones;
    }

    /**
     * Create the email addresses for a contact.
     *
     * @param sTag the tag to append to the data.
     * @return The email addresses.
     */
    private CMEmails createContactEmails(String sTag)
    {
        CMEmails oEmails = new CMEmails();
        oEmails.getEmail().add("Email" + sTag + ".1");
        oEmails.getEmail().add("Email" + sTag + ".2");

        return oEmails;
    }


    /**
     * Create a single address for a contact.
     * @param sTag the tag to append to the data.
     * @return The address that was created.
     */
    private CMAddress createContactAddress(String sTag)
    {
        CMAddress oAddr = new CMAddress();
        oAddr.getAddressLine().add("AddressLine" + sTag + ".1");
        oAddr.getAddressLine().add("AddressLine" + sTag + ".2");
        return oAddr;
    }

    /**
     * Creates addresses for a contact.
     *
     * @param sTag The tag to append to the data.
     * @return The contacts addresses.
     */
    private CMAddresses createContactAddresses(String sTag)
    {
        CMAddresses oAddrs = new CMAddresses();
        oAddrs.getAddress().add(createContactAddress(sTag + ".1"));
        oAddrs.getAddress().add(createContactAddress(sTag + ".2"));
        return oAddrs;
    }

    /**
     * Create a single contact.
     *
     * @param sTag Append this to the end of the data fields.
     * @return The contact that was created.
     */
    private CMContact createContact(String sTag)
    {
        CMContact oContact = new CMContact();

        oContact.setDescriptions(createContactDescriptions(sTag));
        oContact.setPersonNames(createContactPersonNames(sTag));
        oContact.setPhones(createContactPhones(sTag));
        oContact.setEmails(createContactEmails(sTag));
        oContact.setAddresses(createContactAddresses(sTag));

        return oContact;
    }

    /**
     * Create a set of contacts.
     *
     * @param sTag The tag to append to the data.
     * @return The Contacts that were created.
     */
    private CMContacts createContacts(String sTag)
    {
        CMContacts oContacts = new CMContacts();
        oContacts.getContact().add(createContact(sTag + ".1"));
        oContacts.getContact().add(createContact(sTag + ".2"));

        return oContacts;
    }

    /**
     * Create the business descriptions for this business entity.
     *
     * @param sTag The tag to append to the data.
     * @return The BusinessDescriptions that were added.
     */
    private CMBusinessDescriptions createBusinessDescriptions(String sTag)
    {
        CMBusinessDescriptions oDescriptions = new CMBusinessDescriptions();
        oDescriptions.getBusinessDescription().add("BusinessDescription" + sTag + ".1");
        oDescriptions.getBusinessDescription().add("BusinessDescription" + sTag + ".2");
        return oDescriptions;
    }

    /**
     * Create the discovery URLs for the business entity.
     *
     * @param sTag The tag to append to the data.
     * @return The URLs
     */
    private CMDiscoveryURLs createDiscoveryURLs(String sTag)
    {
        CMDiscoveryURLs oURLs = new CMDiscoveryURLs();
        oURLs.getDiscoveryURL().add("URL" + sTag + ".1");
        oURLs.getDiscoveryURL().add("URL" + sTag + ".2");
        return oURLs;
    }

    /**
     * Creates a set of business names.
     *
     * @param sTag the tag to append to the data.
     * @return The busines names.
     */
    private CMBusinessNames createBusinessNames(String sTag)
    {
        CMBusinessNames oNames = new CMBusinessNames();
        oNames.getBusinessName().add("BusinessName" + sTag + ".1");
        oNames.getBusinessName().add("BusinessName" + sTag + ".2");
        return oNames;
    }

    /**
     * Create the states associated with the business entity.
     *
     * @param sTag The tag to append to the data.
     * @return The states that were created.
     */
    private CMStates createBusinessStates(String sTag)
    {
        CMStates oStates = new CMStates();
        oStates.getState().add("State" + sTag + ".1");
        oStates.getState().add("State" + sTag + ".2");
        return oStates;

    }

    /**
     * Create the lift protocols associated with the business entity.
     *
     * @param sTag The tag to append to the data.
     * @return The lift protocols that were created.
     */
    private CMLiftProtocols createBusinessLiftProtocols(String sTag)
    {
        CMLiftProtocols oProtocols = new CMLiftProtocols();
        oProtocols.getProtocol().add("Protocol" + sTag + ".1");
        oProtocols.getProtocol().add("Protocol" + sTag + ".2");
        return oProtocols;

    }

    /**
     * This method loads up a business entity with some test data.
     *
     * @param index The index to add to the end of the values.
     * @return The CMBusinessEntity that was created.
     */
    private CMBusinessEntity createBusinessEntity(String sTag)
    {
        CMBusinessEntity oEntity = new CMBusinessEntity();

        oEntity.setBusinessKey("BusinessKey" + sTag);
        oEntity.setFederalHIE(true);
        oEntity.setHomeCommunityId("1111.1111.1111.1111." + sTag);
        oEntity.setPublicKeyURI("http://www.thekey.com" + sTag);
        oEntity.setBusinessServices(createBusinessServices(sTag));
        oEntity.setContacts(createContacts(sTag));
        oEntity.setDescriptions(createBusinessDescriptions(sTag));
        oEntity.setDiscoveryURLs(createDiscoveryURLs(sTag));
        oEntity.setNames(createBusinessNames(sTag));
        oEntity.setStates(createBusinessStates(sTag));
        oEntity.setPublicKey("AA:11:22");


        return oEntity;
    }

    /**
     * Test of serialize method, of class CMUDDIConnectionInfoXML.
     */
    @Test
    public void testSerializeAndDeserialize()
    {
        try
        {
            System.out.println("serializeAndDeserialize");
            System.out.println("");


            CMUDDIConnectionInfo oConnInfo = new CMUDDIConnectionInfo();
            CMBusinessEntities oEntities = new CMBusinessEntities();
            oConnInfo.setBusinessEntities(oEntities);
            oEntities.getBusinessEntity().add(createBusinessEntity(".1"));
            oEntities.getBusinessEntity().add(createBusinessEntity(".2"));

            String sXML = CMUDDIConnectionInfoXML.serialize(oConnInfo);
            System.out.print(sXML);
            assertNotNull(sXML);

            CMUDDIConnectionInfo oResultConnInfo = CMUDDIConnectionInfoXML.deserialize(sXML);

            assertTrue(oResultConnInfo.equals(oConnInfo));
        }
        catch (Exception e)
        {
            fail("Unexpected exception occurred.  Error: " + e.getMessage());
        }

    }

}