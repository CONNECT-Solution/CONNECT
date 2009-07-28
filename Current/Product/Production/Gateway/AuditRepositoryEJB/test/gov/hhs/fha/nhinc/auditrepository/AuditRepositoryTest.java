/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.auditrepository;

import javax.xml.bind.JAXBElement;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType;
import gov.hhs.fha.nhinc.common.nhinccommon.ResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import org.hl7.v3.PIXConsumerMCCIIN000002UV01RequestType;
import org.hl7.v3.PIXConsumerPRPAIN201301UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201302UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201303UVRequestType;

import org.hl7.v3.PRPAIN201301UV;
import org.hl7.v3.PRPAIN201302UV;
import org.hl7.v3.PRPAIN201303UV;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.MCCIMT000200UV01Receiver;
import org.hl7.v3.MCCIMT000200UV01Device; 
import org.hl7.v3.II;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType;
import com.services.nhinc.schema.auditmessage.FindAuditEventsType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
/**
 *
 * @author mflynn02
 */
public class AuditRepositoryTest {
    private static Log log = LogFactory.getLog(AuditRepositoryTest.class);

    public AuditRepositoryTest() {
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
     * Test of queryAuditEvents method, of class AuditRepository.
     */
    @Test
    public void testQueryAuditEvents() {
        log.debug("Begin - testQueryAuditEvents");
        
        FindCommunitiesAndAuditEventsRequestType queryAuditEventsRequest = new FindCommunitiesAndAuditEventsRequestType();
        FindAuditEventsType event = new FindAuditEventsType();
        event.setPatientId("3333333^^^&2.16.840.1.113883.13.25&ISO");
        queryAuditEventsRequest.setFindAuditEvents(event);
        AuditRepository instance = new AuditRepository();
        FindCommunitiesAndAuditEventsResponseType expResult = new FindCommunitiesAndAuditEventsResponseType();
        FindCommunitiesAndAuditEventsResponseType result = instance.queryAuditEvents(queryAuditEventsRequest);
        
        log.debug("result. : " + result.getFindAuditEventResponse().getFindAuditEventsReturn().size());
        
//        assertEquals(expResult, result);
        
        log.debug("Begin - testQueryAuditEvents");
    }

    public II createII(String root, String extension) {
        II newII = new II();
        
        newII.setRoot(root);
        newII.setExtension(extension);
        
        return newII;
    }

    public PersonNameType createPersonName (String givenName, String familyName) {
        PersonNameType newPerson = new PersonNameType();
        
        newPerson.setFamilyName(familyName);
        newPerson.setGivenName(givenName);
        
        return newPerson;
    }
    public HomeCommunityType createHomeCommunity (String homeCommunityId, String homeCommunityName) {
        HomeCommunityType newHC = new HomeCommunityType();

        newHC.setHomeCommunityId(homeCommunityId);
        newHC.setName(homeCommunityName);
        
        return newHC;
    }
    
    public UserType createUserType (String given, String family, String homeCommunityId, String homeCommunityName) {
        UserType newUser = new UserType();
        
        newUser.setPersonName(createPersonName(given, family));
        newUser.setUserName(given + " " + family);
        newUser.setOrg(createHomeCommunity(homeCommunityId, homeCommunityName));
        
        return newUser;
    }

}