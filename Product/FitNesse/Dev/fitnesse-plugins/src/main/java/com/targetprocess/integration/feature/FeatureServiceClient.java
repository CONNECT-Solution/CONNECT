
package com.targetprocess.integration.feature;

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

public class FeatureServiceClient {

    private static XFireProxyFactory proxyFactory = new XFireProxyFactory();
    private HashMap endpoints = new HashMap();
    private Service service0;

    public FeatureServiceClient() {
        create0();
        Endpoint FeatureServiceSoapLocalEndpointEP = service0 .addEndpoint(new QName("http://targetprocess.com", "FeatureServiceSoapLocalEndpoint"), new QName("http://targetprocess.com", "FeatureServiceSoapLocalBinding"), "xfire.local://FeatureService");
        endpoints.put(new QName("http://targetprocess.com", "FeatureServiceSoapLocalEndpoint"), FeatureServiceSoapLocalEndpointEP);
        Endpoint FeatureServiceSoapEP = service0 .addEndpoint(new QName("http://targetprocess.com", "FeatureServiceSoap"), new QName("http://targetprocess.com", "FeatureServiceSoap"), "http://nodalexchange.tpondemand.com/Services/FeatureService.asmx");
        endpoints.put(new QName("http://targetprocess.com", "FeatureServiceSoap"), FeatureServiceSoapEP);
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
        service0 = asf.create((com.targetprocess.integration.feature.FeatureServiceSoap.class), props);
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://targetprocess.com", "FeatureServiceSoap"), "http://schemas.xmlsoap.org/soap/http");
        }
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://targetprocess.com", "FeatureServiceSoapLocalBinding"), "urn:xfire:transport:local");
        }
    }

    public FeatureServiceSoap getFeatureServiceSoapLocalEndpoint() {
        return ((FeatureServiceSoap)(this).getEndpoint(new QName("http://targetprocess.com", "FeatureServiceSoapLocalEndpoint")));
    }

    public FeatureServiceSoap getFeatureServiceSoapLocalEndpoint(String url) {
        FeatureServiceSoap var = getFeatureServiceSoapLocalEndpoint();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

    public FeatureServiceSoap getFeatureServiceSoap() {
        return ((FeatureServiceSoap)(this).getEndpoint(new QName("http://targetprocess.com", "FeatureServiceSoap")));
    }

    public FeatureServiceSoap getFeatureServiceSoap(String url) {
        FeatureServiceSoap var = getFeatureServiceSoap();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

}
