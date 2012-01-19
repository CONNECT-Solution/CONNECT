package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.orchestration.NhinDelegate;
import gov.hhs.fha.nhinc.orchestration.NhinResponseProcessor;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;



/**
 * EntityDocQueryOrchestratable_a0 returns the response for the a0 specification
 * Note that for DocQuery, the individual response is a AdhocQueryResponse
 * and the cumulative response is also a AdhocQueryResponse
 * @author paul.eftis
 */
public class EntityDocQueryOrchestratable_a0
        extends EntityDocQueryOrchestratable{

    private AdhocQueryResponse response = null;
    private AdhocQueryResponse cumulativeResponse = null;


    public EntityDocQueryOrchestratable_a0(){
        super();
    }
    

    public EntityDocQueryOrchestratable_a0(NhinDelegate d, NhinResponseProcessor p,
            AuditTransformer at, PolicyTransformer pt, AssertionType a, String name,
            NhinTargetSystemType t, AdhocQueryRequest req){

        super(d, p, at, pt, a, name, t, req);
    }


    // EntityDocQueryOrchestratable objects are run by the nhin delegate
    // so we should override this and return null so that you can't get a circular reference
    // by accident
    @Override
    public NhinDelegate getDelegate(){
        return null;
    }

    public AdhocQueryResponse getResponse(){
        return response;
    }

    public void setResponse(AdhocQueryResponse r){
        response = r;
    }

    public AdhocQueryResponse getCumulativeResponse(){
        return cumulativeResponse;
    }

    public void setCumulativeResponse(AdhocQueryResponse r){
        cumulativeResponse = r;
    }

}
