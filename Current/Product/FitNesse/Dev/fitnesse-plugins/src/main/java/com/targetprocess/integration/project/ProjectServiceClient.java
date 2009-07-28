
package com.targetprocess.integration.project;

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

public class ProjectServiceClient {

    private static XFireProxyFactory proxyFactory = new XFireProxyFactory();
    private HashMap endpoints = new HashMap();
    private Service service0;

    public ProjectServiceClient() {
        create0();
        Endpoint ProjectServiceSoapLocalEndpointEP = service0 .addEndpoint(new QName("http://targetprocess.com", "ProjectServiceSoapLocalEndpoint"), new QName("http://targetprocess.com", "ProjectServiceSoapLocalBinding"), "xfire.local://ProjectService");
        endpoints.put(new QName("http://targetprocess.com", "ProjectServiceSoapLocalEndpoint"), ProjectServiceSoapLocalEndpointEP);
        Endpoint ProjectServiceSoapEP = service0 .addEndpoint(new QName("http://targetprocess.com", "ProjectServiceSoap"), new QName("http://targetprocess.com", "ProjectServiceSoap"), "http://nodalexchange.tpondemand.com/Services/ProjectService.asmx");
        endpoints.put(new QName("http://targetprocess.com", "ProjectServiceSoap"), ProjectServiceSoapEP);
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
        service0 = asf.create((com.targetprocess.integration.project.ProjectServiceSoap.class), props);
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://targetprocess.com", "ProjectServiceSoapLocalBinding"), "urn:xfire:transport:local");
        }
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://targetprocess.com", "ProjectServiceSoap"), "http://schemas.xmlsoap.org/soap/http");
        }
    }

    public ProjectServiceSoap getProjectServiceSoapLocalEndpoint() {
        return ((ProjectServiceSoap)(this).getEndpoint(new QName("http://targetprocess.com", "ProjectServiceSoapLocalEndpoint")));
    }

    public ProjectServiceSoap getProjectServiceSoapLocalEndpoint(String url) {
        ProjectServiceSoap var = getProjectServiceSoapLocalEndpoint();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

    public ProjectServiceSoap getProjectServiceSoap() {
        return ((ProjectServiceSoap)(this).getEndpoint(new QName("http://targetprocess.com", "ProjectServiceSoap")));
    }

    public ProjectServiceSoap getProjectServiceSoap(String url) {
        ProjectServiceSoap var = getProjectServiceSoap();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

}
