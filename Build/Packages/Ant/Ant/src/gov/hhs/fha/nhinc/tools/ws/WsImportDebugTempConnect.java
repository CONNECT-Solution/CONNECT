/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.tools.ws;

import java.util.ArrayList;

/**
 *
 * @author westberg
 */
public class WsImportDebugTempConnect
{
    public static void main(String[] args) throws Throwable
    {
        String saMyArgs[] = new String[0];
        ArrayList<String> saMyArgsArrayList = new ArrayList<String>();

        saMyArgsArrayList.add("-d");
        saMyArgsArrayList.add("c:\\temp\\jaxws\\output");
        saMyArgsArrayList.add("-Xnocompile");
        saMyArgsArrayList.add("-verbose");
        saMyArgsArrayList.add("c:\\projects\\nhinc\\Current\\Product\\Production\\Common\\Interfaces\\src\\wsdl\\AdapterPIP.wsdl");

        saMyArgs = (String[]) saMyArgsArrayList.toArray(new String[0]);
        int iResult = WsImportConnect.doMain(saMyArgs);
        
        System.out.println("Result = " + iResult);

    }
}
