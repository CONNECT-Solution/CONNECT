/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.properties;

import java.io.File;
import java.util.Properties;
import java.io.FileWriter;

/**
 * This class is used to manage a property file programmtically.
 * 
 * @author Les Westberg
 */
public class PropertyFileManager
{
    /**
     * This saves out the properties to the specified property file.
     * If the file already exists, it is replaced by the specified one.
     * If it does not exist, it is created.
     * 
     * @param sPropertyFile The name of the property file without the ".properties" extension.
     * @param oProps The contents to write out as the property file.
     * @throws PropertyAccessException This exception is thrown if there are any errors.
     */
    public static void writePropertyFile (String sPropertyFile, Properties oProps)
        throws PropertyAccessException
    {
        if ((sPropertyFile == null) ||
            (sPropertyFile.length() <= 0))
        {
            throw new PropertyAccessException("writePropertyFile called with no property file name.");
        }

        if (oProps == null)
        {
            throw new PropertyAccessException("writePropertyFile called with no property file.");
        }
        
        String sPropFile = PropertyAccessor.getPropertyFileLocation() + sPropertyFile + ".properties";
        FileWriter fwPropFile = null;
        Exception eError = null;
        String sErrorMessage = "";
        
        try
        {
            fwPropFile = new FileWriter(sPropFile);
            oProps.store(fwPropFile, "");
        }
        catch (Exception e)
        {
            sErrorMessage = "Failed to store property file: " +
                             sPropFile + ".  Error: " + e.getMessage();
            eError = e;
        }
        finally
        {
            if (fwPropFile != null)
            {
                try
                {
                    fwPropFile.close();
                }
                catch (Exception e)
                {
                    sErrorMessage = "Failed to close property file: " +
                                     sPropFile + ".  Error: " + e.getMessage();
                    eError = e;
                }
            }
        }
        
        if (eError != null)
        {
            throw new PropertyAccessException(sErrorMessage, eError);
        }
    }
    
    /**
     * Delete the specified property file.
     * 
     * @param sPropertyFile The file to be deleted.  This is the name of the property file
     *                      without the ".properties" extension.  It must be located in the
     *                      configured properties directory.
     * @throws gov.hhs.fha.nhinc.properties.PropertyAccessException This exception is thrown if 
     *                                                              there is an error.
     */
    public static void deletePropertyFile(String sPropertyFile)
        throws PropertyAccessException
    {
        String sPropFile = PropertyAccessor.getPropertyFileLocation() + sPropertyFile + ".properties";
        File fPropFile = new File(sPropFile);
        
        try
        {
            if (fPropFile.exists())
            {
                fPropFile.delete();
            }
        }
        catch (Exception e)
        {
            throw new PropertyAccessException("Failed to delete file: " + sPropFile + 
                                              ".  Error: " + e.getMessage(), e);
        }
        
    }
}
