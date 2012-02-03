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
package gov.hhs.fha.nhinc.service;

import gov.hhs.fha.nhinc.tools.ws.processor.generator.ServicePropertyLoader;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class ServiceUtil
{

    private Log log = null;

    public ServiceUtil()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected String getWsdlPath()
    {
        return ServicePropertyLoader.getBaseWsdlPath();
    }

    protected Service constructService(String wsdlURL, String namespaceURI, String serviceLocalPart) throws MalformedURLException
    {
        return Service.create(new URL(wsdlURL), new QName(namespaceURI, serviceLocalPart));
    }

    public Service createService(String wsdlFile, String namespaceURI, String serviceLocalPart) throws MalformedURLException
    {
        Service service = null;
        log.debug("Begin createService");

        if((wsdlFile == null) || (wsdlFile.length() < 1))
        {
            log.error("WSDL file name is required.");
        }
        else if((namespaceURI == null) || (namespaceURI.length() < 1))
        {
            log.error("Namespace URI is required.");
        }
        else if((serviceLocalPart == null) || (serviceLocalPart.length() < 1))
        {
            log.error("Service local part name is required.");
        }
        else
        {
            final String wsdlPath = getWsdlPath();
            if((wsdlPath != null) && (wsdlPath.length() > 0))
            {
                String wsdlURL = wsdlPath + wsdlFile;
                log.debug("Creating service using the URL: " + wsdlURL);
                service = constructService(wsdlURL, namespaceURI, serviceLocalPart);
            }
            else
            {
                log.error("Unable to retrieve the WSDL path.");
            }
        }

        log.debug("End createService");
        return service;
    }

}
