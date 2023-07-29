package com.CheckIn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.CheckIn.integration.ReservationRestfulClient;
import com.CheckIn.integration.dto.Reservation;
import com.CheckIn.integration.dto.ReservationUpdateRequest;

@Controller
public class ReservationController {

	@Autowired
	private ReservationRestfulClient restClient;//Here we r creating a reference variable of the type interface, where class upcasting will happen, 
	
	@RequestMapping("/startCheckIn")
	public String startCheckIn() {
		return "startCheckIn";
	}
	
	@RequestMapping("/proceedCheckIn")//This is the place from where we need to call our consuming services part of the application. 
	public String proceedCheckIn(@RequestParam("id") Long id, ModelMap modelMap) {
		Reservation reservation = restClient.findReservation(id);
//		System.out.println(reservation.getId());
//		System.out.println(reservation.getNumberOfBags());
//		System.out.println(reservation.isCheckedIn());
//		System.out.println(reservation.getPassenger().getFirstName());//If we just write "reservation.getPassenger()" then it give the object address bcs Passgenger is an object we created in Reservation class not a field. Therefore we write "reservation.getPassenger().getFirstName()" by which we can get all the contents of the passenger object.
		modelMap.addAttribute("reservation", reservation);
		return "displayReservation";
	}
	
	@RequestMapping("proceedToCheckIn")
	public String ProceedToCheckIn(@RequestParam("id")Long id, @RequestParam("numberOfBags") int numberOfBags, @RequestParam("checkedIn") boolean checkedInStatus) {//We r taking the form data and converting or copying them to Java object of ReservationUpdateRequest class. So that we can send the content as JSON object by the Url/Api.
		ReservationUpdateRequest reservation = new ReservationUpdateRequest();
		reservation.setId(id);
		reservation.setNumberOfBags(numberOfBags);
		reservation.setCheckedInStatus(true);//We are hard coding the value here.
		
		restClient.updateReservation(reservation);
		return "confirmReservationUpdate";
	}
}