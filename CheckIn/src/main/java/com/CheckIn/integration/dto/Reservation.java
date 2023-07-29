package com.CheckIn.integration.dto;

//In Interviews we may asked to use microservice and handle the project from JSON object content using API url. 
public class Reservation {//They will give us URL and we have to build the structure of the class seeing the url details or analyze the JSON object. With the help of these URL/JSON we need to convert them to JAVA object, then only we can perform any operation.
	private long id;
	private boolean checkedIn;
	private int numberOfBags;
	private Passenger passenger;//Object within Object.
	private Flight flight;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
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
