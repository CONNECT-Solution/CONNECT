/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.EntityOrchestratable;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.NhinDelegate;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mweaver
 */
public class EntityDocRetrieveOrchestratable implements EntityOrchestratable {
    protected RetrieveDocumentSetRequestType request = null;
    protected NhinTargetSystemType target = null;
    private AssertionType _assertion = null;
    private final String serviceName = "NhinDocumentRetrieve";
    private PolicyTransformer _policyTransformer = null;
    private AuditTransformer _auditTransformer = null;
    private NhinDelegate _nhinDelegate = null;
    private NhinAggregator _nhinAggregator = null;
    

    public NhinTargetSystemType getTarget() {
        return target;
    }

    public void setTarget(NhinTargetSystemType target) {
        this.target = target;
    }
    
    public void setAssertion(AssertionType _assertion) {
        this._assertion = _assertion;
    }

    public EntityDocRetrieveOrchestratable()
    {

    }

    public EntityDocRetrieveOrchestratable(PolicyTransformer pt, AuditTransformer at, NhinDelegate ad, NhinAggregator na)
    {
        _policyTransformer = pt;
        _auditTransformer = at;
        _nhinDelegate = ad;
        _nhinAggregator = na;
    }

    public RetrieveDocumentSetRequestType getRequest() {
        return request;
    }

    public void setRequest(RetrieveDocumentSetRequestType request) {
        this.request = request;
    }
    
    public NhinDelegate getNhinDelegate() {
        return _nhinDelegate;
    }

    public boolean isEnabled() {
        boolean result = false;
        try {
            result = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHINC_DOCUMENT_RETRIEVE_SERVICE_KEY);
        } catch (PropertyAccessException ex) {
            Logger.getLogger(EntityDocRetrieveOrchestratable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public boolean isPassthru() {
        boolean result = false;
        try {
            result = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHINC_DOCUMENT_RETRIEVE_SERVICE_PASSTHRU_PROPERTY);
        } catch (PropertyAccessException ex) {
            Logger.getLogger(EntityDocRetrieveOrchestratable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public AuditTransformer getAuditTransformer() {
        return _auditTransformer;
    }

    public PolicyTransformer getPolicyTransformer() {
        return _policyTransformer;
    }

    public AssertionType getAssertion() {
        return _assertion;
    }

    public String getServiceName() {
        return serviceName;
    }

    public NhinAggregator getAggregator() {
        return _nhinAggregator;
    }

}
