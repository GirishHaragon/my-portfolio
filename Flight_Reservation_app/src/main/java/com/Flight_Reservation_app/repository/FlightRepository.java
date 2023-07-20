package com.Flight_Reservation_app.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Flight_Reservation_app.entity.Flight;

public interface FlightRepository extends JpaRepository<Flight, Long> {

	@Query("from Flight where departureCity=:departureCity and arrivalCity=:arrivalCity and dateOfDeparture=:dateOfDeparture")//Actually the departureCity first seen is of DB column name and second departureCity we see is from Entity field, where both are being mapped.
	List<Flight> findFlights(@Param("departureCity") String from, @Param("arrivalCity") String to, @Param("dateOfDeparture") Date departureDate);//Or in IntelliJ we have also written as findByDepartureCityArrivalCityDateOfDeparture()

	//We have to face security threat while using Sql queries
	//"What is SQL Injection?" This is a testing question not for development but still interviewer might ask the question. Which comes under security testing concept.
	//SQl injection is a way of breaking an application by injecting SQL Queries through the edit boxes of the applications.
	//It weakens the security of the application.
	//Through the edit boxes of our application we try to inject SQL Queries(SQL Cheat sheets) which will break the application, & when the application breaksdown it will reveal the sensitive information like DB name, table structures etc...
}