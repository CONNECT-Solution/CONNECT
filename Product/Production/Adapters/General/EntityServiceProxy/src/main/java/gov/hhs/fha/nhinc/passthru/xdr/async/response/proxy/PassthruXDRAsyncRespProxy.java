/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.passthru.xdr.async.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType;
import ihe.iti.xdr._2007.AcknowledgementType;

/**
 *
 * @author jhoppesc
 */
public interface PassthruXDRAsyncRespProxy {
    public AcknowledgementType provideAndRegisterDocumentSetBAsyncResponse(RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType provideAndRegisterAsyncRespRequest);
}
