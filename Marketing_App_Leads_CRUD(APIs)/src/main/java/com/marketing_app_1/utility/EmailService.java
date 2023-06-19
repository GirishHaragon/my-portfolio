package com.marketing_app_1.utility;

public interface EmailService {
	public void sendEmail(String to, String sub, String msg);//To send an email we require to addr, subj & message. And we will override this method by creating a class in utility..
}