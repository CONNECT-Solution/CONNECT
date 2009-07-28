
package com.targetprocess.integration.release;

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

public class ReleaseServiceClient {

    private static XFireProxyFactory proxyFactory = new XFireProxyFactory();
    private HashMap endpoints = new HashMap();
    private Service service0;

    public ReleaseServiceClient() {
        create0();
        Endpoint ReleaseServiceSoapLocalEndpointEP = service0 .addEndpoint(new QName("http://targetprocess.com", "ReleaseServiceSoapLocalEndpoint"), new QName("http://targetprocess.com", "ReleaseServiceSoapLocalBinding"), "xfire.local://ReleaseService");
        endpoints.put(new QName("http://targetprocess.com", "ReleaseServiceSoapLocalEndpoint"), ReleaseServiceSoapLocalEndpointEP);
        Endpoint ReleaseServiceSoapEP = service0 .addEndpoint(new QName("http://targetprocess.com", "ReleaseServiceSoap"), new QName("http://targetprocess.com", "ReleaseServiceSoap"), "http://nodalexchange.tpondemand.com/Services/ReleaseService.asmx");
        endpoints.put(new QName("http://targetprocess.com", "ReleaseServiceSoap"), ReleaseServiceSoapEP);
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
        service0 = asf.create((com.targetprocess.integration.release.ReleaseServiceSoap.class), props);
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://targetprocess.com", "ReleaseServiceSoapLocalBinding"), "urn:xfire:transport:local");
        }
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://targetprocess.com", "ReleaseServiceSoap"), "http://schemas.xmlsoap.org/soap/http");
        }
    }

    public ReleaseServiceSoap getReleaseServiceSoapLocalEndpoint() {
        return ((ReleaseServiceSoap)(this).getEndpoint(new QName("http://targetprocess.com", "ReleaseServiceSoapLocalEndpoint")));
    }

    public ReleaseServiceSoap getReleaseServiceSoapLocalEndpoint(String url) {
        ReleaseServiceSoap var = getReleaseServiceSoapLocalEndpoint();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

    public ReleaseServiceSoap getReleaseServiceSoap() {
        return ((ReleaseServiceSoap)(this).getEndpoint(new QName("http://targetprocess.com", "ReleaseServiceSoap")));
    }

    public ReleaseServiceSoap getReleaseServiceSoap(String url) {
        ReleaseServiceSoap var = getReleaseServiceSoap();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

}
