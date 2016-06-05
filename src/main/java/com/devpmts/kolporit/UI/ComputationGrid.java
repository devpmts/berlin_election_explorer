package com.devpmts.kolporit.UI;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.devpmts.DevsLogger;
import com.devpmts.kolporit.domain.Computation;
import com.devpmts.kolporit.domain.Election;
import com.devpmts.kolporit.domain.ElectionResult;
import com.devpmts.kolporit.expression.KolporitExpressionParser;
import com.devpmts.kolporit.expression.ParseException;
import com.devpmts.kolporit.repository.ComputationRepository;
import com.devpmts.kolporit.repository.ElectionRepository;
import com.devpmts.kolporit.repository.ElectionResultRepository;

@Component
public class ComputationGrid extends KolporitGrid<Computation> {

	@Autowired
	ElectionResultRepository electionResultRepo;

	@Autowired
	ComputationRepository computationRepo;

	@Autowired
	ElectionRepository electionRepo;

	public void updateComputationColumn(String expression) {
		DevsLogger.log("updating table with expression '" + expression + "'");
		getContainerDataSource().removeAllItems();
		Election election = electionRepo.findAll().get(0);
		List<Computation> computations = computationRepo.findAll();
		List<ElectionResult> electionResults = electionResultRepo.findAll();
		for (int i = 0; i < electionResults.size(); i++) {
			Computation computation = null;
			if (computations.size() <= i) {
				addRow(new Object[3]);
				computation = new Computation();
			} else {
				computation = computations.get(i);
			}
			ElectionResult electionResult = electionResults.get(i);
			updateComputationRow(election, computation, electionResult, expression);
		}
		addRowsFromRepo(computationRepo);
	}

	private void updateComputationRow(Election election, Computation computation,
			ElectionResult electionResult, String expression) {
		try {
			double result = KolporitExpressionParser.parse(expression,
					VariableDefinitionPanel.variableDefinitions(), electionResult.results);
			computation.wahlbezirk = electionResult.wahlbezirk;
			computation.computationResult = result;
			computationRepo.save(computation);
		} catch (ParseException e) {
			DevsLogger.log(e.getMessage());
		}
	}

	@Override
	Object[] createRowObjectArray(Computation computation) {
		return new Object[] { computation.wahlbezirk.bezirk.name, computation.wahlbezirk.anschrift,
				computation.computationResult };
	}

	@Override
	void createColumns() {
		getContainerDataSource().addContainerProperty("stadtteil", String.class, null);
		getContainerDataSource().addContainerProperty("wahlbezirk", String.class, null);
		getContainerDataSource().addContainerProperty("result", Double.class, null);
	}

	@Override
	protected CrudRepository<Computation, String> getRepo() {
		return computationRepo;
	}

}
