package ru.egerev.vacationApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.egerev.vacationApp.models.Holiday;
import ru.egerev.vacationApp.repositories.HolidaysRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HolidaysService {

    private HolidaysRepository holidaysRepository;

    @Autowired
    public HolidaysService(HolidaysRepository holidaysRepository) {
        this.holidaysRepository = holidaysRepository;
    }

    @Transactional()
    public void addHoliday(LocalDate date) {
        Holiday holiday = new Holiday(date);
        holidaysRepository.save(holiday);
    }

    @Transactional(readOnly = true)
    public List<Holiday> getAllHolidays() {
        return holidaysRepository.findAll()
                .stream()
                .sorted((x, y) -> x.compareTo(y))
                .collect(Collectors.toList());
    }

    @Transactional()
    public void delete(LocalDate dateToDelete) {
        Holiday holidayToDelete = new Holiday(dateToDelete);
        holidaysRepository.delete(holidayToDelete);
    }


}
