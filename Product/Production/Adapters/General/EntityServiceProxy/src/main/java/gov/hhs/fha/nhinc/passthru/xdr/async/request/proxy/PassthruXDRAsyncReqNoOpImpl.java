/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.passthru.xdr.async.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xdr._2007.AcknowledgementType;

/**
 *
 * @author jhoppesc
 */
public class PassthruXDRAsyncReqNoOpImpl implements PassthruXDRAsyncReqProxy {

    public AcknowledgementType provideAndRegisterDocumentSetBAsyncRequest(RespondingGatewayProvideAndRegisterDocumentSetRequestType provideAndRegisterAsyncReqRequest) {
        return new AcknowledgementType();
    }

}
