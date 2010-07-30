package gov.hhs.fha.nhinc.docretrievedeferred.nhin.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;

/**
 * Created by
 * User: ralph
 * Date: Jul 26, 2010
 * Time: 1:34:33 PM
 */
public interface NhinDocRetrieveDeferredReqProxy {

    public void  sendToAdapter(EDXLDistribution body, AssertionType assertion, NhinTargetSystemType  target);
}
