package com.marketing_app_1.service;

import java.util.List;

import com.marketing_app_1.entities.Lead;

public interface LeadService {
	public void saveLead(Lead lead);//To this saveLead we supply the object address bcs the data is present inside this object. Lead (Ctrl+1 & Import).
			//Lead lead is the object which has the data, so the data from the form goes to this object  & this object address will go to Service layer, service layer will save the Entity object to the Database. Lead lead is the Entity object.
			//We just created an interface here actual method Overriding happens in LeadServiceImpl,, While creating the class add LeadService to the interface to it..
			//We have created this interface so as to adopt Data Abstraction Concept.
	public List<Lead> getAllLeads();//After creating getAllLeads method in Controller we will create this mthod which will return ListofLead=> List<Lead>, y we written List bcs it'll not just return one Lead object, bcs there are more than one rows of records in DB, List will return that many entity objects that needs to be stored that require a collection that is List.
	//Import List from util Package.
	public void deleteLeadById(long id);
	public Lead findLeadById(long id);
}
