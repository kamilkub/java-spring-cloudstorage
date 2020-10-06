package com.cloudstorage.model;


import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Document
@NoArgsConstructor
public class StorageUser implements UserDetails, Persistable<String> {

	@Id
	private String id;

	@Email(message = "Must be a well formed email address")
	@NotEmpty(message = "Email is empty")
	private String email;

	@NotBlank(message = "Username is empty")
	@Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "You can't use special characters only _ is allowed")
	private String username;

	@NotBlank(message = "Password is empty")
	@Size(min = 6, message = "Password must be at least 6 characters long")
	private String password;

	@CreatedDate
	private Date createdDate;

	private long diskSpace = 1080000000;

	private String pin;

	private String directoryName;

	private boolean isEnabled = false;

	private boolean isCredentialsNonExpired = true;

	private boolean isAccountNonExpired = true;

	private boolean isAccountNonLocked = true;

	private boolean persisted;


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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public long getDiskSpace() {
		return diskSpace;
	}

	public void setDiskSpace(long diskSpace) {
		this.diskSpace = diskSpace;
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
	public boolean isNew() {
		return !persisted;
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

	public void setPersisted(boolean persisted) {
		this.persisted = persisted;
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
		return "StorageUser{" +
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
