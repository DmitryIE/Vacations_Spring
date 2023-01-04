package ru.egerev.vacationApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.egerev.vacationApp.services.EmployeesService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("vacations")
public class EmployeesController {

    private EmployeesService employeesService;

    private static final String FILE_NAME = "Vacation.xlsx";

    @Autowired
    public EmployeesController(EmployeesService employeesService) {
        this.employeesService = employeesService;
    }

    @GetMapping("employees/showAllVacations")
    public String showAllVacations(Model model, RedirectAttributes attributes) {
        try {
            model.addAttribute("employees", employeesService.loadEmployees());
        } catch (Exception e) {
            attributes.addFlashAttribute("message", "Ошибка при обработке файла. Файл невалидный");
            return "redirect:/vacations";
        }
        return "employees/showAllVacations";
    }

    @GetMapping("employees/chooseEmployees")
    public String chooseEmployees(Model model) {
        model.addAttribute("employees", employeesService.getEmployeeList());
        return "employees/chooseEmployees";
    }

    @PostMapping("result")
    public String resultOfComparing(@RequestParam(value = "checked", required = false) List<String> employeeList, Model model) {
        if (employeeList == null) {
            return "redirect:/vacations/employees/chooseEmployees";
        }
        model.addAttribute("results",
                employeesService.compareSelectedVacations(employeeList));
        return "vacations/result";
    }

    @GetMapping("load")
    public String loadData() {
        return "vacations/load";
    }

    @PostMapping("load")
    public String upLoad(@RequestParam(value = "file", required = false) MultipartFile file,
                         RedirectAttributes attributes) {

        if (file.isEmpty()) {
            attributes.addFlashAttribute("message", "Пожалуйста выберите файл для загрузки");
            return "redirect:/vacations/load";
        }

        try {
            Path path = Paths.get("src\\main\\resources\\" + FILE_NAME);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            attributes.addFlashAttribute("message",
                    "Произошла непредвиденная ошибка. Повторите действия позже.");
            return "redirect:/vacations/load";
        }

        attributes.addFlashAttribute("message", "Файл успешно загружен");

        return "redirect:/vacations/load";
    }

    @GetMapping("sample")
    public String showSample() {
        return "vacations/sample";
    }
}
