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
package gov.hhs.fha.nhinc.connectmgr.data;

/**
 * This class represents Endpoint Reference (EPR) Information
 * 
 * @author Les Westberg
 */
public class CMEprInfo {
    // Member variables
    // ------------------
    private String uniformServiceName = "";
    private String namespacePrefix = "";
    private String namespaceURI = "";
    private String portName = "";
    private String serviceName = "";

    /**
     * Returns the uniform service name for this EPR. This is the key by which this service is known in the UDDI.
     * 
     * @return The uniform service name for this EPR.
     */
    public String getUniformServiceName() {
        return uniformServiceName;
    }

    /**
     * Sets the uniform service name for this EPR. This is the key by which this service is known in the UDDI.
     * 
     * @param uniformServiceName The uniform service name for this EPR.
     * 
     */
    public void setUniformServiceName(String uniformServiceName) {
        this.uniformServiceName = uniformServiceName;
    }

    /**
     * Return the namespace prefix.
     * 
     * @return The namespace prefix.
     */
    public String getNamespacePrefix() {
        return namespacePrefix;
    }

    /**
     * Set the namespace prefix.
     * 
     * @param namespacePrefix The namespace prefix.
     */
    public void setNamespacePrefix(String namespacePrefix) {
        this.namespacePrefix = namespacePrefix;
    }

    /**
     * Returns the namespace URI for the endpoint.
     * 
     * @return Returns the namespace URI for the endpoint.
     */
    public String getNamespaceURI() {
        return namespaceURI;
    }

    /**
     * Sets the namespace URI for the endpoint.
     * 
     * @param namespaceURI The namespace URI for the endpoint.
     */
    public void setNamespaceURI(String namespaceURI) {
        this.namespaceURI = namespaceURI;
    }

    /**
     * Returns the port name for the endpoint.
     * 
     * @return The port name for this endpoint.
     */
    public String getPortName() {
        return portName;
    }

    /**
     * Sets the port name for the endpoint.
     * 
     * @param portName The port name for the endpoint.
     */
    public void setPortName(String portName) {
        this.portName = portName;
    }

    /**
     * Returns the service name for the endpoint.
     * 
     * @return The service name for the endpoint.
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Sets the service name for the endpoint.
     * 
     * @param serviceName The service name for the endpoint.
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

}
