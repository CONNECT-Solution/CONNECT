//
// Non-Export Controlled Information
//
//####################################################################
//## The MIT License
//## 
//## Copyright (c) 2010 Harris Corporation
//## 
//## Permission is hereby granted, free of charge, to any person
//## obtaining a copy of this software and associated documentation
//## files (the "Software"), to deal in the Software without
//## restriction, including without limitation the rights to use,
//## copy, modify, merge, publish, distribute, sublicense, and/or sell
//## copies of the Software, and to permit persons to whom the
//## Software is furnished to do so, subject to the following conditions:
//## 
//## The above copyright notice and this permission notice shall be
//## included in all copies or substantial portions of the Software.
//## 
//## THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//## EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
//## OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//## NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
//## HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
//## WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//## FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
//## OTHER DEALINGS IN THE SOFTWARE.
//## 
//####################################################################
//********************************************************************
// FILE: InterProcessSocketProtocol.java
//
// Copyright (C) 2010 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: InterProcessSocketProtocol.java
//
// LIMITATIONS: None
//
// SOFTWARE HISTORY:
//
//
//Feb 24 2010 PTR xxx R.Robinson   Initial Coding.
//
//********************************************************************
package gov.hhs.fha.nhinc.lift.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author rrobin20
 *
 */
public class InterProcessSocketProtocol {
	/**
	 * Sends data by appending the length of the data, then a space, then the
	 * data itself.
	 * @param data
	 * @param out
	 * @throws IOException
	 */
	public static void sendData(String data, OutputStream out) throws IOException
	{
		StringBuffer str = new StringBuffer();
		
		str.append(data.length());
		str.append(" ");
		str.append(data);
		
		out.write(str.toString().getBytes());
	}
	
	/**
	 * Reads data from the stream assuming that it has been written as the send
	 * method above.
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String readData(InputStream in) throws IOException
	{
		StringBuffer str = new StringBuffer();
		
		int i = in.read();
		while(i != ' ' && i != -1)
		{
			str.append((char)i);
			
			i = in.read();
		}
		
		if(i == -1)
			return null;
		
		int length;
		
		try
		{
			length = Integer.parseInt(str.toString());
		}catch(NumberFormatException e){
			return null;
		}
		
		// TODO should log how many bytes expected/trying to read.
		byte[] data = new byte[length];
		
		int off = 0;
		while(off < length)
		{
			// Read more bytes into the array while not all expected bytes have been read
			int numRead = in.read(data, off, length-off);
			
			if(numRead == -1)
			{
				return null;
			}
			
			off += numRead;
		}
		
		return new String(data);
	}
}
