# Проект "Анализ графика отпусков сотрудников"
(стек применяемых технологий: *Spring, Spring Boot, Spring Data JPA, Stream API, PostgreSQL, Thymeleaf, Hibernate*)

**Задача:**

Необходимо проанализировать график отпусков структурного подразделения. Графики отпусков создаются по одному шаблону в файле формата Excel. Веб-приложение должно иметь возможность загружать данный файл, обрабатывать его содержимое и после выбора интересующих сотрудников анализировать его.

Функционал:
1) Страница с возможностью загрузить файл с графиком отпусков. При успешной загрузке файла появляется сообщение об этом.
2) Страция с возможностью добавления, удаления праздничных дней (влияют на дату окончания отпуска). Все праздничные дни записываются в базу данных.
3) Страница с возможностью выбрать сотрудников, которых необходимо сравнить (список сотрудников подгружается из файла с графиком).
4) Страница с возможностью посмотреть результат сравнения. Представляется информация по каждому сраниваемому сотруднику (с кем сравнивается, есть ли пересечения, если есть, то какие даты совпали).
5) Страница с возможностью посмотреть загруженную информацию (список сотрудников и их отпусков в удобном табличном виде).
6) Страница с образцом графика отпусков.
7) При загрузке невалидного файла и попытке получить из него информацию появляются предупреждающие сообщения.

## Изображения работы программы:
<u>**1. Стартовая страница**</u>

![](./screenshots/001.jpg)

<u>**2. Загрузка файла**</u>

![](./screenshots/008.jpg)

<u>**3. Результат успешной загрузки файла**</u>

![](./screenshots/009.jpg)

<u>**4. Попытка загрузить файл (файл не был выбран)**</u>

![](./screenshots/009.jpg)

<u>**5. Добавление, удаление праздничных дней (применяется валидация полей)**</u>

![](./screenshots/002.jpg)

<u>**6. Добавление праздничных дней с помощью календаря**</u>

![](./screenshots/003.jpg)

<u>**7. Выбор сотрудников для сравнения**</u>

![](./screenshots/004.jpg)

<u>**8. Результат сравнения**</u>

![](./screenshots/005.jpg)

<u>**9. Загруженные отпуска сотрудников**</u>

![](./screenshots/006.jpg)

<u>**10. Образец графика отпусков**</u>

![](./screenshots/007.jpg)

<u>**11. Попытка выбрать сотрудников для сравнения (файл невалидный, либо не загружен)**</u>

![](./screenshots/011.jpg)

<u>**12. Попытка получить список загруженных отпусков (файл невалидный)**</u>

![](./screenshots/012.jpg)
