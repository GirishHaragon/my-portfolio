package com.Flight_Reservation_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Flight_Reservation_app.dto.ReservationRequest;
import com.Flight_Reservation_app.entity.Reservation;
import com.Flight_Reservation_app.service.ReservationService;

@Controller
public class ReservationController {
	
	@Autowired
	private ReservationService reservationService;//Class up-casting automatically happens where we are creating object/bean of interface which is not possible but the SpringIoc will create object of the ReservationServiceImpl itself. A reference of variable of the type interface can hold any object address. So what Spring Boot does is even if we r giving ReservationService Interface, Spring IOC will automatically create an object of ReservationServiceimpl class. By Class-Upcasting. So whenever we r using interface and a class, give the interface name and Spring IOC will automatically create a dependent class object. 
	
	@RequestMapping("/confirmReservation")
	public String confirmReservation(ReservationRequest request, ModelMap modelMap) {
		Reservation reservationId = reservationService.bookFlight(request);
		modelMap.addAttribute("reservationId", reservationId.getId());
		return "confirmReservation";
	}
	
}