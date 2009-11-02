/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/UUIDFactory.java,v 1.7 2006/02/08 18:38:52 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.common;

//import java.security.SecureRandom;

import java.util.regex.Pattern;

/**
 * A <code>UUIDFactory</code> generates a UUID
 */
public class UUIDFactory {
    /**
     * @link
     * @shapeType PatternLink
     * @pattern Singleton
     * @supplierRole Singleton factory
     */

    /*# private UUIDFactory _uuidFactory; */
    private static UUIDFactory instance = null;

	/** Regex pattern for a UUID */
	private static final Pattern uuidPattern =
		Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");

    /**
     * random number generator for UUID generation
     */
    //private final SecureRandom secRand = new SecureRandom();

    /**
     *         128-bit buffer for use with secRand
     */
    //private final byte[] secRandBuf16 = new byte[16];

    /**
     * 64-bit buffer for use with secRand
     */
    //private final byte[] secRandBuf8 = new byte[8];

    protected UUIDFactory() {
    }

    public java.util.UUID newUUID() {
        return java.util.UUID.randomUUID();
        /* HIEOS/BHT: REMOVED
        secRand.nextBytes(secRandBuf16);
        secRandBuf16[6] &= 0x0f;
        secRandBuf16[6] |= 0x40; // version 4
        secRandBuf16[8] &= 0x3f;
        secRandBuf16[8] |= 0x80; // IETF variant
        secRandBuf16[10] |= 0x80; // multicast bit

        long mostSig = 0;

        for (int i = 0; i < 8; i++) {
            mostSig = (mostSig << 8) | (secRandBuf16[i] & 0xff);
        }

        long leastSig = 0;

        for (int i = 8; i < 16; i++) {
            leastSig = (leastSig << 8) | (secRandBuf16[i] & 0xff);
        }

        return new UUID(mostSig, leastSig);
        */
    }

    /**
	 * Returns true if and only if uuid matches the pattern for valid UUIDs.
	 *
	 * @param uuid the <code>String</code> to check.
	 * @return <code>true</code> if and only if the specified.
	 * <code>String</code> matches the pattern; <code>false</code> otherwise.
	 */
	public boolean isValidUUID(String uuid) {
		return uuidPattern.matcher(uuid).matches();
    }

    /**
	 * Returns true if and only if uuidURN matches the pattern for valid UUID URNs.
	 *
	 * @param uuidURN the <code>String</code> to check.
	 * @return <code>true</code> if and only if the specified.
	 * <code>String</code> matches the pattern; <code>false</code> otherwise.
	 */
	public boolean isValidUUIDURN(String uuidURN) {
		return uuidURN.startsWith("urn:uuid:") && isValidUUID(uuidURN.substring(9));
    }

    private static void printUsage() {
        System.err.println(
            "...UUIDFactory [help] cnt=<number of uuids required>");
        System.exit(-1);
    }

    public static void main(String[] args) {
        int cnt = 1;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("help")) {
                printUsage();
            } else if (args[i].startsWith("cnt=")) {
                cnt = Integer.parseInt(args[i].substring(4, args[i].length()));
            } else {
                System.err.println("Unknown parameter: '" + args[i] +
                    "' at position " + i);

                if (i > 0) {
                    System.err.println("Last valid parameter was '" +
                        args[i - 1] + "'");
                }

                printUsage();
            }
        }

        UUIDFactory uf = UUIDFactory.getInstance();

        for (int i = 0; i < cnt; i++) {
            java.util.UUID id = uf.newUUID();
            System.out.println("new UUID : " + id.toString());
        }
    }

    public synchronized static UUIDFactory getInstance() {
        if (instance == null) {
            instance = new UUIDFactory();
        }

        return instance;
    }
}
