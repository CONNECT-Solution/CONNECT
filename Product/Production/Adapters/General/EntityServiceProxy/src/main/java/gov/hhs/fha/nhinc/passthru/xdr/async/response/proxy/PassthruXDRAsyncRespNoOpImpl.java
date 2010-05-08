/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.passthru.xdr.async.response.proxy;

import ihe.iti.xdr._2007.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType;

/**
 *
 * @author jhoppesc
 */
public class PassthruXDRAsyncRespNoOpImpl implements PassthruXDRAsyncRespProxy {

    public AcknowledgementType provideAndRegisterDocumentSetBAsyncResponse(RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType provideAndRegisterAsyncRespRequest) {
        return new AcknowledgementType();
    }

}
