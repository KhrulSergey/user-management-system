package mngt.controller;

import mngt.model.User;
import mngt.service.UserService;
import mngt.util.ResponseMessage;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.net.URI;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static mngt.controller.UserController.BASE_PATH;


/**
 * Контроллер управления сущностью "Пользователи"
 */
@RestController
@RequestMapping(path = BASE_PATH)
public class UserController {

    public static final String BASE_PATH = "/user";
    public static final String PATH_ADD = "/add";
    public static final String PATH_GET = "/get/{id}";
    public static final String PATH_DELETE = "/delete/{id}";
    public static final String PATH_LIST = "/list";
    public static final String PATH_EDIT = "/edit/{id}";

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Добавление пользователя в Систему
     * @param user добавляемый пользователь
     * @return результат выполнения операции в JSON формате. При ошибке возвращает 404ую ошибку
     */
    @PostMapping(path = PATH_ADD)
    public ResponseEntity<ResponseMessage> add(@RequestBody User user) {
        try {
            long id = userService.add(user);
            URI location = ServletUriComponentsBuilder.fromPath(PATH_GET)
                    .buildAndExpand(id)
                    .toUri();
            return ResponseEntity.created(location)
                    .body(new ResponseMessage());
        } catch (ConstraintViolationException e) {
            Set<String> errors = e.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toSet());
            return new ResponseEntity<>(new ResponseMessage(errors), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Получение одного пользователя из системы
     * @param id идентификатор для получения данных
     * @return результат выполнения операции в JSON формате. При ошибке возвращает 404ую ошибку
     */
    @GetMapping(path = PATH_GET)
    public ResponseEntity<User> get(@PathVariable("id") long id) {
        User user = userService.get(id);
        if (Objects.nonNull(user)) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Получение списка пользователей из системы
     * @param pageable параметр пагинации выдачи результата
     * @return результат выполнения операции в JSON формате. При ошибке возвращает 404ую ошибку
     */
    @GetMapping(path = PATH_LIST)
    public ResponseEntity<Page<User>> list(@PageableDefault Pageable pageable) {
        Page<User> users = userService.list(pageable);
        return ResponseEntity.ok(users);
    }

    /**
     * Обновление данных пользователя
     * @param id идентификатор пользователя указанный в запросе
     * @param user данные пользователя для обновления
     * @return результат выполнения операции в JSON формате. При ошибке возвращает 404ую ошибку
     */
    @PutMapping(path = PATH_EDIT)
    public ResponseEntity<ResponseMessage> edit(@PathVariable("id") long id,
                                                @RequestBody User user) {
        if (user.getId() != id) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage(asList("Please provide 'id' parameter the same as in request")));
        }
        try {
            User updatedUser = userService.edit(user);
            if (Objects.nonNull(updatedUser)) {
                return ResponseEntity.ok(new ResponseMessage());
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage(e.getConstraintViolations()
                            .stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.toSet())));
        }
    }

    /**
     * Удаление пользователя из Системы
     * @param id идентификатор пользователя для удаления
     * @return результат выполнения операции в JSON формате. При ошибке возвращает 404ую ошибку
     */
    @DeleteMapping(path = PATH_DELETE)
    public ResponseEntity<ResponseMessage> delete(@PathVariable("id") long id) {
        try {
            userService.delete(id);
            return ResponseEntity.ok(new ResponseMessage());
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }
}
