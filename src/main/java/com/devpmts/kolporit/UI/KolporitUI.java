package com.devpmts.kolporit.UI;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.VaadinUI;

import com.devpmts.DevsLogger;
import com.vaadin.annotations.Push;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@VaadinUI
@Push(PushMode.AUTOMATIC)
public class KolporitUI extends UI {

	@Autowired
	ComputationGrid computationGrid;

	@Autowired
	ElectionResultGrid electionResultGrid;

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		prepareMainView();
	}

	private void prepareMainView() {
		DevsLogger.log("UI > preparing Main View");
		TextField inputTextField = new TextField("Data Destillery");
		inputTextField.addValueChangeListener(event -> {
			String expression = (String) event.getProperty().getValue();
			computationGrid.updateComputationColumn(expression);
		});

		HorizontalLayout hlayout = new HorizontalLayout();
		hlayout.addComponent(electionResultGrid);
		hlayout.addComponent(computationGrid);

		VerticalLayout vLayout = new VerticalLayout();
		vLayout.addComponent(VariableDefinitionPanel.definitionListContainer);
		vLayout.addComponent(inputTextField);
		vLayout.addComponent(hlayout);
		setContent(vLayout);
	}

}
