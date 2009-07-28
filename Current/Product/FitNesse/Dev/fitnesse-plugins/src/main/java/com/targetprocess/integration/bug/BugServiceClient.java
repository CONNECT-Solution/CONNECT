
package com.targetprocess.integration.bug;

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

public class BugServiceClient {

    private static XFireProxyFactory proxyFactory = new XFireProxyFactory();
    private HashMap endpoints = new HashMap();
    private Service service0;

    public BugServiceClient() {
        create0();
        Endpoint BugServiceSoapEP = service0 .addEndpoint(new QName("http://targetprocess.com", "BugServiceSoap"), new QName("http://targetprocess.com", "BugServiceSoap"), "http://nodalexchange.tpondemand.com/Services/BugService.asmx");
        endpoints.put(new QName("http://targetprocess.com", "BugServiceSoap"), BugServiceSoapEP);
        Endpoint BugServiceSoapLocalEndpointEP = service0 .addEndpoint(new QName("http://targetprocess.com", "BugServiceSoapLocalEndpoint"), new QName("http://targetprocess.com", "BugServiceSoapLocalBinding"), "xfire.local://BugService");
        endpoints.put(new QName("http://targetprocess.com", "BugServiceSoapLocalEndpoint"), BugServiceSoapLocalEndpointEP);
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
        service0 = asf.create((com.targetprocess.integration.bug.BugServiceSoap.class), props);
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://targetprocess.com", "BugServiceSoap"), "http://schemas.xmlsoap.org/soap/http");
        }
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://targetprocess.com", "BugServiceSoapLocalBinding"), "urn:xfire:transport:local");
        }
    }

    public BugServiceSoap getBugServiceSoap() {
        return ((BugServiceSoap)(this).getEndpoint(new QName("http://targetprocess.com", "BugServiceSoap")));
    }

    public BugServiceSoap getBugServiceSoap(String url) {
        BugServiceSoap var = getBugServiceSoap();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

    public BugServiceSoap getBugServiceSoapLocalEndpoint() {
        return ((BugServiceSoap)(this).getEndpoint(new QName("http://targetprocess.com", "BugServiceSoapLocalEndpoint")));
    }

    public BugServiceSoap getBugServiceSoapLocalEndpoint(String url) {
        BugServiceSoap var = getBugServiceSoapLocalEndpoint();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

}
