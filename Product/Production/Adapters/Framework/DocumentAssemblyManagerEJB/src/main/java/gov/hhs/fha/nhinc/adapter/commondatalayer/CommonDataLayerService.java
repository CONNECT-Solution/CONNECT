/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 *
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 *
 * All other rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adapter.commondatalayer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.3.1-hudson-749-SNAPSHOT
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "CommonDataLayerService", targetNamespace = "urn:gov:hhs:fha:nhinc:adapter:commondatalayer", wsdlLocation = "file:/C:/projects/NHINC/3.1/Product/Production/Common/Interfaces/src/wsdl/AdapterCommonDataLayer.wsdl")
public class CommonDataLayerService
    extends Service
{

    private final static URL COMMONDATALAYERSERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(gov.hhs.fha.nhinc.adapter.commondatalayer.CommonDataLayerService.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = gov.hhs.fha.nhinc.adapter.commondatalayer.CommonDataLayerService.class.getResource(".");
            url = new URL(baseUrl, "file:/C:/projects/NHINC/3.1/Product/Production/Common/Interfaces/src/wsdl/AdapterCommonDataLayer.wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'file:/C:/projects/NHINC/3.1/Product/Production/Common/Interfaces/src/wsdl/AdapterCommonDataLayer.wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        COMMONDATALAYERSERVICE_WSDL_LOCATION = url;
    }

    public CommonDataLayerService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public CommonDataLayerService() {
        super(COMMONDATALAYERSERVICE_WSDL_LOCATION, new QName("urn:gov:hhs:fha:nhinc:adapter:commondatalayer", "CommonDataLayerService"));
    }

    /**
     * 
     * @return
     *     returns CommonDataLayerPortType
     */
    @WebEndpoint(name = "CommonDataLayerPort")
    public CommonDataLayerPortType getCommonDataLayerPort() {
        return super.getPort(new QName("urn:gov:hhs:fha:nhinc:adapter:commondatalayer", "CommonDataLayerPort"), CommonDataLayerPortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CommonDataLayerPortType
     */
    @WebEndpoint(name = "CommonDataLayerPort")
    public CommonDataLayerPortType getCommonDataLayerPort(WebServiceFeature... features) {
        return super.getPort(new QName("urn:gov:hhs:fha:nhinc:adapter:commondatalayer", "CommonDataLayerPort"), CommonDataLayerPortType.class, features);
    }

}
