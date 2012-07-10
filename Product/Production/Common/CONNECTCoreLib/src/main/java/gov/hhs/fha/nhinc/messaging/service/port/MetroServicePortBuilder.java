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

package gov.hhs.fha.nhinc.messaging.service.port;

import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

import java.util.HashMap;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author bhumphrey
 *
 */
public class MetroServicePortBuilder<T> implements ServicePortBuilder<T> {

    private static HashMap<String, Service> cachedServiceMap = new HashMap<String, Service>();
    private static Log log = LogFactory.getLog(MetroServicePortBuilder.class);
    
    private WebServiceProxyHelper proxyHelper = null;
    private String namespaceURI;
    private String serviceLocalPart;
    private String portLocalPart;
    private String wsdlFile;
    private Class<T> serviceEndpointClass; 
     
    public MetroServicePortBuilder(ServicePortDescriptor<T> portDescriptor) {
        super();
        this.namespaceURI = portDescriptor.getNamespaceUri();
        this.serviceLocalPart = portDescriptor.getServiceLocalPart();
        this.portLocalPart = portDescriptor.getPortLocalPart();
        this.wsdlFile = portDescriptor.getWSDLFileName();
        this.serviceEndpointClass = portDescriptor.getPortClass();
    
        this.proxyHelper = new WebServiceProxyHelper();
    }
    
    public T createPort() {
        Service service = getService(wsdlFile);
        T port = (T) service.getPort(new QName(namespaceURI, portLocalPart), serviceEndpointClass);
      
        return port;
    }

    /**
     * Retrieve the service class for this web service.
     * 
     * @return The service class for this web service.
     */
    protected Service getService(String wsdlFile) {
        
        Service cachedService = cachedServiceMap.get(wsdlFile);
        if (cachedService == null) {
            try {
                cachedService = proxyHelper.createService(wsdlFile, namespaceURI, serviceLocalPart);
                cachedServiceMap.put(wsdlFile, cachedService);
            } catch (Throwable t) {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }

}
