/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.fta;

import gov.hhs.fha.nhinc.common.ftaconfigmanager.FTAConfiguration;
import gov.hhs.fha.nhinc.common.ftaconfigmanager.FTAChannel;
import  gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import javax.xml.ws.BindingProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import java.io.File;
import java.io.StringWriter;
import java.io.PrintWriter;

import gov.hhs.fha.nhinc.entitynotificationconsumersecured.EntityNotificationConsumerSecured;
import gov.hhs.fha.nhinc.entitynotificationconsumersecured.EntityNotificationConsumerSecuredPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import java.util.Map;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;

import org.oasis_open.docs.wsn.b_2.Notify;

import org.oasis_open.docs.wsn.b_2.NotificationMessageHolderType;
import org.oasis_open.docs.wsn.b_2.TopicExpressionType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.entitynotificationconsumer.EntityNotificationConsumer;
import gov.hhs.fha.nhinc.entitynotificationconsumer.EntityNotificationConsumerPortType;

/**
 *
 * @author dunnek
 */
public class FTATimerTask  {
    private static Log log = LogFactory.getLog(FTATimerTask.class);
    private FTAConfiguration ftaConfig = null;

    public void setConfiguration(FTAConfiguration config)
    {
        ftaConfig = config;
    }
    public FTAConfiguration getConfiguration()
    {
        return ftaConfig;
    }
    public void run()
    {
        try
        {
            if(ftaConfig == null)
            {
                log.error("FTA Configuration not loaded.");;
            }
            else
            {
                for(FTAChannel channel : ftaConfig.getInboundChannels())
                {
                    processDir(channel.getLocation(), channel.getTopic());
                }
            }
        }
        catch (Throwable t)
        {
            log.error("****** FTATimerTask THROWABLE: " + t.getMessage(), t);

            StringWriter stackTrace = new StringWriter();
            t.printStackTrace(new PrintWriter(stackTrace));
            String sValue = stackTrace.toString();
            if (sValue.indexOf("EJBClassLoader") >= 0)
            {
                FTATimer.stopTimer();
            }
 
        }
    }

     private static void processDir(String dirName, String topic)
    {
        File dir = new File(dirName);

        String[] children = dir.list();

        if (children == null)
        {
        // Either dir does not exist or is not a directory
        }
        else
        {
            for (int i=0; i<children.length; i++)
            {

                File child = new File(dir.getAbsolutePath() + "/" + children[i]);

                if (! child.isDirectory())
                {
                    log.info("FTATimerTask Processing File: " + child.getName());
                    String contents = Util.getFileContents(child);
                    log.debug("FTATimerTask File Contents: " + contents);
                    sendNotification(contents, topic);

                    child.delete();

                }

            }
        }

    }
  private static void sendNotification(String contents, String topic)
  {
        try { // Call Web Service Operation
            EntityNotificationConsumer service = new EntityNotificationConsumer();
            EntityNotificationConsumerPortType port = service.getEntityNotificationConsumerPortSoap();


           String endpointURL = PropertyAccessor.getProperty("adapter", "EntityNotificationConsumerURL");
           //String endpointURL = "http://localhost:8088/mockEntityNotificationConsumerBindingSoap11";
           log.info("EntitySubscriptionURL :"+endpointURL);
           ((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);


            // TODO initialize WS operation arguments here
            gov.hhs.fha.nhinc.common.nhinccommonentity.NotifyRequestType notifyRequest = new gov.hhs.fha.nhinc.common.nhinccommonentity.NotifyRequestType();
            NotificationMessageHolderType messageHolder = new NotificationMessageHolderType();


            NotificationMessageHolderType.Message message = new NotificationMessageHolderType.Message();
            Notify notify = new Notify();

            message.setAny(Util.marshalPayload(contents));

            messageHolder.setMessage(message);

            //TopicExpressionType topicExpression = new TopicExpressionType();
            //topicExpression.setDialect("http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple");
            //topicExpression.getContent().add(topic);

            TopicExpressionType topicExpression;
            gov.hhs.fha.nhinc.hiem.dte.marshallers.TopicMarshaller marshaller;
            marshaller = new gov.hhs.fha.nhinc.hiem.dte.marshallers.TopicMarshaller();

            //topic = "<wsnt:TopicExpression xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2' Dialect='http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple' xmlns:nhinc='urn:gov.hhs.fha.nhinc.hiemtopic'>nhinc:testTopic</wsnt:TopicExpression>";
            log.info("topic :"+topic);
            topicExpression =  (TopicExpressionType) marshaller.unmarshal(topic);

            messageHolder.setTopic(topicExpression);

            notify.getNotificationMessage().add(messageHolder);

            notifyRequest.setAssertion(Util.createAssertion());

            notifyRequest.setNotify(notify);

            AcknowledgementType result = port.notify(notifyRequest);

            log.info("Result = "+result);
        } catch (Exception ex) {
            log.error(ex.getMessage(),ex);
        }
  }
  private static void sendNotificationSecured(String contents, String topic)
  {
        try { // Call Web Service Operation
            EntityNotificationConsumerSecured service = new EntityNotificationConsumerSecured();
            EntityNotificationConsumerSecuredPortType port = service.getEntityNotificationConsumerSecuredPortSoap();


           String endpointURL = PropertyAccessor.getProperty("adapter", "EntityNotificationConsumerURL");
           //String endpointURL = "http://localhost:8088/mockEntityNotificationConsumerBindingSoap11";
           log.info("EntitySubscriptionURL :"+endpointURL);
           ((BindingProvider)port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);


            // TODO initialize WS operation arguments here
            NotificationMessageHolderType messageHolder = new NotificationMessageHolderType();


            NotificationMessageHolderType.Message message = new NotificationMessageHolderType.Message();
            Notify notify = new Notify();

            message.setAny(Util.marshalPayload(contents));
            
            messageHolder.setMessage(message);

            TopicExpressionType topicExpression;
            gov.hhs.fha.nhinc.hiem.dte.marshallers.TopicMarshaller marshaller;
            marshaller = new gov.hhs.fha.nhinc.hiem.dte.marshallers.TopicMarshaller();

            //topic = "<wsnt:TopicExpression xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2' Dialect='http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple' xmlns:nhinc='urn:gov.hhs.fha.nhinc.hiemtopic'>nhinc:testTopic</wsnt:TopicExpression>";
            log.info("topic :"+topic);
            topicExpression =  (TopicExpressionType) marshaller.unmarshal(topic);

            messageHolder.setTopic(topicExpression);

            notify.getNotificationMessage().add(messageHolder);
            

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            AssertionType assertion = Util.createAssertion();
            
            Map requestContext = tokenCreator.CreateRequestContext(assertion, endpointURL, NhincConstants.PAT_CORR_ACTION);
			((BindingProvider) port).getRequestContext().putAll(requestContext);

            AcknowledgementType result = port.notify(notify);

            log.info("Result = "+result);
        } catch (Exception ex) {
            log.error(ex.getMessage(),ex);
        }
  }

}
