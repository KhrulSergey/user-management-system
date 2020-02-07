# Система управления пользователями через REST API Spring Framework / Springboot

Запустить можно командой из корневого каталога проекта: ./mvn spring-boot:run

Испольуется БД Postgres, с таблицами:
users - пользователи, 
roles - поли, 
user_roles - связка пользователей и ролей

Сервис работает с форматом JSON и имеет следующий набор методов:
 
- /list Получает список пользователей из БД
- /get Получает конкретного пользователя (с его ролями) из БД
- /delete Удаляет пользователя в БД
- /add Добавляет нового пользователя в БД. В json запроса могут передаваться роли, например 
{"name": "Вася", "login": "vasa","password": "123123T", "roles": [2,3]} .
- /edit Редактирует существующего пользователя в БД. В json запроса могут передаваться роли, например 
{"id":26, "name": "Rut", "login": "rui2","password": "ff2", "roles": [1,6]} .
 
Запустить можно командой из корневого каталога проекта
`mvn spring-boot:run`

После запуска REST сервисы будут доступны по URLs:
 - GET http://localhost:8080/user/list
 - GET http://localhost:8080/user/get/{id}
 - DELETE http://localhost:8080/user/delete/{id}
 - POST http://localhost:8080/user/add
 - PUT http://localhost:8080/user/edit/{id}
 
 Коллекция POSTMAN запросов находится в файле UserManagementLocalHost.postman_collection.json 