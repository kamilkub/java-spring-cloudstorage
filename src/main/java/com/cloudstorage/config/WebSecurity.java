package com.cloudstorage.config;


import com.cloudstorage.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@EnableConfigurationProperties
@RequiredArgsConstructor
public class WebSecurity extends WebSecurityConfigurerAdapter {
	private final AuthenticationService authenticationService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authenticationService)
				.passwordEncoder(bCryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();

		http.authorizeRequests()
				.antMatchers("/user", "/user/*").authenticated()
				.antMatchers("/home", "/", "/sign-up", "/activate").permitAll()
				.and()
				.formLogin().loginPage("/sign-in")
				.loginProcessingUrl("/sign-in")
				.defaultSuccessUrl("/user")
				.permitAll()
				.and()
				.rememberMe()
				.and()
				.logout().permitAll()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/sign-in?logout")
				.deleteCookies("remember-me");



	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}
}
