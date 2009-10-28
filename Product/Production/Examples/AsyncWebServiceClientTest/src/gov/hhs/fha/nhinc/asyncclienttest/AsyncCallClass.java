/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.asyncclienttest;

/**
 *
 * @author westberg
 */
public class AsyncCallClass
{
    public void testMethod()
    {

        try
        { // Call Web Service Operation(async. callback)
            gov.hhs.fha.nhinc.adapterpip.AdapterPIP service = new gov.hhs.fha.nhinc.adapterpip.AdapterPIP();
            gov.hhs.fha.nhinc.adapterpip.AdapterPIPPortType port = service.getAdapterPIPPortSoap11();
            // TODO initialize WS operation arguments here
            gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType retrievePtConsentByPtIdRequest = new gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType();
            javax.xml.ws.AsyncHandler<gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType> asyncHandler = new javax.xml.ws.AsyncHandler<gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType>()
            {
                public void handleResponse(javax.xml.ws.Response<gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType> response)
                {
                    try
                    {
                        // TODO process asynchronous response here
                        System.out.println("Result = "+ response.get());
                    }
                    catch(Exception ex)
                    {
                        // TODO handle exception
                    }
                }
            };
            java.util.concurrent.Future<? extends java.lang.Object> result = port.retrievePtConsentByPtIdAsync(retrievePtConsentByPtIdRequest, asyncHandler);
            while(!result.isDone())
            {
                // do something
                Thread.sleep(100);
            }
        }
        catch (Exception ex)
        {
            // TODO handle custom exceptions here
        }

    }
}
