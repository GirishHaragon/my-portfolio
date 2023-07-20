package com.CheckIn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.CheckIn.integration.ReservationRestfulClient;
import com.CheckIn.integration.dto.Reservation;

@Controller
public class ReservationController {

	@Autowired
	private ReservationRestfulClient restClient;//Here we r creating a reference variable of the type interface, where class upcasting will happen, 
	
	@RequestMapping("/startCheckIn")
	public String startCheckIn() {
		return "startCheckIn";
	}
	
	@RequestMapping("/proceedCheckIn")//This is the place from where we need to call our consuming services part of the application. 
	public String proceedCheckIn(@RequestParam("id") Long id) {
		Reservation reservation = restClient.findReservation(id);
		System.out.println(reservation.getId());
		System.out.println(reservation.getNumberOfBags());
		System.out.println(reservation.isCheckedIn());
		System.out.println(reservation.getPassenger().getFirstName());//If we just write "reservation.getPassenger()" then it give the object address bcs Passgenger is an object we created in Reservation class not a field. Therefore we write "reservation.getPassenger().getFirstName()" by which we can get all the contents of the passenger object. 
		return "";
	}
}