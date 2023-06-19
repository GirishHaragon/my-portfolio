package com.marketing_app_1.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component//If we don't write this annotation, object of this class cannot be created with @Autowired. This class we need to hand it over to spring boot, telling that "SB you manage this class, u help me create this object of this class" & thats possible when we apply this annotation. If we don't apply @Autowired will not work.
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender mailSender;//This will create the required bean. And JavaMailSender is a built-in method.
	
	@Override
	public void sendEmail(String to, String sub, String msg) {
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(sub);//This sub has to be same as written in this method Argument.
		message.setText(msg);
		
		mailSender.send(message);//This is like a send button.
	}

}
