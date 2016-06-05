package com.devpmts.kolporit.domain;

import org.springframework.data.annotation.Id;

public class Party {

	@Id
	private String partyName;

	public String getPartyName() {
		return partyName;
	}

	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

	@Override
	public String toString() {
		return getPartyName();
	}
}
