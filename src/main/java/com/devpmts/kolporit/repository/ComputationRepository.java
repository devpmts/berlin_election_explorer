package com.devpmts.kolporit.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.devpmts.kolporit.domain.Computation;

public interface ComputationRepository extends MongoRepository<Computation, String> {

	@Override
	public List<Computation> findAll();

}
