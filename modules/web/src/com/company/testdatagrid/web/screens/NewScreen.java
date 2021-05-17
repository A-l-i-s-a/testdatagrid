package com.company.testdatagrid.web.screens;

import com.company.testdatagrid.entity.NewEntity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.actions.list.RemoveAction;
import com.haulmont.cuba.gui.app.core.inputdialog.DialogActions;
import com.haulmont.cuba.gui.app.core.inputdialog.DialogOutcome;
import com.haulmont.cuba.gui.app.core.inputdialog.InputParameter;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.data.datagrid.ContainerDataGridItems;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataComponents;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@LoadDataBeforeShow
@UiController("testdatagrid_")
@UiDescriptor("new-screen.xml")
public class NewScreen extends StandardLookup<NewEntity> {
    @Inject
    private DataGrid<NewEntity> ideasDataGrid;
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
    private DataManager dataManager;
    @Inject
    private DataComponents dataComponents;
    @Inject
    private VBoxLayout vbox;
    @Named("ideasDataGrid.remove")
    private RemoveAction<NewEntity> ideasDataGridRemove;


    @Subscribe
    public void onInit(InitEvent event) {
        ideasDataGrid.setItemClickAction(new BaseAction("itemClickAction")
                .withHandler(actionPerformedEvent ->
                        ideasDataGrid.setDetailsVisible(ideasDataGrid.getSingleSelected(),
                                true)));
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        for (NewEntity item : ideasDc.getItems()) {
            ideasDataGrid.setDetailsVisible(item, true);
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
                        String comment = closeEvent.getValue("Comment");
                        NewEntity singleSelected = ideasDataGrid.getSingleSelected();
                        singleSelected.setTest3(comment);
//                        dataManager.commit(singleSelected);
//                        dataContext.merge(singleSelected);
//                        dataContext.commit();
//                        ideasDl.load();
//                        ideasDc.getMutableItems().remove(singleSelected);
//                        List<NewEntity> items = ideasDc.getItems();
//                        DataGrid<NewEntity> component = uiComponents.create(DataGrid.NAME);
//                        CollectionContainer<NewEntity> collectionContainer = dataComponents.createCollectionContainer(NewEntity.class);
//                        collectionContainer.setItems(items);
//                        component.setItems(new ContainerDataGridItems<>(collectionContainer));
//                        component.setDetailsGenerator(new DataGrid.DetailsGenerator<NewEntity>() {
//                            @Nullable
//                            @Override
//                            public Component getDetails(NewEntity entity) {
//                                return ideasDataGridDetailsGenerator(entity);
//                            }
//                        });
//                        component.setItemClickAction(new BaseAction("itemClickAction")
//                                .withHandler(actionPerformedEvent ->
//                                        component.setDetailsVisible(component.getSingleSelected(),
//                                                true)));
//                        vbox.remove(ideasDataGrid);
//                        component.setId("ideasDataGrid");
//                        vbox.add(component);
                        ideasDataGridRemove.execute();
                        ideasDataGrid.repaint();

                    }
                }).show();
    }

    @Install(to = "ideasDataGrid", subject = "detailsGenerator")
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
                        ideasDataGrid.setDetailsVisible(entity, false))
                .withCaption("");
        closeButton.setAction(closeAction);
        return closeButton;
    }
}