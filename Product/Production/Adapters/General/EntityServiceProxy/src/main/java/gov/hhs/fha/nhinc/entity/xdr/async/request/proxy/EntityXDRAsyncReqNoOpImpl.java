/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.entity.xdr.async.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xdr._2007.AcknowledgementType;

/**
 *
 * @author jhoppesc
 */
public class EntityXDRAsyncReqNoOpImpl implements EntityXDRAsyncReqProxy {

    public AcknowledgementType provideAndRegisterDocumentSetBAsyncRequest(RespondingGatewayProvideAndRegisterDocumentSetRequestType provideAndRegisterRequestRequest) {
        return new AcknowledgementType();
    }

}
