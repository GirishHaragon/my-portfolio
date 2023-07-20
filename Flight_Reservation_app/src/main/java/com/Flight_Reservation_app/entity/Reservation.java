package com.Flight_Reservation_app.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Reservation extends AbstractEntity{
	
	private boolean checkedIn;//Why Booolean bcz we r only true/false is going to be stored. 
	private int numberOfBags;
	@OneToOne//To specify we r mapping the fields one to one way we use @OneToOne annotation.
	private Passenger passenger;//Here is a challenge, we need one to one mapping for this field. //we r not directly creating id field of passenger table but creating the reference of Passenger class.
	@OneToOne
	private Flight flight;//Here we have created Object within Object.
	//For now we r not creating the field 'created' here. We will deal with it later.
	
	public boolean isCheckedIn() {
		return checkedIn;
	}
	public void setCheckedIn(boolean checkedIn) {
		this.checkedIn = checkedIn;
	}
	public int getNumberOfBags() {
		return numberOfBags;
	}
	public void setNumberOfBags(int numberOfBags) {
		this.numberOfBags = numberOfBags;
	}
	public Passenger getPassenger() {
		return passenger;
	}
	public void setPassenger(Passenger passenger) {
		this.passenger = passenger;
	}
	public Flight getFlight() {
		return flight;
	}
	public void setFlight(Flight flight) {
		this.flight = flight;
	}
}