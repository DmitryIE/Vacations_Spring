package ru.egerev.vacationApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.egerev.vacationApp.models.Holiday;

import java.util.Date;

@Repository
public interface HolidaysRepository extends JpaRepository<Holiday, Date> {

}
