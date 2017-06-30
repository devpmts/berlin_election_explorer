package com.devpmts.kolporit.UI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.devpmts.kolporit.domain.Election;
import com.devpmts.kolporit.domain.ElectionResult;
import com.devpmts.kolporit.repository.ElectionRepository;
import com.devpmts.kolporit.repository.ElectionResultRepository;

@Component
public class ElectionResultGrid extends KolporitGrid<ElectionResult> {

    @Autowired
    ElectionRepository electionRepo;

    @Autowired
    ElectionResultRepository electionResultRepo;

    @Autowired
    Election election;

    public void createColumns() {
        election = electionRepo.findAll().get(0);
        election.parties.keySet().stream().forEach(partyName -> getContainerDataSource().addContainerProperty(partyName, Object.class, null));
    }

    @Override
    Object[] createRowObjectArray(ElectionResult electionResult) {
        Object[] resultValues = election.parties.keySet().stream().map(partyName -> electionResult.results.get(partyName)).toArray();
        return resultValues;
    }

    @Override
    protected CrudRepository<ElectionResult, String> getRepo() {
        return electionResultRepo;
    }

}
