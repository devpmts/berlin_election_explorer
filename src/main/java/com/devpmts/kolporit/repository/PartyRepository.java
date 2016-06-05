package com.devpmts.kolporit.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.devpmts.kolporit.domain.Party;

public interface PartyRepository extends MongoRepository<Party, String> {

	@Override
	public List<Party> findAll();
}
