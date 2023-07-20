package com.Flight_Reservation_app.service;

import com.Flight_Reservation_app.dto.ReservationRequest;
import com.Flight_Reservation_app.entity.Reservation;

public interface ReservationService {
	Reservation bookFlight(ReservationRequest request);
}
