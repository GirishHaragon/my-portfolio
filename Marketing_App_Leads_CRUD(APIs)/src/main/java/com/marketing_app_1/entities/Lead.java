package com.marketing_app_1.entities;

import javax.persistence.*;//Better to avoid errors/mistakes, we change Entity to *. Bcs all imports should happens from this package only.

@Entity//In the DB Table header row is not generating in sequence or as the case we have written in here.. Which means Table header row is generating as id, email, first_name, last_name, mobile. Also the first letter of Table name and first letters all headers in table are being small.
@Table(name="leads")//Class name is different than table name in DB, then we can map the DB-table like this.
public class Lead {//Lead - Marketing guys Whenever an enquiry they get, they don't call that as an Enquiry data but they call it as a Lead. Any interested customer in the product who has done the enquiry, he is termed as the Lead.
	//Whenever our customer walks in, we want to take their basic details like First&Last Name, email, mobile, etc..
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//This will auto-increment the value ID.
	private long id;
	//If we don't want the DB names to be like CamelCasing(as in Java), instead we like to have in Snake casing, like first_name as column name in table. And the variables to be different. To do this we apply the below Annotation.
	@Column(name ="first_name", nullable=false)//If variable names(firstName) and column names (first_name) are not same then use @Column Annotation. & if we don't want this to be null then use nullable=false.
	private String firstName;//Camel casing must & should, if we don't use Camelcasing firstName then there will be a problem in some instances..
	@Column(name ="last_name", nullable=false)
	private String lastName;
	@Column(name ="email", nullable=false, unique=true)//If we don't want duplicate email ids then have to use unique=true in the argument.
	private String email;
	@Column(name ="mobile", nullable=false, unique=true)
	private long mobile;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
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
}
