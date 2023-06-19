package com.marketing_app_1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marketing_app_1.entities.Lead;
import com.marketing_app_1.repositories.LeadRepository;

@Service//Whenever we build a service layer we must use this @Service Annotation otherwise it will not be treated as a service layer(Mandatory). 
public class LeadServiceImpl implements LeadService {//If we miss the @Service Annotation, then we get Exception saying The injection point has the Following annotations, Only when @Service is used then only @Autowired will work on the controller layer...

	//Here we need it to save to the database & and we do here dependency injection.
	@Autowired //We have created a Bean/object names leadRepo.
	private LeadRepository leadRepo;
	
	@Override//This method is overriding the method in interface,,
	public void saveLead(Lead lead) {
		leadRepo.save(lead);
	}

	@Override
	public List<Lead> getAllLeads() {//Here also we import List from util package
		List<Lead> leads = leadRepo.findAll();//After we write leadRepo.findAll(); we press CTRL+1 and assign it to new local var. So what findAll will do it'll go to the DB bcs there r 2 rows, findAll will create 2 object entities. And those 2 objects are stored in this List & that abj adr stored in leads.
		return leads;
	}

	@Override
	public void deleteLeadById(long id) {
		leadRepo.deleteById(id);
		
	}

	@Override
	public Lead findLeadById(long id) {
		Optional<Lead> findById = leadRepo.findById(id);//This will give us an Optional class(Ctrl+1=Assign to new LV), convert optional class to lead(<Lead>).
		return findById.get();//On Optional class we apply .get(). That will convert optional class to Lead and we r returning the Lead object.
	}
}
