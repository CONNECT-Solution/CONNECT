//********************************************************************
// FILE: GetShell.java
//
// 2009 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: GetShell - access for returning the command shell
//                         environment variables.
//
// LIMITATIONS: None
//
// SOFTWARE HISTORY: //
//> 22OCT09 D. Cannon
// Initial Coding.
//<
//********************************************************************
package connectuddimodifier;

import java.io.*;

/**
 *
 * @author dcannon
 */
public class GetShell
{

// Read the output of the echo
    public static String getEnvironmentVariable(String keyWord)
    {
        String value;
        String command = getShell() + " /c echo %" + keyWord + "%";

        try
        {
            Process proc = Runtime.getRuntime().exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            value = in.readLine();

        } catch (Exception e)
        {
            System.out.println(e);
            value = "";
        }

        return value;
    }

// Get shell program depending on the plateform
    private static String getShell()
    {
        String shell;
        String osName = System.getProperty("os.name");

        if (osName.equals("Windows 98") || osName.equals("Windows 95"))
        {
            shell = "command.com";
        } else
        {
            shell = "cmd.exe";
        }

        return shell;
    }
}