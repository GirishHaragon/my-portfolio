package com.Flight_Reservation_app.utilities;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {
	
	@Autowired
	private JavaMailSender sender;
	
	public void sendItinerary(String toAddress, String filePath, String flightNum, String opAirLine, String depCity, String depTime) {//The String filePath is the attachment, from where the filePath consist of the pdf ticket file
		System.out.println(filePath);
			
		MimeMessage message = sender.createMimeMessage();
		try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(message,true);
			messageHelper.setTo(toAddress);
			messageHelper.setSubject("Itinerary Of Flight");
			messageHelper.setText("Your ticket is Confirmed for Flight Number: "+flightNum+".  Operating AirLine company is: "+opAirLine+".  Departing from: "+depCity+".  On time at: "+depTime+".   Please find the ticket attached to this email. Happy Journey! Bon Voyage..");
			messageHelper.addAttachment("Itinerary", new File(filePath));//Add attachment is the extra thing in here.
			sender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
