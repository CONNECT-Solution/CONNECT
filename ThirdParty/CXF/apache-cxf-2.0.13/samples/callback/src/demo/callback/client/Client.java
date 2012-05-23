/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


package demo.callback.client;

import java.io.File;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;

import org.apache.callback.SOAPService;
import org.apache.callback.ServerPortType;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.apache.cxf.wsdl.EndpointReferenceUtils;





public final class Client {

    private static final QName SERVICE_NAME 
        = new QName("http://apache.org/callback", "SOAPService");

    private static final QName SERVICE_NAME_CALLBACK 
        = new QName("http://apache.org/callback", "CallbackService");
    
    private static final QName PORT_NAME_CALLBACK 
        = new QName("http://apache.org/callback", "CallbackPort");
    
    private static final QName PORT_TYPE_CALLBACK
        = new QName("http://apache.org/callback", "CallbackPortType");

    private Client() {
    } 

    public static void main(String args[]) throws Exception {
        
        
        Object implementor = new CallbackImpl();
        String address = "http://localhost:9005/CallbackContext/CallbackPort";
        Endpoint.publish(address, implementor);
        
        if (args.length == 0) { 
            System.out.println("please specify wsdl");
            System.exit(1); 
        }

        URL wsdlURL;
        File wsdlFile = new File(args[0]);
        if (wsdlFile.exists()) {
            wsdlURL = wsdlFile.toURL();
        } else {
            wsdlURL = new URL(args[0]);
        }
        
        SOAPService ss = new SOAPService(wsdlURL, SERVICE_NAME);
        ServerPortType port = ss.getSOAPPort();
        
        EndpointReferenceType ref =
            EndpointReferenceUtils.getEndpointReference(wsdlURL,
                                                        SERVICE_NAME_CALLBACK, 
                                                        PORT_NAME_CALLBACK.getLocalPart());
        EndpointReferenceUtils.setInterfaceName(ref, PORT_TYPE_CALLBACK);
        

        String resp = port.registerCallback(ref);
        System.out.println("Response from server: " + resp);
        
        System.exit(0); 
    }

}
