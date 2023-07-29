package com.CheckIn.integration.dto;

public class ReservationUpdateRequest {
	private Long id;
	private int numberOfBags;
	private boolean checkedInStatus;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getNumberOfBags() {
		return numberOfBags;
	}
	public void setNumberOfBags(int numberOfBags) {
		this.numberOfBags = numberOfBags;
	}
	public boolean isCheckedInStatus() {
		return checkedInStatus;
	}
	public void setCheckedInStatus(boolean checkedInStatus) {
		this.checkedInStatus = checkedInStatus;
	}
}
