/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/security/authentication/AuthenticationServiceImpl.java,v 1.56 2007/05/25 23:26:39 psterk Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.security.authentication;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freebxml.omar.common.BindingUtility;
import org.freebxml.omar.common.CommonProperties;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.common.exceptions.UserNotFoundException;
import org.freebxml.omar.common.exceptions.UserRegistrationException;
import org.freebxml.omar.server.cache.ServerCache;
import org.freebxml.omar.server.common.RegistryProperties;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.persistence.PersistenceManagerFactory;
import org.freebxml.omar.server.util.ServerResourceBundle;
import org.oasis.ebxml.registry.bindings.query.ResponseOption;
import org.oasis.ebxml.registry.bindings.query.ReturnType;
import org.oasis.ebxml.registry.bindings.rim.User;
import org.oasis.ebxml.registry.bindings.rim.UserType;


/**
 * Manages authentication functionality for the registry.
 * This includes management of user public keys in the server key store.
 *
 * @author <a href="mailto:Farrukh.Najmi@Sun.COM">Farrukh S. Najmi</a>
 */
public class AuthenticationServiceImpl /*implements AuthenticationService*/ {

    /* Aliases/ids for pre-defined Users.*/
    public static final String ALIAS_REGISTRY_OPERATOR =
	"urn:freebxml:registry:predefinedusers:registryoperator";
    public static final String ALIAS_REGISTRY_GUEST =
	"urn:freebxml:registry:predefinedusers:registryguest";

    /* Aliases/ids for test Users */
    public static final String ALIAS_FARRUKH =
	"urn:freebxml:registry:predefinedusers:farrukh";
    public static final String ALIAS_NIKOLA =
	"urn:freebxml:registry:predefinedusers:nikola";
    
    org.freebxml.omar.server.persistence.PersistenceManager pm = PersistenceManagerFactory.getInstance()
          .getPersistenceManager();

    //Use pm not qm do avoid deadlock or inifinite loop with cache filling
    //QueryManager qm = QueryManagerFactory.getInstance().getQueryManager();
    
    /**
     * @link
     * @shapeType PatternLink
     * @pattern Singleton
     * @supplierRole Singleton factory
     */

    /*# private AuthenticationServiceImpl _authenticationServiceImpl; */
    private static AuthenticationServiceImpl instance = null;
    private static final Log log = LogFactory.getLog(AuthenticationServiceImpl.class);
    KeyStore keyStore = null;

    /* A lock object to prevent another thread to  access the keystore when
     * there is a thread adding certificate to it and writing it to disk
     */
    Object keyStoreWriteLock = new Object();
    KeyStore trustAnchorsKeyStore;
    public User registryGuest = null;
    public User registryOperator = null;
    /* HIEOS/BHT: Disabled:
    public User farrukh = null;
    public User nikola = null;
     */
    java.util.HashMap publicKeyToCertMap = new java.util.HashMap();
    java.util.HashSet adminIdSet = new java.util.HashSet();
    RegistryProperties propsReader = RegistryProperties.getInstance();

    protected AuthenticationServiceImpl() {
        try {
            loadPredefinedUsers();
            loadRegistryAdministrators();
            if (false)  // FIXME (HIEOS/BHT): Do not load certs.
            {
                loadPublicKeyToCertMap();
            }
        } catch (RegistryException e) {
            throw new java.lang.reflect.UndeclaredThrowableException(e);            
        }
    }
    
    //Key is PublicKey, Value is X509Certificate
    private void loadPublicKeyToCertMap() throws RegistryException {
        try {
            KeyStore store = getKeyStore();

            for (Enumeration e = store.aliases() ; e.hasMoreElements() ;) {
                String alias = (String)e.nextElement();
                X509Certificate cert = (X509Certificate)store.getCertificate(alias);
                PublicKey publicKey = cert.getPublicKey();
                publicKeyToCertMap.put(publicKey, cert);
            }
        } catch (KeyStoreException e) {
            throw new RegistryException(e);
        }
        
    }

    public KeyStore getTrustAnchorsKeyStore() throws RegistryException {
        try {
            if (trustAnchorsKeyStore == null) {
                synchronized (AuthenticationServiceImpl.class) {
                    if (trustAnchorsKeyStore == null) {
                        String keyStoreFile = propsReader.getProperty(
                                "omar.security.trustAnchors.keystoreFile");
                        String keystorePassword = propsReader.getProperty(
                                "omar.security.trustAnchors.keystorePassword");
                        String keystoreType = propsReader.getProperty(
                                "omar.security.trustAnchors.keystoreType");
                        trustAnchorsKeyStore = KeyStore.getInstance(keystoreType);
                        trustAnchorsKeyStore.load(new java.io.FileInputStream(
                                keyStoreFile), keystorePassword.toCharArray());
                    }
                }
            }

            return trustAnchorsKeyStore;
        } catch (NoSuchAlgorithmException e) {
            throw new RegistryException(ServerResourceBundle.getInstance().getString("message.trustAnchorsKeystore"),
                e);
        } catch (KeyStoreException e) {
            throw new RegistryException(ServerResourceBundle.getInstance().getString("message.trustAnchorsKeystore"),
                e);
        } catch (java.security.cert.CertificateException e) {
            throw new RegistryException(ServerResourceBundle.getInstance().getString("message.trustAnchorsKeystore"),
                e);
        } catch (java.io.FileNotFoundException e) {
            throw new RegistryException(ServerResourceBundle.getInstance().getString("message.trustAnchorsKeystore"),
                e);
        } catch (IOException e) {
            throw new RegistryException(ServerResourceBundle.getInstance().getString("message.trustAnchorsKeystore"),
                e);
        }
    }

    /**
     * Get the keystore whose path is specified  by {@link #getKeyStoreFileName()}.
     * Note that all the methods that access the keystore MUST access the keystore
     * via this method. Do not access the keystore directly by accessing the keystore
     * field. Otherwise the checking the write lock to keystore will be bypassed.
     */
    public KeyStore getKeyStore() throws RegistryException {
        synchronized (keyStoreWriteLock) {
            if (keyStore == null) {
                java.io.FileInputStream fis = null;

                try {
                    String keystoreType = RegistryProperties.getInstance().getProperty("omar.security.keystoreType", "JKS");
                    keyStore = KeyStore.getInstance(keystoreType);

                    String keystoreFile = getKeyStoreFileName();
                    fis = new java.io.FileInputStream(keystoreFile);

                    String keystorePass = getKeyStorePassword();
                    keyStore.load(fis, keystorePass.toCharArray());
                } catch (java.security.cert.CertificateException e) {
                    throw new RegistryException(e);
                } catch (KeyStoreException e) {
                    throw new RegistryException(e);
                } catch (NoSuchAlgorithmException e) {
                    throw new RegistryException(e);
                } catch (java.io.FileNotFoundException e) {
                    throw new RegistryException(e);
                } catch (IOException e) {
                    throw new RegistryException(e);
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            return keyStore;
        }
    }

    public java.security.PrivateKey getPrivateKey(String alias, String password)
        throws RegistryException {
        java.security.PrivateKey privateKey = null;

        try {
            privateKey = (java.security.PrivateKey) getKeyStore().getKey(alias,
                    password.toCharArray());
        } catch (KeyStoreException e) {
            throw new RegistryException(ServerResourceBundle.getInstance().getString("message.privateKey"), e);
        } catch (NoSuchAlgorithmException e) {
            throw new RegistryException(ServerResourceBundle.getInstance().getString("message.privateKey"), e);
        } catch (java.security.UnrecoverableKeyException e) {
            throw new RegistryException(ServerResourceBundle.getInstance().getString("message.privateKey"), e);
        }

        return privateKey;
    }

    public X509Certificate getCertificate(String alias)
        throws RegistryException {
        X509Certificate cert = null;

        try {
            cert = (X509Certificate) getKeyStore().getCertificate(alias);

            if (cert == null) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.certificateNotFound", new Object[]{alias}));
            }
        } catch (KeyStoreException e) {
            throw new RegistryException(ServerResourceBundle.getInstance().getString("message.gettingCertificate"), e);
        }

        return cert;
    }

    public java.security.cert.Certificate[] getCertificateChain(String alias)
        throws RegistryException {
        try {
            return getKeyStore().getCertificateChain(alias);
        } catch (KeyStoreException e) {
            throw new RegistryException(ServerResourceBundle.getInstance().getString("message.gettingCertificateChain"), e);
        }
    }

    public synchronized static AuthenticationServiceImpl getInstance() {
        if (instance == null) {
            instance = new org.freebxml.omar.server.security.authentication.AuthenticationServiceImpl();
        }

        return instance;
    }

    public String getKeyStoreFileName() throws RegistryException {
        String fileName = RegistryProperties.getInstance().getProperty("omar.security.keystoreFile");

        return fileName;
    }

    public String getKeyStorePassword() throws RegistryException {
        String pw = RegistryProperties.getInstance().getProperty("omar.security.keystorePassword");

        return pw;
    }

    /**
     * Check if the signatures CA is trusted by the registry.
     *
     * @throws UserRegistrationException if the certificate issuing CA is not trusted.
     * @throws RegistryException if the certificates cannot be verified for some other reasons, such as unable to load
     * trust anchors keystore
     */
    public void validateCertificate(
        X509Certificate cert)
        throws UserRegistrationException, RegistryException {
        //TODO:
    }

    /**
     * Gets the alias within the KeyStore for a User
     */
    public String getAliasFromUser(User user) throws RegistryException {
        return user.getId();
    }

    /**
     * Gets the alias within the KeyStore for a User
     */
    public X509Certificate getCertificateFromUser(User user)
        throws RegistryException {
        X509Certificate cert = null;

        try {
            String alias = getAliasFromUser(user);

            cert = (X509Certificate) (getKeyStore().getCertificate(alias));
        } catch (KeyStoreException e) {
            throw new RegistryException(e);
        }

        return cert;
    }

    /**
     * Gets the User that is associated with the given alias.
     *
     * @throws UserNotFoundException when no matching User is found
     */
    public UserType getUserFromAlias(String alias) throws RegistryException {
        UserType user = null;

        ServerRequestContext context = null;
        try {
            context = new ServerRequestContext("AuthenticationServiceImpl.getUserFromAlias", null);
            context.setUser(this.registryOperator);            
            String userId = alias;            
            user = (UserType)ServerCache.getInstance().getRegistryObject(context, userId, "User");
            if (user == null) {
                throw new UserNotFoundException(userId);
            }

            //See if User need to be auto-classified as RegistryAdministrator
            boolean isAdmin = isRegistryAdministratorInPropFile(user);

            if (isAdmin) {
                //Make sure that the user is classified with the RegistryAdministrator role
                makeRegistryAdministrator(context, user);
            }

	    context.commit();
	    context = null;
        } catch (RegistryException e) {
            throw e;
        } catch (Exception e) {
            throw new RegistryException(e);
        } finally {
	    if (null != context) {
		// Oh no, it's still here...
		context.rollback();
	    }
	}
        
        return user;
    }

    /**
     * See if User is declared as a RegistryAdministrator in prop file.
     */
    private boolean isRegistryAdministratorInPropFile(UserType user)
        throws RegistryException {
        boolean isAdmin = false;

        if (user != null) {
            String id = user.getId();

            if (adminIdSet.contains(id)) {
                isAdmin = true;
            }
        }

        return isAdmin;
    }

    /**
     * Determines if user has RegistryAdministrator role.
     *
     * @return true if user has role Intermediary, otherwise return false
     *
     */
    public boolean hasRegistryAdministratorRole(UserType user)
        throws RegistryException {
        return hasRole(user, BindingUtility.CANONICAL_SUBJECT_ROLE_ID_RegistryAdministrator);
    }
    
    /**
     * Determines if user has Intermediary role.
     *
     * @return true if user has role Intermediary, otherwise return false
     *
     */
    public boolean hasIntermediaryRole(UserType user)
        throws RegistryException {
        return hasRole(user, BindingUtility.CANONICAL_SUBJECT_ROLE_ID_Intermediary);
    }
    
    /**
     * Determines if specified user has the role specified by roleId.
     *
     * @return true if user has role specified by roleId, otherwise return false
     *
     */
    public boolean hasRole(UserType user, String roleId)
        throws RegistryException {
        boolean hasRole = false;

        List classifications = user.getClassification();
        java.util.Iterator iter = classifications.iterator();

        while (iter.hasNext()) {
            org.oasis.ebxml.registry.bindings.rim.Classification classification = (org.oasis.ebxml.registry.bindings.rim.Classification) iter.next();
            String classificationNodeId = classification.getClassificationNode();

            if (classificationNodeId.equals(roleId)) {
                hasRole = true;

                break;
            }
        }
        return hasRole;
    }            

    /**
     * Make sure user gets auto-classified as RegistryAdministrator if not so already.
     */
    private void makeRegistryAdministrator(ServerRequestContext context,
					   UserType user)
        throws RegistryException {
        
        try {
            if (user != null) {
                boolean isAdmin = hasRegistryAdministratorRole(user);

                if (!isAdmin) {
		    /* ??? Need new message for this logging. 
		    // Log real changes to security realm -- new admins.
                    if (log.isInfoEnabled()) {
                        log.info(ServerResourceBundle.getInstance().
				 getString("message.getRegistryAdministratorsAddingAdmin",
					   new Object[]{user.getId()}));
                    }
		    */
                    org.oasis.ebxml.registry.bindings.rim.Classification classification =
                        BindingUtility.getInstance().rimFac.createClassification();
                    classification.setId(org.freebxml.omar.common.Utility.getInstance().createId());
                    classification.setClassificationNode(BindingUtility.CANONICAL_SUBJECT_ROLE_ID_RegistryAdministrator);
                    classification.setClassifiedObject(user.getId());
                    user.getClassification().add(classification);

                    //Now persists updated User 
                    java.util.List al = new java.util.ArrayList();
                    al.add(user);
                    pm.update(context, al);
                }
            }

	    // ??? This method did not create this context, should it commit?
	    context.commit();
	    // A local signal to the finally block below.
	    context = null;
        } catch (RegistryException e) {
            throw e;
        } catch (Exception e) {
            throw new RegistryException(e);
        } finally {
	    if (null != context) {
		// Oh no, it's still here...
		context.rollback();
	    }
	}
    }
    
    

    /*
     * Loads and caches the predefined users during startup.
     */
    private void loadPredefinedUsers() throws RegistryException {
        ServerRequestContext context = null;
        try {
            context = new ServerRequestContext("AuthenticationServiceImpl.loadPredefinedUsers", null);
            registryOperator = (User)pm.getRegistryObject(context, ALIAS_REGISTRY_OPERATOR, "User");
            registryGuest = (User)pm.getRegistryObject(context, ALIAS_REGISTRY_GUEST, "User");
            /* HIEOS/BHT: DISABLED
            farrukh = (User)pm.getRegistryObject(context, ALIAS_FARRUKH, "User");
            nikola = (User)pm.getRegistryObject(context, ALIAS_NIKOLA, "User");
             */
            if (registryOperator == null) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.registryOperator", new Object[]{ALIAS_REGISTRY_OPERATOR}));
            }
            if (registryGuest == null) {
                throw new RegistryException(ServerResourceBundle.getInstance().getString("message.registryGuest", new Object[]{ALIAS_REGISTRY_GUEST}));
            }
        } catch (RegistryException e) {
            log.error(ServerResourceBundle.getInstance().getString("message.InternalErrorCouldNotLoadPredefinedUsers"), e);
            throw e;
        } finally {
	    // All of above uses of this context were queries, roll back.
	    context.rollback();
	}
    }
    
    /*
     * Loads the list of RegistryAdministrators from the property file during startup.
     */
    private void loadRegistryAdministrators() {
        String adminList = RegistryProperties.getInstance().getProperty("omar.security.authorization.registryAdministrators");

        if (adminList != null) {
            java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(adminList,
                    "|");

            while (tokenizer.hasMoreTokens()) {
                try {
                    String adminId = tokenizer.nextToken();
		    // ??? Note these messages are output whether or not
		    // ??? user already has this role.
                    if (log.isDebugEnabled()) {
                        log.debug(ServerResourceBundle.getInstance().getString("message.getRegistryAdministratorsAddingAdmin", new Object[]{adminId}));
                    }
                    adminIdSet.add(adminId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            log.warn(ServerResourceBundle.getInstance().getString("message.RegistryHasNotDefinedRegistryAdministratorsYet"));
        }
    }
    
    /**
     * Gets the User that is associated with the specified certificate.
     *
     * @throws UserNotFoundException when no matching User is found
     */
    public UserType getUserFromCertificate(X509Certificate cert) throws RegistryException {
        UserType user = null;

        if (cert == null) {
            boolean noRegRequired = 
                Boolean.valueOf(CommonProperties.getInstance()
                    .getProperty("omar.common.noUserRegistrationRequired", "false")).booleanValue();
            if (noRegRequired) {
                return registryOperator;
            } else {
                return registryGuest;
            }
        }

        //The registry expects the KeyInfo to either have the PublicKey or the DN from the public key
        //In case of DN the registry can lookup the public key based on the DN
        java.security.PublicKey publicKey = null;
        String alias = null;

        try {
            
            // lots of trace
            if (log.isTraceEnabled()) {
                log.trace("getUserFromCertificate cert:\n" + cert);
                StringBuffer storedCerts = new StringBuffer("Stored certificates:");
                Enumeration aliases = getKeyStore().aliases();
                while (aliases.hasMoreElements()) {
                    X509Certificate storedCert = (X509Certificate)getKeyStore().getCertificate((String)aliases.nextElement());
                    storedCerts.append("\n").append(storedCert).append("\n--------");
                }
                log.trace(storedCerts.toString());
            } else if (log.isDebugEnabled()) {
                log.debug("getUserFromCertificate cert:\n" + cert);
            }
            
            alias = getKeyStore().getCertificateAlias(cert);
            if (alias == null) {
                if (log.isDebugEnabled()) {
                    log.debug("Unknown certificate: " + cert.getSubjectDN().getName());
                }
                throw new UserNotFoundException(cert.getSubjectDN().getName()); 
            }
            
            if (log.isDebugEnabled()) {
                log.debug("Alias found for certificate:: " + alias);
            }
        } catch (KeyStoreException e) {
            throw new RegistryException(e);
        }

        user = getUserFromAlias(alias);

        return user;
        
    }
    
    /**
     * Compares two certificates. It will compare the issuerUniqueID and subjectUniqueID
     * fields of the certificates. If either certificate does not contain either
     * field, it will return false.
     */
    private boolean certificatesAreSame(X509Certificate cert,
        X509Certificate oldCert) throws RegistryException {
        boolean[] certIssuerID = cert.getIssuerUniqueID();
        boolean[] oldCertIssuerID = oldCert.getIssuerUniqueID();

        if ((certIssuerID == null) || (oldCertIssuerID == null) ||
                (certIssuerID.length != oldCertIssuerID.length)) {
            return false;
        }

        for (int i = 0; i < certIssuerID.length; i++) {
            if (certIssuerID[i] != oldCertIssuerID[i]) {
                return false;
            }
        }

        boolean[] certSubjectID = cert.getSubjectUniqueID();
        boolean[] oldCertSubjectID = oldCert.getSubjectUniqueID();

        if ((certSubjectID == null) || (oldCertSubjectID == null) ||
                (certSubjectID.length != oldCertSubjectID.length)) {
            return false;
        }

        for (int i = 0; i < certSubjectID.length; i++) {
            if (certSubjectID[i] != oldCertSubjectID[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Add a certificate entry in the keystore.
     * @param userId The alias of the certificate
     * @param signature The XMLSignature containing the certificate
     * @throws UserRegistration fails if the keystore already contrains the entry
     * whose alias is equal to userId
     */
//    protected void registerUserCertificate(String userId, XMLSignature signature)
//        throws RegistryException {
//        try {
//            X509Certificate cert = getCertificate(signature);
//            registerUserCertificate(userId, cert);
//        } catch (NoSuchAlgorithmException e) {
//            throw new UserRegistrationException(e);
//        } catch (java.security.cert.CertificateException e) {
//            throw new UserRegistrationException(e);            
//        } catch (javax.xml.crypto.KeySelectorException e) {
//            throw new RegistryException(e);
//        } catch (java.security.InvalidAlgorithmParameterException e) {
//            throw new RegistryException(e);
//        }            
//    }
    
    /**
     * This method is used to remove a certificate from the server keystore.
     * This is called, for example, when a rim:User has been deleted and the 
     * User's credentials need to be cleared from the server keystore
     *
     * @param alias
     *  A java.lang.String that contains the alias of the public key credential
     */
    public void deleteUserCertificate(String alias) throws RegistryException {
        KeyStore keyStore = getKeyStore();
        java.io.FileOutputStream fos = null;
        try {
            String keystoreFile = getKeyStoreFileName();
            synchronized (keyStoreWriteLock) { 
                fos = new java.io.FileOutputStream(keystoreFile);
                keyStore.deleteEntry(alias);
                String keystorePass = getKeyStorePassword();
                keyStore.store(fos, keystorePass.toCharArray());
                fos.flush();
                this.keyStore = null;
            }
        } catch (Throwable t) {
            throw new RegistryException(t);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException io){}
            }
        }
    }
    
    protected void registerUserCertificate(String userId, X509Certificate cert)
        throws RegistryException {
        java.io.FileOutputStream fos = null;
                
        try {
            KeyStore keyStore = getKeyStore();

            //Make sure no other user is registered with same cert under a different alias
            String alias = getKeyStore().getCertificateAlias(cert);
            if ((null != alias) && (!userId.equalsIgnoreCase(alias))) {
                throw new UserRegistrationException(ServerResourceBundle.getInstance().getString("message.error.certificateAlreadyExists", new Object[]{userId, alias}));                
            }
            
            // Check if already in store
            X509Certificate oldCert = null;

            try {
                oldCert = getCertificate(userId);
            } catch (Exception e) {
            }

            //System.err.println("Checking the certificates are the same...");
            if ((oldCert != null) && !certificatesAreSame(cert, oldCert)) {
                throw new UserRegistrationException(ServerResourceBundle.getInstance().getString("message.userRegistrationFailed", new Object[]{userId}));
            }

            //Add the cert. to the keystore if the cert. does not exist yet
            if (oldCert == null) {
                if (propsReader.getProperty(
                            "omar.security.validateCertificates").trim().equalsIgnoreCase("true")) {
                    validateCertificate(cert);
                }

                synchronized (keyStoreWriteLock) {                    
                    keyStore.setCertificateEntry(userId, cert);

                    String keystoreFile = getKeyStoreFileName();
                    fos = new java.io.FileOutputStream(keystoreFile);

                    String keystorePass = getKeyStorePassword();
                    keyStore.store(fos, keystorePass.toCharArray());
                    fos.flush();
                    fos.close();
                    this.keyStore = null;
                    
                    //Update publicKeyToCertMap
                    publicKeyToCertMap.put(cert.getPublicKey(), cert);
                }
            }
        } catch (KeyStoreException e) {
            throw new UserRegistrationException(e);
        } catch (IOException e) {
            throw new UserRegistrationException(e);
        } catch (java.security.cert.CertificateException e) {
            throw new UserRegistrationException(e);            
        } catch (NoSuchAlgorithmException e) {
            throw new UserRegistrationException(e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }        
    }

//    private X509Certificate getCertificate(XMLSignature signature)
//            throws NoSuchAlgorithmException, java.security.cert.CertificateException,
//            javax.xml.crypto.KeySelectorException, java.security.InvalidAlgorithmParameterException,
//            RegistryException {
//        XMLSignatureFactory fac = SecurityUtil.getInstance().createXMLSignatureFactory();
//
//        KeySelector keySelector = new X509KeySelector(keyStore);
//        KeyInfo keyInfo = signature.getKeyInfo();
//        KeySelectorResult result = keySelector.select(keyInfo, KeySelector.Purpose.VERIFY,
//            fac.newSignatureMethod(SignatureMethod.DSA_SHA1, null), null);
//        return ((KeyAliasSelectorResult)result).getCertificate();
//    }
}
