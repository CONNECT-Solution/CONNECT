/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.passthru.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

/**
 * 
 *
 * @author patlollav
 */
public interface PassthruDocQueryDeferredRequestProxy {

    public DocQueryAcknowledgementType crossGatewayQueryRequest(AdhocQueryRequest msg, AssertionType assertion, NhinTargetSystemType target);

}
