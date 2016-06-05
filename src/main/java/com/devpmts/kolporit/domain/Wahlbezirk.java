package com.devpmts.kolporit.domain;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.Id;

public class Wahlbezirk {

	@Id
	String id;

	public Bezirk bezirk;

	public Integer nummer;

	public Integer plz;

	public String anschrift;

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
