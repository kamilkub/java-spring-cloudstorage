package com.cloudstorage.config;


import com.cloudstorage.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserAuthenticationFilter {

	private UsersRepository usersRepository;

	@Autowired
	public UserAuthenticationFilter(UsersRepository usersRepository) {
		this.usersRepository = usersRepository;
	}

	public Authentication isAuthenticatedObject() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication instanceof UsernamePasswordAuthenticationToken)
			return authentication;
		else
			return null;
	}

	public boolean isAuthenticatedBool() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication instanceof UsernamePasswordAuthenticationToken;
	}

	public String getAuthenticationUsername() {
		Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
			return authentication instanceof UsernamePasswordAuthenticationToken ? authentication.getName() : null;
	}

	public String getAuthenticatedDirectory() {
		Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
		return usersRepository.findByUsername(authentication.getName()).getDirectoryName();
	}


}
