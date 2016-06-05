package com.devpmts.kolporit.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.devpmts.kolporit.domain.Bezirk;

public interface BezirkRepository extends MongoRepository<Bezirk, String> {

	@Override
	public List<Bezirk> findAll();

	public List<Bezirk> findByBezirkNummer(Integer bezirkNummer);

}
