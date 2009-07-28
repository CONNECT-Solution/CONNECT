
package com.targetprocess.integration.userstory;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import javax.xml.namespace.QName;
import org.codehaus.xfire.XFireRuntimeException;
import org.codehaus.xfire.aegis.AegisBindingProvider;
import org.codehaus.xfire.annotations.AnnotationServiceFactory;
import org.codehaus.xfire.annotations.jsr181.Jsr181WebAnnotations;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.jaxb2.JaxbTypeRegistry;
import org.codehaus.xfire.service.Endpoint;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.soap.AbstractSoapBinding;
import org.codehaus.xfire.transport.TransportManager;

public class UserStoryServiceClient {

    private static XFireProxyFactory proxyFactory = new XFireProxyFactory();
    private HashMap endpoints = new HashMap();
    private Service service0;

    public UserStoryServiceClient() {
        create0();
        Endpoint UserStoryServiceSoapLocalEndpointEP = service0 .addEndpoint(new QName("http://targetprocess.com", "UserStoryServiceSoapLocalEndpoint"), new QName("http://targetprocess.com", "UserStoryServiceSoapLocalBinding"), "xfire.local://UserStoryService");
        endpoints.put(new QName("http://targetprocess.com", "UserStoryServiceSoapLocalEndpoint"), UserStoryServiceSoapLocalEndpointEP);
        Endpoint UserStoryServiceSoapEP = service0 .addEndpoint(new QName("http://targetprocess.com", "UserStoryServiceSoap"), new QName("http://targetprocess.com", "UserStoryServiceSoap"), "http://nodalexchange.tpondemand.com/Services/UserStoryService.asmx");
        endpoints.put(new QName("http://targetprocess.com", "UserStoryServiceSoap"), UserStoryServiceSoapEP);
    }

    public Object getEndpoint(Endpoint endpoint) {
        try {
            return proxyFactory.create((endpoint).getBinding(), (endpoint).getUrl());
        } catch (MalformedURLException e) {
            throw new XFireRuntimeException("Invalid URL", e);
        }
    }

    public Object getEndpoint(QName name) {
        Endpoint endpoint = ((Endpoint) endpoints.get((name)));
        if ((endpoint) == null) {
            throw new IllegalStateException("No such endpoint!");
        }
        return getEndpoint((endpoint));
    }

    public Collection getEndpoints() {
        return endpoints.values();
    }

    private void create0() {
        TransportManager tm = (org.codehaus.xfire.XFireFactory.newInstance().getXFire().getTransportManager());
        HashMap props = new HashMap();
        props.put("annotations.allow.interface", true);
        AnnotationServiceFactory asf = new AnnotationServiceFactory(new Jsr181WebAnnotations(), tm, new AegisBindingProvider(new JaxbTypeRegistry()));
        asf.setBindingCreationEnabled(false);
        service0 = asf.create((com.targetprocess.integration.userstory.UserStoryServiceSoap.class), props);
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://targetprocess.com", "UserStoryServiceSoapLocalBinding"), "urn:xfire:transport:local");
        }
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://targetprocess.com", "UserStoryServiceSoap"), "http://schemas.xmlsoap.org/soap/http");
        }
    }

    public UserStoryServiceSoap getUserStoryServiceSoapLocalEndpoint() {
        return ((UserStoryServiceSoap)(this).getEndpoint(new QName("http://targetprocess.com", "UserStoryServiceSoapLocalEndpoint")));
    }

    public UserStoryServiceSoap getUserStoryServiceSoapLocalEndpoint(String url) {
        UserStoryServiceSoap var = getUserStoryServiceSoapLocalEndpoint();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

    public UserStoryServiceSoap getUserStoryServiceSoap() {
        return ((UserStoryServiceSoap)(this).getEndpoint(new QName("http://targetprocess.com", "UserStoryServiceSoap")));
    }

    public UserStoryServiceSoap getUserStoryServiceSoap(String url) {
        UserStoryServiceSoap var = getUserStoryServiceSoap();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

}
