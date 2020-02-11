package com.cloudstorage.config;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserAuthenticationFilter {

	public Authentication isAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(!(authentication instanceof AnonymousAuthenticationToken))
			return authentication;
		else
			return null;

	}


}
