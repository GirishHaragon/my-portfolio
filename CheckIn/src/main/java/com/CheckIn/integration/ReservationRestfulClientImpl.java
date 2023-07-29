package com.CheckIn.integration;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.CheckIn.integration.dto.Reservation;
import com.CheckIn.integration.dto.ReservationUpdateRequest;

//This method will be responsible to take the data from the url of Restful web services.
@Component
public class ReservationRestfulClientImpl implements ReservationRestfulClient {

	@Override
	public Reservation findReservation(Long id) {
		RestTemplate restTemplate = new RestTemplate();//Mandatory to create the object of RestTemplate. Which has lot of builtin methods in this class.
		Reservation reservation = restTemplate.getForObject("http://localhost:8080/flights/reservation/"+id, Reservation.class);//We paste the API/webservice url which has to be consumed. We r making use of the Url's JSON object and mapping the data to the Reservation class we replicated by the url which helps us to convert the JSON object to JAVA object.
		return reservation;//We cannot directly consume the JSON object and perform activities. We need to convert that JSON objects/contents to JAVA class object, therefore we created the Reservation, passenger, flight Java classes to perform the activities by creating objects of those classess.
	}//Our code understands that the JSON contents in the URL is to be mapped with Reservation class when we give the statement as "restTemplate.getForObject("http://localhost:8080/flights/reservation/"+id, Reservation.class)".
//Now we need to call this above method from the controller layer.

	@Override
	public Reservation updateReservation(ReservationUpdateRequest request) {
		RestTemplate restTemplate = new RestTemplate();
		Reservation reservation = restTemplate.postForObject("http://localhost:8080/flights/reservation", request, Reservation.class);//The request contains the update data and we r mapping the JSON content with the Entity class Reservation in the Main project.
		return reservation;
	}

}