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
package gov.hhs.fha.nhinc.fta;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.NotifyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.ftaconfigmanager.FTAConfigurationHelper;
import gov.hhs.fha.nhinc.common.ftaconfigmanager.FTAConfiguration;
import gov.hhs.fha.nhinc.common.ftaconfigmanager.FTAChannel;

import org.oasis_open.docs.wsn.b_2.Notify;
import org.oasis_open.docs.wsn.b_2.NotificationMessageHolderType;
import org.oasis_open.docs.wsn.b_2.NotificationMessageHolderType.Message;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.Writer;
import java.io.FileWriter;
import java.io.BufferedWriter;

/**
 * 
 * @author dunnek
 */
public class NotificationImpl {
    private static final Logger LOG = Logger.getLogger(NotificationImpl.class);

    public static AcknowledgementType processNotify(Notify request) {
        AcknowledgementType result = new AcknowledgementType();

        try {
            for (NotificationMessageHolderType msgHolder : request.getNotificationMessage()) {
                result = processNotifyMsg(msgHolder);
            }
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            result.setMessage("Unable to process: " + ex.getMessage());
        }

        return result;
    }

    public static AcknowledgementType processNotify(NotifyRequestType notifyRequest) {
        AcknowledgementType result = new AcknowledgementType();

        try {
            for (NotificationMessageHolderType msgHolder : notifyRequest.getNotify().getNotificationMessage()) {
                result = processNotifyMsg(msgHolder);
            }
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            result.setMessage("Unable to process: " + ex.getMessage());
        }

        return result;
    }

    private static AcknowledgementType processNotifyMsg(NotificationMessageHolderType msgHolder) {
        AcknowledgementType result = new AcknowledgementType();
        String topic = null;
        if ((msgHolder != null) && (msgHolder.getTopic() != null)) {
            topic = (String) msgHolder.getTopic().getContent().get(0);
        }

        FTAConfiguration config = FTAConfigurationHelper.loadFTAConfiguration();

        FTAChannel channel = Util.getChannelByTopic(config.getOutboundChannels(), topic);

        if (channel == null) {
            result.setMessage("Topic: " + topic + " not defined. ");
        } else {
            result = processChannel(channel, msgHolder.getMessage());
        }

        return result;
    }

    private static AcknowledgementType processChannel(FTAChannel channel, Message msg) {
        AcknowledgementType result = new AcknowledgementType();

        org.w3c.dom.Element element = (org.w3c.dom.Element) msg.getAny();
        String contents = Util.unmarshalPayload(element);
        try {
            saveFile(contents, channel.getLocation());
            result.setMessage("Success");
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            result.setMessage("unable to create text file: " + ex.getMessage());
        }
        return result;
    }

    public static void saveFile(String fileContents, String dirName) throws Exception {
        String fileName = generateUID().replaceAll("[-:<>*?\\/]", "") + ".txt";

        File f = new File(dirName, fileName);

        Writer output = null;
        f.createNewFile();
		
	    try {
            output = new BufferedWriter(new FileWriter(f));
            output.write(fileContents);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (Exception e) {
                    LOG.error("Failed to close file : " + fileName + " : " + e.getMessage());
                }
            }
        }
    }

    private static String generateUID() {
        java.rmi.server.UID uid = new java.rmi.server.UID();
        return uid.toString();
    }
}
