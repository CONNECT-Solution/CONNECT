/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2012(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
/*
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 *
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 *
 * All other rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.saml.creation;

import java.util.HashMap;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class creates a HashMap of all known UserRolesSNOMED to their assigned SNOMED code.
 * @author rhaslam
 */
 public class UserRolesSNOMED {

    private static HashMap<String,String> nameToCode = new HashMap<String,String>(20);


    public static final String SNOMED_USER_ROLES_CODE_SYSTEM = "2.16.840.1.113883.6.96";
    public static final String SNOMED_USER_ROLES_CODE_SYSTEM_NAME = "SNOMED_CT";
    public static final String SNOMED_MAPPING_ERROR_CODE = "223366009";
    public static final String SNOMED_MAPPRING_ERROR_ROLE_NAME = "Healthcare professional";

    private static final String USER_ROLE_PROPERTIES = "UserRoleOptions";
    private static Log log = LogFactory.getLog(UserRolesSNOMED.class);
    private static final String sFailedPathMessage = "Unable to determine the path to the configuration files.  " +
            "Please make sure that the runtime nhinc.properties.dir system property is set to the absolute location " +
            "of your CONNECT configuration files.";

    private static boolean userRolesInitialized = false;

    private static boolean initializeUserRoles()
    {
        String sPropertyFileDirAbsolutePath = System.getProperty("nhinc.properties.dir");

        if(sPropertyFileDirAbsolutePath == null) {
            log.warn("The runtime property nhinc.properties.dir is not set!!!  " +
                    "Looking for the environment variable NHINC_PROPERTIES_DIR as a fall back.  " +
                    "Please set the runtime nhinc.properties.dir system property in your configuration files.");
            sPropertyFileDirAbsolutePath = System.getenv(NhincConstants.NHINC_PROPERTIES_DIR);
            if(sPropertyFileDirAbsolutePath == null) {
                  log.error(sFailedPathMessage);
            }
        }

        Scanner scanner = null;
        try {
            //
            // Set it up so that we always have a "/" at the end - in case
            //------------------------------------------------------------
            if (sPropertyFileDirAbsolutePath.endsWith(File.separator) == false) {
                sPropertyFileDirAbsolutePath = sPropertyFileDirAbsolutePath + File.separator;
            }


            String userRoleFileName = sPropertyFileDirAbsolutePath + USER_ROLE_PROPERTIES + ".properties";
            scanner = new Scanner(new FileInputStream(userRoleFileName), "utf-8");
            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                String[] values = line.split("=");
                if (values.length != 2)
                {
                    log.error("Invalid line found in file " + USER_ROLE_PROPERTIES + ". This line will be ignored: " + line);
                }
                nameToCode.put(values[1].trim(), values[0].trim());
            }
        }
        catch (FileNotFoundException e)
        {
            log.error("Could not locate the file " + USER_ROLE_PROPERTIES +". ,System will respond with only the default role = " + SNOMED_MAPPRING_ERROR_ROLE_NAME);
        }
        finally
        {
            scanner.close();
        }
        return true;
    }

    public static String[] getRoleNameAndCode(String name)
    {
        if (!userRolesInitialized)
        {
            userRolesInitialized = initializeUserRoles();
        }
        String[] nameAndCode = new String[2];

        String code = (String)nameToCode.get(name);
        if (code == null)
        {
            nameAndCode[0] = SNOMED_MAPPRING_ERROR_ROLE_NAME;
            nameAndCode[1] = SNOMED_MAPPING_ERROR_CODE;
        }
        else
        {
            nameAndCode[0] = name;
            nameAndCode[1] = code;
        }
        return nameAndCode;
    }

    /* For local testing only */
	public static void main(String[] args)
	{
	    String[] values = getRoleNameAndCode("Medical doctor");
	    System.out.println("THe code for Medical doctor is " + values[1]);
	    values = getRoleNameAndCode("Fisherman");
	    System.out.println("The code for Fisherman is " + values[1]);
	
	}

}