package com.example.soap;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * SOAP Sending application based on a suggested approach found at
 * http://www.ibm.com/developerworks/xml/library/x-soapcl/
 *
 * @author Neil Webb
 */
public class RawSoapSender
{

    public static void main(String[] args) throws Exception
    {

        if (args.length < 2)
        {
            System.err.println("Usage:  java RawSoapSender " +
                    "http://soapURL soapEnvelopefile.xml" +
                    " [SOAPAction]");
            System.err.println("SOAPAction is optional.");
            System.exit(1);
        }

        String SOAPUrl = args[0];
        String xmlFile2Send = args[1];

        String SOAPAction = "";
        if (args.length > 2)
        {
            SOAPAction = args[2];
        }

        String response = sendMessage(SOAPUrl, xmlFile2Send, SOAPAction);
        System.out.println(response);

    }

    public static String sendMessage(String endpointAddress, String fileName, String soapAction) throws Exception
    {

        // Create the connection where we're going to send the file.
        URL url = new URL(endpointAddress);
        URLConnection connection = url.openConnection();
        HttpURLConnection httpConn = (HttpURLConnection) connection;
        String response = null;

        // Open the input file. After we copy it to a byte array, we can see
        // how big it is so that we can set the HTTP Cotent-Length
        // property. (See complete e-mail below for more on this.)

        FileInputStream fin = new FileInputStream(fileName);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        // Copy the SOAP file to the open connection.
        copy(fin, bout);
        fin.close();

        byte[] b = bout.toByteArray();
        System.out.println("Sending SOAP request...");
        System.out.println(new String(b));

        // Set the appropriate HTTP parameters.
        httpConn.setRequestProperty("Content-Length",
                String.valueOf(b.length));
        httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        if(soapAction != null)
        {
            httpConn.setRequestProperty("SOAPAction", soapAction);
        }
        httpConn.setRequestMethod("POST");
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);

        try
        {
            // Everything's set up; send the XML that was read in to b.
            OutputStream out = httpConn.getOutputStream();
            out.write(b);
            out.close();
            System.out.println("Request has been written out");
            // Read the response and write it to standard out.

            InputStreamReader isr =
                    new InputStreamReader(httpConn.getInputStream());
            BufferedReader in = new BufferedReader(isr);

            String inputLine;

            StringBuffer sb = new StringBuffer();
            while ((inputLine = in.readLine()) != null)
            {
                sb.append(inputLine + "\r\n");
            }
            response = sb.toString();

            in.close();
        }
        catch(Exception e)
        {
            System.out.println("Capturing error message");
            InputStreamReader esr =
                    new InputStreamReader(httpConn.getErrorStream());
            BufferedReader err = new BufferedReader(esr);
            StringBuffer eb = new StringBuffer();
            String errorLine;
            while((errorLine = err.readLine()) != null)
            {
                eb.append(errorLine + "\r\n");
            }
            err.close();
            System.out.println("Error response...");
            System.out.println(eb.toString());
            throw e;
        }


        return response;
    }

    // copy method from From E.R. Harold's book "Java I/O"
    public static void copy(InputStream in, OutputStream out)
            throws IOException
    {

        // do not allow other threads to read from the
        // input or write to the output while copying is
        // taking place

        synchronized (in)
        {
            synchronized (out)
            {

                byte[] buffer = new byte[256];
                while (true)
                {
                    int bytesRead = in.read(buffer);
                    if (bytesRead == -1)
                    {
                        break;
                    }
                    out.write(buffer, 0, bytesRead);
                }
            }
        }
    }
}
