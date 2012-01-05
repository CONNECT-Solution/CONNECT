/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.nhin;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.AdapterDelegate;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.Delegate;
import gov.hhs.fha.nhinc.orchestration.NhinOrchestratable;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mweaver
 */
public abstract class NhinDocRetrieveOrchestratable implements NhinOrchestratable {

    private AssertionType _assertion = null;
    private final String serviceName = "NhinDocumentRetrieve";
    private PolicyTransformer _policyTransformer = null;
    private AuditTransformer _auditTransformer = null;
    private AdapterDelegate _adapterDelegate = null;


    public void setAssertion(AssertionType _assertion) {
        this._assertion = _assertion;
    }
    
    public NhinDocRetrieveOrchestratable()
    {
        
    }
    
    

    @Override
	public Delegate getDelegate() {
		return getAdapterDelegate();
	}

	public NhinDocRetrieveOrchestratable(PolicyTransformer pt, AuditTransformer at, AdapterDelegate ad)
    {
        _policyTransformer = pt;
        _auditTransformer = at;
        _adapterDelegate = ad;
    }

    public AdapterDelegate getAdapterDelegate() {
        return _adapterDelegate;
    }

    public boolean isEnabled() {
        boolean result = false;
        try {
            result = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHINC_DOCUMENT_RETRIEVE_SERVICE_KEY);
        } catch (PropertyAccessException ex) {
            Logger.getLogger(NhinDocRetrieveOrchestratable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public boolean isPassthru() {
        boolean result = false;
        try {
            result = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHINC_DOCUMENT_RETRIEVE_SERVICE_PASSTHRU_PROPERTY);
        } catch (PropertyAccessException ex) {
            Logger.getLogger(NhinDocRetrieveOrchestratable.class.getName()).log(Level.SEVERE, null, ex);
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

}
