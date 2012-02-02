package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.OutboundResponseProcessor;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;



/**
 * OutboundDocQueryOrchestratable_a1 returns the response for the a1 specification
 * Note that for DocQuery, the individual response is a AdhocQueryResponse
 * and the cumulative response is also a AdhocQueryResponse
 * Currently, a0 and a1 specs are same for Doc Query
 * @author paul.eftis
 */
public class OutboundDocQueryOrchestratable_a1
        extends OutboundDocQueryOrchestratable{

    private AdhocQueryResponse cumulativeResponse = null;


    public OutboundDocQueryOrchestratable_a1(){
        super();
    }
    

    public OutboundDocQueryOrchestratable_a1(OutboundDelegate d, OutboundResponseProcessor p,
            AuditTransformer at, PolicyTransformer pt, AssertionType a, String name,
            NhinTargetSystemType t, AdhocQueryRequest req){

        super(d, p, at, pt, a, name, t, req);
    }

    // OutboundDocQueryOrchestratable objects are run by the nhin delegate
    // so we should override this and return null so that you can't get a circular reference
    // by accident
    @Override
    public OutboundDelegate getDelegate(){
        return null;
    }

    public AdhocQueryResponse getCumulativeResponse(){
        return cumulativeResponse;
    }

    public void setCumulativeResponse(AdhocQueryResponse r){
        cumulativeResponse = r;
    }

}
