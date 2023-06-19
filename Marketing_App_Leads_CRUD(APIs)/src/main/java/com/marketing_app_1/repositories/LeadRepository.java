package com.marketing_app_1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketing_app_1.entities.Lead;

public interface LeadRepository extends JpaRepository<Lead, Long> {
//The table name we changed from JpaRepository<T to <Lead is the Table name that is mentioned in the java interface Lead.java 
}