/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admingui.proxy;

import java.net.MalformedURLException;
import java.net.URL;
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

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:8080/CONNECTDirectConfig/ConfigurationService?wsdl");
        } catch (MalformedURLException ex) {
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

    public ConfigurationServiceImplService(URL wsdlLocation) {
        super(wsdlLocation, CONFIGURATIONSERVICEIMPLSERVICE_QNAME);
    }

    public ConfigurationServiceImplService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, CONFIGURATIONSERVICEIMPLSERVICE_QNAME);
    }

    public ConfigurationServiceImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ConfigurationServiceImplService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName);
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

