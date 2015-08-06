/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.admingui.util;

import gov.hhs.fha.nhinc.admingui.managed.CsrfBean;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.apache.log4j.Logger;

/**
 *
 * @author achidamb
 */
public class CryptoRandmomGenerator {

    private static final Logger LOG = Logger.getLogger(CryptoRandmomGenerator.class);

    SecureRandom random = new SecureRandom();

    private CryptoRandmomGenerator() {

    }

    /**
     *
     * @return @throws NoSuchAlgorithmException
     */
    @SuppressWarnings("empty-statement")
    public synchronized String createToken() throws NoSuchAlgorithmException {
        return (new BigInteger(130, random).toString(32));

    }

    private static class SingletonHolder {

        private static final CryptoRandmomGenerator instance = new CryptoRandmomGenerator();
    }

    public static CryptoRandmomGenerator getInstance() {
        return SingletonHolder.instance;
    }

}
