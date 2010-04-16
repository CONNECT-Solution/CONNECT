/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.entity.async.request;

import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.proxy.async.request.NhincProxyPatientDiscoverySecuredAsyncReqImpl;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.CommunityPRPAIN201306UV02ResponseType;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02SecuredRequestType;

/**
 *
 * @author jhoppesc
 */
public class EntityPatientDiscoverySecuredAsyncReqImpl {
    private static Log log = LogFactory.getLog(EntityPatientDiscoverySecuredAsyncReqImpl.class);

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(RespondingGatewayPRPAIN201305UV02SecuredRequestType request, WebServiceContext context) {
        RespondingGatewayPRPAIN201305UV02RequestType unsecureRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
        unsecureRequest.setNhinTargetCommunities(request.getNhinTargetCommunities());
        unsecureRequest.setPRPAIN201305UV02(request.getPRPAIN201305UV02());
        unsecureRequest.setAssertion(SamlTokenExtractor.GetAssertion(context));

        MCCIIN000002UV01 ack = processPatientDiscoveryAsyncReq(unsecureRequest);

        return ack;
    }

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(RespondingGatewayPRPAIN201305UV02RequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();
        CMUrlInfos urlInfoList = null;
        NhincProxyPatientDiscoverySecuredAsyncReqImpl proxy = new NhincProxyPatientDiscoverySecuredAsyncReqImpl();

        // Obtain all the URLs for the targets being sent to
        try {
            urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(request.getNhinTargetCommunities(), NhincConstants.PATIENT_DISCOVERY_ASYNC_REQ_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Failed to obtain target URLs for service " + NhincConstants.PATIENT_DISCOVERY_ASYNC_REQ_SERVICE_NAME);
            return null;
        }

        //loop through the communities and send request if results were not null
        if (urlInfoList != null &&
                urlInfoList.getUrlInfo() != null) {
            for (CMUrlInfo urlInfo : urlInfoList.getUrlInfo()) {

                    CommunityPRPAIN201306UV02ResponseType communityResponse = new CommunityPRPAIN201306UV02ResponseType();

                    NhinTargetSystemType oTargetSystemType = new NhinTargetSystemType();
                    oTargetSystemType.setUrl(urlInfo.getUrl());

                    //format request for nhincProxyPatientDiscoveryImpl call
                    ProxyPRPAIN201305UVProxyRequestType proxyReq = new ProxyPRPAIN201305UVProxyRequestType();
                    proxyReq.setPRPAIN201305UV02(request.getPRPAIN201305UV02());
                    proxyReq.setAssertion(request.getAssertion());
                    proxyReq.setNhinTargetSystem(oTargetSystemType);

                    ack = proxy.proxyProcessPatientDiscoveryAsyncReq(proxyReq);
            }
        } else {
            log.warn("No targets were found for the Patient Discovery Request");
        }

        

        return ack;
    }

}
