package com.company.testdatagrid.entity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "TESTDATAGRID_NEW_ENTITY")
@Entity(name = "testdatagrid_NewEntity")
public class NewEntity extends StandardEntity {
    private static final long serialVersionUID = -7549928590665483725L;

    @Column(name = "TEST1")
    private String test1 = "1";

    @Column(name = "TEST2")
    private String test2 = "1";

    @Column(name = "TEST3")
    private String test3 = "1";

    public String getTest3() {
        return test3;
    }

    public void setTest3(String test3) {
        this.test3 = test3;
    }

    public String getTest2() {
        return test2;
    }

    public void setTest2(String test2) {
        this.test2 = test2;
    }

    public String getTest1() {
        return test1;
    }

    public void setTest1(String test1) {
        this.test1 = test1;
    }
}