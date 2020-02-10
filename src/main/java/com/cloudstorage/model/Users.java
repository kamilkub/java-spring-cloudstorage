package com.cloudstorage.model;


import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Document
public class Users implements UserDetails {

	@Id
	private String id;

	@Email
	@NotNull(message = "Email is empty")
	private String email;

	@NotNull(message = "Username is empty")
	private String username;

	@NotNull(message = "Password is empty")
	@Size(min = 6, message = "Password must be at least 6 characters long")
	private String password;

	@CreatedDate
	private Date createdDate;

	private String pin;

	private String directoryName;


	private boolean isEnabled = false;
	private boolean isCredentialsNonExpired = true;
	private boolean isAccountNonExpired = true;
	private boolean isAccountNonLocked = true;


	public void setEnabled(boolean enabled) {
		this.isEnabled = enabled;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return isAccountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return isAccountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return isCredentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority("USER"));
	}

	public String getDirectoryName() {
		return directoryName;
	}

	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	@Override
	public String toString() {
		return "Users{" +
				"id='" + id + '\'' +
				", email='" + email + '\'' +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", pin='" + pin + '\'' +
				", isEnabled=" + isEnabled +
				", isCredentialsNonExpired=" + isCredentialsNonExpired +
				", isAccountNonExpired=" + isAccountNonExpired +
				", isAccountNonLocked=" + isAccountNonLocked +
				'}';
	}
}
