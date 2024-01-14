## Сервис потоковой загрузки данных.

---
**Некоторые пояснения к предлагаемому решению**

1. В исполнение принципа "Single Responsibility" функционал сервисов распределен по разным иерархиям интерфейсов:

   - Функция валидации вынесен в отдельную
     иерархию [Validator](./src/main/java/local/interviews/homework/exercise/configurations/validators/Validator.java);
   - Функционал модификации потока вынесен в отельную
     иерархию [StreamAppender](./src/main/java/local/interviews/homework/exercise/StreamAppender.java) (в процессе обработки в поток данных
     добавляется
     null-значение в качестве маркера окончания потока).

2. Реализован подход, позволяет обрабатывать потоки произвольной длины - финализирующих операций в коде самого сервиса нет.
3. Добавление поддержки нового формата данных осуществляется путем создания нового компонента. Для создания компонента
   достаточно унаследовать его от
   класса [AbstractValidator](./src/main/java/local/interviews/homework/exercise/configurations/validators/AbstractValidator.java),
   и указать в нем сигнатуру формата (качестве демонстрации добавлена поддержка PNG-формата, имеющего сигнатуру, отличную от JPEG - класс
   [PngValidator](./src/main/java/local/interviews/homework/exercise/configurations/validators/PngValidator.java)).
4. Параметры приложения вынесены в ресурсный файл [application.properties](./src/main/resources/application.properties).
5. Сообщения, используемые в приложении вынесены в ресурсный файл [messages.properties](./src/main/resources/i18n/messages.properties).
   Приложение интернационализировано, но учитывая тестовый характер задачи, локализация применена только для английского языка без 
   функции переключения.
6. Все компоненты снабжены собственными тестами.
7. Простые участки кода считаются самодокументированными и не комментировались. Прокомментированы только моменты, упрощающие понимание
   алгоритма;
8. Комментарии в исходных файлах приведены на английском языке во избежание проблем с кодировками
   при работе в интернациональной команде.

---
17.01.2024 г.
