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
import org.junit.Ignore;

/**
 *
 * @author svalluripalli
 */
@Ignore //Todo : Move this test to integration test suite.
public class PatientCorrelationServiceImplTest {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(PatientCorrelationServiceImplTest.class);

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
        RemoveExistingCorrelations();
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

    private void RemoveExistingCorrelations() {
        log.info("RemoveExistingCorrelations is not yet implemented, therefore test is not fully conclusive");
    //todo - determine method for removing existing correlations
    }

    @Test
    public void testSimpleAddRetrieveCorrelation() {
        log.debug("begin testSimpleAddRetrieveCorrelation");
        II corr1 = IIFactory("AA1", "MRN-1");
        II corr2 = IIFactory("AA2", "MRN-2");

        //clear db
        //create message
        PRPAIN201301UV02 addRequest = createAddRequestMessage(corr1, corr2);

        //send
        AddPatientCorrelationSecuredRequestType addPatientCorrelationRequest = new AddPatientCorrelationSecuredRequestType();
        addPatientCorrelationRequest.setPRPAIN201301UV02(addRequest);
        PatientCorrelationServiceImpl.addPatientCorrelation(addPatientCorrelationRequest);

        //build retrieve
        PRPAIN201309UV02 retrieveRequest = createRetrieveMessage(corr1);
        RetrievePatientCorrelationsSecuredRequestType retrievePatientCorrelationsRequest = new RetrievePatientCorrelationsSecuredRequestType();
        retrievePatientCorrelationsRequest.setPRPAIN201309UV02(retrieveRequest);
        RetrievePatientCorrelationsSecuredResponseType retrievePatientCorrelationsResponse = PatientCorrelationServiceImpl.retrievePatientCorrelations(retrievePatientCorrelationsRequest);
        PRPAIN201310UV02 retrieveResult = retrievePatientCorrelationsResponse.getPRPAIN201310UV02();
        List<II> resultsIds = extractIdsFromRetrieveResult(retrieveResult);
        assertTrue(isIdInList(resultsIds, corr2));

        retrieveRequest = createRetrieveMessage(corr2);
        retrievePatientCorrelationsRequest = new RetrievePatientCorrelationsSecuredRequestType();
        retrievePatientCorrelationsRequest.setPRPAIN201309UV02(retrieveRequest);
        retrievePatientCorrelationsResponse = PatientCorrelationServiceImpl.retrievePatientCorrelations(retrievePatientCorrelationsRequest);
        retrieveResult = retrievePatientCorrelationsResponse.getPRPAIN201310UV02();
        resultsIds = extractIdsFromRetrieveResult(retrieveResult);
        assertTrue(isIdInList(resultsIds, corr1));
    }
    
    @Test
    public void testRetrieveDoesNotReturnInputId() {
        log.debug("begin testRetrieveDoesNotReturnInputId");

        II corr1 = IIFactory("MRN-1", "AA1");
        II corr2 = IIFactory("MRN-2", "AA2");

        //clear db
        //create message
        PRPAIN201301UV02 addRequest = createAddRequestMessage(corr1, corr2);
        AddPatientCorrelationSecuredRequestType addReq = new AddPatientCorrelationSecuredRequestType();
        addReq.setPRPAIN201301UV02(addRequest);
        //send
        PatientCorrelationServiceImpl.addPatientCorrelation(addReq);

        //build retrieve
        PRPAIN201309UV02 retrieveRequest = createRetrieveMessage(corr1);
        RetrievePatientCorrelationsSecuredRequestType retrRequest = new RetrievePatientCorrelationsSecuredRequestType();
        retrRequest.setPRPAIN201309UV02(retrieveRequest);
        PRPAIN201310UV02 retrieveResult = PatientCorrelationServiceImpl.retrievePatientCorrelations(retrRequest).getPRPAIN201310UV02();
        List<II> resultsIds = extractIdsFromRetrieveResult(retrieveResult);
        for(II aII : resultsIds)
        {
            System.out.println(aII.getExtension()+" <----------------> "+aII.getRoot());
        }
        assertFalse(isIdInList(resultsIds, corr1));
        assertEquals(1, resultsIds.size());
    }
}