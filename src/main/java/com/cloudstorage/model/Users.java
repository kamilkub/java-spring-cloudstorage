package com.cloudstorage.model;



import org.springframework.data.annotation.Id;



import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;



public class Users {

	@Id
	public String id;

	@Email
	@NotNull(message = "Email is empty")
	public String email;

	@NotNull(message = "Username is empty")
	public String username;

	@NotNull(message = "Password is empty")
	@Size(min = 6, message = "Password must be at least 6 characters long")
	public String password;


	public boolean activated = false;


    public String pin;



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

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
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
				", activated=" + activated +
				'}';
	}
}
