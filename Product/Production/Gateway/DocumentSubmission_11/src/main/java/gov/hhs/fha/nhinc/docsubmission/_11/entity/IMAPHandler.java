package gov.hhs.fha.nhinc.docsubmission._11.entity;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;

import org.nhindirect.gateway.smtp.MessageProcessResult;
import org.nhindirect.gateway.smtp.SmtpAgent;
import org.nhindirect.gateway.smtp.SmtpAgentException;
import org.nhindirect.gateway.smtp.SmtpAgentFactory;
import org.nhindirect.stagent.AddressSource;
import org.nhindirect.stagent.NHINDAddress;
import org.nhindirect.stagent.NHINDAddressCollection;

public class IMAPHandler {

    private static SmtpAgent agent;
    private static final String SENDER = "ashish.rathee@esacinc.com";
    private static final String RECIPIENT = "ashish.rathee@esacinc.com";

    public static void main(String args[]) {
        Folder inbox;
        Store store;
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        try {
            Session session = Session.getDefaultInstance(props, null);
            store = session.getStore("imaps");
            store.connect("imap.gmail.com", "ashish.rathee@esacinc.com", "xxx");
            inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_ONLY);
            FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
            Message messages[] = inbox.search(ft);
            Message message[] = reverseMessageOrder(messages);

            for (int i = 0; i < 1; i++) {
                Format formatter = new SimpleDateFormat("MM/dd/yy");
                String sentDate = formatter.format(message[i].getSentDate());
                String todaysDate = formatter.format(new Date());
                if (sentDate.equalsIgnoreCase(todaysDate)) {
                    String sender = message[i].getFrom()[0].toString();
                    if (sender.indexOf("ashish.rathee@esacinc.com") != -1) {
                        decryptMessage((MimeMessage) message[i]);
                        Multipart multipart = (Multipart) message[i].getContent();
                        System.out.println("Multipart Count >>" + multipart.getCount());
                        for (int x = 1; x < multipart.getCount(); x++) {
                            BodyPart bodyPart = multipart.getBodyPart(x);
                            String disposition = bodyPart.getDisposition();
                            if (disposition != null && (disposition.equals(BodyPart.ATTACHMENT))) {
                                DataHandler handler = bodyPart.getDataHandler();
                            } else {
                                System.out.println("   Mail Body   ");
                                System.out.println(bodyPart.getContent());
                            }
                        }
                    }
                }
            }
            inbox.close(true);
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void decryptMessage(MimeMessage message) {
        String configURLParam = "http://localhost:8081/config-service/ConfigurationService";
        URL configURL = null;
        try {
            configURL = new URL(configURLParam);
        } catch (MalformedURLException ex) {
            System.out.println("Invalid configuration URL:" + ex.getMessage());

        }
        try {
            agent = SmtpAgentFactory.createAgent(configURL);
        } catch (SmtpAgentException e) {
            System.out.println("Failed to create the SMTP agent: " + e.getMessage());
        }

        try {
            Address recipAddr = new InternetAddress(RECIPIENT);
            NHINDAddressCollection recipients = new NHINDAddressCollection();
            recipients.add(new NHINDAddress(recipAddr.toString(), (AddressSource) null));
            InternetAddress senderAddr = new InternetAddress(SENDER);
            NHINDAddress sender = new NHINDAddress(senderAddr, AddressSource.From);
            org.nhindirect.stagent.mail.Message msg = new org.nhindirect.stagent.mail.Message(message);
            // IncomingMessage theCreateMessage = new IncomingMessage(msg, recipients, sender);
            MessageProcessResult result = agent.processMessage(message, recipients, sender);
            System.out.println("result >>" + result);
        } catch (Exception ex) {
            System.out.println("Invalid configuration URL:" + ex.getMessage());

        }
    }

    private static Message[] reverseMessageOrder(Message[] messages) {
        Message revMessages[] = new Message[messages.length];
        int i = messages.length - 1;
        for (int j = 0; j < messages.length; j++, i--) {
            revMessages[j] = messages[i];
        }
        return revMessages;
    }
}
