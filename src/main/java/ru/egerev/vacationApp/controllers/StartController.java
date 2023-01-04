package ru.egerev.vacationApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalTime;

@Controller
@RequestMapping("/vacations")
public class StartController {

    @Autowired
    public StartController() {
    }

    @GetMapping
    public String startWebApp(Model model) {
        int currentHour = LocalTime.now().getHour();
        String greeting = "";
        if (currentHour >= 5 & currentHour <= 11) {
            greeting = "Доброе утро!";
        } else if (currentHour >= 12 & currentHour <= 17) {
            greeting = "Добрый день!";
        } else if (currentHour >=0 & currentHour <= 4){
            greeting = "Доброй ночи!";
        } else {
            greeting = "Добрый вечер!";
        }
        model.addAttribute("greeting", greeting);
        return "vacations";
    }
}
