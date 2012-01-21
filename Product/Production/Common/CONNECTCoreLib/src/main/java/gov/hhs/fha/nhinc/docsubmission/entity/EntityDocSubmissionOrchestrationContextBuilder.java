/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docsubmission.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.orchestration.NhinDelegate;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContext;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import org.apache.commons.logging.Log;

/**
 *
 * @author zmelnick
 */
public abstract class EntityDocSubmissionOrchestrationContextBuilder implements OrchestrationContextBuilder {

    private static Log log;
    private AssertionType assertionType;
    private NhinDelegate nhinDelegate;
    private ProvideAndRegisterDocumentSetRequestType request;
    private NhinTargetSystemType target;

    public abstract OrchestrationContext build();

    public AssertionType getAssertionType() {
        return assertionType;
    }

    public void setAssertionType(AssertionType assertionType) {
        this.assertionType = assertionType;
    }

    public static Log getLog() {
        return log;
    }

    public NhinDelegate getNhinDelegate() {
        return nhinDelegate;
    }

    public void setNhinDelegate(NhinDelegate nhinDelegate) {
        this.nhinDelegate = nhinDelegate;
    }


    public ProvideAndRegisterDocumentSetRequestType getRequest() {
        return request;
    }

    public void setRequestType(ProvideAndRegisterDocumentSetRequestType request) {
        this.request = request;
    }

    public NhinTargetSystemType getNhinTargetSystemType() {
        return target;
    }

    public void setNhinTargetSystemType(NhinTargetSystemType target) {
        this.target = target;
    }
}
