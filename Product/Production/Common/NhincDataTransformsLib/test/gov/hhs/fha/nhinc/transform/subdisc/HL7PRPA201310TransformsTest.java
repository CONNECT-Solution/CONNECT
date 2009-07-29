/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.subdisc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAMT201307UVQueryByParameter;
import org.hl7.v3.II;
import org.hl7.v3.CS;
import org.hl7.v3.PRPAIN201310UV;
import org.hl7.v3.ST;
import org.hl7.v3.PRPAMT201307UVParameterList;
import org.hl7.v3.PRPAMT201307UVPatientIdentifier;

/**
 *
 * @author vvickers
 */
public class HL7PRPA201310TransformsTest {

    private static Log log = LogFactory.getLog(HL7PRPA201301TransformsTest.class);
    
    public HL7PRPA201310TransformsTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    
    /**
     * Test of createPRPA201301 method, of class HL7PRPA201301Transforms.
     */
    @Test
    public void testCreatePRPA201310() {
        log.info("testCreatePRPA201310");
        
        String patientId = "123";
        String assigningAuthorityId = "Auth.111.222.333";
        String localDeviceId = "9.8.7";
        String senderOID = "Sender1";
        String receiverOID = "Receiver1";
        
        String qid = "2.222";
        II queryId = HL7DataTransformHelper.IIFactory(qid);
        
        String qsc = "QueryCode";
        CS statusCode = HL7DataTransformHelper.CSFactory(qsc);
          
        ST semantics = new ST();
        semantics.setLanguage("TestSemantics");
        
        String pidValue = "Pat_Id";
        
        PRPAMT201307UVPatientIdentifier pid = new PRPAMT201307UVPatientIdentifier();
        pid.setSemanticsText(semantics);
        pid.getValue().add(HL7DataTransformHelper.IIFactory(pidValue));
        
        PRPAMT201307UVParameterList pList = new PRPAMT201307UVParameterList();
        pList.getPatientIdentifier().add(pid);
        
        PRPAMT201307UVQueryByParameter queryParam = new PRPAMT201307UVQueryByParameter();
        queryParam.setQueryId(queryId);
        queryParam.setStatusCode(statusCode);
        queryParam.setParameterList(pList);
        
        PRPAIN201310UV result = HL7PRPA201310Transforms.createPRPA201310(patientId, assigningAuthorityId, localDeviceId, senderOID, receiverOID, queryParam);
        
        TestHelper.assertReceiverIdEquals(receiverOID, result);
        TestHelper.assertSenderIdEquals(senderOID, result);
        TestHelper.assertPatientIdEquals(patientId, assigningAuthorityId, localDeviceId, result);
        TestHelper.assertQueryParam(queryParam, result);
    }
    
    /**
     * Test of createPRPA201301 method, of class HL7PRPA201301Transforms.
     */
    @Test
    public void testCreateNullPRPA201310() {
        log.info("testCreateNullPRPA201310");
        
        PRPAIN201310UV result = HL7PRPA201310Transforms.createPRPA201310(null, null, null, null, null, null);
    
        TestHelper.assertReceiverIdEquals("", result);
        TestHelper.assertSenderIdEquals("", result);
        TestHelper.assertPatientIdEquals("", "", HL7Constants.DEFAULT_LOCAL_DEVICE_ID, result);
        
        PRPAMT201307UVQueryByParameter queryParam = new PRPAMT201307UVQueryByParameter();
        TestHelper.assertQueryParam(queryParam, result);
    }
    
    /**
     * Test of createPRPA201301 method, of class HL7PRPA201301Transforms.
     */
    @Test
    public void testCreateFaultPRPA201310() {
        log.info("testCreateFaultPRPA201310");
        
        String senderOID = "Sender1";
        String receiverOID = "Receiver1";
        PRPAIN201310UV result = HL7PRPA201310Transforms.createFaultPRPA201310(senderOID, receiverOID);
    
        TestHelper.assertReceiverIdEquals(receiverOID, result);
        TestHelper.assertSenderIdEquals(senderOID, result);
        TestHelper.assertPatientIdEquals("", "", HL7Constants.DEFAULT_LOCAL_DEVICE_ID, result);
        
        PRPAMT201307UVQueryByParameter queryParam = new PRPAMT201307UVQueryByParameter();
        TestHelper.assertQueryParam(queryParam, result);
    }
}
