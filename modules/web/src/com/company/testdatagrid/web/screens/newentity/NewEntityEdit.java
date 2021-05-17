package com.company.testdatagrid.web.screens.newentity;

import com.haulmont.cuba.gui.screen.*;
import com.company.testdatagrid.entity.NewEntity;

@UiController("testdatagrid_NewEntity.edit")
@UiDescriptor("new-entity-edit.xml")
@EditedEntityContainer("newEntityDc")
@LoadDataBeforeShow
public class NewEntityEdit extends StandardEditor<NewEntity> {
}