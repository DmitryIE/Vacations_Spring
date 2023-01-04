package ru.egerev.vacationApp.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Vacation {

    private LocalDate dateOfBegin;
    private int period;

    private LocalDate dateOfEnd;

    public Vacation(LocalDate dateOfBegin, int period) {
        this.dateOfBegin = dateOfBegin;
        this.period = period;
        this.dateOfEnd = null;
    }

    public Vacation() {
    }

    public LocalDate getDateOfBegin() {
        return dateOfBegin;
    }

    public void setDateOfBegin(LocalDate dateOfBegin) {
        this.dateOfBegin = dateOfBegin;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public LocalDate getDateOfEnd() {
        return dateOfEnd;
    }

    public void setDateOfEnd(LocalDate dateOfEnd) {
        this.dateOfEnd = dateOfEnd;
    }

    @Override
    public String toString() {
        return "Начало: " + dateOfBegin.format(DateTimeFormatter.ofPattern("dd.MM.yy")) +
                ", количество календарных дней: " + period;
    }
}
