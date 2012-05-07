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
package gov.hhs.fha.nhinc.filetransfer;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.entitynotificationconsumer.EntityNotificationConsumerPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.NotifyRequestType;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.*;
import java.io.File;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import org.oasis_open.docs.wsn.b_2.NotificationMessageHolderType;
import org.oasis_open.docs.wsn.b_2.NotificationMessageHolderType.Message;
import org.oasis_open.docs.wsn.b_2.Notify;
import org.oasis_open.docs.wsn.b_2.TopicExpressionType;

/**
 * 
 * @author dunnek
 */
public class CDCTimerTask {

    private static Log log = LogFactory.getLog(CDCTimerTask.class);
    private String monitorDirectory = "";
    private static final String ADAPTER_PROPERTY_FILE = "adapter";
    private static final String CDC_PROCESS_FILES_STATUS = "CDCProcessFiles";

    private static Service cachedService = null;
    private static WebServiceProxyHelper oProxyHelper = null;

    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:entitynotificationconsumer";
    private static final String SERVICE_LOCAL_PART = "EntityNotificationConsumer";
    private static final String PORT_LOCAL_PART = "EntityNotificationConsumerPortType";
    private static final String WSDL_FILE = "EntityNotificationConsumer.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:Notify";

    public void setMonitorDirectory(String value) {
        monitorDirectory = value;

    }

    public String getMonitorDirectory() {
        return monitorDirectory;

    }

    public void run() {
        try {

            // dynamic setting that determines wheter or not to process directory.
            if (CDCTimerTask.directoryProcessingEnabled()) {
                processDir(monitorDirectory);
            }

        } catch (Throwable t) {
            log.error("****** CDCTimerTask THROWABLE: " + t.getMessage(), t);

            StringWriter stackTrace = new StringWriter();
            t.printStackTrace(new PrintWriter(stackTrace));
            String sValue = stackTrace.toString();
            if (sValue.indexOf("EJBClassLoader") >= 0) {
                CDCTimer.stopTimer();
            }
        }
    }

    public static boolean directoryProcessingEnabled() {
        boolean process = false;

        try {
            process = PropertyAccessor.getInstance().getPropertyBoolean(ADAPTER_PROPERTY_FILE, CDC_PROCESS_FILES_STATUS);
        } catch (Exception ex) {
            log.error("****** CDCTimerTask THROWABLE: " + ex.getMessage(), ex);
            process = false;
        }
        return process;
    }

    private static void processDir(String dirName) {
        File dir = new File(dirName);

        String[] children = dir.list();

        if (children == null) {
            // Either dir does not exist or is not a directory
        } else {
            for (int i = 0; i < children.length; i++) {

                File child = new File(dir.getAbsolutePath() + "/" + children[i]);

                if (!child.isDirectory()) {
                    log.info("CDCTimerTask Processing File: " + child.getName());
                    String contents = getContents(child);
                    log.info("CDCTimerTask File Contents: " + contents);
                    sendNotification(contents);

                    child.delete();

                }

            }
        }

    }

    static private String getContents(File aFile) {
        StringBuilder contents = new StringBuilder();

        try {
            // use buffering, reading one line at a time
            // FileReader always assumes default encoding is OK!
            BufferedReader input = new BufferedReader(new FileReader(aFile));
            try {
                String line = null;

                while ((line = input.readLine()) != null) {
                    contents.append(line);
                    contents.append(System.getProperty("line.separator"));
                }
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            log.error("****** CDCTimerTask THROWABLE: " + ex.getMessage(), ex);
        }

        return contents.toString();
    }

    public static void sendNotification(String contents) {
        log.debug("Begin - CDCFileTransferAdapter.sendNotification() - End");
        try {
            // Create End point Dynamically
            String endpointURL = PropertyAccessor.getInstance().getProperty("adapter", "EntityNotificationConsumerURL");
            log.info("EntityNotificationConsumerURL :" + endpointURL);

            NotifyRequestType notifyRequest = new NotifyRequestType();

            // build Set Assertion
            AssertionType assertion = new AssertionType();
            notifyRequest.setAssertion(assertion);

            EntityNotificationConsumerPortType port = getPort(endpointURL, assertion);

            // build set CdcBioPackageElement
            gov.hhs.healthit.nhin.cdc.ObjectFactory factory = new gov.hhs.healthit.nhin.cdc.ObjectFactory();
            JAXBElement<byte[]> cdcBioPackageElement = factory.createCdcBioPackagePayload(Util.convertToByte(contents));
            Notify notify = new Notify();
            NotificationMessageHolderType messageHolderType = new NotificationMessageHolderType();

            // Set Message with byte array
            Message message = new Message();
            message.setAny(cdcBioPackageElement);
            messageHolderType.setMessage(message);

            // create set Simple Topic
            TopicExpressionType topic = new TopicExpressionType();
            topic.setDialect("http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple");
            messageHolderType.setTopic(topic);

            notify.getNotificationMessage().add(messageHolderType);
            notifyRequest.setNotify(notify);
            AcknowledgementType result = port.notify(notifyRequest);
            log.info("Result = " + result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        log.debug("End - CDCFileTransferAdapter.sendNotification() - End");
    }

    protected static EntityNotificationConsumerPortType getPort(String url, AssertionType assertIn) {
        EntityNotificationConsumerPortType oPort = null;
        try {
            Service oService = getService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);

            if (oService != null) {
                log.debug("subscribe() Obtained service - creating port.");
                oPort = oService.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART),
                        EntityNotificationConsumerPortType.class);

                // Initialize unsecured port
                getWebServiceProxyHelper().initializeUnsecurePort((BindingProvider) oPort, url, WS_ADDRESSING_ACTION,
                        assertIn);
            } else {
                log.error("Unable to obtain serivce - no port created.");
            }
        } catch (Throwable t) {
            log.error("Error creating service: " + t.getMessage(), t);
        }
        return oPort;
    }

    private static WebServiceProxyHelper getWebServiceProxyHelper() {
        if (oProxyHelper == null) {
            oProxyHelper = new WebServiceProxyHelper();
        }
        return oProxyHelper;
    }

    private static Service getService(String wsdl, String uri, String service) {
        if (cachedService == null) {
            try {
                cachedService = getWebServiceProxyHelper().createService(wsdl, uri, service);
            } catch (Throwable t) {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }
}
