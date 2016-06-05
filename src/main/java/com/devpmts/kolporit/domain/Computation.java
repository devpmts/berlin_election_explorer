package com.devpmts.kolporit.domain;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.Id;

public class Computation {

	@Id
	public String _id;

	public Wahlbezirk wahlbezirk;

	public Double computationResult;

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
