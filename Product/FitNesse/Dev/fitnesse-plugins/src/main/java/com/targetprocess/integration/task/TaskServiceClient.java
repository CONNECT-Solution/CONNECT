
package com.targetprocess.integration.task;

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

public class TaskServiceClient {

    private static XFireProxyFactory proxyFactory = new XFireProxyFactory();
    private HashMap endpoints = new HashMap();
    private Service service0;

    public TaskServiceClient() {
        create0();
        Endpoint TaskServiceSoapLocalEndpointEP = service0 .addEndpoint(new QName("http://targetprocess.com", "TaskServiceSoapLocalEndpoint"), new QName("http://targetprocess.com", "TaskServiceSoapLocalBinding"), "xfire.local://TaskService");
        endpoints.put(new QName("http://targetprocess.com", "TaskServiceSoapLocalEndpoint"), TaskServiceSoapLocalEndpointEP);
        Endpoint TaskServiceSoapEP = service0 .addEndpoint(new QName("http://targetprocess.com", "TaskServiceSoap"), new QName("http://targetprocess.com", "TaskServiceSoap"), "http://nodalexchange.tpondemand.com/Services/TaskService.asmx");
        endpoints.put(new QName("http://targetprocess.com", "TaskServiceSoap"), TaskServiceSoapEP);
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
        service0 = asf.create((com.targetprocess.integration.task.TaskServiceSoap.class), props);
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://targetprocess.com", "TaskServiceSoapLocalBinding"), "urn:xfire:transport:local");
        }
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://targetprocess.com", "TaskServiceSoap"), "http://schemas.xmlsoap.org/soap/http");
        }
    }

    public TaskServiceSoap getTaskServiceSoapLocalEndpoint() {
        return ((TaskServiceSoap)(this).getEndpoint(new QName("http://targetprocess.com", "TaskServiceSoapLocalEndpoint")));
    }

    public TaskServiceSoap getTaskServiceSoapLocalEndpoint(String url) {
        TaskServiceSoap var = getTaskServiceSoapLocalEndpoint();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

    public TaskServiceSoap getTaskServiceSoap() {
        return ((TaskServiceSoap)(this).getEndpoint(new QName("http://targetprocess.com", "TaskServiceSoap")));
    }

    public TaskServiceSoap getTaskServiceSoap(String url) {
        TaskServiceSoap var = getTaskServiceSoap();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

}
