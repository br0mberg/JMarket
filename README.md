JMarket by Brombin Andrey
Pet project on Java 17 and Spring Framework.
CRUD/REST приложение.
Итерируясь по коммитам можно проследить эволюцию проекта от паттерна MVC до добавления Service и Repository слоёв.
Здесь в ветке develop, я начинаю проект JMarket используя Spring MVC, JDBC API, далее JDBCTemplate, Hibernate, и в последнем коммите реализую Spring Data JPA.
Также в сервисе реализована валидация полей, валидация паттернов, отображение ошибок.
Шаблонизатор - Thymeleaf.
Сервис пакуется в war формате и деплоится на Apache Tomcat 9.
Файл hibernate.properties.origin - указывает шаблон для заполнения конфигурационного файла.

В ветке develop2.0 - дальнейшая реализация проекта с переходом на конфигурацию со Spring Boot.
