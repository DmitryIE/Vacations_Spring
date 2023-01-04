package ru.egerev.vacationApp.services;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.egerev.vacationApp.models.Employee;
import ru.egerev.vacationApp.models.Holiday;
import ru.egerev.vacationApp.models.Vacation;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EmployeesService {

    private static final String filePath = "src\\main\\resources\\Vacation.xlsx";
    private List<Employee> employeeList;

    private HolidaysService holidaysService;

    public EmployeesService(HolidaysService holidaysService) {
        this.holidaysService = holidaysService;
        try {
            this.employeeList = loadEmployees();
        } catch (Exception e) {
            this.employeeList = null;
        }
    }

    // загрузка данных из файла Excel
    public String loadTextData() throws IOException {

        String result = "";

        try (FileInputStream file = new FileInputStream(filePath)) {
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                String rowStr = "";
                int countCellsWithNumbers = 0;
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    CellType cellType = cell.getCellType();
                    switch (cellType) {
                        case STRING:
                            rowStr += cell.getStringCellValue() + "=";
                            break;
                        case NUMERIC:
                            rowStr += cell.getNumericCellValue() + "=";
                            countCellsWithNumbers++;
                    }
                }
                if (countCellsWithNumbers != 0) {
                    result += rowStr + "\n";
                }
            }
        }

        return result;
    }

    // преобразование полученных данных в мапу, где ключ - Ф.И.О,
    // значение - лист из объектов типа Vacation
    public Map<String, List<Vacation>> parseToEmployees(String str) {
        String[] lines = str.split("\n");
        Set<String> namesOfEmployees = Stream.of(lines).map(x -> x.split("=")[1]).collect(Collectors.toSet());
        Map<String, List<Vacation>> vacationMap = namesOfEmployees.stream().collect(Collectors.toMap(x -> x, y -> new ArrayList<>()));
        for (String line : lines) {
            String[] parseLine = line.split("=");
            String name = parseLine[1];
            LocalDate dateOfStartVacation = LocalDate.of(1899, Month.DECEMBER, 30)
                    .plusDays((long) Double.parseDouble(parseLine[2]));
            int periodOfVacation = (int) Double.parseDouble(parseLine[3]);
            vacationMap.get(name).add(new Vacation(dateOfStartVacation, periodOfVacation));
        }
        return vacationMap;
    }

    //загрузка данных из файла Excel и преобразование их в объекты типа Employee
    public List<Employee> loadEmployees() throws IOException {
        String textFromFile = loadTextData();
        Map<String, List<Vacation>> employeesVacations = parseToEmployees(textFromFile);

        List<Employee> employeeList = employeesVacations.entrySet()
                .stream()
                .map(x -> new Employee(x.getKey(), x.getValue()))
                .sorted(((o1, o2) -> o1.getName().compareTo(o2.getName())))
                .collect(Collectors.toList());

        employeeList.forEach(x -> x.setVacationList(x.getVacationList().stream()
                .sorted((o1, o2) -> o1.getDateOfBegin().getDayOfYear() - o2.getDateOfBegin().getDayOfYear())
                .collect(Collectors.toList())
        ));

        return employeeList;
    }

    public List<Employee> getEmployeeList() {
        try {
            this.employeeList = loadEmployees();
        } catch (Exception e) {
            this.employeeList = null;
        }
        return employeeList;
    }

    // создаем по имени сотрудника мапу, где ключ - ФИО человек, значение список дат,
    // которые попадают в его отпуск с учетом совпадений с праздничными днями и
    // переносами
    public Map<String, List<LocalDate>> employeeAllDaysVacation(String employeeName) {
        Employee employee = employeeList.stream()
                .filter(x -> x.getName().equals(employeeName))
                .findAny()
                .get();

        employee.getVacationList().stream().forEach(x ->
                x.setDateOfEnd(determineEndOfVacation(x.getDateOfBegin(), x.getPeriod())));

        List<LocalDate> allDatesOfVacation = new ArrayList<>();
        employee.getVacationList().stream().forEach(new Consumer<Vacation>() {
            @Override
            public void accept(Vacation vacation) {
                for (LocalDate date = vacation.getDateOfBegin();
                     date.isBefore(vacation.getDateOfEnd());
                     date = date.plusDays(1)) {
                    allDatesOfVacation.add(date);
                }
            }
        });
        Map<String, List<LocalDate>> result = new HashMap<>();
        result.put(employeeName, allDatesOfVacation);
        return result;
    }

    // сравниваем все отпуска двух сотрудников. В качестве результатам возвращаем
    // ФИО человека, с которым сравнивали и все даты, которые совпали
    public String compareTwoEmployees(String firstName, String secondName) {
        Map<String, List<LocalDate>> firstEmployeeAllDaysVacation
                = employeeAllDaysVacation(firstName);
        Map<String, List<LocalDate>> secondEmployeeAllDaysVacation
                = employeeAllDaysVacation(secondName);

        String resultOfCompare = "";
        String matchesByDates = "";

        matchesByDates = firstEmployeeAllDaysVacation.get(firstName).stream().filter(x ->
                        secondEmployeeAllDaysVacation.get(secondName).contains(x))
                .map(x -> x.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .collect(Collectors.toList()).toString();

        if (matchesByDates.equals("[]")) {
            matchesByDates = "пересечений нет";
        }

        resultOfCompare = secondName + " - " + matchesByDates;
        return resultOfCompare;
    }

    //возвращаем мапу, в которой ключ - ФИО сотрудника, а значение - List стрингов (с ФИО сотрудника,
    // с которым сравнивали и всеми совпадающими датами)
    public Map<String, List<String>> compareSelectedVacations(List<String> employeesNames) {
        Map<String, List<String>> compareMap = new HashMap<>();

        //сравниваем каждого сотрудника в списке с остальными
        for (int i = 0; i < employeesNames.size(); i++) {
            for (String name : employeesNames) {
                //проверяем не сравниваем ли сотрудника с самим собой
                if (!employeesNames.get(i).equals(name)) {
                    //если уже есть ключ равный имени сотрудника, то добавляем в значения к нему новую информацию
                    if (compareMap.containsKey(employeesNames.get(i))) {
                        compareMap.get(employeesNames.get(i)).add(compareTwoEmployees(employeesNames.get(i), name));
                    } else {
                        //если ключа нет, то создаем его и добавляем новую информацию о сравнении отпусков
                        compareMap.put(employeesNames.get(i), new ArrayList<>(Arrays.asList(compareTwoEmployees(employeesNames.get(i), name))));
                    }
                }
            }
        }

        return compareMap;
    }

    //вычисляем окончание отпуска с учетом праздничных дней и переносов праздничных дней
    public LocalDate determineEndOfVacation(LocalDate beginOfVacation, int period) {
        List<Holiday> holidayList = holidaysService.getAllHolidays();
        long countHolidaysDuringVacation = (holidayList.stream()
                .filter(x -> x.getDate().isAfter(beginOfVacation)
                        && x.getDate().isBefore(beginOfVacation.plusDays(period - 1)))
                .count());
        LocalDate endOfVacation = beginOfVacation.plusDays(period + countHolidaysDuringVacation);

        return endOfVacation;
    }

}
