package com.devpmts.kolporit.domain;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;

public class Election {

	public Map<String, Party> parties;

	public List<ElectionResult> electionResults;

	@Id
	private String name;

	public int numbersOfComputations;

	public Party getParty(String partyName) {
		return parties.get(partyName);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
