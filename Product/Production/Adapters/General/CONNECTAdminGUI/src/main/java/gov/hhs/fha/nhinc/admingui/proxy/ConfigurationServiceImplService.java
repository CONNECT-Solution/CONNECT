/**
 * Copyright (c) 2009-2014, United States Government, as represented by the Secretary of Health and Human Services. All
 * rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met: Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution. Neither the name of the United States Government nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package gov.hhs.fha.nhinc.admingui.proxy;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 *
 * @author nsubrama
 */
@WebServiceClient(name = "ConfigurationServiceImplService", targetNamespace = "http://nhind.org/config", wsdlLocation = "http://localhost:8080/CONNECTDirectConfig/ConfigurationService?wsdl")
public class ConfigurationServiceImplService
    extends Service

{

    private final static URL CONFIGURATIONSERVICEIMPLSERVICE_WSDL_LOCATION;
    private final static WebServiceException CONFIGURATIONSERVICEIMPLSERVICE_EXCEPTION;
    private final static QName CONFIGURATIONSERVICEIMPLSERVICE_QNAME = new QName("http://nhind.org/config", "ConfigurationServiceImplService");

    private static final WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();
    
    static {
        URL url = null;
        WebServiceException e = null;
        try {
            String sUrl = oProxyHelper.getAdapterEndPointFromConnectionManager(DirectConfigConstants.DIRECT_CONFIG_SERVICE_NAME);
            url = new URL(sUrl);
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        } catch (ConnectionManagerException ex) {
            e = new WebServiceException(ex);
        }
        CONFIGURATIONSERVICEIMPLSERVICE_WSDL_LOCATION = url;
        CONFIGURATIONSERVICEIMPLSERVICE_EXCEPTION = e;
    }

    public ConfigurationServiceImplService() {
        super(__getWsdlLocation(), CONFIGURATIONSERVICEIMPLSERVICE_QNAME);
    }

    public ConfigurationServiceImplService(WebServiceFeature... features) {
        super(__getWsdlLocation(), CONFIGURATIONSERVICEIMPLSERVICE_QNAME);
    }

    /**
     * 
     * @return
     *     returns ConfigurationService
     */
    @WebEndpoint(name = "ConfigurationServiceImplPort")
    public org.nhind.config.ConfigurationService getConfigurationServiceImplPort() {
        return super.getPort(new QName("http://nhind.org/config", "ConfigurationServiceImplPort"), org.nhind.config.ConfigurationService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ConfigurationService
     */
    @WebEndpoint(name = "ConfigurationServiceImplPort")
    public org.nhind.config.ConfigurationService getConfigurationServiceImplPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://nhind.org/config", "ConfigurationServiceImplPort"), org.nhind.config.ConfigurationService.class, features);
    }

    private static URL __getWsdlLocation() {
        if (CONFIGURATIONSERVICEIMPLSERVICE_EXCEPTION!= null) {
            throw CONFIGURATIONSERVICEIMPLSERVICE_EXCEPTION;
        }
        return CONFIGURATIONSERVICEIMPLSERVICE_WSDL_LOCATION;
    }

}

