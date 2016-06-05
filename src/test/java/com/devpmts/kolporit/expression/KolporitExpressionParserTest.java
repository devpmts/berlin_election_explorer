package com.devpmts.kolporit.expression;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.devpmts.kolporit.domain.ElectionResult;
import com.devpmts.kolporit.domain.Party;

public class KolporitExpressionParserTest {

	String PARSE_EXPRESSION = "#PARTY1 + #PARTY2 + #var1";

	Map<String, Party> parties = new HashMap<>();

	Map<String, Object> variableDefinitions;

	private ElectionResult electionResult;

	@Before
	public void setup() {
		Party party1 = Mockito.mock(Party.class);
		Party party2 = Mockito.mock(Party.class);
		electionResult = new ElectionResult();
		electionResult.results = new HashMap<>();
		electionResult.results.put("PARTY1", 1);
		electionResult.results.put("PARTY2", 3);
		parties.put("PARTY1", party1);
		parties.put("PARTY2", party2);
		variableDefinitions = new LinkedHashMap<>();
		variableDefinitions.put("var1", "#PARTY1 + 2");
		variableDefinitions.put("var2", "#var1 + #PARTY2");
	}

	@Test
	public void testStaticParseMethod() throws ParseException {
		KolporitExpressionParser.parse(PARSE_EXPRESSION, variableDefinitions,
				electionResult.results);
	}
}
