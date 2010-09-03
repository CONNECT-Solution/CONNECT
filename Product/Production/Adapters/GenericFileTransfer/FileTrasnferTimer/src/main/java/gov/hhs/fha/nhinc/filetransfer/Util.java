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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

/**
 *
 * @author dunnek
 */
public class Util 
{
    private static Log log = LogFactory.getLog(CDCTimerTask.class);
    private static final String ADAPTER_PROPERTY_FILE = "adapter";
    
    public static byte[] convertToByte(String value)
    {
        byte[] rc =null;
        
        try
        {
            rc =  value.getBytes("UTF8");
        }
        catch (Exception ex)
        {
          log.error("****** CDCTimerTask THROWABLE: " + ex.getMessage(), ex);  
        }
        
        return rc;
        
    }
    public static String convertToString(byte[] value)
    {
        String rc = "";
         
        try
        {
            rc = new String(value, 0, value.length, "UTF8");//in string
        }
        catch (Exception ex)
        {
          log.error("****** CDCTimerTask THROWABLE: " + ex.getMessage(), ex);  
        }
        return rc;
    }
    public static String getExportDirectory()
    {
        String dir ="";
        try
        {
            dir = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE, "CDCExportDir");
        }
        catch (Exception ex)
        {
            
        }
        
        return dir;
    }    
}
