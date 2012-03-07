/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.admindistribution;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhincadmindistribution.NhincAdminDistPortType;
import gov.hhs.fha.nhinc.nhincadmindistribution.NhincAdminDistSecuredPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

public class PassthruAdminDistributionHelper extends AdminDistributionHelper {

    private Log log = LogFactory.getLog(PassthruAdminDistributionHelper.class);

    WebServiceProxyHelper proxyHelper;

    private String WSDL_FILE_G0;
    private String WSDL_FILE_G1;
    private String NAMESPACE_URI;
    private String SERVICE_LOCAL_PART;
    private String action;
    private String PORT_LOCAL_PART;

    public PassthruAdminDistributionHelper(WebServiceProxyHelper proxyHelper, String wsdlFileG0, String wsdlFileG1,
            String namespaceURI, String serviceLocalPart, String portLocalPart, String action) {
        this.proxyHelper = proxyHelper;
        this.WSDL_FILE_G0 = wsdlFileG0;
        this.WSDL_FILE_G1 = wsdlFileG1;
        this.NAMESPACE_URI = namespaceURI;
        this.SERVICE_LOCAL_PART = serviceLocalPart;
        this.PORT_LOCAL_PART = portLocalPart;
        this.action = action;

    }

    public Service getService(String wsdl, String uri, String service) {
        try {

            return proxyHelper.createService(wsdl, uri, service);
        } catch (Throwable t) {
            log.error("Error creating service: " + t.getMessage(), t);
        }
        return null;
    }

    public NhincAdminDistSecuredPortType getSecuredPort(String url, String nhincAdminDistSecuredServiceName,
            String wsAddressingAction, AssertionType assertion, GATEWAY_API_LEVEL apiLevel) {
        NhincAdminDistSecuredPortType port = null;
        String wsdlFile = (apiLevel.equals(NhincConstants.GATEWAY_API_LEVEL.LEVEL_g0)) ? WSDL_FILE_G0 : WSDL_FILE_G1;
        Service service = getService(wsdlFile, NAMESPACE_URI, SERVICE_LOCAL_PART);
        if (service != null) {
            log.debug("Obtained service - creating port.");
            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), NhincAdminDistSecuredPortType.class);

            proxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, action, wsAddressingAction,
                    assertion);
        } else {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    public NhincAdminDistPortType getUnsecuredPort(String url, String wsAddressingAction, AssertionType assertion,
            NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        NhincAdminDistPortType port = null;
        String wsdlFile = (apiLevel.equals(NhincConstants.GATEWAY_API_LEVEL.LEVEL_g0)) ? WSDL_FILE_G0 : WSDL_FILE_G1;
        Service service = getService(wsdlFile, NAMESPACE_URI, SERVICE_LOCAL_PART);
        if (service != null) {
            log.debug("Obtained service - creating port.");
            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), NhincAdminDistPortType.class);
            proxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
        } else {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

}
