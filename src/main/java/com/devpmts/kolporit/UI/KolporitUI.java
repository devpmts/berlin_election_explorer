package com.devpmts.kolporit.UI;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.VaadinUI;

import com.vaadin.annotations.Push;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import lombok.extern.slf4j.Slf4j;

@VaadinUI
@Slf4j
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
        log.info("UI > preparing Main View");
        TextField inputTextField = new TextField("Data Destillery (refer to variables with preceeding hash, e.g. #X + #Y)");
        inputTextField.addValueChangeListener(event -> {
            String expression = (String) event.getProperty().getValue();
            computationGrid.updateComputationColumn(expression);
        });

        HorizontalLayout hlayout = new HorizontalLayout();
        hlayout.setSizeFull();
        hlayout.setSpacing(true);
        electionResultGrid.setSizeFull();
        electionResultGrid.setHeightMode(HeightMode.ROW);
        electionResultGrid.setHeightByRows(35);
        computationGrid.setSizeFull();
        computationGrid.setHeightMode(HeightMode.ROW);
        computationGrid.setHeightByRows(35);
        hlayout.addComponent(electionResultGrid);
        hlayout.addComponent(computationGrid);

        VerticalLayout vLayout = new VerticalLayout();
        vLayout.addComponent(VariableDefinitionPanel.definitionListContainer);
        vLayout.addComponent(inputTextField);
        vLayout.addComponent(hlayout);
        setContent(vLayout);
    }

}
