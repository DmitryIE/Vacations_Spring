package ru.egerev.vacationApp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "holidays")
public class Holiday implements Comparable<Holiday> {

    @Id
    @Column(name = "holiday_date")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @NotNull(message = "Пожалуйста введите дату")
    private LocalDate date;

    public Holiday(LocalDate date) {
        this.date = date;
    }

    public Holiday() {
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public int compareTo(Holiday o) {
        if (this.getDate().getYear() != o.getDate().getYear()) {
            return this.getDate().getYear() - o.getDate().getYear();
        }
        return this.getDate().getDayOfYear() - o.getDate().getDayOfYear();
    }
}
