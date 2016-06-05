package com.devpmts.kolporit.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.devpmts.kolporit.domain.Election;

public interface ElectionRepository extends MongoRepository<Election, String> {

	@Override
	public List<Election> findAll();

}
