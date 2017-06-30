package com.devpmts.kolporit.bootstrap;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
import com.devpmts.util.DevpmtsSpringUtil;
import com.rabbitmq.tools.json.JSONReader;

import lombok.extern.slf4j.Slf4j;

@Component
@Configuration
@Slf4j
public class KolporitDataImporter {

    static final File PARTY_FILE = getFile("parties.json");

    static final File STADTTEILE_FILE = getFile("stadtteile.json");

    static final File WAHLBEZIRKE_FILE = getFile("wahlbezirke.csv");

    static final File ELECTION_FILE = getFile("election.csv");

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
        String content = getStringFromFile(PARTY_FILE);
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
        String content = getStringFromFile(STADTTEILE_FILE);
        Map<String, String> stadtteile = (HashMap<String, String>) new JSONReader().read(content);
        stadtteile.entrySet().stream().map(entry -> {
            Bezirk bezirk = new Bezirk();
            bezirk.bezirkNummer = getIntOrZero(entry.getKey());
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

    static File getFile(String fileName) {
        URL resource = Object.class.getClassLoader().getResource(fileName);
        if (resource == null) {
            System.err.println(fileName + " not found.");
            return null;
        }
        try {
            return Paths.get(resource.toURI()).toFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param file
     * @return the Stringcontent of the file or an empty String
     */
    static String getStringFromFile(File file) {
        try (Scanner scanner = new Scanner(file)) {
            return scanner.useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    static Integer getIntOrZero(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

}
