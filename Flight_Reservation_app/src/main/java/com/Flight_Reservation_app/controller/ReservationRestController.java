package com.Flight_Reservation_app.controller;

import java.util.Optional;

import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Flight_Reservation_app.dto.ReservationUpdateRequest;
import com.Flight_Reservation_app.entity.Reservation;
import com.Flight_Reservation_app.repository.ReservationRepositroy;

@RestController//It helps to convert JAVA object to JSON objects and JSON object back to JAVA object.
public class ReservationRestController {
	
	@Autowired
	private ReservationRepositroy reservationRepo;
	
	@RequestMapping("/reservation/{id}")//To supply the data from Url to this method we have to use @Pathvariable.
	public Reservation findReservation(@PathVariable("id") Long id) {
		Optional<Reservation> findById = reservationRepo.findById(id);
		Reservation reservation = findById.get();//This will give us the exact entity class object. Then after assigning to new local var it gives the exact matching entity object.
		return reservation;
	}
	//What the RESTful webservices does by the way is, we can just make use of the URL, and can do all the CRUD Operations with the DB.
	
	@RequestMapping("/reservation")
	public Reservation updateReservation(@RequestBody ReservationUpdateRequest request) {//@RequestBody will bind the JSON object data with the JAVA class object. @RequestBody will take the JSON content on body section of Postman and stores it in ReservationUpdateRequest request. 
		Optional<Reservation> findById = reservationRepo.findById(request.getId());//This findById is giving us the Optional class object and we have to convert it to Reservation object.
		Reservation reservation = findById.get();
		reservation.setCheckedIn(request.isCheckedIn());//In the reservation object we r just changing the checkedIn and noOfBags and updating the reservation table.  
		reservation.setNumberOfBags(request.getNoOfBags());//And then we r saving the updated reservation object.
		reservationRepo.save(reservation);
		return null;
	}
}