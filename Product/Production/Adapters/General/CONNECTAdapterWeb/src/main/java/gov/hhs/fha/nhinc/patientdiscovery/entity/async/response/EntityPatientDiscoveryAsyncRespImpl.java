/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.entity.async.response;

import gov.hhs.fha.nhinc.entity.patientdiscovery.async.response.proxy.EntityPatientDiscoveryAsyncRespProxy;
import gov.hhs.fha.nhinc.entity.patientdiscovery.async.response.proxy.EntityPatientDiscoveryAsyncRespProxyObjectFactory;
import gov.hhs.fha.nhinc.service.WebServiceHelper;

import javax.xml.ws.WebServiceContext;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;

/**
 *
 * @author JHOPPESC
 */
public class EntityPatientDiscoveryAsyncRespImpl {
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(EntityPatientDiscoveryAsyncRespImpl.class);

    public MCCIIN000002UV01 processPatientDiscoveryAsyncResp(RespondingGatewayPRPAIN201306UV02RequestType request, WebServiceContext context) {
        WebServiceHelper oHelper = new WebServiceHelper();
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        EntityPatientDiscoveryAsyncRespProxyObjectFactory entityPatDiscAsyncReqFactory = new EntityPatientDiscoveryAsyncRespProxyObjectFactory();
        EntityPatientDiscoveryAsyncRespProxy proxy = entityPatDiscAsyncReqFactory.getEntityPatientDiscoveryAsyncRespProxy();

        try
        {
            if (request != null && proxy != null)
            {
                ack = (MCCIIN000002UV01) oHelper.invokeDeferredResponseWebService(proxy, proxy.getClass(), "processPatientDiscoveryAsyncResp", request.getAssertion(), request, context);
            } else
            {
                log.error("Failed to call the web orchestration (" + proxy.getClass() + ".processPatientDiscoveryAsyncResp).  The input parameter is null.");
            }
        } catch (Exception e)
        {
            log.error("Failed to call the web orchestration (" + proxy.getClass() + ".processPatientDiscoveryAsyncResp).  An unexpected exception occurred.  " +
                    "Exception: " + e.getMessage(), e);
        }
        return ack;
    }

}
