/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
SyslogWriter is a wrapper around the java.net.DatagramSocket class
so that it behaves like a java.io.Writer.
 */
package com.sun.openesb.ihe.arr.syslogclient;

import java.io.Writer;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.UnknownHostException;
import java.net.SocketException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

/**
 *
 * @author yoga
 */
public class SyslogWriter extends Writer {

    private final static Logger logger = Logger.getLogger(SyslogWriter.class);
    final int SYSLOG_PORT = 514;
    String syslogHost;
    int syslogPort;
    private InetAddress address;
    private DatagramSocket ds;
    private int syslogMsgLevel = 6;
    private String myhostname;
    DateFormat dateFormat = new SimpleDateFormat("MMM dd HH:mm:ss");

    public SyslogWriter(String syslogHost, int syslogPort) {

        this.syslogHost = syslogHost;
        this.syslogPort = syslogPort;

        if (syslogHost == null) {
            throw new NullPointerException("syslogHost is null");
        }

        if (syslogPort == 0) {
            syslogPort = SYSLOG_PORT;
        }

        try {
            this.address = InetAddress.getByName(syslogHost);
        } catch (UnknownHostException e) {
            logger.error("Could not find " + syslogHost +
                    ". All logging will FAIL." + e.getMessage(), e);
        }

        try {
            this.myhostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            logger.error("Could not find localhost name", e);
            this.myhostname = "localhost";
        }

        try {
            this.ds = new DatagramSocket();
        } catch (SocketException e) {
            logger.error("Could not instantiate DatagramSocket to " + syslogHost +
                    ". All logging will FAIL." + e.getMessage(), e);
        }

    }

    public void setLevel(int syslogMsgLevel) {
        this.syslogMsgLevel = syslogMsgLevel;
    }

    public void write(char[] buf, int off, int len) throws IOException {
        this.write(new String(buf, off, len));
    }

    @Override
    public void write(String msg) throws IOException {

        java.util.Date date = new java.util.Date();
        String currentDateTime = dateFormat.format(date);

        if (this.ds != null && this.address != null) {
            String syslogMsg = "<" + syslogMsgLevel + ">" + currentDateTime + " " + myhostname + " " + msg;
            if (logger.isTraceEnabled()) {
                logger.trace("xxx: Syslog msg=[" + syslogMsg + "]");
            }
            byte[] bytes = syslogMsg.getBytes();
            //
            //  syslog packets must be less than 1024 bytes
            //

            int bytesLength = bytes.length;
            /* ravi  removed due to incorrect length
            if (bytesLength >= 1024) {
            bytesLength = 1024;
            }
             */
            DatagramPacket packet = new DatagramPacket(bytes, bytesLength,
                    address, syslogPort);
            ds.send(packet);
        }
    }

    public void flush() {
    }

    public void close() {
        if (ds != null) {
            ds.close();
        }
    }
}