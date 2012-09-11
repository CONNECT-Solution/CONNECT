/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.response;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * 
 * @author JHOPPESC
 */
public class NhinPatientDiscoveryAsyncRespImplTest {

    public NhinPatientDiscoveryAsyncRespImplTest() {
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

    @Test
    public void testRespondingGatewayPRPAIN201306UV02() {
        System.out.println("testRespondingGatewayPRPAIN201306UV02");
    }
    /**
     * Test of respondingGatewayPRPAIN201306UV02 method, of class NhinPatientDiscoveryAsyncRespImpl.
     */
    /*
     * @Test public void testRespondingGatewayPRPAIN201306UV02() {
     * System.out.println("testRespondingGatewayPRPAIN201306UV02");
     * 
     * NhinPatientDiscoveryAsyncRespImpl instance = new NhinPatientDiscoveryAsyncRespImpl() {
     * 
     * @Override protected boolean checkPolicy(PRPAIN201306UV02 response, AssertionType assertion) { return true; }
     * 
     * @Override protected int getResponseMode () { return ResponseFactory.PASSTHRU_MODE; }
     * 
     * @Override protected boolean isServiceEnabled() { return true; }
     * 
     * @Override protected void processRespVerifyMode(PRPAIN201306UV02 body, AssertionType assertion) { return; }
     * 
     * @Override protected void processRespTrustMode(PRPAIN201306UV02 body, AssertionType assertion) { return; }
     * 
     * @Override protected void storeMapping (PRPAIN201306UV02 msg) { return; }
     * 
     * @Override protected MCCIIN000002UV01 sendToAdapter(PRPAIN201306UV02 body, AssertionType assertion) { return
     * HL7AckTransforms.createAckFrom201306(body, "Success"); } };
     * 
     * JAXBElement<PRPAMT201301UV02Person> queryPerson = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith",
     * "M", null, null); PRPAMT201301UV02Patient queryPatient = HL7PatientTransforms.create201301Patient(queryPerson,
     * "1234", "1.1.1"); PRPAIN201305UV02 query = HL7PRPA201305Transforms.createPRPA201305(queryPatient, "1.1", "2.2",
     * "1.1.1");
     * 
     * JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M",
     * null, null); PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "5678", "2.2.2");
     * PRPAIN201306UV02 resp = HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1", "2.2.2", query);
     * 
     * AssertionType assertion = new AssertionType();
     * 
     * MCCIIN000002UV01 result = instance.respondingGatewayPRPAIN201306UV02(resp, assertion);
     * 
     * assertNotNull(result); TestHelper.assertReceiverEquals("2.2", result); TestHelper.assertSenderEquals("1.1",
     * result); TestHelper.assertAckMsgEquals("Success", result); }
     */

    /**
     * Test of respondingGatewayPRPAIN201306UV02 method, of class NhinPatientDiscoveryAsyncRespImpl. Trust Mode
     */
    /*
     * @Test public void testRespondingGatewayPRPAIN201306UV02Trust() {
     * System.out.println("testRespondingGatewayPRPAIN201306UV02Trust");
     * 
     * NhinPatientDiscoveryAsyncRespImpl instance = new NhinPatientDiscoveryAsyncRespImpl() {
     * 
     * @Override protected boolean checkPolicy(PRPAIN201306UV02 response, AssertionType assertion) { return true; }
     * 
     * @Override protected int getResponseMode () { return ResponseFactory.TRUST_MODE; }
     * 
     * @Override protected boolean isServiceEnabled() { return true; }
     * 
     * @Override protected void processRespVerifyMode(PRPAIN201306UV02 body, AssertionType assertion) { return; }
     * 
     * @Override protected void processRespTrustMode(PRPAIN201306UV02 body, AssertionType assertion) { return; }
     * 
     * @Override protected void storeMapping (PRPAIN201306UV02 msg) { return; }
     * 
     * @Override protected MCCIIN000002UV01 sendToAdapter(PRPAIN201306UV02 body, AssertionType assertion) { return
     * HL7AckTransforms.createAckFrom201306(body, "SuccessTrust"); } };
     * 
     * JAXBElement<PRPAMT201301UV02Person> queryPerson = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith",
     * "M", null, null); PRPAMT201301UV02Patient queryPatient = HL7PatientTransforms.create201301Patient(queryPerson,
     * "1234", "1.1.1"); PRPAIN201305UV02 query = HL7PRPA201305Transforms.createPRPA201305(queryPatient, "1.1", "2.2",
     * "1.1.1");
     * 
     * JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M",
     * null, null); PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "5678", "2.2.2");
     * PRPAIN201306UV02 resp = HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1", "2.2.2", query);
     * 
     * AssertionType assertion = new AssertionType();
     * 
     * MCCIIN000002UV01 result = instance.respondingGatewayPRPAIN201306UV02(resp, assertion);
     * 
     * assertNotNull(result); TestHelper.assertReceiverEquals("2.2", result); TestHelper.assertSenderEquals("1.1",
     * result); TestHelper.assertAckMsgEquals("SuccessTrust", result); }
     */
    /**
     * Test of respondingGatewayPRPAIN201306UV02 method, of class NhinPatientDiscoveryAsyncRespImpl. Trust Mode
     */
    /*
     * @Test public void testRespondingGatewayPRPAIN201306UV02Verify() {
     * System.out.println("testRespondingGatewayPRPAIN201306UV02Verify");
     * 
     * NhinPatientDiscoveryAsyncRespImpl instance = new NhinPatientDiscoveryAsyncRespImpl() {
     * 
     * @Override protected boolean checkPolicy(PRPAIN201306UV02 response, AssertionType assertion) { return true; }
     * 
     * @Override protected int getResponseMode () { return ResponseFactory.VERIFY_MODE; }
     * 
     * @Override protected boolean isServiceEnabled() { return true; }
     * 
     * @Override protected void processRespVerifyMode(PRPAIN201306UV02 body, AssertionType assertion) { return; }
     * 
     * @Override protected void processRespTrustMode(PRPAIN201306UV02 body, AssertionType assertion) { return; }
     * 
     * @Override protected void storeMapping (PRPAIN201306UV02 msg) { return; }
     * 
     * @Override protected MCCIIN000002UV01 sendToAdapter(PRPAIN201306UV02 body, AssertionType assertion) { return
     * HL7AckTransforms.createAckFrom201306(body, "SuccessVerify"); } };
     * 
     * JAXBElement<PRPAMT201301UV02Person> queryPerson = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith",
     * "M", null, null); PRPAMT201301UV02Patient queryPatient = HL7PatientTransforms.create201301Patient(queryPerson,
     * "1234", "1.1.1"); PRPAIN201305UV02 query = HL7PRPA201305Transforms.createPRPA201305(queryPatient, "1.1", "2.2",
     * "1.1.1");
     * 
     * JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M",
     * null, null); PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "5678", "2.2.2");
     * PRPAIN201306UV02 resp = HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1", "2.2.2", query);
     * 
     * AssertionType assertion = new AssertionType();
     * 
     * MCCIIN000002UV01 result = instance.respondingGatewayPRPAIN201306UV02(resp, assertion);
     * 
     * assertNotNull(result); TestHelper.assertReceiverEquals("2.2", result); TestHelper.assertSenderEquals("1.1",
     * result); TestHelper.assertAckMsgEquals("SuccessVerify", result); }
     */

    /**
     * Test of respondingGatewayPRPAIN201306UV02 method, of class NhinPatientDiscoveryAsyncRespImpl. Service Not Enabled
     */
    /*
     * @Test public void testRespondingGatewayPRPAIN201306UV02NotEnabled() {
     * System.out.println("testRespondingGatewayPRPAIN201306UV02NotEnabled");
     * 
     * NhinPatientDiscoveryAsyncRespImpl instance = new NhinPatientDiscoveryAsyncRespImpl() {
     * 
     * @Override protected boolean checkPolicy(PRPAIN201306UV02 response, AssertionType assertion) { return true; }
     * 
     * @Override protected int getResponseMode () { return ResponseFactory.PASSTHRU_MODE; }
     * 
     * @Override protected boolean isServiceEnabled() { return false; }
     * 
     * @Override protected void processRespVerifyMode(PRPAIN201306UV02 body, AssertionType assertion) { return; }
     * 
     * @Override protected void processRespTrustMode(PRPAIN201306UV02 body, AssertionType assertion) { return; }
     * 
     * @Override protected void storeMapping (PRPAIN201306UV02 msg) { return; }
     * 
     * @Override protected MCCIIN000002UV01 sendToAdapter(PRPAIN201306UV02 body, AssertionType assertion) { return
     * HL7AckTransforms.createAckFrom201306(body, "Patient Discovery Async Response Service Not Enabled"); } };
     * 
     * JAXBElement<PRPAMT201301UV02Person> queryPerson = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith",
     * "M", null, null); PRPAMT201301UV02Patient queryPatient = HL7PatientTransforms.create201301Patient(queryPerson,
     * "1234", "1.1.1"); PRPAIN201305UV02 query = HL7PRPA201305Transforms.createPRPA201305(queryPatient, "1.1", "2.2",
     * "1.1.1");
     * 
     * JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M",
     * null, null); PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "5678", "2.2.2");
     * PRPAIN201306UV02 resp = HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1", "2.2.2", query);
     * 
     * AssertionType assertion = new AssertionType();
     * 
     * MCCIIN000002UV01 result = instance.respondingGatewayPRPAIN201306UV02(resp, assertion);
     * 
     * assertNotNull(result); TestHelper.assertReceiverEquals("2.2", result); TestHelper.assertSenderEquals("1.1",
     * result); TestHelper.assertAckMsgEquals("Patient Discovery Async Response Service Not Enabled", result); }
     */

    /**
     * Test of respondingGatewayPRPAIN201306UV02 method, of class NhinPatientDiscoveryAsyncRespImpl. Policy Check Failed
     */
    /*
     * @Test public void testRespondingGatewayPRPAIN201306UV02PolicyFailed() {
     * System.out.println("testRespondingGatewayPRPAIN201306UV02PolicyFailed");
     * 
     * NhinPatientDiscoveryAsyncRespImpl instance = new NhinPatientDiscoveryAsyncRespImpl() {
     * 
     * @Override protected boolean checkPolicy(PRPAIN201306UV02 response, AssertionType assertion) { return false; }
     * 
     * @Override protected int getResponseMode () { return ResponseFactory.PASSTHRU_MODE; }
     * 
     * @Override protected boolean isServiceEnabled() { return true; }
     * 
     * @Override protected void processRespVerifyMode(PRPAIN201306UV02 body, AssertionType assertion) { return; }
     * 
     * @Override protected void processRespTrustMode(PRPAIN201306UV02 body, AssertionType assertion) { return; }
     * 
     * @Override protected void storeMapping (PRPAIN201306UV02 msg) { return; }
     * 
     * @Override protected MCCIIN000002UV01 sendToAdapter(PRPAIN201306UV02 body, AssertionType assertion) { return
     * HL7AckTransforms.createAckFrom201306(body, "Policy Check Failed"); } };
     * 
     * JAXBElement<PRPAMT201301UV02Person> queryPerson = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith",
     * "M", null, null); PRPAMT201301UV02Patient queryPatient = HL7PatientTransforms.create201301Patient(queryPerson,
     * "1234", "1.1.1"); PRPAIN201305UV02 query = HL7PRPA201305Transforms.createPRPA201305(queryPatient, "1.1", "2.2",
     * "1.1.1");
     * 
     * JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M",
     * null, null); PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "5678", "2.2.2");
     * PRPAIN201306UV02 resp = HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1", "2.2.2", query);
     * 
     * AssertionType assertion = new AssertionType();
     * 
     * MCCIIN000002UV01 result = instance.respondingGatewayPRPAIN201306UV02(resp, assertion);
     * 
     * assertNotNull(result); TestHelper.assertReceiverEquals("2.2", result); TestHelper.assertSenderEquals("1.1",
     * result); TestHelper.assertAckMsgEquals("Policy Check Failed", result); }
     */

}