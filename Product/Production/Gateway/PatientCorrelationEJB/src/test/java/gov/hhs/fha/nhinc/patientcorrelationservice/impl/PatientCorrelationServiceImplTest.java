/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientcorrelationservice.impl;

import java.util.List;
import javax.xml.bind.JAXBElement;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.hl7.v3.*;
import gov.hhs.fha.nhinc.patientcorrelationservice.config.*;
import java.util.Date;
import java.util.Calendar;
import org.junit.Ignore;
/**
 *
 * @author svalluripalli
 */

public class PatientCorrelationServiceImplTest {


    public PatientCorrelationServiceImplTest() {
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

    private PRPAIN201309UV02 createRetrieveMessage(II correlation) {
        PRPAIN201309UV02 requestMessage = new PRPAIN201309UV02();
        II interactionId = new II();
        interactionId.setExtension("PRPA_IN201309");
        requestMessage.setInteractionId(interactionId);
        PRPAIN201309UV02QUQIMT021001UV01ControlActProcess cntlAccess = new PRPAIN201309UV02QUQIMT021001UV01ControlActProcess();
        requestMessage.setControlActProcess(cntlAccess);
        cntlAccess.setMoodCode(XActMoodIntentEvent.fromValue("RQO"));
        CD code = new CD();
        code.setCode("PRPA_TE201309UV");
        code.setCodeSystem("2.16.840.1.113883.1.6");
        cntlAccess.setCode(code);
        QUQIMT021001UV01AuthorOrPerformer authPerformer = new QUQIMT021001UV01AuthorOrPerformer();
        authPerformer.setTypeCode(XParticipationAuthorPerformer.AUT);
        cntlAccess.getAuthorOrPerformer().add(authPerformer);
        PRPAMT201307UV02QueryByParameter qParam = new PRPAMT201307UV02QueryByParameter();
        CS csValue = new CS();
        csValue.setCode("new");
        qParam.setStatusCode(csValue);
        CS resValue = new CS();
        resValue.setCode("I");
        qParam.setResponsePriorityCode(csValue);
        PRPAMT201307UV02ParameterList paramsList = new PRPAMT201307UV02ParameterList();
        PRPAMT201307UV02PatientIdentifier patId = new PRPAMT201307UV02PatientIdentifier();
        patId.getValue().add(correlation);
        paramsList.getPatientIdentifier().add(patId);
        qParam.setParameterList(paramsList);
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "PRPAMT201307UVQueryByParameter");
        JAXBElement<PRPAMT201307UV02QueryByParameter> qP = new JAXBElement<PRPAMT201307UV02QueryByParameter>(xmlqname, PRPAMT201307UV02QueryByParameter.class, qParam);
        cntlAccess.setQueryByParameter(qP);
        return requestMessage;
    }

    private PRPAIN201301UV02 createAddRequestMessage(II corr1, II corr2) {
        PRPAIN201301UV02 addRequest = new PRPAIN201301UV02();
        II interactionId = new II();
        interactionId.setExtension("PRPA_IN201301UV");
        addRequest.setInteractionId(interactionId);
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess cntrlAccess = new PRPAIN201301UV02MFMIMT700701UV01ControlActProcess();
        addRequest.setControlActProcess(cntrlAccess);
        cntrlAccess.setMoodCode(XActMoodIntentEvent.EVN);

        PRPAIN201301UV02MFMIMT700701UV01Subject1 subj = new PRPAIN201301UV02MFMIMT700701UV01Subject1();
        cntrlAccess.getSubject().add(subj);
        subj.getTypeCode().add("SUBJ");
        PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent regEvent = new PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent();
        subj.setRegistrationEvent(regEvent);
        CS csValue = new CS();
        csValue.setCode("active");
        regEvent.setStatusCode(csValue);
        PRPAIN201301UV02MFMIMT700701UV01Subject2 subj1 = new PRPAIN201301UV02MFMIMT700701UV01Subject2();
        regEvent.setSubject1(subj1);
        PRPAMT201301UV02Patient pat = new PRPAMT201301UV02Patient();
        subj1.setPatient(pat);
        pat.getClassCode().add("PAT");
        pat.getId().add(corr1);
        pat.getId().add(corr2);

        return addRequest;
    }

    private List<II> extractIdsFromRetrieveResult(PRPAIN201310UV02 retrieveResult) {
        return retrieveResult.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId();
    }

    private boolean isIdInList(List<II> list, II id) {
        boolean found = false;

        if (id != null) {
            for (II idInList : list) {
                if (areEqual(id, idInList)) {
                    found = true;
                    break;
                }
            }
        }

        return found;
    }

    private boolean areEqual(II a, II b) {
        return ((a.getExtension().equals(b.getExtension())) && (a.getRoot().equals(b.getRoot())));
    }

    private II IIFactory(String root, String extension) {
        II id = new II();
        id.setRoot(root);
        id.setExtension(extension);
        return id;
    }

    @Test
    public void testcalculateCorrelationExpirationDate_Null()
    {
        System.out.println("testcalculateCorrelationExpirationDate_Null()");
        Expiration exp = null;
        Date result = PatientCorrelationServiceImpl.calculateCorrelationExpirationDate(exp);

        assertNull(result);

    }
    @Test
    public void testcalculateCorrelationExpirationDate_Year()
    {
        System.out.println("testcalculateCorrelationExpirationDate()");
        Expiration exp = new Expiration("", "YEAR", 1);
        Date result = PatientCorrelationServiceImpl.calculateCorrelationExpirationDate(exp);
        //Date now = Calendar.getInstance().getTime();
        Calendar expDate = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        assertNotNull(result);
        expDate.setTime(result);

        assertTrue(expDate.after(now));

        now.add(Calendar.YEAR, 1);

        assertEquals(now.getTime().getYear(), expDate.getTime().getYear());




    }
    @Test
    public void testcalculateCorrelationExpirationDate_Month()
    {
        System.out.println("testcalculateCorrelationExpirationDate_Month()");
        Expiration exp = new Expiration("", "MONTH", 2);
        Date result = PatientCorrelationServiceImpl.calculateCorrelationExpirationDate(exp);

        Calendar expDate = Calendar.getInstance();
        Calendar now = Calendar.getInstance();


        assertNotNull(result);
        expDate.setTime(result);
        //result should be two months from today
        System.out.println(expDate.getTime());
        assertTrue(expDate.after(now));
        now.add(Calendar.MONTH, 2);
        assertEquals(now.getTime().getMonth(), expDate.getTime().getMonth());
    }
    @Test
    public void testcalculateCorrelationExpirationDate_Day()
    {
        System.out.println("testcalculateCorrelationExpirationDate_Day()");
        Expiration exp = new Expiration("", "DAY", 30);
        Date result = PatientCorrelationServiceImpl.calculateCorrelationExpirationDate(exp);

        Calendar expDate = Calendar.getInstance();
        Calendar now = Calendar.getInstance();


        assertNotNull(result);
        expDate.setTime(result);
        //result should be two months from today
        System.out.println(expDate.getTime());
        assertTrue(expDate.after(now));
        now.add(Calendar.DAY_OF_YEAR, 30);
        assertEquals(now.getTime().getDay(), expDate.getTime().getDay());
    }

    @Ignore //Fix the date issue it is failing regularly on Build Server
    @Test
    public void testcalculateCorrelationExpirationDate_Negative()
    {
        System.out.println("testcalculateCorrelationExpirationDate_Negative()");
        Expiration exp = new Expiration("", "DAY", -30);
        Date result = PatientCorrelationServiceImpl.calculateCorrelationExpirationDate(exp);

        Calendar expDate = Calendar.getInstance();
        Calendar now = Calendar.getInstance();


        assertNotNull(result);
        expDate.setTime(result);
        //result should be two months from today
        System.out.println(expDate.getTime());
        assertFalse(expDate.after(now));
        now.add(Calendar.DAY_OF_YEAR, -30);
        assertEquals(now.getTime(), expDate.getTime());
    }

}