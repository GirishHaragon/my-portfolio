package com.blog;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication//Wherever  the @SpringBootApplication anno is written, that happens to be starting point of our projects. this can asked in Interviews as "What is @SpringBootApplication" -> It indicates that is where the Bootstrapping starts form, which means that is where the project execution begins from. And This has a main method. So when we run this, the project starting point happens to begin from here. It is also considered as a Configuration class. Configuration class means when Dependency Injection doesn't work on creation of required bean for External Library (for ex. ModelMapper), we can configure the bean type in here by a method in here, therefore it is called a Configuration class..
public class BlogApplication {//This class will be default present, and the project starts from this class file.

	public static void main(String[] args) {

		SpringApplication.run(BlogApplication.class, args);
	}//Open Mysql workbench, if not application may crash while execution.

	@Bean//This anno is applied on a method, when an external library class object we want to create. //Now by adding this annot, in our config file, now IOC has the info, that if we use ModelMapper, then it knows this is the object to create.
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}//We r not gonna put this project in our Resume, this is just for our learning and understanding. This is only to explore the skills, we r doing it. So if anybody tells us to Develop SignIn feature, SignUp feature we should be able to do it.