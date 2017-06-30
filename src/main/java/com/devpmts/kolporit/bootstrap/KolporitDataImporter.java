package com.devpmts.kolporit.bootstrap;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.devpmts.kolporit.domain.Bezirk;
import com.devpmts.kolporit.domain.Election;
import com.devpmts.kolporit.domain.Party;
import com.devpmts.kolporit.domain.Wahlbezirk;
import com.devpmts.kolporit.repository.BezirkRepository;
import com.devpmts.kolporit.repository.ComputationRepository;
import com.devpmts.kolporit.repository.ElectionRepository;
import com.devpmts.kolporit.repository.ElectionResultRepository;
import com.devpmts.kolporit.repository.PartyRepository;
import com.devpmts.kolporit.repository.WahlbezirkRepository;
import com.devpmts.util.DevpmtsUtil;
import com.devpmts.util.spring.DevpmtsSpringUtil;
import com.rabbitmq.tools.json.JSONReader;

import lombok.extern.slf4j.Slf4j;

@Component
@Configuration
@Slf4j
public class KolporitDataImporter {

    static final File PARTY_FILE = DevpmtsUtil.getFile("parties.json");

    static final File STADTTEILE_FILE = DevpmtsUtil.getFile("stadtteile.json");

    static final File WAHLBEZIRKE_FILE = DevpmtsUtil.getFile("wahlbezirke.csv");

    static final File ELECTION_FILE = DevpmtsUtil.getFile("election.csv");

    @Autowired
    PartyRepository partyRepository;

    @Autowired
    BezirkRepository bezirkRepository;

    @Autowired
    WahlbezirkRepository wahlbezirkRepository;

    @Autowired
    ElectionRepository electionRepository;

    @Autowired
    ElectionResultRepository electionResultRepository;

    @Autowired
    ComputationRepository computationRepository;

    @Autowired
    ApplicationContext context;

    @Autowired
    ElectionReader electionReader;

    @PostConstruct
    public void importData() {
        log.info("emptying repos");
        clearRepositories();
        importPartys();
        log.info(partyRepository.count() + " parties imported.");
        importBezirke();
        log.info(bezirkRepository.count() + " bezirke imported.");
        importWahlbezirke();
        log.info(wahlbezirkRepository.count() + " wahlbezirke imported.");
        importElection();
    }

    void clearRepositories() {
        partyRepository.deleteAll();
        bezirkRepository.deleteAll();
        wahlbezirkRepository.deleteAll();
        electionRepository.deleteAll();
        electionResultRepository.deleteAll();
        computationRepository.deleteAll();
    }

    void importPartys() {
        String content = DevpmtsUtil.getStringFromFile(PARTY_FILE);
        List<String> parties = (List<String>) new JSONReader().read(content);
        parties.stream()//
                .map(partyName -> {
                    Party party = new Party();
                    party.setPartyName(partyName);
                    return party;
                }) //
                .forEach(party -> partyRepository.save(party));
    }

    void importBezirke() {
        String content = DevpmtsUtil.getStringFromFile(STADTTEILE_FILE);
        Map<String, String> stadtteile = (HashMap<String, String>) new JSONReader().read(content);
        stadtteile.entrySet().stream().map(entry -> {
            Bezirk bezirk = new Bezirk();
            bezirk.bezirkNummer = DevpmtsUtil.getIntOrZero(entry.getKey());
            bezirk.name = entry.getValue();
            return bezirk;
        }).forEach(bezirk -> bezirkRepository.save(bezirk));
    }

    private void importWahlbezirke() {
        DevpmtsSpringUtil.readAndPersistFromCsv(WAHLBEZIRKE_FILE.getPath(), values -> {
            Wahlbezirk wahlbezirk = new Wahlbezirk();
            int bezirkNummer = Integer.parseInt(values[0]);
            wahlbezirk.bezirk = bezirkRepository.findByBezirkNummer(bezirkNummer).get(0);
            wahlbezirk.nummer = Integer.parseInt(values[1]);
            wahlbezirk.plz = Integer.parseInt(values[2]);
            wahlbezirk.anschrift = values[3];
            return wahlbezirk;
        }, wahlbezirkRepository);
    }

    private void importElection() {
        electionReader.readElectionFile(ELECTION_FILE);
    }

    @Bean
    public Election election() {
        return electionRepository.findAll().get(0);
    }
}
