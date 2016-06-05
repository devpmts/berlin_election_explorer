package com.devpmts.kolporit.bootstrap;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.devpmts.DevsLogger;
import com.devpmts.kolporit.domain.Election;
import com.devpmts.kolporit.domain.ElectionResult;
import com.devpmts.kolporit.domain.Party;
import com.devpmts.kolporit.domain.Wahlbezirk;
import com.devpmts.kolporit.repository.BezirkRepository;
import com.devpmts.kolporit.repository.ElectionRepository;
import com.devpmts.kolporit.repository.ElectionResultRepository;
import com.devpmts.kolporit.repository.PartyRepository;
import com.devpmts.kolporit.repository.WahlbezirkRepository;
import com.devpmts.util.DevpmtsUtil;

@Component
public class ElectionReader {

	@Autowired
	ElectionRepository electionRepo;

	@Autowired
	ElectionResultRepository electionResultRepo;

	@Autowired
	BezirkRepository bezirkRepo;

	@Autowired
	WahlbezirkRepository wahlbezirkRepo;

	@Autowired
	PartyRepository partyRepo;

	Election election;

	String[] csvHeader;

	public void readElectionFile(File electionFile) {
		createElection(electionFile);
		createHeader(electionFile);
		computePartiesFromHeader();
		computeResults(electionFile);
		electionRepo.save(election);
	}

	private void createElection(File electionFile) {
		election = new Election();
		election.setName(electionFile.getName());
	}

	private void createHeader(File electionFile) {
		csvHeader = DevpmtsUtil.readFirstLine(electionFile.getPath());
		for (int i = 0; i < csvHeader.length; i++) {
			csvHeader[i] = csvHeader[i].toUpperCase();
		}
	}

	private void computeResults(File electionFile) {
		List<ElectionResult> electionResults = new ArrayList<>();
		Function<String[], ElectionResult> converter = values -> {
			ElectionResult electionResult = createElectionResult(values);
			electionResults.add(electionResult);
			return electionResult;
		};
		DevsLogger.log("reading election file");
		DevpmtsUtil.readAndPersistFromCsv(electionFile.getPath(), converter, electionResultRepo);
		DevsLogger.log("save election");
		election.electionResults = electionResults;
		electionRepo.save(election);
	}

	private void computePartiesFromHeader() {
		List<Party> parties = new ArrayList<>();
		for (String column : csvHeader) {
			Party party = new Party();
			party.setPartyName(column);
			parties.add(party);
		}
		election.parties = parties.stream().//
				collect(Collectors.toMap(Party::getPartyName, party -> party));
	}

	private ElectionResult createElectionResult(String[] values) {
		ElectionResult electionResult = new ElectionResult();
		electionResult.results = createResultMap(values);

		Integer nummer = getWahlbezirkNummer(electionResult);
		Integer bezirksNummer = getBezirksNummer(electionResult);
		List<Wahlbezirk> wahlbezirkResult = wahlbezirkRepo.findByNummer(nummer).stream()
				.filter(wb -> wb.bezirk.bezirkNummer == bezirksNummer).collect(Collectors.toList());
		if (!wahlbezirkResult.isEmpty()) {
			electionResult.wahlbezirk = wahlbezirkResult.get(0);
		} else {
			electionResult.wahlbezirk = wahlbezirkRepo.findAll().get(0);
		}
		return electionResult;
	}

	private Integer getWahlbezirkNummer(ElectionResult electionResult) {
		return electionResult.results.get(ElectionCsvColumnNames.WAHLBEZIRK);
	}

	private Integer getBezirksNummer(ElectionResult electionResult) {
		return electionResult.results.get(ElectionCsvColumnNames.BEZIRK_NUMMER);
	}

	private Map<String, Integer> createResultMap(String[] values) {
		Map<String, Integer> results = new HashMap<>();
		for (int i = 0; i < values.length; i++) {
			String column = csvHeader[i];
			String value = values[i];
			results.put(column, DevpmtsUtil.getIntOrZero(value));
		}
		return results;
	}

}