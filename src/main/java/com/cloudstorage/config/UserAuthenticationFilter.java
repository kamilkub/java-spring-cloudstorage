package com.cloudstorage.config;


import com.cloudstorage.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.scope.ScopedProxyFactoryBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@RequiredArgsConstructor
public class UserAuthenticationFilter {
	private final UsersRepository usersRepository;

	public Authentication getAuthentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication instanceof UsernamePasswordAuthenticationToken ? authentication : null;
	}
	public boolean isAuthenticated() {
		return this.getAuthentication() instanceof UsernamePasswordAuthenticationToken;
	}
	public String getAuthenticationUsername() {
		Authentication authentication = this.getAuthentication();
			return authentication instanceof UsernamePasswordAuthenticationToken ? authentication.getName() : null;
	}
	public String getAuthenticatedDirectory() {
		Authentication authentication =  this.getAuthentication();
		return usersRepository.findByUsername(authentication.getName()).getDirectoryName();
	}
}