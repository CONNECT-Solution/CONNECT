/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.entity.xdr.async.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType;
import ihe.iti.xdr._2007.AcknowledgementType;

/**
 *
 * @author jhoppesc
 */
public class EntityXDRAsyncRespNoOpImpl implements EntityXDRAsyncRespProxy {

    public AcknowledgementType provideAndRegisterDocumentSetBAsyncResponse(RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType provideAndRegisterDocumentSetAsyncRespRequest) {
        return new AcknowledgementType();
    }

}
