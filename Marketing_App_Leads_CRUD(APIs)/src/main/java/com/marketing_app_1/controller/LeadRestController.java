package com.marketing_app_1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marketing_app_1.dto.LeadDto;
import com.marketing_app_1.entities.Lead;
import com.marketing_app_1.repositories.LeadRepository;

@RestController//Just by applying this annotation this becomes an API Class
@RequestMapping("/api/leads")//When we use this url(http://localhost:8080/api/leads) in browser, it will automatically call the below method, this method will now interact with the DB, will take all the data, it put that into JSON object, and that JSON object we can see in the browser. Let's just test the application. 
public class LeadRestController {

	@Autowired
	private LeadRepository leadRepo;//Here we have created a bean. In which we store the datas from the DB. 
	
	@GetMapping//This ann will take all the data from the DB, puts that into JSON Object, & will display that in browser. So after this we apply another ann @RequestMapping to generate the URL.
	public List<Lead> getAllLeads(){//Here we want to read all the data from the DB, Lead Import from util package & <Lead> import from entities package.
		List<Lead> leads = leadRepo.findAll();//But now how do we call this method? First we need to now read the data from the DB put that into JSON object and give it to the end user.
		return leads;//So we have transfered the DB contents to java object (leads). Then we will take all the contents from this object & put it on JSON object & display that in our browser, So we r not developing the front end part here. We will just develop an API that is a JSON object which contains all data from DB & give it to the Front End team/Client to carry the Frontend work as they required. For this we apply @GetMapping ann.
	}
	
	//http://localhost:8080/api/leads
	@PostMapping
	public void createLead(@RequestBody Lead lead) {//In PostMan we pasted the url, and copied first JSON object & pasted in new tab with options Post-Raw-JSON, then paste the same url in there. We are only dealing with Backend code, because its Post option in Postman & PostMapping annotation on this method, controller knows that it has to call this method only. In this method we need to take the JSON content, & we need to copy that to lead object so that Java Object gets the record saved into DB. To copy the content from JSON to JAVA object we use anno @RequestBody. This ann takes the JSON content & copies that to Lead object which is java. 
		leadRepo.save(lead);//If @RequestBody is not used the JSON content will not be copied to lead java object.
	}
	
	//http://localhost:8080/api/leads/1
	@DeleteMapping("/{id}")
	public void deleteLead(@PathVariable("id") long id) {//PathVariable reads the value from the URL.
		leadRepo.deleteById(id);
	}
	//@DeleteMapping will delete the record, @PostMapping will create the record, @GetMapping will get all the record.
	
	//http://localhost:8080/api/leads/1
	@PutMapping("/{id}")
	public void updateLead(@RequestBody LeadDto dto, @PathVariable("id") long id) {//While we update the record we need to give the ID no. Now the JSON Object content will go to lead via @RequestBody and id no we'll read it with @Pathvariable. So 2 things will go to this method JSON objec & the Id no.  
		Lead l = new Lead();//Because in dto the data is there, that data we need to update that in Entity, then re-save it into the DB.
		l.setId(id);//Once we get the record from ID no, we need to set the ID no same. so there will be updated content into lead with Id no, but here we will be using a class Dto. so the content will go to dto variable.
		l.setFirstName(dto.getFirstName());
		l.setLastName(dto.getLastName());
		l.setEmail(dto.getEmail());
		l.setMobile(dto.getMobile());
		leadRepo.save(l);
	}//So we tested Update feature by updating ID 9 from Carl to Mike & all CRUD operations on DB via API URL,  
}
//Before Testing we should know that=> earlier this whole thing, Data that we read from the DB would get displayed as XML, but XML as said is difficult to implement which is known as SOAP web services. But what r we learning here is REST web services, there is something called as RESTFUL also. REST & RESTFUL both will extract the data as JSON object only. Keep it in mind Later we'll cover full on it. JSON object is like the Flower bracket {}.
//Which means with the URL/API now we can give it to frontend team & tell "you can read all the data from this url, and display that in Angular". Our backend is now common. Let's say Android team comes to us & tells they want to display that in their app, so we'll give this url and tell, you take the content & display that in ur app, like that IOS team comes & we do the same (giving same url). That means this is the common interface(url) that interacts with the database and with this URL any frontend team can use that & extract the content and form the content.
//But now in our API we have just retrieved the data from DB, but to perform further CREate Update & Delete we cannot test CUD in browser. We can tell that API is a URL that interacts with the Backend, API URL can do CRUD Operation on DB. But in order to do the Create Update & Delete, we Use popular tool PostMan 