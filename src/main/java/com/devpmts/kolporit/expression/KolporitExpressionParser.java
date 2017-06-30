package com.devpmts.kolporit.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KolporitExpressionParser {

    private String expression;

    @Autowired
    private Map<String, Object> variableDefinitions;

    private Map<String, Object> electionResults;

    public KolporitExpressionParser(String expression, Map<String, Object> variableDefinitions, Map<String, Object> electionResults) {
        this.variableDefinitions = variableDefinitions;
        this.electionResults = electionResults;
        this.expression = expression;
    }

    public static double parse(String expression, Map<String, Object> variableDefinitions, Map<String, Object> electionResults) throws ParseException {
        if (expression == null) {
            return -1d;
        }
        return new KolporitExpressionParser(expression, variableDefinitions, electionResults).parse();
    }

    private double parse() throws ParseException {
        parseVariableDefinitions();
        return parseExpression();
    }

    private double parseExpression() {
        StandardEvaluationContext evaluationContext = createEvaluationContextWithElectionResults();
        addAllVariables(evaluationContext);
        return parseExpression(this.expression, evaluationContext);
    }

    private void addAllVariables(StandardEvaluationContext evaluationContext) {
        addKnownVariablesUpToIndex(variableDefinitions.size(), evaluationContext);
    }

    private void parseVariableDefinitions() {
        for (int i = 0; i < variableDefinitions.size(); i++) {
            StandardEvaluationContext evaluationContext = createVariableContextUpToIndex(i);
            parseVariableDefinition(i, evaluationContext);
        }
    }

    private void parseVariableDefinition(int i, StandardEvaluationContext evaluationContext) {
        String name = getVariableName(i);
        Object definition = variableDefinitions.get(name);
        if (definition instanceof String) {
            double parsed = parseExpression((String) definition, evaluationContext);
            variableDefinitions.put(name, parsed);
            log.trace(name + ": " + parsed);
        } else {
            assert definition instanceof Integer;
        }
    }

    private StandardEvaluationContext createVariableContextUpToIndex(int i) {
        StandardEvaluationContext evaluationContext = createEvaluationContextWithElectionResults();
        addKnownVariablesUpToIndex(i, evaluationContext);
        return evaluationContext;
    }

    void addKnownVariablesUpToIndex(int lastIndex, StandardEvaluationContext evaluationContext) {
        List<String> variableNames = new ArrayList<>(variableDefinitions.keySet());
        List<String> subList = variableNames.subList(0, lastIndex);
        subList.forEach(variable -> {
            Object definition = variableDefinitions.get(variable);
            setParserVariable(evaluationContext, variable, definition);
        });
    }

    private double parseExpression(String expression, StandardEvaluationContext evaluationContext) {
        log.trace("parsing expression: " + expression);
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        Expression spelExpression = spelExpressionParser.parseExpression(expression);
        try {
            Object parsed = spelExpression.getValue(evaluationContext);
            log.trace("Parsed '" + expression + "' to '" + parsed + "'");
            return Double.valueOf(parsed.toString());
        } catch (Exception see) {
            log.error(see.getMessage(), see);
        }
        return -1;
    }

    private StandardEvaluationContext createEvaluationContextWithElectionResults() {
        StandardEvaluationContext context = new StandardEvaluationContext();
        addParserVariablesFromMap(electionResults, context);
        return context;
    }

    private void addParserVariablesFromMap(Map<String, ?> variables, StandardEvaluationContext evaluationContext) {
        Set<String> keySet = variables.keySet();
        keySet.stream().forEach(name -> {
            setParserVariable(evaluationContext, name, variables.get(name));
        });
    }

    private void setParserVariable(StandardEvaluationContext evaluationContext, String variable, Object definition) {
        if (variable == null || definition == null) {
            log.warn("Variable " + variable + "=" + definition + " has null");
            return;
        }
        // DevsLogger.log("setting " + variable + "->" + definition);
        evaluationContext.setVariable(variable, definition);
    }

    private String getVariableName(int i) {
        ArrayList<String> variableNames = new ArrayList<String>(variableDefinitions.keySet());
        return variableNames.get(i);
    }

}
