
package com.targetprocess.integration.iteration;

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

public class IterationServiceClient {

    private static XFireProxyFactory proxyFactory = new XFireProxyFactory();
    private HashMap endpoints = new HashMap();
    private Service service0;

    public IterationServiceClient() {
        create0();
        Endpoint IterationServiceSoapEP = service0 .addEndpoint(new QName("http://targetprocess.com", "IterationServiceSoap"), new QName("http://targetprocess.com", "IterationServiceSoap"), "http://nodalexchange.tpondemand.com/Services/IterationService.asmx");
        endpoints.put(new QName("http://targetprocess.com", "IterationServiceSoap"), IterationServiceSoapEP);
        Endpoint IterationServiceSoapLocalEndpointEP = service0 .addEndpoint(new QName("http://targetprocess.com", "IterationServiceSoapLocalEndpoint"), new QName("http://targetprocess.com", "IterationServiceSoapLocalBinding"), "xfire.local://IterationService");
        endpoints.put(new QName("http://targetprocess.com", "IterationServiceSoapLocalEndpoint"), IterationServiceSoapLocalEndpointEP);
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
        service0 = asf.create((com.targetprocess.integration.iteration.IterationServiceSoap.class), props);
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://targetprocess.com", "IterationServiceSoapLocalBinding"), "urn:xfire:transport:local");
        }
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://targetprocess.com", "IterationServiceSoap"), "http://schemas.xmlsoap.org/soap/http");
        }
    }

    public IterationServiceSoap getIterationServiceSoap() {
        return ((IterationServiceSoap)(this).getEndpoint(new QName("http://targetprocess.com", "IterationServiceSoap")));
    }

    public IterationServiceSoap getIterationServiceSoap(String url) {
        IterationServiceSoap var = getIterationServiceSoap();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

    public IterationServiceSoap getIterationServiceSoapLocalEndpoint() {
        return ((IterationServiceSoap)(this).getEndpoint(new QName("http://targetprocess.com", "IterationServiceSoapLocalEndpoint")));
    }

    public IterationServiceSoap getIterationServiceSoapLocalEndpoint(String url) {
        IterationServiceSoap var = getIterationServiceSoapLocalEndpoint();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

}
