package com.company.testdatagrid.web.screens;

import com.company.testdatagrid.entity.NewEntity;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.app.core.inputdialog.DialogActions;
import com.haulmont.cuba.gui.app.core.inputdialog.DialogOutcome;
import com.haulmont.cuba.gui.app.core.inputdialog.InputParameter;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.web.gui.components.WebTabWindow;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@LoadDataBeforeShow
@UiController("testdatagrid_")
@UiDescriptor("new-screen.xml")
public class NewScreen extends StandardLookup<NewEntity> {

    @Inject
    private CollectionContainer<NewEntity> ideasDc;
    @Inject
    private CollectionLoader<NewEntity> ideasDl;
    @Inject
    private UiComponents uiComponents;
    @Inject
    private DataContext dataContext;
    @Inject
    private Dialogs dialogs;
    @Inject
    private DataGrid<NewEntity> ideasDataGrid1;
    @Inject
    private DataGrid<NewEntity> ideasDataGrid2;
    @Inject
    private Filter filter;

    private DataGrid<NewEntity> activeDataGridProvider() {
        if (ideasDataGrid1.isVisible()) {
            return ideasDataGrid1;
        }
        return ideasDataGrid2;
    }

    private DataGrid<NewEntity> inactiveDataGridProvider() {
        if (ideasDataGrid1.isVisible()) {
            return ideasDataGrid2;
        }
        return ideasDataGrid1;
    }

    private void switchVisibility() {
        ideasDataGrid1.setVisible(!ideasDataGrid1.isVisible());
        ideasDataGrid2.setVisible(!ideasDataGrid2.isVisible());
        for (NewEntity item : ideasDc.getItems()) {
            activeDataGridProvider().setDetailsVisible(item, inactiveDataGridProvider().isDetailsVisible(item));
        }
        Component parent = ideasDataGrid2.getParent();
        WebTabWindow window = (WebTabWindow) parent;
        window.expand(activeDataGridProvider());
        filter.setApplyTo(activeDataGridProvider());
    }

    @Subscribe
    public void onInit(InitEvent event) {
        ideasDataGrid1.setItemClickAction(new BaseAction("itemClickAction")
                .withHandler(actionPerformedEvent ->
                        ideasDataGrid1.setDetailsVisible(ideasDataGrid1.getSingleSelected(),
                                true)));
        ideasDataGrid2.setItemClickAction(new BaseAction("itemClickAction")
                .withHandler(actionPerformedEvent ->
                        ideasDataGrid2.setDetailsVisible(ideasDataGrid2.getSingleSelected(),
                                true)));
        ideasDataGrid1.setVisible(true);
        ideasDataGrid1.setDetailsGenerator(this::ideasDataGridDetailsGenerator);
        ideasDataGrid2.setDetailsGenerator(this::ideasDataGridDetailsGenerator);
        ideasDataGrid2.setVisible(false);

    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        for (NewEntity item : ideasDc.getItems()) {
            activeDataGridProvider().setDetailsVisible(item, true);
        }
    }

    @Subscribe("button")
    public void onButtonClick(Button.ClickEvent event) {
        List<InputParameter> inputParameters = Stream
                .of(InputParameter.offsetTimeParameter("Comment").withField(() -> {
                    TextArea<String> commentField = uiComponents.create(TextArea.NAME);
                    commentField.setWidth("100%");
                    commentField.setCaption("messageBundle.getMessage");
                    commentField.setRows(5);
                    return commentField;
                })).collect(Collectors.toList());
        dialogs.createInputDialog(this).withCaption("messages.getMessage(votingResultType)")
                .withParameters(inputParameters.toArray(new InputParameter[0]))
                .withActions(DialogActions.OK_CANCEL)
                .withCloseListener(closeEvent -> {
                    if (closeEvent.closedWith(DialogOutcome.OK)) {
//
                        String comment = closeEvent.getValue("Comment");
                        NewEntity singleSelected = activeDataGridProvider().getSingleSelected();
                        singleSelected.setTest3(comment);
                        dataContext.merge(singleSelected);
                        dataContext.commit();
                        ideasDl.load();
                        switchVisibility();
                    }
                }).show();

    }


    private Component ideasDataGridDetailsGenerator(NewEntity entity) {
        VBoxLayout mainLayout = uiComponents.create(VBoxLayout.NAME);
        mainLayout.setWidth("100%");
        mainLayout.setMargin(true);

        HBoxLayout headerBox = uiComponents.create(HBoxLayout.NAME);
        headerBox.setWidth("100%");

        Component closeButton = createCloseButton(entity);
        headerBox.add(closeButton);
        mainLayout.add(headerBox);


        Label<String> label1 = uiComponents.create(Label.TYPE_STRING);
        label1.setValue(entity.getTest1());
        Label<String> label2 = uiComponents.create(Label.TYPE_STRING);
        label2.setValue(entity.getTest2());
        Label<String> label3 = uiComponents.create(Label.TYPE_STRING);
        label3.setValue(entity.getTest3());

        mainLayout.add(label1);
        mainLayout.add(label2);
        mainLayout.add(label3);

        return mainLayout;
    }

    private Component createCloseButton(NewEntity entity) {
        Button closeButton = uiComponents.create(Button.class);
        closeButton.setAlignment(Component.Alignment.TOP_RIGHT);
        closeButton.setIcon("icons/close.png");
        BaseAction closeAction = new BaseAction("closeAction")
                .withHandler(actionPerformedEvent ->
                        activeDataGridProvider().setDetailsVisible(entity, false))
                .withCaption("");
        closeButton.setAction(closeAction);
        return closeButton;
    }

    @Subscribe("button2")
    public void onButton2Click(Button.ClickEvent event) {
        for (NewEntity item : ideasDc.getItems()) {
            activeDataGridProvider().setDetailsVisible(item, false);
        }
    }
}