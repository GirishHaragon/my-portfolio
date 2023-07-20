package com.Flight_Reservation_app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.Flight_Reservation_app.dto.ReservationRequest;
import com.Flight_Reservation_app.entity.Flight;
import com.Flight_Reservation_app.entity.Passenger;
import com.Flight_Reservation_app.entity.Reservation;
import com.Flight_Reservation_app.repository.FlightRepository;
import com.Flight_Reservation_app.repository.PassengerRepository;
import com.Flight_Reservation_app.repository.ReservationRepositroy;
import com.Flight_Reservation_app.utilities.EmailUtil;
import com.Flight_Reservation_app.utilities.PdfGenerator;

@Service//"What is this @Service anno or layer?" -> It helps us to define that the class is a service layer of our application, which is a Spring boot annotation. And if we don't put this annotation then this class would be ordinary class. And for ordinary class a bean cannot be created using autowired. "Can you Tell me why would we create service layer?" -> Let's take a scenario, i am booking a flight wherein flight & passenger details & card details needs to be saved to the DB, in this case i would create a service layer whoch will now deal with 3 DB Operation. Inserting Passenger details, Flight details & inserting Card details for payment. And that service layer help us to build the project with more structured manner.
public class ReservationServiceImpl implements ReservationService {

	@Autowired
	private PassengerRepository passengerRepo;
	
	@Autowired
	private FlightRepository flightRepo;
	
	@Autowired
	private ReservationRepositroy reservationRepo;
	
	@Autowired
	private PdfGenerator pdfGenerator; 
	
	@Autowired
	private EmailUtil emailUtil;
	
	//The purpose of this service layer is simple: We want this layer to perform 3 tasks, 1-Its going to inject the passenger details/data in the passenger table, 2-It's going to inject the flight details/data in flight table, 3-It will save the passenger data, flight data into the reservation data. Bcz we r dealing with multiple datas we r using service layer. 
	@Override
	public Reservation bookFlight(ReservationRequest request) {
		
		
//		String firstName = request.getFirstName();
//		String middleName = request.getMiddleName();
//		String lastName = request.getLastName();
//		String email = request.getEmail();
//		String phone = request.getPhone(); //We have increased the no of lines of code by storing the reservation details.
		//Instead of writing these above lines we can reduce the line of codes by directly using the data from request variable in the passenger object variable using get and set method.
		
		Passenger passenger = new Passenger();
		passenger.setFirstName(request.getFirstName());
		passenger.setMiddleName(request.getMiddleName());
		passenger.setLastName(request.getLastName());
		passenger.setEmail(request.getEmail());
		passenger.setPhone(request.getPhone());
		passengerRepo.save(passenger);//By this we have pushed the passenger data into passenger DB table.
		//Now this passenger data will go/saved to the DB passenger table.
		
		long flightId = request.getFlightId();//From the request variable we get the flightId and with that from the flightRepo we can get all the details of flight using findById method.
		Optional<Flight> findById = flightRepo.findById(flightId);
		Flight flight = findById.get();
		//Flight data was already there in the DB, nowhere we developed feature to add flight in this project, so we are just getting the flight object by optional class.
		
		//Now passenger and flight details will go to the reservation table. Bcz this service layer will confirm the reservation considering these 3 tables(Passenger, Flight, Reservation).
		Reservation reservation = new Reservation();
		reservation.setFlight(flight);//Whenever the reservation variable is saved all the flight & passenger details will be saved into the Reservation Table.
		reservation.setPassenger(passenger);
		reservation.setCheckedIn(false);
		reservation.setNumberOfBags(0);
		reservationRepo.save(reservation);
//		PDFGenerator pdf = new PDFGenerator();//We are not using these lines bcz this is depricated.
//		pdf.generatePDF(filePath+reservation.getId()+".pdf", request.getFirstName(), request.getEmail(), request.getPhone(), flight.getOperatingAirlines(), flight.getDateOfDeparture(), flight.getDepartureCity(), flight.getArrivalCity());
		
		String filePath = "D:\\Sanket\\Sanket Java Developer\\STS Old Version Proj Workspace\\Flight_Reservation_app\\Ticket_Booked\\reservations"+reservation.getId()+".pdf";
		String toAddress = reservation.getPassenger().getEmail();
		String flightNum = reservation.getFlight().getFlightNumber().toString();
		String opAirLine = reservation.getFlight().getOperatingAirlines();
		String depCity = reservation.getFlight().getDepartureCity();
		String depTime = reservation.getFlight().getEstimatedDepartureTime().toString();
		
//		pdfGenerator.generatePDF(filePath, name, emailId, phone, operatingAirlines, departureDate, departureCity, arrivalCity);
		pdfGenerator.generateItenerary(reservation, filePath);
		emailUtil.sendItinerary(toAddress, filePath, flightNum, opAirLine, depCity, depTime);
		
		return reservation;
	}
}