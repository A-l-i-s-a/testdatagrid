package com.company.testdatagrid.web.screens.newentity;

import com.haulmont.cuba.gui.screen.*;
import com.company.testdatagrid.entity.NewEntity;

@UiController("testdatagrid_NewEntity.browse")
@UiDescriptor("new-entity-browse.xml")
@LookupComponent("newEntitiesTable")
@LoadDataBeforeShow
public class NewEntityBrowse extends StandardLookup<NewEntity> {
}