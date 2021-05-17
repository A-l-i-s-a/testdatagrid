-- begin TESTDATAGRID_NEW_ENTITY
create table TESTDATAGRID_NEW_ENTITY (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    TEST1 varchar(255),
    TEST2 varchar(255),
    TEST3 varchar(255),
    --
    primary key (ID)
)^
-- end TESTDATAGRID_NEW_ENTITY
