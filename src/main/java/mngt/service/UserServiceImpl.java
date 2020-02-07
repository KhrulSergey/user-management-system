package mngt.service;

import mngt.repository.UserRepository;
import mngt.model.User;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Collections;
import java.util.Set;

/**
 * Реализация сервиса-прослойки между ВЕБ-контроллером и сервисом доступа к данным
 */
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;
    private Validator validator;

    public UserServiceImpl(UserRepository userRepository, Validator validator) {
        this.userRepository = userRepository;
        this.validator = validator;
    }

    /**
     * Получение списка пользователей из БД
     *
     * @return Список имеющихся пользователей из БД
     */
    @Override
    public Page<User> list(Pageable pageable) {
        LOG.info("getting users: ");
        return userRepository.findAll(pageable);
    }

    /**
     * Добавление нового пользователя в БД.
     *
     * @param user Добавляемый пользователь
     * @return добавленный пользователь
     */
    @Override
    public long add(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (violations.isEmpty()) {
            LOG.info("save user: %s", user);
            return userRepository.save(user)
                    .getId();
        } else {
            LOG.warn("user: %s have constraint violations: %s", user, violations);
            throw new ConstraintViolationException(violations);
        }
    }

    /**
     * Получение конкретного пользователя из БД
     *
     * @param id Идентификатор пользователя для поиска
     * @return Данные о пользователе или null - если пользователь не найден
     */
    @Override
    public User get(long id) {
        User user = userRepository.findOne(id);
        LOG.info("getting user: %s", user);
        return user;
    }

    /**
     * Редактирование существующего пользователя в БД.
     *
     * @param user Обновленные данные для внесения в БД
     * @return отредактированный пользователь
     */
    @Override
    public User edit(User user) {
        User updatedUser = null;
        if (userRepository.exists(user.getId())) {
            Set<ConstraintViolation<User>> violations = validator.validate(user);
            if (violations.isEmpty()) {
                LOG.info("user: %s is edited", user);
                updatedUser = userRepository.save(user);
            } else {
                LOG.warn("edited user: %o have constraint violations: %s", user, violations);
                throw new ConstraintViolationException(violations);
            }
        } else {
            ConstraintViolation<User> violation = ConstraintViolationImpl.forBeanValidation(null,
                    null, "User is not exist", User.class, user, null, user, null, null, null, null);
            LOG.warn("user: %s is not exist");
            throw new ConstraintViolationException(Collections.singleton(violation));
        }
        return updatedUser;
    }

    /**
     * Удаление пользователя в БД
     *
     * @param id Идентификатор пользователя для удаления
     */
    @Override
    public void delete(long id) {
        userRepository.delete(id);
        LOG.info("delete user by id: %d", id);
    }


}
