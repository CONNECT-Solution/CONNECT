/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/security/authentication/UserRegistrar.java,v 1.18 2006/07/20 02:33:28 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.server.security.authentication;

import java.security.cert.X509Certificate;
import javax.xml.registry.JAXRException;
import javax.xml.registry.RegistryException;
import org.freebxml.omar.common.exceptions.UnregisteredUserException;
import org.freebxml.omar.common.exceptions.UserRegistrationException;
import org.freebxml.omar.server.util.ServerResourceBundle;

import org.oasis.ebxml.registry.bindings.rim.User;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Registers new users with the registry. Registration involves saving the public
 * key certificate for the user in server KeyStore and storing their User object
 * In registry.
 *
 * @author <a href="mailto:Farrukh.Najmi@Sun.COM">Farrukh S. Najmi</a>
 */
public class UserRegistrar {
    public static final String ASSOC_TYPE_HAS_CERTIFICATE = "ebxmlrr_HasCertificate";

    /** The log */
    private static final Log log = LogFactory.getLog(UserRegistrar.class.getName());
    
    /**
     * @link
     * @shapeType PatternLink
     * @pattern Singleton
     * @supplierRole Singleton factory
     */

    /*# private UserRegistrar _authenticationServiceImpl; */
    private static UserRegistrar instance = null;

    protected UserRegistrar() {
    }

    /**
     * It will try to register the user if the certificate in a signed SubmitObjectsRequest
     * is not yet in the keystore. The SubmitObjectsRequest must contain a single
     * User object and its id must be a valid UUID and equal to the alias parameter,
     * which should be extracted from the KeyInfo of XML signature element.
     * @return the User object of the newly registered user
     * @throws UserRegistrationException if SubmitObjectsRequest has more than
     * one User object, or its alias is not equal to the id of the unique User object,
     * or the id is not a valid UUID.
     */
    public User registerUser(
        X509Certificate cert,
        org.oasis.ebxml.registry.bindings.lcm.SubmitObjectsRequest req)
        throws RegistryException {
        User user = null;

        try {
            AuthenticationServiceImpl ac = AuthenticationServiceImpl.getInstance();

            //Get all User objects
            org.oasis.ebxml.registry.bindings.rim.RegistryObjectListType objs = req.getRegistryObjectList();
            java.util.List al = org.freebxml.omar.common.BindingUtility.getInstance()
                                                                       .getRegistryObjectList(objs);

            java.util.List users = new java.util.ArrayList();
            java.util.Iterator objIter = al.iterator();

            while (objIter.hasNext()) {
                org.oasis.ebxml.registry.bindings.rim.RegistryObjectType obj = (org.oasis.ebxml.registry.bindings.rim.RegistryObjectType) objIter.next();

                if (obj instanceof User) {
                    User _user = (User) obj;

                    // check to see if a user ACL file exists, and
                    // if it does, check to see if the user is in
                    // the list
                    boolean isInACLFile = isUserInACLFile(_user);

                    if (isInACLFile) {
                        log.info(ServerResourceBundle.getInstance().
				 getString("message.isAuthorized",
					   new Object[] {_user.getPersonName().
							 getFirstName(),
							 _user.getPersonName().
							 getLastName()}));
                    } else {
                        String message = ServerResourceBundle.getInstance().
			    getString("message.isNotAuthorized",
				      new Object[] {_user.getPersonName().
						    getFirstName(),
						    _user.getPersonName().
						    getLastName()});
                        log.warn(message);
                        throw new UserRegistrationException(message);
                    }

                    String userId = _user.getId();
                    users.add(_user);
                }
            }

            if (users.size() == 0) {
                //This Exception seems to be misleading. Should we throw UserRegistrationException with message saying no user was found, instead?
                //Then again I doubt that this can this ever happen.
                throw new UnregisteredUserException(cert);
            }

            if (!((users.size() == 1) && ((users.get(0)) instanceof User))) {
                throw new UserRegistrationException(
                    ServerResourceBundle.getInstance().
		    getString("message.userRegistrationFailedOneUser"));
            }

            user = (User) users.get(0);

            String userId = user.getId();

            //System.err.println("UserId: " + userId);
            if (!(org.freebxml.omar.common.Utility.getInstance()
                                                      .isValidRegistryId(userId))) {
                throw new UserRegistrationException(
                    ServerResourceBundle.getInstance().
		    getString("message.userRegistrationFailedUUID"));
            }

            if (log.isInfoEnabled()) {
                log.info(ServerResourceBundle.getInstance().
			 getString("message.registeringNewUser",
				   new Object[] {userId}));
            }

            ac.registerUserCertificate(userId, cert);

            if (log.isInfoEnabled()) {
                log.info(ServerResourceBundle.getInstance().
			 getString("message.userRegistered",
				   new Object[] {userId}));
            }
            
        } catch (JAXRException e) {
            throw new RegistryException(e);
        }

        return user;
    }
    
    /*
     * This method is used to determine if a user is allowed to
     * self-register.  If the ebxmlrr.security.selfRegistration.acl
     * property does not exist or the value is an empty string, anyone
     * can self-register. In this case, the method returns 'true'.
     * If this property exists, it contains a comma-delimited list of users
     * that are authorized to self-register. For example:
     * ebxmlrr.security.selfRegistration.acl=Jane Doe, Srinivas Patel
     * The list is parsed into tokens (e.g., "Jane Doe")
     * This method will check to see if both firstName and lastName from the
     * User object appear in one of the tokens.  The firstName must also
     * appear in the token before the lastName.
     * If it does appear, this method returns 'true'. Otherwise 'false'.
     */
    private boolean isUserInACLFile(User user) throws IllegalArgumentException {
        boolean isInACLFile = false;

        if (user == null) {
            throw new IllegalArgumentException(ServerResourceBundle.getInstance().getString("message.nilUserReference"));
        }

        org.freebxml.omar.server.common.RegistryProperties rp = org.freebxml.omar.server.common.RegistryProperties.getInstance();

        // The reason we reload the properties is that Registry Admins
        // will be updating the ACL list more often than the ebxmlrr is
        // recycled. Reloading the properties makes the latest edits
        // available to this class.
        rp.reloadProperties();

        String aclList = rp.getProperty("omar.security.selfRegistration.acl");

        // If property does not exist, or the property value is "", allow
        // all self-registrations. This is the default setting
        if ((aclList == null) || (aclList.length() == 0)) {
            return true;
        }

        org.oasis.ebxml.registry.bindings.rim.PersonNameType pName = user.getPersonName();
        String firstName = pName.getFirstName();
        String lastName = pName.getLastName();
        java.util.StringTokenizer st = new java.util.StringTokenizer(aclList,
                ",");

        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            int firstNameIndex = token.indexOf(firstName);
            int lastNameIndex = token.indexOf(lastName);

            if ((firstNameIndex != -1) && (lastNameIndex != -1) &&
                    (firstNameIndex < lastNameIndex)) {
                isInACLFile = true;

                break;
            }
        }

        return isInACLFile;
    }

    public static void main(String[] args) throws Exception {
        UserRegistrar service = UserRegistrar.getInstance();
    }

    public synchronized static UserRegistrar getInstance() {
        if (instance == null) {
            instance = new org.freebxml.omar.server.security.authentication.UserRegistrar();
        }

        return instance;
    }
}
