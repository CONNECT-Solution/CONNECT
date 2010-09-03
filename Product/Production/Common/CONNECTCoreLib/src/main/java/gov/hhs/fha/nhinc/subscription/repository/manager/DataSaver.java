/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.subscription.repository.manager;

import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionRecordList;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Performs file operations for the subscription repository
 * 
 * @author dunnek
 */
class DataSaver
{
    private static Log log = LogFactory.getLog(DataSaver.class);

    /**
     * Save a subscription list using the provided file name
     * 
     * @param list Subscription list
     * @param file File name to use when saving the file
     */
    public void saveList(SubscriptionRecordList list, String file)
    {
        log.info("Saving " + list.size() + " items(s)");

        // Create output stream.
        log.info("Filename=" + file);

        FileOutputStream fos;

        try
        {
            fos = new FileOutputStream(file);

            try
            {
                // Create XML encoder.
                XMLEncoder xenc = new XMLEncoder(fos);
                try
                {
                    // Write object.
                    xenc.writeObject(list);
                    xenc.flush();
                }
                finally
                {
                    xenc.close();
                }
            }
            finally
            {
                try
                {
                    fos.close();
                }
                catch (IOException ex)
                {
                    log.info("Could not close " + file + ": " + ex.getMessage());
                }
            }
        }
        catch (FileNotFoundException ex)
        {
            log.error("Error accessing storage " + file + ": " + ex.getMessage());
        }
        log.info("Save complete");
    }

    /**
     * Load a subscription list from the provided file name
     * 
     * @param fileName Name of the file containing the subscription list
     * @return Subscription list
     */
    public SubscriptionRecordList loadList(String fileName)
    {
        log.info("Loading list");

        SubscriptionRecordList subscriptionListlist = new SubscriptionRecordList();
        File file;

        // Create input stream.
        log.info("Filename=" + fileName);
        log.info("user.dir: " + System.getProperty("user.dir"));

        file = new File(fileName);
        XMLDecoder xdec = null;
        FileInputStream fis = null;

        try
        {
            if (!file.exists())
            {
                //fileName does not exist, so create it
                //for testing purposes
                //for testing purposes
                file.createNewFile();
                saveList(new SubscriptionRecordList(), fileName);
            }

            fis = new FileInputStream(fileName);

            // Create XML decode.
            xdec = new XMLDecoder(fis);

            // Write object.
            log.info("Loading object");
            Object obj = xdec.readObject();
            if (obj instanceof SubscriptionRecordList)
            {
                subscriptionListlist = (SubscriptionRecordList) obj;
            }
            else
            {
                log.debug("Loaded object was not of expected type - SubscriptionListlist - default used");
            }

        }
        catch (IOException ex)
        {
            log.error("Error accessing storage " + fileName + ": " + ex.getMessage());
        }
        finally
        {
            try
            {
                xdec.close();
                fis.close();
            }
            catch (IOException ex)
            {
                log.info("Unable to close streams: " + ex.getMessage());
            }
        }

        log.info("Loaded " + subscriptionListlist.size() + " subscription(s)");
        return subscriptionListlist;
    }
}
