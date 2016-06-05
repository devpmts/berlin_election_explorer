package com.devpmts.kolporit.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.devpmts.kolporit.domain.ElectionResult;

public interface ElectionResultRepository extends MongoRepository<ElectionResult, String> {

	@Override
	public List<ElectionResult> findAll();

}
