package com.CheckIn.integration;

import com.CheckIn.integration.dto.Reservation;
import com.CheckIn.integration.dto.ReservationUpdateRequest;

public interface ReservationRestfulClient {
	
	public Reservation findReservation(Long id);
	public Reservation updateReservation(ReservationUpdateRequest request);
}