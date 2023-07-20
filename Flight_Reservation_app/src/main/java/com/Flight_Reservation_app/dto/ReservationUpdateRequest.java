package com.Flight_Reservation_app.dto;

public class ReservationUpdateRequest {//This class is utilized to store the JSON object data into this class object.
	
	private long id;
	private boolean checkedIn;
	private int noOfBags;
	
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
	public int getNoOfBags() {
		return noOfBags;
	}
	public void setNoOfBags(int noOfBags) {
		this.noOfBags = noOfBags;
	}
}
