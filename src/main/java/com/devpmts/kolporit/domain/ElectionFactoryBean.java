package com.devpmts.kolporit.domain;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.devpmts.kolporit.repository.ElectionRepository;

public class ElectionFactoryBean implements FactoryBean<Election> {

	@Autowired
	ElectionRepository electionRepo;

	@Override
	public Election getObject() throws Exception {
		return electionRepo.findAll().get(0);
	}

	@Override
	public Class<?> getObjectType() {
		return Election.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
