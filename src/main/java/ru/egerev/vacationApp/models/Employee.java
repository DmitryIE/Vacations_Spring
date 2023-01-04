package ru.egerev.vacationApp.models;

import java.util.List;

public class Employee {

    private String name;
    private List<Vacation> vacationList;

    public Employee(String name, List<Vacation> vacationList) {
        this.name = name;
        this.vacationList = vacationList;
    }

    public Employee() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Vacation> getVacationList() {
        return vacationList;
    }

    public void setVacationList(List<Vacation> vacationList) {
        this.vacationList = vacationList;
    }

}
