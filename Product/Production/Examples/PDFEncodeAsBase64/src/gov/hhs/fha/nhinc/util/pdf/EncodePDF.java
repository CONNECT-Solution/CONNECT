package gov.hhs.fha.nhinc.util.pdf;

import gov.hhs.fha.nhinc.callback.Base64Coder;
import gov.hhs.fha.nhinc.util.StringUtil;

import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.CharBuffer;

/**
 *  This class encodes a PDF into a Base64 String.
 *
 *  @author Les Westberg
 */
public class EncodePDF
{

    /**
     * This method reads the entire contents of a text file and returns the contents in
     * a string variable.
     *
     * @param sFileName The path and location of the text file.
     * @return The contents that was read in.
     */
    public static byte[] readPDFFile(String sFileName)
            throws Exception
    {
        byte[] baPDF = null;
        FileInputStream fisPDFFile = null;
        ByteArrayOutputStream bosPDF = new ByteArrayOutputStream();

        try
        {
            fisPDFFile = new FileInputStream(sFileName);
            byte baBuf[] = new byte[1024];
            int iLen = 0;
            while ((iLen = fisPDFFile.read(baBuf, 0, 1024)) != -1)
            {
                bosPDF.write(baBuf, 0, iLen);
            }

            baPDF = bosPDF.toByteArray();
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to read pdf file: " + sFileName + ". Error: " + e.getMessage();
            throw new Exception(sErrorMessage, e);
        }
        finally
        {
            if (bosPDF != null)
            {
                try
                {
                    bosPDF.close();
                }
                catch (Exception e)
                {
                }
            }
            if (fisPDFFile != null)
            {
                try
                {
                    fisPDFFile.close();
                }
                catch (Exception e)
                {
                }
            }
        }

        return baPDF;
    }

    /**
     * Main method.
     * 
     * @param args
     */
    public static void main(String args[])
    {
        // TODO:  Put the location of the PDF file here...
        //------------------------------------------------
        String sFileName = "C:\\projects\\nhinc_non_svn\\docs\\PDFExample\\testPDFDocument.pdf";
        byte[] baPDF = null;
        String sBase64EncodePDF = "";

        try
        {
            baPDF = readPDFFile(sFileName);
            char[] caBase64Encoded = Base64Coder.encode(baPDF);
            sBase64EncodePDF = new String(caBase64Encoded);
            System.out.println(sBase64EncodePDF);

        } catch (Exception e)
        {
            System.out.println("Unexpected exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
