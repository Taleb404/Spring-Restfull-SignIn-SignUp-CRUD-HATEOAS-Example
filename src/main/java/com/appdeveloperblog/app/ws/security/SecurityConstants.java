package com.appdeveloperblog.app.ws.security;

import com.appdeveloperblog.app.ws.SpringApplicaitonContext;

public class SecurityConstants {

	public static final long EXPIRATION_TIME = 864000000;
	public static final String TOKEN_PREFIX = "Brear ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/users";
	public static final String VERIFICATION_EMAIL_URL = "/users/email-verification";
	
	public static String getTokenSecret()
	{
		AppProperties appProperties = (AppProperties) SpringApplicaitonContext.getBean("AppProperties");
		return appProperties.getTokenSecret();
	}
}
