package com.Flight_Reservation_app.entity;

import javax.persistence.Entity;

@Entity
public class User extends AbstractEntity{//Interview que, "Did you use Inheritance concept in your project? Where did you use that?"  "Java Implementation where all you did in your project?" -> Sir whenever i created an entity class what i did there is encapsulation i implemented. Now for my project the Id field in the entity class was common so i applied inheritance there and i used that id field in every class by inheritance. And that helped me with re-usability of code.
	
	private String firstName;//"Where was encapsulation used in your project?" -> Sir i did encapsulation to hide the implementation details. And made the fields private and used Dto class instead of exposing entity class.
	private String middleName;
	private String lastName;
	private String email;
	private String password;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
