package com.CheckIn.integration;

import com.CheckIn.integration.dto.Reservation;

public interface ReservationRestfulClient {
	
	public Reservation findReservation(Long id);
	
}