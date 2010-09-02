/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.filetransfer;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.entitynotificationconsumer.EntityNotificationConsumer;
import gov.hhs.fha.nhinc.entitynotificationconsumer.EntityNotificationConsumerPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.NotifyRequestType;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.*;
import java.io.File;
import javax.xml.bind.JAXBElement;
import javax.xml.ws.BindingProvider;
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

    public void setMonitorDirectory(String value) {
        monitorDirectory = value;

    }

    public String getMonitorDirectory() {
        return monitorDirectory;

    }

    public void run() {
        try {

            //dynamic setting that determines wheter or not to process directory.
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
            process = PropertyAccessor.getPropertyBoolean(ADAPTER_PROPERTY_FILE, CDC_PROCESS_FILES_STATUS);
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
            //use buffering, reading one line at a time
            //FileReader always assumes default encoding is OK!
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
        try { // Call Web Service Operation
            EntityNotificationConsumer service = new EntityNotificationConsumer();
            EntityNotificationConsumerPortType port = service.getEntityNotificationConsumerPortSoap();
            //Create End point Dynamically
            String endpointURL = PropertyAccessor.getProperty("adapter", "EntityNotificationConsumerURL");
            log.info("EntityNotificationConsumerURL :" + endpointURL);
            ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);

            NotifyRequestType notifyRequest = new NotifyRequestType();

            //build Set Assertion
            AssertionType assertion = new AssertionType();
            notifyRequest.setAssertion(assertion);

            //build set CdcBioPackageElement
            gov.hhs.healthit.nhin.cdc.ObjectFactory factory = new gov.hhs.healthit.nhin.cdc.ObjectFactory();
            JAXBElement<byte[]> cdcBioPackageElement = factory.createCdcBioPackagePayload(Util.convertToByte(contents));
            Notify notify = new Notify();
            NotificationMessageHolderType messageHolderType = new NotificationMessageHolderType();

            //Set Message with byte array
            Message message = new Message();
            message.setAny(cdcBioPackageElement);
            messageHolderType.setMessage(message);

            //create set Simple Topic
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
}
