package com.marketing_app_1.dto;

public class LeadDto {//In Spring Boot any class reference variable if we give, automatically in SB data gets copied into it. The form data we can put into any java class but that java class should have the variables matching to the name attribute in jsp.
	private long id;//We were missing this field while update feature adding. Without this field we cannot put the id into the object. After writing this line we press Ctrl+1 and create getter&setter for this id.
	private String firstName;
	private String lastName;
	private String email;
	private long mobile;//After this we generate getters & setters.
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public long getMobile() {
		return mobile;
	}
	public void setMobile(long mobile) {
		this.mobile = mobile;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
}
