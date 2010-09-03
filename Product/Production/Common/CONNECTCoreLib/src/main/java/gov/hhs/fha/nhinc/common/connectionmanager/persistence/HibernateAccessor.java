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

package gov.hhs.fha.nhinc.common.connectionmanager.persistence;
import java.io.FileReader;
import java.io.File;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 *
 * @author dunnek
 */
public class HibernateAccessor
{
    private static String m_sPropertyFileDir = "";

    static
    {
        m_sPropertyFileDir = PropertyAccessor.getPropertyFileLocation();
    }

    public static File getHibernateFile(String hibernateFileName)
           throws Exception
    {
        String sFileSeparator = System.getProperty("file.separator");
        checkEnvVarSet();

        File result = new File(m_sPropertyFileDir + "hibernate" +
                sFileSeparator + hibernateFileName );

        if(result == null)
        {
            throw new Exception("Unable to locate " +
                    hibernateFileName);
        }
        return result;
    }
    private static boolean checkEnvVarSet()
        throws Exception
    {
        PropertyAccessor.checkEnvVarSet();

        return true;            // We only get here if the env variable was loaded.
    }
}
