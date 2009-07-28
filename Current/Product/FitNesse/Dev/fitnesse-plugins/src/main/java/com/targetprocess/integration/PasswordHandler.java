package com.targetprocess.integration;

import org.apache.ws.security.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;
public class PasswordHandler implements CallbackHandler {

	private static String userName;
	private static String password;

	public static void setUserName(String userName) {
		PasswordHandler.userName = userName;
	}

    public static String getUserName() {
		return userName;
	}

	public static void setPassword(String password) {
		PasswordHandler.password = password;
	}

	public static String getPassword() {
		return password;
	}

	public PasswordHandler() throws Exception {
    }

    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        WSPasswordCallback callback = (WSPasswordCallback) callbacks[0];
        if (!callback.getIdentifer().equals(getUserName()))
        	throw new RuntimeException("I was expecting the user name: " + getUserName() + " but was asked about: " + callback.getIdentifer());
        callback.setPassword(getPassword());
    }

}
