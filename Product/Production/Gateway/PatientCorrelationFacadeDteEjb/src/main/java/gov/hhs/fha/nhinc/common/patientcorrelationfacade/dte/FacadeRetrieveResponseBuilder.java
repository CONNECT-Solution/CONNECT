/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.common.patientcorrelationfacade.dte;

import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsResponseType;
import java.util.List;
import org.hl7.v3.*;

/**
 *
 * @author rayj
 */
public class FacadeRetrieveResponseBuilder {

    public static CreateFacadeRetrieveResultResponseType createFacadeRetrieveResult(CreateFacadeRetrieveResultRequestType createFacadeRetrieveResultRequest) {
        PRPAIN201310UV02 pixRetrieveResponse = createFacadeRetrieveResultRequest.getPRPAIN201310UV02();
        RetrievePatientCorrelationsResponseType facadeRetrieveResult = createFacadeRetrieveResult(pixRetrieveResponse);
        CreateFacadeRetrieveResultResponseType response = new CreateFacadeRetrieveResultResponseType();
        response.setRetrievePatientCorrelationsResponse(facadeRetrieveResult);
        return response;
    }

    public static RetrievePatientCorrelationsResponseType createFacadeRetrieveResult(PRPAIN201310UV02 pixRetrieveResponse) {
        RetrievePatientCorrelationsResponseType response = new RetrievePatientCorrelationsResponseType();

        List<II> idList;

        idList = pixRetrieveResponse.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId();

        gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifiersType qualifedIds;

        for (II patId : idList) {
            QualifiedSubjectIdentifierType id;
            id = new QualifiedSubjectIdentifierType();
            id.setAssigningAuthorityIdentifier(patId.getRoot());
            id.setSubjectIdentifier(patId.getExtension());

            response.getQualifiedPatientIdentifier().add(id);
        }
        
        return response;
    }
}
