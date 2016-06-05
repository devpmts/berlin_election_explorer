package com.devpmts.kolporit.domain;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.Id;

public class Bezirk {

	public String name;

	@Id
	public Integer bezirkNummer;

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
