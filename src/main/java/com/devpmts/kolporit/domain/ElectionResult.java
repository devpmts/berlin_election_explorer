package com.devpmts.kolporit.domain;

import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.Id;

public class ElectionResult {

	@Id
	String _id;

	public Wahlbezirk wahlbezirk;

	// public Election election;

	public Map<String, Integer> results;

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
