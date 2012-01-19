package gov.hhs.fha.nhinc.patientdiscovery.entity;

import gov.hhs.fha.nhinc.orchestration.NhinDelegate;
import gov.hhs.fha.nhinc.orchestration.NhinResponseProcessor;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;



/**
 * EntityPatientDiscoveryOrchestratable_a0 returns the response for the a0 specification
 * Note that for PatientDiscovery, the individual response is a PRPAIN201306UV02
 * and the cumulative response is the wrapper class RespondingGatewayPRPAIN201306UV02ResponseType
 * @author paul.eftis
 */
public class EntityPatientDiscoveryOrchestratable_a0
        extends EntityPatientDiscoveryOrchestratable{

    private PRPAIN201306UV02 response = null;
    private RespondingGatewayPRPAIN201306UV02ResponseType cumulativeResponse = null;

    public EntityPatientDiscoveryOrchestratable_a0(){
        super();
    }
    

    public EntityPatientDiscoveryOrchestratable_a0(NhinDelegate d, NhinResponseProcessor p,
            AuditTransformer at, PolicyTransformer pt, AssertionType a, String name,
            NhinTargetSystemType t, PRPAIN201305UV02 req){

        super(d, p, at, pt, a, name, t, req);
    }

    // EntityDocQueryOrchestratable objects are run by the nhin delegate
    // so we should override this and return null so that you can't get a circular reference
    // by accident
    @Override
    public NhinDelegate getDelegate(){
        return null;
    }

    public PRPAIN201306UV02 getResponse(){
        return response;
    }

    public void setResponse(PRPAIN201306UV02 r){
        response = r;
    }

    public RespondingGatewayPRPAIN201306UV02ResponseType getCumulativeResponse(){
        return cumulativeResponse;
    }

    public void setCumulativeResponse(RespondingGatewayPRPAIN201306UV02ResponseType r){
        cumulativeResponse = r;
    }
}
