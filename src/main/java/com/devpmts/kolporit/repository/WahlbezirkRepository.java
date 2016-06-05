package com.devpmts.kolporit.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.devpmts.kolporit.domain.Wahlbezirk;

public interface WahlbezirkRepository extends MongoRepository<Wahlbezirk, String> {

	@Override
	public List<Wahlbezirk> findAll();

	public List<Wahlbezirk> findByNummer(Integer nummer);
}
