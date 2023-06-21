package com.marketing_app_1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.marketing_app_1.dto.LeadDto;
import com.marketing_app_1.entities.Lead;
import com.marketing_app_1.service.LeadService;
import com.marketing_app_1.utility.EmailService;

@Controller//Just by applying this annotation will make this class a controller layer. And what a controller layer does, it interacts with the view & with backend business logic. So we don't have servlets in springboot, Servlets gone & controller classes took forward.
public class LeadController {
	
	@Autowired//How do we call the service layer from the controller layer? =>Firstly u need to create an object of LeadServiceImpl.
	private LeadService LeadService;//This is an Interface and we imported it. For data hiding we used private here, which is not Mandatory. Now what @Autowired will do?=>It'll automatically create object of LeadServiceImpl, and inject it into this Interface reference variable, Class up-casting happening, LeadServiceImpl is child and LeadService is the parent. Child object address will automatically get injected to Parent.
	
	@Autowired//For this to work it requires @Component written on top of class EmailServiceImpl, only then SpringIOC will load this, as there is something called Tomcat container no! (Logical area) like that there is a logical area in Spring where this class is loaded, once the class is loaded into that logical area, it's object can be created using @Autowired. 
	private EmailService emailService;
	
	//http://localhost:8080/create
	@RequestMapping("/create")//we can write anything we want in the link.
	public String viewCreateLead() {
			return "create_lead";
	}
	//1st way of reading the form data
	//To run the saveLead form in create_lead.jsp, we will be creating this method 
	@RequestMapping("/saveLead")//This should match with the form action attribute.//There are totally 3 ways we can read the form Data. One of the way we have seen with this method.  
	public String saveLead(@ModelAttribute Lead lead, ModelMap model) {//To take the form data just type the Lead lead (Entity Class). Lead should be imported (Ctrl+1). All our form data will go to this object automatically. We usually use annotation @ModelAttribute which is not mandatory, without using this also, the data from the form will go to this object. But bcs they'll ask this in interviews, What is @ModelAttribute Annotation? => @ModelAttribute will copy the data from the form to the object. In the argument we create a model variable, then we give addAttriute to insert the required message into it. There two ways to share the data from controller to view- 1.Use Model 2.Use ModelMap.     
		LeadService.saveLead(lead);//This helps us to save the data from object lead. By this line we are calling the object LeadService and saving it to DB using lead entity...
		emailService.sendEmail(lead.getEmail(), "Registered Successfully in our Marketing Community", "Welcome, this is a computer generated email for successfully registering to become a part of our community. Name :"+lead.getFirstName()+" "+lead.getLastName()+" -> "+lead.getMobile());//We will now send the email when we save the Lead.
		model.addAttribute("msg", "Record is Saved!!");//Now this will act as set attribute equivalent to it as addAttribute. 
		return "create_lead";//This acts like Request Dispatcher.
	}
	
	//2nd way of reading form data would be,
//	@RequestMapping("/saveLead")
//	public String saveLead(
//			@RequestParam("firstName") String firstName,//This line will only Read FirstName Data and this data will further stored into the String Variable String firstName. The RequestParam("firstName") should match the name attribute in jsp.
//			@RequestParam("lastName") String lastName,
//			@RequestParam("email") String email,
//			@RequestParam("mobile") long mobile) {//All the data we are reading now will get saved into the variables mentioned lastly in all the lines.
//		Lead lead = new Lead();//To Initialise the object we are writing the below lines.
//		lead.setFirstName(firstName);//But this approach is little Lengthier when bigger form are to be generated.
//		lead.setLastName(lastName);
//		lead.setEmail(email);
//		lead.setMobile(mobile);//Mobile here gives error if we dont change the data type of mobile String to long. We can write long(Datatype) or Long, both will work
//		LeadService.saveLead(lead);//The LeadService is the name of your service layer & we can't change the case of the name.
//		return "create_lead";
//	}
	
	//3rd way of reading/saving the form data.
//	@RequestMapping("/saveLead")
//	public String saveLead(LeadDto leadDto) {//Here we will not give the Entity class name, we can give any java class name like LeadDTO classes(DTO-Data Transfer Objects). Initially we don't have this class, so create one by Ctrl+1.
//		Lead lead = new Lead();//To copy the data to Entity object we created an Entity object-lead 
//		lead.setFirstName(leadDto.getFirstName());
//		lead.setLastName(leadDto.getLastName());
//		lead.setEmail(leadDto.getEmail());
//		lead.setMobile(leadDto.getMobile());//By doing SOP, the data gets into console not into DB, only Entity object can only go to DB. Until we copy this data to Entity object.
//		LeadService.saveLead(lead);//Here LeadService.java will work as Model layer(like we used to call model layer as DAOService&Impl) not like Service layer but instead model layer we r calling it as Service layer.
//		return "create_lead";
//		//When we submit the form the form data will automatically get copied into (LeadDto leadDto). But this is not Entity object. It's an ordinary java class with Encapsulation & into that object as well form data can go. 
//	}
	//http://localhost:8080/listall
	@RequestMapping("/listall")
	public String getAllLeads(Model model) {//After creating this we go to Service layer and build a method which will return list of Lead => List<Lead>
		List<Lead> leads = LeadService.getAllLeads();
		model.addAttribute("leads", leads);//In this method method argument we put Model model so that we can add addAttribute and take leads then put that in double quotes then paste it in here, so that all the entity gets showed on the page. It'll act like request.setAttribute.
		return "search_result";//When we don't keep anything in return then 404 error shows on page & in console you can see the objects & their addresses generated based on the number of records present in DB.
	}
	
	@RequestMapping("/delete")//Here we have to give delete, bcs we already used delete in jsp & that cannot be different.
	public String deleteLeadById(@RequestParam("id") long id, Model model) {//Here we have built a method deleteLeadById. Now to read the data from the url we use RequestParam to fetch the data, RequestParam can read this id, & since we have given the attribute name in jsp as id we r writing here id, It will fetch that value from that url and that value it'll stored in long id. It'll just take the data & it'll initialise id written after long. 
		LeadService.deleteLeadById(id);
		List<Lead> leads = LeadService.getAllLeads();//Once we delete the record we should return back to the listall page
		model.addAttribute("leads", leads);//Here model will be showing error because it require model written in method argument bcs without that it cannot display the data. 
		return "search_result";
	}
	
	@RequestMapping("/update")
	public String getLeadById(@RequestParam("id") long id, Model model) {
		Lead lead = LeadService.findLeadById(id);//This should return back Lead object thats y we write 'Lead lead ='.
		model.addAttribute("lead", lead);
		return "update_lead";
	}
	
	@RequestMapping("/updateLead")
	public String updateLead(LeadDto dto, Model model) {//Now we need to read all that data from the form, we will use DTO(Data Transfer Object) by which all the form data will go to the dto variable in LeadDto object. Now Dto also has the id, so take this Dto and put that into Entity. 
		Lead l = new Lead();
		l.setId(dto.getId());//In Lead Dto one data we were missing that is getId, after creating id data in LeadDto then only we can use it in here.
		l.setFirstName(dto.getFirstName());
		l.setLastName(dto.getLastName());
		l.setEmail(dto.getEmail());
		l.setMobile(dto.getMobile());//Now after this don't create a new method to save the updated data, we can use the same save method.
		
		LeadService.saveLead(l);
		
		List<Lead> leads = LeadService.getAllLeads();
		model.addAttribute("leads", leads);
		return "search_result";
	}	
}