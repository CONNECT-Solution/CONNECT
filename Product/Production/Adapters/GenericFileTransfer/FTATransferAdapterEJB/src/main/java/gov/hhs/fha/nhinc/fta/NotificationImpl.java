package gov.hhs.fha.nhinc.fta;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.NotifyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.ftaconfigmanager.FTAConfigurationHelper;
import gov.hhs.fha.nhinc.common.ftaconfigmanager.FTAConfiguration;
import gov.hhs.fha.nhinc.common.ftaconfigmanager.FTAChannel;

import org.oasis_open.docs.wsn.b_2.Notify;
import org.oasis_open.docs.wsn.b_2.NotificationMessageHolderType;
import org.oasis_open.docs.wsn.b_2.NotificationMessageHolderType.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.Writer;
import java.io.FileWriter;
import java.io.BufferedWriter;

/**
 *
 * @author dunnek
 */
public class NotificationImpl
{
    private static Log log = LogFactory.getLog(NotificationImpl.class);

        public static AcknowledgementType processNotify(Notify request)
        {
        AcknowledgementType result = new AcknowledgementType();

        try
        {
            for(NotificationMessageHolderType msgHolder : request.getNotificationMessage())
            {
                result = processNotifyMsg(msgHolder);
            }
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage(), ex);
            result.setMessage("Unable to process: " + ex.getMessage());
        }

        return result;
        }
    public static AcknowledgementType processNotify(NotifyRequestType notifyRequest)
    {
        AcknowledgementType result = new AcknowledgementType();

        try
        {
            for(NotificationMessageHolderType msgHolder :notifyRequest.getNotify().getNotificationMessage())
            {
                result = processNotifyMsg(msgHolder);
            }
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage(), ex);
            result.setMessage("Unable to process: " + ex.getMessage());
        }

        return result;
    }
    private static AcknowledgementType processNotifyMsg(NotificationMessageHolderType msgHolder )
    {
        AcknowledgementType result = new AcknowledgementType();
        String topic = null;
        if((msgHolder != null) && (msgHolder.getTopic() != null))
        {
            topic = (String) msgHolder.getTopic().getContent().get(0);
        }

        FTAConfiguration config = FTAConfigurationHelper.loadFTAConfiguration();

        FTAChannel channel = Util.getChannelByTopic(config.getOutboundChannels(), topic);

        if (channel == null)
        {
            result.setMessage("Topic: " + topic + " not defined. ");
        }
        else
        {
            result = processChannel(channel, msgHolder.getMessage());
        }

        return result;
    }
    private static AcknowledgementType processChannel(FTAChannel channel, Message msg)
    {
        AcknowledgementType result = new AcknowledgementType();

        org.w3c.dom.Element element = (org.w3c.dom.Element) msg.getAny();
        String contents = Util.unmarshalPayload(element);
        try
        {
            saveFile(contents, channel.getLocation());
            result.setMessage("Success");
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage(), ex);
            result.setMessage("unable to create text file: " + ex.getMessage());
        }
        return result;
    }
    public static void saveFile(String fileContents, String dirName) throws Exception
    {
        String fileName = generateUID().replaceAll("[-:<>*?\\/]", "") + ".txt";
        
        File f = new File(dirName, fileName);


        Writer output = null;
        f.createNewFile();
        output = new BufferedWriter(new FileWriter(f));
        output.write( fileContents );
        output.close();


    }
    private static String generateUID()
    {
        java.rmi.server.UID uid = new java.rmi.server.UID();
        return uid.toString();
    }
}
