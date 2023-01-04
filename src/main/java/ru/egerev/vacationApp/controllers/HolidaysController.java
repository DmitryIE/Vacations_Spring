package ru.egerev.vacationApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.egerev.vacationApp.services.HolidaysService;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/vacations")
public class HolidaysController {

    private HolidaysService holidaysService;

    @Autowired
    public HolidaysController(HolidaysService holidaysService) {
        this.holidaysService = holidaysService;
    }

    @GetMapping("all_holidays")
    public String show(Model model) {
        model.addAttribute("holidays", holidaysService.getAllHolidays());
        return "vacations/all_holidays";
    }

    @PostMapping("all_holidays")
    public String delete(@RequestParam("checked") List<LocalDate> datesToDelete) {
        datesToDelete.forEach(x -> holidaysService.delete(x));
        return "redirect:/vacations/all_holidays";
    }

    @PostMapping("add")
    public String add(@RequestParam("dateAdd") String dateToAdd) {
        LocalDate localDate = LocalDate.parse(dateToAdd);
        holidaysService.addHoliday(localDate);
        return "redirect:/vacations/all_holidays";
    }

}
