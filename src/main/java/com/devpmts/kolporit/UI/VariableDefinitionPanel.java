package com.devpmts.kolporit.UI;

import java.util.LinkedHashMap;
import java.util.Map;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class VariableDefinitionPanel extends HorizontalLayout {

    public static VerticalLayout definitionListContainer = new VerticalLayout();

    static Button plus = new Button("+");

    static {
        init();
    }

    private static void init() {
        plus.addClickListener(event -> addVariableDefinition());
        definitionListContainer.addComponent(plus);
        addVariableDefinition();
    }

    VariableDefinition variableDefinition = new VariableDefinition();

    TextField leftSide = new TextField();

    Label equals = new Label("  =  ");

    TextField rightSide = new TextField();

    Button delete = new Button("X");

    private VariableDefinitionPanel() {
        addComponents();
        registerListeners();
    }

    private void addComponents() {
        addComponent(leftSide);
        addComponent(equals);
        addComponent(rightSide);
        addComponent(delete);
    }

    private void registerListeners() {
        delete.addClickListener(event -> removeVariableDefinition());
        leftSide.addTextChangeListener(event -> {
            variableDefinition.leftSide = event.getText().toUpperCase();
        });
        rightSide.addTextChangeListener(event -> {
            variableDefinition.rightSide = event.getText().toUpperCase();
        });
    }

    static void addVariableDefinition() {
        definitionListContainer.addComponent(new VariableDefinitionPanel());
    }

    void removeVariableDefinition() {
        definitionListContainer.removeComponent(this);
    }

    public static Map<String, Object> variableDefinitions() {
        Map<String, Object> definitions = new LinkedHashMap<>();
        definitionListContainer.forEach(component -> {
            if (!(component instanceof VariableDefinitionPanel)) {
                return;
            }
            if (((VariableDefinitionPanel) component).variableDefinition.leftSide == null || ((VariableDefinitionPanel) component).variableDefinition.rightSide == null) {
                return;
            }
            VariableDefinition definition = ((VariableDefinitionPanel) component).variableDefinition;
            definitions.put(definition.leftSide, definition.rightSide);
        });
        return definitions;
    }
}
